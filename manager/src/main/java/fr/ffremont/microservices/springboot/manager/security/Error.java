/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ffremont.microservices.springboot.manager.security;

/**
 *
 * @author florent
 */
public class Error {
    public final static String TYPE_MIME = "application/vnd.error+json";
    
    private int code;
    private String label;

    public Error(int code, String label) {
        this.code = code;
        this.label = label;
    }
    
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    
}
