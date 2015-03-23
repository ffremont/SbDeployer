/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.pojo;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author florent
 */
public class PropertyRest {
    private String id;
    
    @NotNull
    @Length(max = 128)
    private String name;
    
    @Length(max = 128)
    private String namespace;
    
    @NotNull
    @Length(max = 256)
    private String value;

    public PropertyRest() {
    }

    public PropertyRest(String name, String namespace, String value) {
        this.name = name;
        this.namespace = namespace;
        this.value = value;
    }

    public PropertyRest(String id, String name, String namespace, String value) {
        this.id = id;
        this.name = name;
        this.namespace = namespace;
        this.value = value;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    
}
