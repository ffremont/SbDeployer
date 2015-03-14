/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ffremont.microservices.springboot.manager.resources;

import fr.ffremont.microservices.springboot.manager.SbManagerWorldApp;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
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
    
    @Value("${local.server.port}")
    int port;
    
    RestTemplate restTemplate = new TestRestTemplate("testeur", "secret");
    

    /**
     * Test of message method, of class HelloWorldRessource.
     */
    @Test
    public void testMessage() {
        assertEquals("Hello world !", this.restTemplate.getForEntity("http://localhost:"+port+"/manager/hello", String.class).getBody());
    }
    
}
