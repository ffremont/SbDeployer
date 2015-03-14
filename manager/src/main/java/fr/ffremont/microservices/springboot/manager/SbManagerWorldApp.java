package fr.ffremont.microservices.springboot.manager;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author florent
 */
@SpringBootApplication(exclude=ErrorMvcAutoConfiguration.class)
public class SbManagerWorldApp extends SpringBootServletInitializer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SbManagerWorldApp.class);
    }

    public static void main(String[] args) {
        new SbManagerWorldApp().configure(new SpringApplicationBuilder(SbManagerWorldApp.class)).run(args);
    }
}
