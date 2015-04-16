/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.NodeHelper;
import com.github.ffremont.microservices.springboot.node.services.MsService;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author florent
 */
public class SimpleTestConfiguration {

    public final static String SHA1_HELLO_JAR = "ce367887c52387d73a84c265201f4b14504edc8d";

    @Bean
    public NodeHelper getNodeHelper() {
        return Mockito.mock(NodeHelper.class);
    }
    
    @Bean
    public InstallJarTask getInstallJarTask() {
        return new InstallJarTask();
    }

    @Bean
    public InstallPropertiesTask getInstallPropertiesTask() {
        return new InstallPropertiesTask();
    }

    @Bean
    public InstallTask getInstallTask(){
        return new InstallTask();
    }
    
    @Bean
    public MsService getMockMsService() {
        return Mockito.mock(MsService.class);
    }
    @Bean
    public RestTemplate getRT() {
        return Mockito.mock(RestTemplate.class);
    }

    @Bean
    public PropertyPlaceholderConfigurer getProps() throws IOException {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        Properties props = new Properties();
        Map myMap = new HashMap<>();

        myMap.put("app.base", Files.createTempDirectory("appBaseNode").toString());
        myMap.put("app.cluster", "myCuster");
        myMap.put("app.node", "myNode");
        myMap.put("app.master.host", "localhost");
        myMap.put("app.master.port", "9999");
        myMap.put("app.master.contextRoot", "/test");
        
        myMap.put("app.master.user", "");
        myMap.put("app.master.pwd", "");
        props.putAll(myMap);

        configurer.setProperties(props);
        return configurer;
    }
}
