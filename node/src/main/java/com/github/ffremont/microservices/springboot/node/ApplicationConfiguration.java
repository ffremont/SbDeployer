/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node;

import com.github.ffremont.microservices.springboot.node.security.BasicAuthorizationInterceptor;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author florent
 */
@Configuration
public class ApplicationConfiguration {

    @Value("${app.master.user}")
    private String username;
    
    @Value("${app.master.pwd}")
    private String password;
    
    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate rTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = Collections
                .<ClientHttpRequestInterceptor>singletonList(new BasicAuthorizationInterceptor(
                                this.username, this.password));
        rTemplate.setRequestFactory(new InterceptingClientHttpRequestFactory(rTemplate.getRequestFactory(),
                interceptors));
        
        return rTemplate;
    }
}
