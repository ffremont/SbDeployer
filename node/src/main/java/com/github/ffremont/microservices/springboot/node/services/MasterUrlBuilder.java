/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author florent
 */
public class MasterUrlBuilder {
    
    private final static Logger LOG = LoggerFactory.getLogger(MasterUrlBuilder.class);
    
    private String cluster;
    private String node;
    private String host;
    private String port;
    private String contextRoot;
    
    private String uri;

    public MasterUrlBuilder(String cluster, String node, String host, String port, String contextRoot) {
        this.cluster = cluster;
        this.node = node;
        this.host = host;
        this.port = port;
        this.contextRoot = contextRoot;
        this.uri = "";
    }
    
    public String build(){
        String tpl = "http://{h}:{p}/{cr}/clusters/{c}/nodes/{n}/microservices/{uri}";
        
        String url = tpl.replace("{h}", host).replace("{p}", port).replace("{cr}", contextRoot);
        url = url.replace("{c}", cluster).replace("{n}", node).replace("{uri}", uri);
        
        LOG.debug("Build {}", url);
        
        return url;
    }
    
    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public void setContextRoot(String contextRoot) {
        this.contextRoot = contextRoot;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
