/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.resources;

import com.github.ffremont.microservices.springboot.manager.security.Roles;
import java.util.logging.Level;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
public class MicroServiceResource {
    
    private final static Logger LOG = LoggerFactory.getLogger(MicroServiceResource.class);
    
    public final static String PATH = "microservices";
    
    public final static String TYPE_MIME = "application/vnd.microservice+json";

    private String cluster;
    
    @GET    
    @RolesAllowed({Roles.ADMIN, Roles.GUEST, Roles.USER})
    public Response hello(){
        return Response.ok("Hello "+this.cluster).build();
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }
}
