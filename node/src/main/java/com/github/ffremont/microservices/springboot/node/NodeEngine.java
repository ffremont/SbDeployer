/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node;

import com.github.ffremont.microservices.springboot.node.services.MsService;
import com.github.ffremont.microservices.springboot.node.services.PsCommand;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import com.github.ffremont.microservices.springboot.pojo.MsEtatRest;
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

    @Value("${app.node}")
    private String node;

    public void update() {
        LOG.info("Démarrage du node \"{}\"", node);

        // récup des ms du node
        List<MicroServiceRest> lesMs = msService.getMicroServices();

        PsCommand.PsCommandResult psResult = (new PsCommand()).exec();
        for (MicroServiceRest ms : lesMs) {
            // version courante - NOT RUNNING ?
            if (!psResult.contains(ms.getIdVersion())) {
                // ancienne version running ?
                if (psResult.contains(ms.getId())) {
                    LOG.info("Arrêt du microService {}", ms.getId());
                    // kill 
                }

                if (MsEtatRest.ACTIF.equals(ms.getMsEtat())) {
                    // start le ms
                }

            }

        }
    }
}
