/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.resources;

import com.github.ffremont.microservices.springboot.manager.JerseyConfig;
import com.github.ffremont.microservices.springboot.manager.SbManagerWorldApp;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author florent
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SbManagerWorldApp.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
@ActiveProfiles("test")
public class MicroServiceResourceTest {
    
    private final static Logger LOG = LoggerFactory.getLogger(MicroServiceResourceTest.class);
    
    @Value("${local.server.port}")
    int port;
    
    RestTemplate restTemplate = new TestRestTemplate("testeur", "secret");
    
    @Test
    public void helloTest(){
        String url = "http://localhost:"+port+"/"+JerseyConfig.APP_PATH+"/"+ClustersResource.PATH+"/testt/"+MicroServiceResource.PATH;
        
        LOG.debug("MicroServiceResource.hello : "+url);
        ResponseEntity<String> response = this.restTemplate.getForEntity("http://localhost:"+port+"/"+JerseyConfig.APP_PATH+"/"+ClustersResource.PATH+"/testt/"+MicroServiceResource.PATH, String.class);
        
        assertEquals("Hello testt", response.getBody());
        assertTrue(response.getHeaders().getContentType().toString().startsWith(MicroServiceResource.TYPE_MIME));
    }
    
}
