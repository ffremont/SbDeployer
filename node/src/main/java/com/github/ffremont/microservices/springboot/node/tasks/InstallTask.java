/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.exceptions.FailCreateMsException;
import com.github.ffremont.microservices.springboot.node.exceptions.InvalidInstallationException;
import com.github.ffremont.microservices.springboot.node.exceptions.TaskException;
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
public class InstallTask implements IMicroServiceTask {

    private final static Logger LOG = LoggerFactory.getLogger(InstallTask.class);

    public final static String CHECKSUM_FILE_NAME = "checksum";

    @Value("${app.base}")
    private String nodeBase;
    
    @Autowired
    private InstallJarTask installJarTask;

    @Autowired
    private InstallPropertiesTask installPropTask;
    
    /**
     * Installation
     *
     * @param task
     * @throws
     * com.github.ffremont.microservices.springboot.node.exceptions.InvalidInstallationException
     * @throws
     * com.github.ffremont.microservices.springboot.node.exceptions.FailCreateMsException
     */
    @Override
    public void run(MicroServiceTask task) throws InvalidInstallationException, FailCreateMsException, TaskException {
        LOG.info("Installation du micro service {}...", task.getMs().getName());
        Path msVersionFolder = Paths.get(this.nodeBase, task.getMs().getName(), task.getMs().getVersion());
        LOG.debug("Répertoire d'installation {}", msVersionFolder);

        try {
            if (Files.notExists(msVersionFolder)) {
                Files.createDirectories(msVersionFolder);
                LOG.debug("Répertoire créé");
            }

            // vérification du CHECKSUM
            Path checksumPath = Paths.get(msVersionFolder.toString(), CHECKSUM_FILE_NAME + ".txt");

            if (Files.exists(checksumPath)) {
                LOG.debug("Checksum récupéré {}", checksumPath);
                if (!task.getMs().getSha1().equals(new String(Files.readAllBytes(checksumPath)))) {
                    throw new InvalidInstallationException("Le checksum n'est pas valide : " + this.nodeBase);
                }
            } else {
                installJarTask.run(task);
            }

            installPropTask.run(task);
        } catch (IOException ex) {
            LOG.warn("Anomalie non prévue lors de l'installation", ex);
            throw new FailCreateMsException("Installation impossible", ex);
        }
        
        LOG.info("Micro service {} installé", task.getMs().getName());
    }

}
