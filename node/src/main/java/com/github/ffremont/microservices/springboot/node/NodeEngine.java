/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node;

import com.github.ffremont.microservices.springboot.node.exceptions.FailStopedException;
import com.github.ffremont.microservices.springboot.node.exceptions.FileMsNotFoundException;
import com.github.ffremont.microservices.springboot.node.exceptions.InvalidInstallationException;
import com.github.ffremont.microservices.springboot.node.exceptions.TaskException;
import com.github.ffremont.microservices.springboot.node.services.MsService;
import com.github.ffremont.microservices.springboot.node.services.PsCommand;
import com.github.ffremont.microservices.springboot.node.tasks.InstallTask;
import com.github.ffremont.microservices.springboot.node.tasks.MicroServiceTask;
import com.github.ffremont.microservices.springboot.node.tasks.ShutdownTask;
import com.github.ffremont.microservices.springboot.node.tasks.StartTask;
import com.github.ffremont.microservices.springboot.node.tasks.UninstallTask;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import com.github.ffremont.microservices.springboot.pojo.MsEtatRest;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
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
public class NodeEngine {

    private final static Logger LOG = LoggerFactory.getLogger(NodeEngine.class);

    @Autowired
    private MsService msService;

    @Autowired
    private InstallTask installTask;

    @Autowired
    private UninstallTask unInstallTask;

    @Autowired
    private StartTask startTask;

    @Autowired
    private ShutdownTask shutdownTask;

    @Value("${app.node}")
    private String node;

    public void update() {
        LOG.debug("Mise à jour du node \"{}\"", node);

        // récup des ms du node
        List<MicroServiceRest> lesMs = msService.getMicroServices();

        PsCommand.PsCommandResult psResult = (new PsCommand()).exec();
        MicroServiceTask msTask;
        for (MicroServiceRest ms : lesMs) {
            try {
                msTask = new MicroServiceTask(ms);

                if (MsEtatRest.INACTIF.equals(ms.getMsEtat()) && psResult.isRunning(ms.getId())) {
                    try {
                        shutdownTask.run(msTask);
                    } catch (FailStopedException fe) {
                        LOG.warn("Arrêt du micro service impossible", fe);
                    }
                }

                // version courante - NOT RUNNING ? 
                if (MsEtatRest.ACTIF.equals(ms.getMsEtat()) && !psResult.isRunning(ms.getIdVersion())) {
                    // ancienne version running ?
                    if (psResult.isRunning(ms.getId())) {
                        try {
                            shutdownTask.run(msTask);
                        } catch (FailStopedException fe) {
                            LOG.warn("Arrêt du micro service impossible", fe);
                        }
                    }

                    try {
                        startTask.run(msTask);
                    } catch (FileMsNotFoundException ex) {
                        LOG.warn("Démarrage du micro service impossible : " + ms.getId(), ex);

                        try {
                            unInstallTask.run(msTask);
                        } catch (InvalidInstallationException e) {
                            LOG.warn("Désinstallation impossible", e);
                        }
                        msTask.setJar(msService.getBinary(ms.getName()));
                        try {
                            LOG.info("Tentative d'installation du microservice {}", ms.getId());
                            installTask.run(msTask);
                        } finally {
                            if (msTask.getJar() != null) {
                                try {
                                    Files.delete(msTask.getJar());
                                } catch (IOException ex1) {
                                    LOG.warn("Suppression du binaire temporaire impossible", ex1);
                                }
                            }
                        }
                    }

                }
            } catch (TaskException ex) {
                LOG.error("Mise à jour du micro service impossible : " + ms.getId(), ex);
            }
        }
    }
}
