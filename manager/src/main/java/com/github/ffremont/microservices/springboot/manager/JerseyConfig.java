/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.server.validation.ValidationFeature;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
@ApplicationPath(JerseyConfig.APP_PATH)
public class JerseyConfig extends ResourceConfig {
    
    public final static String APP_PATH = "manager";

    public JerseyConfig() {
        packages("com.github.ffremont.microservices.springboot.manager.resources");
        packages("com.github.ffremont.microservices.springboot.manager.security");
        register(RolesAllowedDynamicFeature.class);
        register(ValidationFeature.class);
        
        register(JacksonFeature.class);
    }
}