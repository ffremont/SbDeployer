/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.exceptions;

/**
 *
 * @author florent
 */
public class InvalidInstallationException extends TaskException {

    public InvalidInstallationException(String msg) {
        super(msg);
    }
    
    public InvalidInstallationException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
