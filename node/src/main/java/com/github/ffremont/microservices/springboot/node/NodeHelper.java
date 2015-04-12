/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node;

import com.github.ffremont.microservices.springboot.node.tasks.MicroServiceTask;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
public class NodeHelper {

    @Value("${app.base}")
    private String nodeBase;

    /**
     *
     * @param ms
     * @return
     */
    public Path targetDirOf(MicroServiceRest ms) {
        return Paths.get(this.nodeBase, ms.getName(), ms.getVersion());
    }
    
    public Path targetJarOf(MicroServiceRest ms) {
        return Paths.get(this.targetDirOf(ms).toString(), NodeHelper.jarNameOf(ms));
    }    
    
    public static String jarNameOf(MicroServiceRest ms){
        return ms.getIdVersion()+".jar";
    }

    public String getNodeBase() {
        return nodeBase;
    }

    public void setNodeBase(String nodeBase) {
        this.nodeBase = nodeBase;
    }
}
