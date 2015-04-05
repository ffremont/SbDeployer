/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.nexus;

import java.util.Arrays;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author florent
 */
public class NexusTestConfiguration {
    
    @Bean
    public NexusClientApi getNexusClientApi(){
        return new NexusClientApi();
    }
    
    @Bean
    public NexusProperties getProp(){
        NexusProperties prop = new NexusProperties();
        prop.setBaseurl("http://localhost/nexus");
        prop.setRepo(Arrays.asList("snapshots"));
        
        return prop;
    }
    
    @Bean
    @Qualifier("nexusRestTemplate")
    public RestTemplate getRestTemplate() {
        return Mockito.mock(RestTemplate.class);
    }
}
