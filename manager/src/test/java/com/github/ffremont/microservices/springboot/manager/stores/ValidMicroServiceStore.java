/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.stores;

import com.github.ffremont.microservices.springboot.manager.models.Gav;
import com.github.ffremont.microservices.springboot.manager.models.MicroService;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 *
 * @author florent
 */
public class ValidMicroServiceStore {
    public static void provide(MongoTemplate mongoTemplate){
        MicroService ms = new MicroService();
        ms.setCluster("myCluster");
        ms.setNode("myNodeA");
        ms.setGav(new Gav("fr.ffremont", "myArtifact", null, "0.0.1"));
        ms.setName("toto");
        ms.setNsProperties("fr.ffremont.toto");
        
        mongoTemplate.insert(ms);
    }
}
