/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.services.MsService;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author florent
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SimpleTestConfiguration.class)
@ActiveProfiles("test")
public class InstallPropertiesTaskTest extends AbstractInstallTest{
    
    private final static Logger LOG = LoggerFactory.getLogger(InstallPropertiesTaskTest.class);
    
    @Autowired
    private InstallPropertiesTask task;
    
    @Autowired
    private MsService msService;
    
    /**
     *
     * @throws IOException
     */
    @Before
    @Override
    public void before() throws IOException{
        Mockito.reset(msService);
        
        super.before();
    }

    @Test
    public void testRun() throws Exception {
        MicroServiceTask msTask = this.create();
        MicroServiceRest ms = msTask.getMs();
        
        Path propSrc = Paths.get(Thread.currentThread().getContextClassLoader().getResource("samples.ms.properties").toURI());
        byte[] propContent = Files.readAllBytes(propSrc);
        when(msService.getContentOfProperties(eq(ms.getName()))).thenReturn(propContent);
        
        Path msVersionFolder = Paths.get(this.nodeBase, ms.getVersion());
        Files.createDirectory(msVersionFolder);
        
        task.run(msTask);
        
        Path appProp = Paths.get(msVersionFolder.toString(), "application.properties");
        assertTrue(Files.exists( appProp ));
        assertArrayEquals(propContent, Files.readAllBytes(appProp));
    }
    
}
