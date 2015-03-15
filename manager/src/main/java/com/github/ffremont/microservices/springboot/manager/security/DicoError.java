/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.security;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
public class DicoError {

    public final static int DEFAULT_ERROR_CODE = 1;
    
    private Map<Integer, String> errors;
    
    public DicoError(){
        this.errors = new HashMap<>();
        errors.put(1, "Erreur interne");
    }

    public Error findError(int code){
        if(this.errors.containsKey(code)){
            return new Error(code, this.errors.get(code));
        }else{
            return new Error(DEFAULT_ERROR_CODE, this.errors.get(DEFAULT_ERROR_CODE));
        }
    }
}
