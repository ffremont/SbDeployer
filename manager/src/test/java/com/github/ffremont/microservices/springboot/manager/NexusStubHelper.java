/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 *
 * @author florent
 */
public class NexusStubHelper {
    public static void stub(String g, String a, String v) throws IOException{
        byte[] readAllBytes = Files.readAllBytes(Paths.get(Thread.currentThread().getContextClassLoader().getResource("nexus-"+g+"-"+a+"-"+v+"-mock.json").getPath()));
        
        stubFor(get(urlEqualTo("/nexus/service/local/artifact/maven/resolve?r=releases&g=fr.ffremont&a=myArtifact&v=0.0.1&p=jar"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(new String(readAllBytes))));
    }
}
