/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.nexus;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
@ConfigurationProperties(prefix="nexus")
public class NexusProperties {
    private String baseurl;
    private List<String> repo = new ArrayList<>();

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    public List<String> getRepo() {
        return repo;
    }

    public void setRepo(List<String> repo) {
        this.repo = repo;
    }
    
    
}
