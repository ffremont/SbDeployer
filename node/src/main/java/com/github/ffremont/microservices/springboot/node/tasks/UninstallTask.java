/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.exceptions.FailCreateMsException;
import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
public class UninstallTask implements IMicroServiceTask {

    @Value("${app.base}")
    private String nodeBase;

    /**
     * Installation
     *
     * @param task
     */
    @Override
    public void run(MicroServiceTask task) {
        
    }

}
