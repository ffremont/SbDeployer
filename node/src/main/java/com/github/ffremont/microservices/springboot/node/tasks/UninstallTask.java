/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.NodeHelper;
import com.github.ffremont.microservices.springboot.node.exceptions.InvalidInstallationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
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
public class UninstallTask implements IMicroServiceTask {

    private final static Logger LOG = LoggerFactory.getLogger(UninstallTask.class);
    
    @Autowired
    private NodeHelper helper;
    
    /**
     * 
     * @param path
     * @throws IOException 
     */
    private void remove(Path path) throws IOException{
        Files.list(path).forEach((Path item) -> {
            try{
                if(Files.isDirectory(item)){
                    this.remove(item);
                }else{
                     Files.delete(item);
                }
            }catch(IOException e){
                LOG.error("Impossible de supprmimer les éléments dans "+item.toAbsolutePath(), e);
            }
        });
        
         Files.delete(path);
    }

    /**
     * Suppression du répertoire lié à la version
     *
     * @param task
     * @throws com.github.ffremont.microservices.springboot.node.exceptions.InvalidInstallationException
     */
    @Override
    public void run(MicroServiceTask task) throws InvalidInstallationException {
        LOG.info("Désinstallation du micro service {}...", task.getMs().getName());
        try {
            this.remove(helper.targetDirOf(task.getMs()));
        } catch (IOException ex) {
            throw new InvalidInstallationException("Impossible de désinstaller", ex);
        }
        
        LOG.info("Micro service {} désinstallé", task.getMs().getName());
    }

}
