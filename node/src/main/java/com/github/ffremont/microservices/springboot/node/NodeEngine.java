/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node;

import com.github.ffremont.microservices.springboot.node.exceptions.FailStartedException;
import com.github.ffremont.microservices.springboot.node.exceptions.FailStopedException;
import com.github.ffremont.microservices.springboot.node.exceptions.FileMsNotFoundException;
import com.github.ffremont.microservices.springboot.node.exceptions.TaskException;
import com.github.ffremont.microservices.springboot.node.services.MsService;
import com.github.ffremont.microservices.springboot.node.services.PsCommand;
import com.github.ffremont.microservices.springboot.node.tasks.InstallTask;
import com.github.ffremont.microservices.springboot.node.tasks.MicroServiceTask;
import com.github.ffremont.microservices.springboot.node.tasks.ShutdownTask;
import com.github.ffremont.microservices.springboot.node.tasks.StartTask;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import com.github.ffremont.microservices.springboot.pojo.MsEtatRest;
import java.util.List;
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
public class NodeEngine {

    private final static Logger LOG = LoggerFactory.getLogger(NodeEngine.class);

    @Autowired
    private MsService msService;
    
    @Autowired
    private InstallTask installTask;
    
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
        for (MicroServiceRest ms : lesMs) {
            try{
                // version courante - NOT RUNNING ?
                if (!psResult.isRunning(ms.getIdVersion())) {
                    // ancienne version running ?
                    if (psResult.isRunning(ms.getId())) {
                        shutdownTask.run(new MicroServiceTask(ms));
                    }

                    if (MsEtatRest.ACTIF.equals(ms.getMsEtat())) {
                        try {
                            startTask.run(new MicroServiceTask(ms));
                        } catch (FileMsNotFoundException ex) {
                            LOG.warn("Démarrage du micro service impossible : "+ms.getId(), ex);
                            
                            
                            //installTask.run();
                        } 
                    }
                }
            } catch (TaskException ex) {
                LOG.error("Arrêt du micro service impossible : "+ms.getId());
            }
        }
    }
}
