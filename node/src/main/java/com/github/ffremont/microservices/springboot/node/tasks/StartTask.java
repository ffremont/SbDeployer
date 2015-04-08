/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.exceptions.FailStartedException;
import com.github.ffremont.microservices.springboot.node.exceptions.FileMsNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
public class StartTask implements IMicroServiceTask {

    
    private final static Logger LOG = LoggerFactory.getLogger(StartTask.class);
    
    @Value("${app.base}")
    private String nodeBase;
    
    /**
     * Syntaxe : java [-options] class [args...] (pour l'exécution d'une classe)
     * ou java [-options] -jar jarfile [args...] (pour l'exécution d'un fichier
     * JAR
     *
     * @param task
     * @throws com.github.ffremont.microservices.springboot.node.exceptions.FailStartedException
     */
    @Override
    public void run(MicroServiceTask task) throws FailStartedException, FileMsNotFoundException {
        LOG.info("Démarrage du micro service {}", task.getMs().getName());
        
        Path msVersionFolder = Paths.get(this.nodeBase, task.getMs().getName(), task.getMs().getVersion());
        Path jar = Paths.get(msVersionFolder.toString(), task.getMs().getIdVersion()+".jar");
        
        if(Files.exists(jar)){
            throw new FileMsNotFoundException("Jar inexistant");
        }
        
        ProcessBuilder ps = new ProcessBuilder("nohup", "java", "-jar", Paths.get(msVersionFolder.toString(), task.getMs().getIdVersion()+".jar").toString(), ">/dev/null", "2>&1", "&");
        try {
            LOG.info("Run de {}", ps.command().toString());
            ps.start();
        } catch (IOException ex) {
           throw new FailStartedException("Impossible de démarrer le programme java : "+task.getMs().getId(), ex);
        }
        
        LOG.info("Micro service {} démarré", task.getMs().getName());
    }

    public String getNodeBase() {
        return nodeBase;
    }

    public void setNodeBase(String nodeBase) {
        this.nodeBase = nodeBase;
    }
}
