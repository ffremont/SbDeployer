/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
public class ShutdownTask implements IMicroServiceTask{

    @Override
    public void run(MicroServiceTask task) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   
    
}
