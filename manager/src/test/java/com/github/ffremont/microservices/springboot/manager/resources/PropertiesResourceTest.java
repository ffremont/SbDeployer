/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.resources;

import com.github.fakemongo.junit.FongoRule;
import com.github.ffremont.microservices.springboot.manager.JerseyConfig;
import com.github.ffremont.microservices.springboot.manager.SbManagerWorldApp;
import com.github.ffremont.microservices.springboot.manager.models.Property;
import com.github.ffremont.microservices.springboot.manager.models.repo.IPropertyRepo;
import com.github.ffremont.microservices.springboot.manager.stores.ValidPropertiesStore;
import com.github.ffremont.microservices.springboot.pojo.PropertyRest;
import java.util.Arrays;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
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
@SpringApplicationConfiguration(classes = SbManagerWorldApp.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
@ActiveProfiles("test")
public class PropertiesResourceTest {

    private final static Logger LOG = LoggerFactory.getLogger(PropertiesResourceTest.class);

    @Value("${local.server.port}")
    int port;

    RestTemplate restTemplate = new TestRestTemplate("admin", "secret");

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IPropertyRepo propertyRepo;

    
    private String getUrlResource(String namespace, String name) {
        return this.getUrlResource(namespace+"/"+name);
    }
    
    private String getUrlResource(String suffix) {
        return "http://localhost:" + port + "/" + JerseyConfig.APP_PATH + "/" + PropertiesResource.PATH + "/" + suffix;
    }

    @After
    public void after(){
        this.mongoTemplate.getDb().dropDatabase();
    }
    
    @Test
    public void getPropertyOkTest() {
        ValidPropertiesStore.provide(mongoTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.parseMediaType(PropertiesResource.TYPE_MIME)));
        HttpEntity<PropertyRest> entity = new HttpEntity<>(headers);
        ResponseEntity<PropertyRest> response = this.restTemplate.exchange(this.getUrlResource("fr", "toto"), HttpMethod.GET, entity, PropertyRest.class);

        assertTrue(response.getHeaders().getContentType().toString().startsWith(PropertiesResource.TYPE_MIME));
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals("toto", response.getBody().getName());
        assertEquals("myValue", response.getBody().getValue());
        assertEquals("fr", response.getBody().getNamespace());
    }
    

    @Test
    public void getPropertyNotFOundTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.parseMediaType(PropertiesResource.TYPE_MIME)));
        HttpEntity<PropertyRest> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = this.restTemplate.exchange(this.getUrlResource("fr", "toto"), HttpMethod.GET, entity, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    public void addPropertyTest() {
        PropertyRest prop = new PropertyRest("myName", "myNameSpace", "myValue");
        String uri = this.getUrlResource("fr", "toto");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(PropertiesResource.TYPE_MIME));
        HttpEntity<PropertyRest> entity = new HttpEntity<>( prop, headers);
        ResponseEntity<String> response = this.restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(uri, response.getHeaders().getLocation().toString());
    }
    
    @Test
    public void addPropertyInvalidTest() {
        PropertyRest prop = new PropertyRest(null, null, null);
        String uri = this.getUrlResource("fr", "toto");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(PropertiesResource.TYPE_MIME));
        HttpEntity<PropertyRest> entity = new HttpEntity<>( prop, headers);
        ResponseEntity<String> response = this.restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
    
    @Test
    public void addPropertyKoDuplicateTest() {
        PropertyRest prop = new PropertyRest("myName", "myNameSpace", "myValue");
        String uri = this.getUrlResource("fr", "toto");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(PropertiesResource.TYPE_MIME));
        HttpEntity<PropertyRest> entity = new HttpEntity<>( prop, headers);
        
        ResponseEntity<String> response = this.restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        
        response = this.restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    public void delPropertyTest() {
        ValidPropertiesStore.provide(mongoTemplate);
        Property prop = this.propertyRepo.findOneByNamespaceAndName("fr.ffremont", "totoBis");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.parseMediaType(PropertiesResource.TYPE_MIME)));
        HttpEntity<Property> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = this.restTemplate.exchange(this.getUrlResource(prop.getId()), HttpMethod.DELETE, entity, String.class);

        assertNull(response.getHeaders().getContentType());
        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
    }
}
