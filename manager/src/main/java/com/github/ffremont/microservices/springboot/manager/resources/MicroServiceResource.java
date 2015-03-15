/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.resources;

import com.github.ffremont.microservices.springboot.manager.models.MicroService;
import com.github.ffremont.microservices.springboot.manager.security.Roles;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
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

    private String cluster;
    private String node;
    
    @GET    
    @Path("ping")
    public Response hello(){
        return Response.ok("Hello "+this.cluster+":"+this.node).build();
    }
    
    /**
     * Liste des micro services pour un cluster:name
     * @return 
     */
    @GET
    public Response microservices(){
        return Response.ok().build();
    }

    /**
     * Retourne un MS à partir de son nom
     * @param msName
     * @return 
     */
    @GET
    @Path("{msName}")
    @RolesAllowed({Roles.ADMIN, Roles.USER})
    public Response microserviceByName(@PathParam("msName") String msName){
        throw new UnsupportedOperationException("microserviceByName : "+msName);
    }
    
    /**
     * Récupération du binaire
     * @param msName
     * @return 
     */
    @GET
    @Path("{msName}/binary")
    @RolesAllowed({Roles.ADMIN, Roles.USER})
    public Response microserviceBinaryByName(@PathParam("msName") String msName){
        throw new UnsupportedOperationException("microserviceBinaryByName : "+msName);
    }
    
    /**
     * Récupération du contenu fichier de propriété
     * @param msName
     * @return 
     */
    @GET
    @Path("{msName}/properties")
    @RolesAllowed({Roles.ADMIN, Roles.USER})
    public Response microservicePropByName(@PathParam("msName") String msName){
        throw new UnsupportedOperationException("microservicePropByName : "+msName);
    }
    
    /**
     * 
     * @param newMs
     * @return 
     */
    @POST
    @RolesAllowed({Roles.ADMIN})
    public Response addMicroservice(MicroService newMs){
        throw new UnsupportedOperationException("addMicroservice");
    }
    
    /**
     * Mise à jour du ms
     * @param ms
     * @return 
     */
    @PUT
    @Path("{msName}")
    @RolesAllowed({Roles.ADMIN})
    public Response modifyMicroservice(@PathParam("msName") String msName, MicroService ms){
        throw new UnsupportedOperationException("modifyMicroservice : "+msName);
    }
    
    @DELETE
    @Path("{msName}")
    @RolesAllowed({Roles.ADMIN})
    public Response deleteMicroservice(@PathParam("msName") String msName){
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
