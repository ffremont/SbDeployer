/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.services;

import com.github.ffremont.microservices.springboot.node.exceptions.FailUpdateException;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.util.List;
import javax.ws.rs.NotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author florent
 */
@Component
public class MsService {
    
    @Value("${app.cluster}")
    private String cluster;
    
    @Value("${app.node}")
    private String node;
    
    @Autowired
    private RestTemplate restTempate;
    
    public List<MicroServiceRest> getMicroServices(){
        throw new NotSupportedException("not supported");
    }
    
    public List<MicroServiceRest> getMs(){
        throw new NotSupportedException("not supported");
    }
    
    public byte[] getContentOfProperties(String msName){
        throw new NotSupportedException("not supported");
    } 
    
    public void changeVersion() throws FailUpdateException{
        throw new NotSupportedException("not supported");
    }
}
