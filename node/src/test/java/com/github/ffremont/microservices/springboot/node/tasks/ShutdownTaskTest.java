/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.exceptions.FailStopedException;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author florent
 */
public class ShutdownTaskTest {
    
    @Test
    public void testRun() throws URISyntaxException, IOException, FailStopedException {
        MicroServiceRest ms = new MicroServiceRest();
        ms.setName("msClient");
        ms.setVersion("0.0.1");
        ms.setId("AZERTY");
        MicroServiceTask msTask = new MicroServiceTask(ms, Paths.get(Thread.currentThread().getContextClassLoader().getResource("hello.jar").toURI()));

        Path temp = Files.createTempDirectory("testStartTask");
        Files.createDirectories(Paths.get(temp.toString(), msTask.getMs().getName(), msTask.getMs().getVersion()));
        Path jarTarget = Paths.get(temp.toString(), msTask.getMs().getName(), msTask.getMs().getVersion(), msTask.getMs().getIdVersion() + ".jar");
        Files.copy(msTask.getJar(), jarTarget);
        
        new ProcessBuilder("java", "-jar", jarTarget.toString()).start();
        
        ShutdownTask task = new ShutdownTask();
        task.run(msTask);
    }
    
}
