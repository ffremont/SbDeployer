/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.NodeHelper;
import com.github.ffremont.microservices.springboot.node.exceptions.InvalidInstallationException;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
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
public class InstallJarTaskTest extends AbstractInstallTest{

    private final static Logger LOG = LoggerFactory.getLogger(InstallJarTaskTest.class);

    @Autowired
    private InstallJarTask task;
    
    @Autowired
    private NodeHelper helper;
    
    @Before
    @Override
    public void before() throws IOException{
        super.before();
        
        reset(helper);
    }

    @Test
    public void testRun() throws Exception {
        MicroServiceTask msTask = this.create();
        MicroServiceRest ms = msTask.getMs();

        Path msVersionFolder = Paths.get(this.nodeBase, ms.getName(), ms.getVersion());
        Files.createDirectories(msVersionFolder);
        when(helper.targetDirOf(anyObject())).thenReturn(msVersionFolder);
        when(helper.targetJarOf(anyObject())).thenReturn(Paths.get(msVersionFolder.toString(), NodeHelper.jarNameOf(ms)));
        
        task.run(msTask);

        Path checksumFile = Paths.get(msVersionFolder.toString(), InstallTask.CHECKSUM_FILE_NAME + ".txt");
        assertTrue(Files.exists(checksumFile));
        assertArrayEquals(ms.getSha1().getBytes(), Files.readAllBytes(checksumFile));

        Path binary = Paths.get(msVersionFolder.toString(), NodeHelper.jarNameOf(ms));
        assertTrue(Files.exists(binary));

        LOG.info("testRun : " + nodeBase);
    }

    @Test(expected = InvalidInstallationException.class)
    public void testRunKoChecksum() throws Exception {
        MicroServiceRest ms = new MicroServiceRest();
        ms.setId("007");
        ms.setName("msClient");
        ms.setVersion("1.0.0");
        ms.setSha1("FAILLL");
        MicroServiceTask msTask = new MicroServiceTask(ms, Paths.get(Thread.currentThread().getContextClassLoader().getResource("hello.jar").toURI()));

        Path msVersionFolder = Paths.get(this.nodeBase, ms.getName(), ms.getVersion());;
        Files.createDirectories(msVersionFolder);
        when(helper.targetDirOf(anyObject())).thenReturn(msVersionFolder);
        when(helper.targetJarOf(anyObject())).thenReturn(Paths.get(msVersionFolder.toString(), NodeHelper.jarNameOf(ms)));

        task.run(msTask);
    }

}
