/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.NodeHelper;
import com.github.ffremont.microservices.springboot.node.exceptions.FailStartedException;
import com.github.ffremont.microservices.springboot.node.exceptions.FileMsNotFoundException;
import com.github.ffremont.microservices.springboot.node.services.PsCommand;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
public class StartTask implements IMicroServiceTask {

    
    private final static Logger LOG = LoggerFactory.getLogger(StartTask.class);
    
    @Value("${app.java.exec:}")
    private String javaExec;
    
    @Autowired
    private NodeHelper helper;
    
    
    /**
     * Syntaxe : java [-options] class [args...] (pour l'exécution d'une classe)
     * ou java [-options] -jar jarfile [args...] (pour l'exécution d'un fichier
     * JAR
     *
     * @param task
     * @throws com.github.ffremont.microservices.springboot.node.exceptions.FailStartedException
     * @throws com.github.ffremont.microservices.springboot.node.exceptions.FileMsNotFoundException
     */
    @Override
    public void run(MicroServiceTask task) throws FailStartedException, FileMsNotFoundException {
        LOG.info("Démarrage du micro service {}", task.getMs().getName());
        
        Path jar = helper.targetJarOf(task.getMs());
        
        if(!Files.exists(jar) || !Files.exists(Paths.get(helper.targetDirOf(task.getMs()).toString(), InstallTask.CHECKSUM_FILE_NAME+".txt"))){
            throw new FileMsNotFoundException("Jar inexistant ou invalide");
        }
        
        String javaEx = this.javaExec.isEmpty() ? System.getProperty("java.home")+"/bin/java" : this.javaExec;
        
        ProcessBuilder ps = new ProcessBuilder(javaEx, "-jar", helper.targetJarOf(task.getMs()).toString(), "&");
        ps.directory(helper.targetDirOf(task.getMs()).toFile());
        ps.redirectErrorStream(true);
        ps.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        
        try {
            LOG.info("Run de {}", ps.command().toString());
             ps.start();
        } catch (IOException ex) {
           throw new FailStartedException("Impossible de démarrer le programme java : "+task.getMs().getId(), ex);
        }
        
        LOG.info("Micro service {} démarré", task.getMs().getName());
    }

    public NodeHelper getHelper() {
        return helper;
    }

    public void setHelper(NodeHelper helper) {
        this.helper = helper;
    }

    public String getJavaExec() {
        return javaExec;
    }

    public void setJavaExec(String javaExec) {
        this.javaExec = javaExec;
    }
}
