package com.github.ffremont.microservices.springboot.node;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author florent
 */
@SpringBootApplication
@EnableScheduling
public class SbNodeWorldApp extends SpringBootServletInitializer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SbNodeWorldApp.class);
    }

    public static void main(String[] args) {
        new SbNodeWorldApp().configure(new SpringApplicationBuilder(SbNodeWorldApp.class)).run(args);
    }
}
