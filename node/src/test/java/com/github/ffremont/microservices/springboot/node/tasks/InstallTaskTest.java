/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.exceptions.InvalidInstallationException;
import static com.github.ffremont.microservices.springboot.node.tasks.InstallTask.CHECKSUM_FILE_NAME;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author florent
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InstallTaskTestConfiguration.class)
@ActiveProfiles("test")
public class InstallTaskTest extends AbstractInstallTest{

    @Autowired
    private InstallJarTask mockInstallJarTask;
    
    @Autowired
    private InstallPropertiesTask mockInstallPropertiesTask;
    
    @Autowired
    private InstallTask task;
    
    @Before
    @Override
    public void before() throws IOException{
        super.before();
        
        reset(mockInstallJarTask);
        reset(mockInstallPropertiesTask);
    }
    
    @Test
    public void testRunFull() throws Exception {
        MicroServiceTask msTask = this.create();
        
        doNothing().when(mockInstallJarTask).run(anyObject());
        doNothing().when(mockInstallPropertiesTask).run(anyObject());
        
        task.run(msTask);
        
        verify(mockInstallJarTask, times(1)).run(anyObject());
        verify(mockInstallPropertiesTask, times(1)).run(anyObject());
    }
    
    @Test
    public void testRunDetectChecksumOk() throws Exception {
        MicroServiceTask msTask = this.create();
        
        doNothing().when(mockInstallJarTask).run(anyObject());
        doNothing().when(mockInstallPropertiesTask).run(anyObject());
        
        Path msVersionFolder = Paths.get(this.nodeBase, msTask.getMs().getVersion());
        Files.createDirectory(msVersionFolder);
        
        Path checksumPath = Paths.get(msVersionFolder.toString(), InstallTask.CHECKSUM_FILE_NAME + ".txt");
        Files.write(checksumPath, msTask.getMs().getSha1().getBytes());
                
        task.run(msTask);
        
        verify(mockInstallJarTask, times(0)).run(anyObject());
        verify(mockInstallPropertiesTask, times(1)).run(anyObject());
    }
    
    @Test(expected = InvalidInstallationException.class)
    public void testRunDetectChecksumKo() throws Exception {
        MicroServiceTask msTask = this.create();
        
        doNothing().when(mockInstallJarTask).run(anyObject());
        doNothing().when(mockInstallPropertiesTask).run(anyObject());
        
        Path msVersionFolder = Paths.get(this.nodeBase, msTask.getMs().getVersion());
        Files.createDirectory(msVersionFolder);
        
        Path checksumPath = Paths.get(msVersionFolder.toString(), InstallTask.CHECKSUM_FILE_NAME + ".txt");
        Files.write(checksumPath, "FAILLLL SHA1".getBytes());
                
        task.run(msTask);
        
        verify(mockInstallJarTask, times(1)).run(anyObject());
        verify(mockInstallPropertiesTask, times(0)).run(anyObject());
    }
    
}
