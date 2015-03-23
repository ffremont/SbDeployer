/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
public class Scheduler {
    
    /**
     * 
     */
    private final static Logger LOG = LoggerFactory.getLogger(Scheduler.class);
    
    @Scheduled(fixedDelay=60000)
    public void update(){
        LOG.info("Mise à jour...");
        
        
    }
}
