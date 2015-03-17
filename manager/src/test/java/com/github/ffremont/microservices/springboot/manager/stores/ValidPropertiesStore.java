/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.stores;

import com.github.ffremont.microservices.springboot.manager.models.Property;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 *
 * @author florent
 */
public class ValidPropertiesStore {
    public static void provide(MongoTemplate mongoTemplate){
        mongoTemplate.insert(new Property("toto", "fr", "myValue"));
        mongoTemplate.insert(new Property("totoBis", "fr.ffremont", "myValueBis"));
        
        mongoTemplate.insert(new Property("totoCom", "com.ffremont", "myValueCom"));
    }
}
