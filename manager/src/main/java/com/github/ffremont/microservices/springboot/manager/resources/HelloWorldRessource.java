/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.resources;

import com.github.ffremont.microservices.springboot.manager.security.Roles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
@Path("/hello")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldRessource {

    private final static Logger LOG = LoggerFactory.getLogger(HelloWorldRessource.class);
    
    @GET
    @RolesAllowed({Roles.GUEST})
    public String message() throws Exception {
        LOG.info("Hello world response ");
        
        return "Hello world !" ;
    }   
    
}
