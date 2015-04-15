/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.NodeHelper;
import com.github.ffremont.microservices.springboot.node.exceptions.InvalidInstallationException;
import com.github.ffremont.microservices.springboot.node.services.MsService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class InstallPropertiesTask implements IMicroServiceTask {

    private final static Logger LOG = LoggerFactory.getLogger(InstallPropertiesTask.class);
    
    @Autowired
    private NodeHelper helper;
    
    @Autowired
    private MsService msService;
    
    @Override
    public void run(MicroServiceTask task) throws InvalidInstallationException {
        Path msVersionFolder = helper.targetDirOf(task.getMs());
        
        byte[] content = msService.getContentOfProperties(task.getMs().getName());
        Path applicationProp = Paths.get(msVersionFolder.toString(), "application.properties");
        try {
            Files.write(applicationProp, content);
            LOG.info("Création du fichier de propriétés {}", applicationProp.toAbsolutePath());
        } catch (IOException ex) {
            throw new InvalidInstallationException("Impossible d'installer le fichier de properties", ex);
        }
    }

}
