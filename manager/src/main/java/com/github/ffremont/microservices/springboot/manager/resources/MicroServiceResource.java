/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.resources;

import com.github.ffremont.microservices.springboot.manager.JerseyConfig;
import com.github.ffremont.microservices.springboot.manager.annotations.ExpirationCache;
import com.github.ffremont.microservices.springboot.manager.annotations.NoCache;
import com.github.ffremont.microservices.springboot.manager.mappers.MicroServiceMapper;
import com.github.ffremont.microservices.springboot.manager.models.MicroService;
import com.github.ffremont.microservices.springboot.manager.models.Property;
import com.github.ffremont.microservices.springboot.manager.models.repo.IMicroServiceRepo;
import com.github.ffremont.microservices.springboot.manager.models.repo.IPropertyRepo;
import com.github.ffremont.microservices.springboot.manager.nexus.NexusClientApi;
import com.github.ffremont.microservices.springboot.manager.nexus.NexusData;
import com.github.ffremont.microservices.springboot.manager.security.Roles;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
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
        MicroServiceMapper msMapper = new MicroServiceMapper();
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

        return Response.ok((new MicroServiceMapper()).apply(ms)).build();
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

        if(ms == null){
            throw new WebApplicationException("Microservice introuvable", Status.NOT_FOUND);
        }
        NexusData data = nexusClientApi.getData(ms.getGav().getGroupId(), ms.getGav().getArtifactId(), ms.getGav().getPackaging(), ms.getGav().getClassifier(), ms.getGav().getVersion());
        
        if(data == null){
            throw new WebApplicationException("Livrable nexus introuvable", Status.NOT_FOUND);
        }
        
        EntityTag etag = new EntityTag(data.getSha1());
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder == null) {
            Resource resource = nexusClientApi.getBinary(ms.getGav().getGroupId(), ms.getGav().getArtifactId(), ms.getGav().getPackaging(), ms.getGav().getClassifier(), ms.getGav().getVersion());
            builder = Response.ok(resource.getInputStream());
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
            p.put(prop.getName(), prop.getValue());
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
    public Response addMicroservice(MicroService newMs) {
        throw new UnsupportedOperationException("addMicroservice");
    }

    /**
     * Mise à jour du ms
     *
     * @param ms
     * @return
     */
    @PUT
    @Path("{msName}")
    @RolesAllowed({Roles.ADMIN})
    public Response modifyMicroservice(@PathParam("msName") String msName, MicroService ms) {
        throw new UnsupportedOperationException("modifyMicroservice : " + msName);
    }

    @DELETE
    @Path("{msName}")
    @RolesAllowed({Roles.ADMIN})
    public Response deleteMicroservice(@PathParam("msName") String msName) {
        throw new UnsupportedOperationException("deleteMicroservice");
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
