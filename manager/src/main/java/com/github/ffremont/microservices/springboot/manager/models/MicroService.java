/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.models;

import java.util.List;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author florent
 */
@CompoundIndexes({
    @CompoundIndex(name = "cluster.node.name", def = "{'cluster' : 1, 'node' : 1, 'name' : 1}", unique = true)
})
@Document(collection = "microservices")
public class MicroService {
    @Id
    private String id;
    
    private String name;
    
    private String gav;
    
    private String url;
    
    private String version;
    
    private String cluster;
    
    private String node;
    
    private List<String> properties;
    
    private String nsProperties;
    
    private MsEtat etat;

    public MicroService() {
        this.etat = MsEtat.Actif;
    }

    public MicroService(String cluster, String node, String name, String gav) {
        this();
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public MsEtat getEtat() {
        return etat;
    }

    public void setEtat(MsEtat etat) {
        this.etat = etat;
    }
    
    public String generateVersion(){
        return UUID.randomUUID().toString();
    }
    
}
