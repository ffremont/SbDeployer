/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node;

import com.github.ffremont.microservices.springboot.node.services.MsService;
import com.github.ffremont.microservices.springboot.node.services.PsCommand;
import com.github.ffremont.microservices.springboot.node.services.PsCommand.PsCommandResult;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
public class Scheduler {

    @Autowired
    private NodeEngine nodeEngine;

    /**
     *
     */
    private final static Logger LOG = LoggerFactory.getLogger(Scheduler.class);

    @Scheduled(fixedDelay = 60000)
    public void update() {
        LOG.info("Mise à jour des micro services...");

        nodeEngine.update();
    }
}
