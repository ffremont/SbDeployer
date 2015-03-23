/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.mappers;

import com.github.ffremont.microservices.springboot.manager.models.MicroService;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.util.function.Function;

/**
 *
 * @author florent
 */
public class MicroServiceMapper implements Function<MicroService, MicroServiceRest>{

    @Override
    public MicroServiceRest apply(MicroService t) {
        MicroServiceRest msRest = new MicroServiceRest(t.getId(), t.getName(), t.getGav());
        
        msRest.setCluster(t.getCluster());
        msRest.setNode(t.getNode());
        msRest.setNsProperties(t.getNsProperties());
        msRest.setUrl(t.getUrl());
        msRest.setVersion(t.getVersion());
        
        return msRest;
    }
    
}
