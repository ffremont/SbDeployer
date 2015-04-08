/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author florent
 */
public abstract class AbstractInstallTest {
   
    private final static Logger LOG = LoggerFactory.getLogger(AbstractInstallTest.class);
    
    @Value("${app.base}")
    protected String nodeBase;
    
    private void remove(Path path) throws IOException{
        Files.list(path).forEach(item -> {
            try{
                if(Files.isDirectory(item)){
                    this.remove(item);
                }
                
                Files.delete(item);
            }catch(IOException e){
                LOG.error("Impossible de nettoyer", e);
            }
        });
    }
    
    public void before() throws IOException{
        this.remove(Paths.get(nodeBase));
    }
    
    public MicroServiceTask create(){
        try {
            MicroServiceRest ms = new MicroServiceRest();
            ms.setId("007");
            ms.setName("msClient");
            ms.setVersion("1.0.0");
            ms.setSha1(SimpleTestConfiguration.SHA1_HELLO_JAR);
            
            return new MicroServiceTask(ms, Paths.get(Thread.currentThread().getContextClassLoader().getResource("hello.jar").toURI()));
        } catch (URISyntaxException ex) {
            return null;
        }
    }
}
