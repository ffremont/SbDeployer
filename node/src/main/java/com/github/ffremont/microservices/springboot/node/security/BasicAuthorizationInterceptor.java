/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.security;

import java.io.IOException;
import java.util.Base64;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 *
 * @author florent
 */
public class BasicAuthorizationInterceptor implements ClientHttpRequestInterceptor {

    private final String username;

    private final String password;

    public BasicAuthorizationInterceptor(String username, String password) {
        this.username = username;
        this.password = (password == null ? "" : password);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        byte[] token = Base64.getEncoder().encode((this.username + ":" + this.password).getBytes());
        request.getHeaders().add("Authorization", "Basic " + new String(token));
        return execution.execute(request, body);
    }

}
