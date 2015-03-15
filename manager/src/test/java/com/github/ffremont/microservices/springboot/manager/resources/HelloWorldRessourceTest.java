/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.resources;

import com.github.ffremont.microservices.springboot.manager.JerseyConfig;
import com.github.ffremont.microservices.springboot.manager.SbManagerWorldApp;
import javax.ws.rs.core.MediaType;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
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
public class HelloWorldRessourceTest {
    
    private final static Logger LOG = LoggerFactory.getLogger(HelloWorldRessourceTest.class);
    
    @Value("${local.server.port}")
    int port;

    private RestTemplate anonymeRestTemplate = new TestRestTemplate();
    private RestTemplate adminRestTemplate = new TestRestTemplate("admin", "secret");
    private RestTemplate userRestTemplate = new TestRestTemplate("user", "secret");
    private RestTemplate guestRestTemplate = new TestRestTemplate("guest", "secret");
    
    private String getHelloWorldUri(String suffix){
        return "http://localhost:"+port+"/"+JerseyConfig.APP_PATH+"/hello/"+suffix;
    }

    
    public void testFailForbiddenMessage() {
        assertEquals(HttpStatus.FORBIDDEN, this.anonymeRestTemplate.getForEntity(this.getHelloWorldUri("message"), String.class).getStatusCode());
    }
    
    @Test
    public void testOkMessage() {
        ResponseEntity<String> response = this.userRestTemplate.getForEntity(this.getHelloWorldUri("message"), String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().getContentType().toString().startsWith(MediaType.APPLICATION_JSON));
        
        assertEquals("Hello world !", this.userRestTemplate.getForEntity(this.getHelloWorldUri("message"), String.class).getBody());
    }
    
    @Test
    public void testOkError() {
        ResponseEntity<com.github.ffremont.microservices.springboot.manager.security.Error> response = this.guestRestTemplate.getForEntity(this.getHelloWorldUri("error"), com.github.ffremont.microservices.springboot.manager.security.Error.class);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(1, response.getBody().getCode());
    }
    
    @Test
    public void testNotFoundJersey() {
        ResponseEntity<String> response = this.guestRestTemplate.getForEntity(this.getHelloWorldUri("errrrror"), String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertTrue(response.getBody().contains("Not Found"));
    }
}
