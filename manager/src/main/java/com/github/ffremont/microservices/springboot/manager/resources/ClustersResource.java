/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.resources;

import com.github.ffremont.microservices.springboot.manager.security.Roles;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
@Scope("request")
@Path("/"+ClustersResource.PATH)
@Consumes(ClustersResource.TYPE_MIME)
@Produces(ClustersResource.TYPE_MIME)
public class ClustersResource {
    
    /**
     * Accept ou ContentType
     */
    public final static String TYPE_MIME = "application/vnd.cluster+json";
    
    /**
     * Chemin URL
     */
    public final static String PATH = "clusters";
    
    @Autowired
    private NodeResource nodeResource;
    
    @Path("/{clusterName}/"+NodeResource.PATH)
    public NodeResource getNodes(@PathParam("clusterName") String clusterName){
        nodeResource.setCluster(clusterName);
        
        return nodeResource;
    }
}
