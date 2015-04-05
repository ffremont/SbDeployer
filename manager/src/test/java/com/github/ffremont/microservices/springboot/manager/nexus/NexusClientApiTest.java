/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.nexus;

import com.github.ffremont.microservices.springboot.manager.resources.MicroServiceResource;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author florent
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NexusTestConfiguration.class)
public class NexusClientApiTest {

    private final static Logger LOG = LoggerFactory.getLogger(NexusClientApiTest.class);
    
    @Autowired
    private RestTemplate nexusRestTemplate;
    
    @Autowired
    private NexusProperties prop;
    
    @Autowired
    private NexusClientApi nexus;
    
    @Before
    public void before(){
        Mockito.reset(this.nexusRestTemplate);
    }
    
    @Test
    public void testGetBinary() throws IOException {
        String g = "gg", a = "aa", v = "1.0.0", c = "cc", p = "jar";
        
        // init mock
        String path = Thread.currentThread().getContextClassLoader().getResource("empty.jar").getPath();
        ResponseEntity responseEntity = new ResponseEntity(new FileSystemResource(path), HttpStatus.OK);
        when(this.nexusRestTemplate.getForEntity(prop.getBaseurl()+"/service/local/artifact/maven/redirect?r=snapshots&g="+g+"&a="+a+"&v="+v+"&p="+p+"&c="+c, Resource.class)).thenReturn(responseEntity);
        
        Resource r = nexus.getBinary(g, a, p, c, v);
        
        assertNotNull(r);
        assertNotNull(r.getFile());
        assertTrue(Files.readAllBytes(r.getFile().toPath()).length > 0);
    }

    @Test
    public void testGetData() {
        String g = "gg", a = "aa", v = "1.0.0", c = "cc", p = "jar";
        
        // init mock
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.parseMediaType(MediaType.APPLICATION_JSON.toString())));
        HttpEntity<NexusDataResult> entity = new HttpEntity<>(headers);
        NexusDataResult nexusResult = new NexusDataResult();
        nexusResult.setData(new NexusData());
        ResponseEntity responseEntity = new ResponseEntity(nexusResult, HttpStatus.OK);
        when(this.nexusRestTemplate.exchange(prop.getBaseurl()+"/service/local/artifact/maven/resolve?r=snapshots&g="+g+"&a="+a+"&v="+v+"&p="+p+"&c="+c, HttpMethod.GET, entity, NexusDataResult.class)).thenReturn(responseEntity);
        
        NexusData r = nexus.getData(g, a, p, c, v);
        
        assertNotNull(r);
    }
    
}
