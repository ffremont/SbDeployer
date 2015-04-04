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
public class FailCreateMsException extends TaskException {

    public FailCreateMsException(String msg) {
        super(msg);
    }
    
    public FailCreateMsException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
