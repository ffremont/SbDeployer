/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.pojo;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author florent
 */
public class MicroServiceRest {
    
    private String id;
    
    @NotNull
    @Length(max = 256)
    private String name;
    
    @NotNull
    @Length(max = 256)
    private Gav gav;
    
    @Length(max = 256)
    private String url;
    
    @Length(max = 128)
    private String version;
    
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
    
    @Length(max = 24)
    private String msEtat;
   
    private String sha1;
    
    public MicroServiceRest() {
    }

    public MicroServiceRest(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }
    
    @XmlTransient
    public String getIdVersion(){
        return this.id+"_"+this.version;
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

    public Gav getGav() {
        return gav;
    }

    public void setGav(Gav gav) {
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

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
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

    public String getMsEtat() {
        return msEtat;
    }

    public void setMsEtat(String msEtat) {
        this.msEtat = msEtat;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }
}
