/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager;

import com.github.ffremont.microservices.springboot.manager.models.MicroService;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

/**
 *
 * @author florent
 */
public class BeforeSaveMicroService extends AbstractMongoEventListener<MicroService>{

    @Override
    public void onBeforeSave(MicroService source, DBObject dbo) {
        source.setVersion(source.generateVersion());
        
        super.onBeforeSave(source, dbo); 
    }
    
}
