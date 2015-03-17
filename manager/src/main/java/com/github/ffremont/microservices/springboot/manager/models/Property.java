/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.models;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author florent
 */
@CompoundIndexes({
    @CompoundIndex(name = "namespace.name", def = "{'namespace' : 1, 'name' : 1}", unique = true)
})
@Document(collection = "properties")
public class Property {
    
    @Id
    private String id;
    
    @NotNull
    @Length(max = 128)
    private String name;
    
    @Length(max = 128)
    private String namespace;
    
    @NotNull
    @Length(max = 256)
    private String value;

    public Property() {
    }

    public Property(String name, String namespace, String value) {
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
