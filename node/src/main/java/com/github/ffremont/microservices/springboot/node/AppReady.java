/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node;

import com.github.ffremont.microservices.springboot.node.services.MsService;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
public class AppReady implements ApplicationListener<ApplicationStartedEvent>{

    private final static Logger LOG = LoggerFactory.getLogger(AppReady.class);
    
    @Autowired
    private NodeEngine nodeEngine;
    
    @Value("${app.node}")
    private String node;
    
    @Override
    public void onApplicationEvent(ApplicationStartedEvent e) {
        LOG.info("Démarrage du node \"{}\"", node);
        
        nodeEngine.update();
    }
}
