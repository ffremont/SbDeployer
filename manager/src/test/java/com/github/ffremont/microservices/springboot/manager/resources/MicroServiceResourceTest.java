/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.resources;

import com.github.ffremont.microservices.springboot.manager.JerseyConfig;
import com.github.ffremont.microservices.springboot.manager.SbManagerWorldApp;
import com.github.ffremont.microservices.springboot.manager.stores.ValidMicroServiceStore;
import com.github.ffremont.microservices.springboot.manager.stores.ValidPropertiesStore;
import com.github.ffremont.microservices.springboot.pojo.Gav;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.util.Arrays;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author florent
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SbManagerWorldApp.class )
@WebIntegrationTest({"server.port=0", "management.port=0"})
@ActiveProfiles("test")
public class MicroServiceResourceTest {
    
    private final static Logger LOG = LoggerFactory.getLogger(MicroServiceResourceTest.class);
    
    @Value("${local.server.port}")
    int port;
    
    RestTemplate restTemplate = new TestRestTemplate("admin", "secret");
        
    @Autowired
    private MongoTemplate mongoTemplate;
    
    private String getUrlResource(String cluster, String node, String suffix) {
        return "http://localhost:"+port+"/"+JerseyConfig.APP_PATH+"/"+ClustersResource.PATH+"/"+cluster+"/"+NodeResource.PATH+"/"+node+"/"+MicroServiceResource.PATH+"/"+suffix;
    }
    
    @After
    public void after(){
        this.mongoTemplate.getDb().dropDatabase();
    }
    
    @Test
    public void msListOkTest() {
        ValidMicroServiceStore.provide(mongoTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.parseMediaType(MicroServiceResource.TYPE_MIME)));
        HttpEntity<MicroServiceRest[]> entity = new HttpEntity<>(headers);
        ResponseEntity<MicroServiceRest[]> response = this.restTemplate.exchange(this.getUrlResource("myCluster", "myNodeA", ""), HttpMethod.GET, entity, MicroServiceRest[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().getContentType().toString().startsWith(MicroServiceResource.TYPE_MIME));
       
        assertEquals(1, response.getBody().length);
        
        MicroServiceRest ms = response.getBody()[0];
        assertEquals("fr.ffremont:myArtifact:jar:0.0.1", ms.getGav().toString());
        assertEquals("toto", ms.getName());
    }
    
    @Test
    public void msOneOkTest() {
        ValidMicroServiceStore.provide(mongoTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.parseMediaType(MicroServiceResource.TYPE_MIME)));
        HttpEntity<MicroServiceRest> entity = new HttpEntity<>(headers);
        ResponseEntity<MicroServiceRest> response = this.restTemplate.exchange(this.getUrlResource("myCluster", "myNodeA", "toto"), HttpMethod.GET, entity, MicroServiceRest.class);
        
        assertTrue(response.getHeaders().getContentType().toString().startsWith(MicroServiceResource.TYPE_MIME));
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        MicroServiceRest ms = response.getBody();
        
        assertNotNull(ms);
        assertEquals("myCluster" , ms.getCluster());
    }
    
    @Test
    public void msPropsTest() {
        ValidMicroServiceStore.provide(mongoTemplate);
        ValidPropertiesStore.provide(mongoTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.parseMediaType(MediaType.TEXT_PLAIN.toString())));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = this.restTemplate.exchange(this.getUrlResource("myCluster", "myNodeA", "toto/properties"), HttpMethod.GET, entity, String.class);
        
        assertTrue(response.getHeaders().getContentType().toString().startsWith(MediaType.TEXT_PLAIN.toString()));
        assertFalse(response.getBody().isEmpty());
        
        assertTrue(response.getBody().contains("totoBisToto=")); // fr.ffremont.toto
        assertTrue(response.getBody().contains("totoBis=")); // fr.ffremont
        assertTrue(response.getBody().contains("toto=")); // fr
    }
    
    @Test
    public void msGetBinary404(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/java-archive");
        HttpEntity<Resource> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Resource> response = this.restTemplate.exchange(this.getUrlResource("myCluster", "myNodeA", "toti/binary"), HttpMethod.GET, entity, Resource.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
