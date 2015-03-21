/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.models;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author florent
 */
@Document(collection = "microservices")
public class MicroService {
    @Id
    private String id;
    
    @NotNull
    @Length(max = 256)
    @Indexed(unique = true)
    private String name;
    
    @NotNull
    @Length(max = 256)
    private String gav;
    
    @Length(max = 256)
    private String url;
    
    @Length(max = 128)
    @Indexed(unique = true)
    private String hashLaunch;
    
    @NotNull
    @Length(max = 128)
    private String cluster;
    
    @NotNull
    @Length(max = 128)
    private String node;
    
    @Length(max = 20)
    private List<String> properties;
    
    @Length(max = 128)
    private String nsProperties;
    //private JavaConfig javaConf;

    public MicroService() {
    }

    public MicroService(String cluster, String node, String name, String gav) {
        this.name = name;
        this.gav = gav;
        this.cluster = cluster;
        this.node = node;
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

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
    
    public String getGav() {
        return gav;
    }

    public void setGav(String gav) {
        this.gav = gav;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHashLaunch() {
        return hashLaunch;
    }

    public void setHashLaunch(String hashLaunch) {
        this.hashLaunch = hashLaunch;
    }
    
    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public String getNsProperties() {
        return nsProperties;
    }

    public void setNsProperties(String nsProperties) {
        this.nsProperties = nsProperties;
    }
    
    
    
}
