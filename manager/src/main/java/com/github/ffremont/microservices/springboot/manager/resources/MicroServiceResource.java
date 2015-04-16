/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.resources;

import com.github.ffremont.microservices.springboot.manager.JerseyConfig;
import com.github.ffremont.microservices.springboot.manager.annotations.ExpirationCache;
import com.github.ffremont.microservices.springboot.manager.annotations.NoCache;
import com.github.ffremont.microservices.springboot.manager.mappers.MicroServiceFromRestMapper;
import com.github.ffremont.microservices.springboot.manager.mappers.MicroServiceToRestMapper;
import com.github.ffremont.microservices.springboot.manager.models.MicroService;
import com.github.ffremont.microservices.springboot.manager.models.MsEtat;
import com.github.ffremont.microservices.springboot.manager.models.Property;
import com.github.ffremont.microservices.springboot.manager.models.repo.IMicroServiceRepo;
import com.github.ffremont.microservices.springboot.manager.models.repo.IPropertyRepo;
import com.github.ffremont.microservices.springboot.manager.nexus.NexusClientApi;
import com.github.ffremont.microservices.springboot.manager.nexus.NexusData;
import com.github.ffremont.microservices.springboot.manager.security.Roles;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
@Scope("request")
@Consumes(MicroServiceResource.TYPE_MIME)
@Produces(MicroServiceResource.TYPE_MIME)
@RolesAllowed({Roles.ADMIN, Roles.GUEST, Roles.USER})
public class MicroServiceResource {

    private final static Logger LOG = LoggerFactory.getLogger(MicroServiceResource.class);

    public final static String PATH = "microservices";

    public final static String TYPE_MIME = "application/vnd.microservice+json";

    public final static int MAX_PAGE_MS = 30;

    @Autowired
    private IMicroServiceRepo microServiceRepo;

    @Autowired
    private IPropertyRepo propRepo;

    @Autowired
    private NexusClientApi nexusClientApi;

    private String cluster;
    private String node;

    /**
     * Liste des micro services pour un cluster:name
     *
     * @return
     */
    @GET
    @RolesAllowed({Roles.ADMIN, Roles.USER})
    @NoCache
    public Response microservices() {
        Page<MicroService> microservices = microServiceRepo.findByClusterAndNode(this.cluster, this.node, new PageRequest(0, MAX_PAGE_MS));

        List<MicroServiceRest> list = new ArrayList<>();
        MicroServiceToRestMapper msMapper = new MicroServiceToRestMapper();
        microservices.forEach(ms -> {
            list.add(msMapper.apply(ms));
        });

        return Response.ok(list).build();
    }

    /**
     * Retourne un MS à partir de son nom
     *
     * @param msName
     * @return
     */
    @GET
    @Path("{msName}")
    @RolesAllowed({Roles.ADMIN, Roles.USER})
    @NoCache
    public Response microserviceByName(@PathParam("msName") String msName) {
        MicroService ms = microServiceRepo.findOneByClusterAndNodeAndName(this.cluster, this.node, msName);
        if (ms == null) {
            throw new WebApplicationException("Microservice not found", Status.NOT_FOUND);
        }

        return Response.ok((new MicroServiceToRestMapper()).apply(ms)).build();
    }

    /**
     * Récupération du jar
     *
     * @param msName
     * @param request
     * @return
     * @throws java.io.IOException
     */
    @GET
    @Path("{msName}/binary")
    @RolesAllowed({Roles.ADMIN, Roles.USER})
    @Produces("application/java-archive")
    @Consumes("application/java-archive")
    public Response microserviceBinaryByName(@PathParam("msName") String msName, @Context Request request) throws IOException {
        // récupération du binaire
        MicroService ms = this.microServiceRepo.findOneByClusterAndNodeAndName(this.cluster, this.node, msName);

        if (ms == null) {
            throw new WebApplicationException("Microservice introuvable", Status.NOT_FOUND);
        }
        NexusData data = nexusClientApi.getData(ms.getGav().getGroupId(), ms.getGav().getArtifactId(), ms.getGav().getPackaging(), ms.getGav().getClassifier(), ms.getGav().getVersion());

        if (data == null) {
            throw new WebApplicationException("Livrable nexus introuvable", Status.NOT_FOUND);
        }

        EntityTag etag = new EntityTag(data.getSha1());
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder == null) {
            java.nio.file.Path tmpBinary = nexusClientApi.getBinary(ms.getGav().getGroupId(), ms.getGav().getArtifactId(), ms.getGav().getPackaging(), ms.getGav().getClassifier(), ms.getGav().getVersion());

            StreamingOutput stream = (OutputStream os) -> {
                byte[] buffer = new byte[10240];
                int read;
                try (InputStream is = new FileInputStream(tmpBinary.toFile())) {
                    while (-1 != (read = is.read(buffer))) {
                        os.write(buffer, 0, read);
                    }
                }finally{
                    try{
                        Files.delete(tmpBinary);
                    }catch(IOException e){
                        LOG.warn("Suppression impossible du fichier temporaire lié au binaire de nexus", e);
                    }
                }
                
                os.flush();
            };
            builder = Response.ok(stream);
        }
        builder.tag(etag);

        return builder.build();

    }

    /**
     * Récupération du contenu fichier de propriété
     *
     * @param msName
     * @return
     * @throws java.io.IOException
     */
    @GET
    @Path("{msName}/properties")
    @RolesAllowed({Roles.ADMIN, Roles.USER})
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    @ExpirationCache(maxAge = 86400)
    public Response microservicePropByName(@PathParam("msName") String msName) throws IOException {
        MicroService ms = microServiceRepo.findOneByClusterAndNodeAndName(this.cluster, this.node, msName);

        List<Property> props = new ArrayList<>();
        String ns = ms.getNsProperties();
        String[] nsTab;
        while (true) {
            props.addAll(propRepo.findByNamespaceRegex("^" + ns + ""));
            nsTab = ns.split("\\.");

            if (nsTab.length > 1) {
                ns = ns.replaceAll("." + nsTab[nsTab.length - 1] + "$", "");
            } else {
                break;
            }
        }

        Properties p = new Properties();
        props.forEach(prop -> {
            boolean add = false;
            if(ms.getProperties() != null){
                if(ms.getProperties().contains(prop.getName())){
                    add = true;
                }
            }else{
                add = true;
            }
            
            if(add){
                p.put(prop.getName(), prop.getValue());
            }
        });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        p.store(baos, "");

        return Response.ok(baos.toString(JerseyConfig.APP_CHARSET.toString())).build();
    }

    /**
     *
     * @param newMs
     * @return
     */
    @POST
    @RolesAllowed({Roles.ADMIN})
    public Response addMicroservice(MicroServiceRest newMs) {
        MicroService ms = (new MicroServiceFromRestMapper()).apply(newMs);

        NexusData data = nexusClientApi.getData(ms.getGav().getGroupId(), ms.getGav().getArtifactId(), ms.getGav().getPackaging(), ms.getGav().getClassifier(), ms.getGav().getVersion());
        if (data == null) {
            throw new WebApplicationException("Livrable nexus introuvable à partir des informations du nouveau micro service", Status.BAD_REQUEST);
        }

        ms.setCluster(this.cluster);
        ms.setNode(this.node);
        ms.setSha1(data.getSha1());

        try {
            this.microServiceRepo.save(ms);
        } catch (DuplicateKeyException dke) {
            LOG.error("Micro service already exists", dke);
            throw new WebApplicationException("Micro service already exists", Status.CONFLICT);
        }

        return Response.ok().build();
    }

    /**
     * Mise à jour du ms
     *
     * @param msName
     * @param ms
     * @return
     */
    @PUT
    @Path("{msName}")
    @RolesAllowed({Roles.ADMIN})
    public Response modifyMicroservice(@PathParam("msName") String msName, MicroServiceRest ms) {
        MicroService msSrc = microServiceRepo.findOneByClusterAndNodeAndName(this.cluster, this.node, msName);
        if (msSrc == null) {
            throw new WebApplicationException("Microservice introuvable", Status.NOT_FOUND);
        }

        if (!msSrc.getName().equals(ms.getName()) || !msSrc.getId().equals(ms.getId())) {
            throw new WebApplicationException("Le micro service n'est pas cohérent avec la base de données", Status.BAD_REQUEST);
        }

        MicroService msUpdated = (new MicroServiceFromRestMapper()).apply(ms);
        NexusData data = nexusClientApi.getData(msUpdated.getGav().getGroupId(), msUpdated.getGav().getArtifactId(), msUpdated.getGav().getPackaging(), msUpdated.getGav().getClassifier(), msUpdated.getGav().getVersion());
        if (data == null) {
            throw new WebApplicationException("Livrable nexus introuvable à partir des informations du micro service", Status.BAD_REQUEST);
        }
        msUpdated.setCluster(this.cluster);
        msUpdated.setNode(this.node);
        msUpdated.setSha1(data.getSha1());

        this.microServiceRepo.save(msUpdated);

        return Response.ok().build();
    }

    @DELETE
    @Path("{msName}")
    @RolesAllowed({Roles.ADMIN})
    public Response deleteMicroservice(@PathParam("msName") String msName) {
        MicroService ms = microServiceRepo.findOneByClusterAndNodeAndName(this.cluster, this.node, msName);
        if (ms == null) {
            throw new WebApplicationException("Microservice introuvable", Status.NOT_FOUND);
        }

        Date minDate = Date.from(Instant.now().minus(3, ChronoUnit.MINUTES));
        if (MsEtat.Inactif.equals(ms.getEtat()) && ms.getLastModified().before(minDate)) {
            this.microServiceRepo.delete(ms.getId());
        } else {
            throw new WebApplicationException("La délai minimum de 3min avant la suppression n'est pas respecté", Status.BAD_REQUEST);
        }

        return Response.ok().build();
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
}
