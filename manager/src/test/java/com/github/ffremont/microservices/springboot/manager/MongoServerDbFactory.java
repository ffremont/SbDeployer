/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
public class MongoServerDbFactory implements MongoDbFactory{

    public final static String NAME = "mongoServer1";
    
    @Override
    public DB getDb() throws DataAccessException {
        Fongo fongo = new Fongo(NAME);
        return fongo.getDB("mydb");
    }

    @Override
    public DB getDb(String string) throws DataAccessException {
        Fongo fongo = new Fongo(NAME);
        return fongo.getDB(string);
    }

    @Override
    public PersistenceExceptionTranslator getExceptionTranslator() {
        return null;
    }
    
}
