/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

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

    public final static String SHA1_HELLO_JAR = "7d2a4fc8ac8c28ea50f0ce79f57908d42757e0fe";

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
        props.putAll(myMap);

        configurer.setProperties(props);
        return configurer;
    }
}
