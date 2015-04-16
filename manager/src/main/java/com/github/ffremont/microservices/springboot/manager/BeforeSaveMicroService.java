/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager;

import com.github.ffremont.microservices.springboot.manager.models.MicroService;
import com.mongodb.DBObject;
import java.util.Date;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
public class BeforeSaveMicroService extends AbstractMongoEventListener<MicroService> {

    @Override
    public void onBeforeSave(MicroService source, DBObject dbo) {
        source.setVersion(source.generateVersion());
        source.setLastModified(new Date());

        super.onBeforeSave(source, dbo);
    }

}
