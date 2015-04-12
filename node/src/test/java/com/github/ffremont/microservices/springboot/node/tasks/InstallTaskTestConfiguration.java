/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.NodeHelper;
import com.github.ffremont.microservices.springboot.node.services.MsService;
import java.io.IOException;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author florent
 */
public class InstallTaskTestConfiguration {

    public SimpleTestConfiguration simpleTest = new SimpleTestConfiguration();

    @Bean
    public NodeHelper getNodeHelper() {
        return Mockito.mock(NodeHelper.class);
    }
    
    @Bean
    public InstallJarTask getInstallJarTask() {
        return Mockito.mock(InstallJarTask.class);
    }

    @Bean
    public InstallPropertiesTask getInstallPropertiesTask() {
        return Mockito.mock(InstallPropertiesTask.class);
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
    public InstallTask getInstallTask() {
        return new InstallTask();
    }

    @Bean
    public PropertyPlaceholderConfigurer getProps() throws IOException {
        return simpleTest.getProps();
    }
}
