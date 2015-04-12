/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.NodeHelper;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author florent
 */
public class StartTaskTest {

    private final static Logger LOG = LoggerFactory.getLogger(StartTaskTest.class);

    @Test
    public void testRun() throws Exception {
        StartTask task = new StartTask();
        NodeHelper helper = new  NodeHelper();

        MicroServiceRest ms = new MicroServiceRest();
        ms.setName("msClient");
        ms.setVersion("0.0.1");
        ms.setId("AZERTY");
        MicroServiceTask msTask = new MicroServiceTask(ms, Paths.get(Thread.currentThread().getContextClassLoader().getResource("hello.jar").toURI()));

        Path temp = Files.createTempDirectory("testStartTask");
        helper.setNodeBase(temp.toString());
        Files.createDirectories(helper.targetDirOf(ms));
        Files.write(Paths.get( helper.targetDirOf(ms).toString(), InstallTask.CHECKSUM_FILE_NAME+".txt" ), SimpleTestConfiguration.SHA1_HELLO_JAR.getBytes());

        Path jarTarget = Paths.get(temp.toString(), msTask.getMs().getName(), msTask.getMs().getVersion(), msTask.getMs().getIdVersion() + ".jar");
        Files.copy(msTask.getJar(), jarTarget);
      
        helper.setNodeBase(temp.toString());
        task.setHelper(helper);
        task.run(msTask);
    }

}
