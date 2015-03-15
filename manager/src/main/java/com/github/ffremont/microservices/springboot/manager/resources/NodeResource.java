/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.resources;

import com.github.ffremont.microservices.springboot.manager.security.Roles;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
@Scope("request")
@Consumes(NodeResource.TYPE_MIME)
@Produces(NodeResource.TYPE_MIME)
public class NodeResource {
    
    private final static Logger LOG = LoggerFactory.getLogger(NodeResource.class);
    
    public final static String PATH = "nodes";
    
    public final static String TYPE_MIME = "application/vnd.node+json";

    /**
     * Sous ressource fille
     */
    @Autowired
    private MicroServiceResource microServiceResource;
    
    /**
     * Cluster ciblé
     */
    private String cluster;
    
    @Path("{nodeName}/"+MicroServiceResource.PATH)
    public MicroServiceResource getMicroServices(@PathParam("nodeName") String nodeName){
        microServiceResource.setCluster(this.cluster);
        microServiceResource.setNode(nodeName);
        
        return microServiceResource;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }
}
