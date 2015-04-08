/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.nio.file.Path;

/**
 *
 * @author florent
 */
public class MicroServiceTask {
    /**
     * 
     */
    private MicroServiceRest ms;
    
    /**
     * Chemin du fichier binaire
     */
    private Path jar;

    public MicroServiceTask(MicroServiceRest ms) {
        this.ms = ms;
    }
    
    public MicroServiceTask(MicroServiceRest ms, Path path) {
        this.ms = ms;
        this.jar = path;
    }

    public MicroServiceRest getMs() {
        return ms;
    }

    public void setMs(MicroServiceRest ms) {
        this.ms = ms;
    }

    public Path getJar() {
        return jar;
    }

    public void setJar(Path jar) {
        this.jar = jar;
    }
}
