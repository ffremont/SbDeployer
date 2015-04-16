/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.mappers;

import com.github.ffremont.microservices.springboot.manager.models.MicroService;
import com.github.ffremont.microservices.springboot.manager.models.MsEtat;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.util.function.Function;

/** 
 *
 * @author florent
 */
public class MicroServiceFromRestMapper implements Function<MicroServiceRest, MicroService>{

    @Override
    public MicroService apply(MicroServiceRest t) {
        MicroService ms = new MicroService();
        
        ms.setId(t.getId());
        ms.setCluster(t.getCluster());
        ms.setNode(t.getNode());
        ms.setName(t.getName());
        ms.setUrl(t.getUrl());
        ms.setNsProperties(t.getNsProperties());
        ms.setEtat(MsEtat.valueOf(t.getMsEtat()));
        ms.setGav(new com.github.ffremont.microservices.springboot.manager.models.Gav(t.getGav().getGroupId(), t.getGav().getArtifactId(), t.getGav().getClassifier(), t.getGav().getVersion()));
        ms.setProperties(t.getProperties());
        
        return ms;
    }
    
}
