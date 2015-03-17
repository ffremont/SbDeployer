/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager;

import com.github.fakemongo.Fongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

/**
 *
 * @author florent
 */
@Configuration
public class MongoTestConfiguration {

    private final static Logger LOG = LoggerFactory.getLogger(MongoTestConfiguration.class);
    
    @Value("${spring.data.mongodb.database}")
    private String db;
    
    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        Fongo fongo = new Fongo("mongoServer1");
        
        return new SimpleMongoDbFactory(fongo.getMongo(), db);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }
}
