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
public class StartTask implements IMicroServiceTask {

    /**
     * Syntaxe : java [-options] class [args...] (pour l'exécution d'une classe)
     * ou java [-options] -jar jarfile [args...] (pour l'exécution d'un fichier
     * JAR
     *
     * @param ms
     */
    @Override
    public void run(MicroServiceTask task) {
        ProcessBuilder ps = new ProcessBuilder("java", "-jar");
    }

}
