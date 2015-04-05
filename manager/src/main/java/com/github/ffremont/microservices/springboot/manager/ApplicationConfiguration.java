/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author florent
 */
@Configuration
public class ApplicationConfiguration {

    @Bean
    @Qualifier("nexusRestTemplate")
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
