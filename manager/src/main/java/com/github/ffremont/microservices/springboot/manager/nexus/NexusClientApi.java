/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.nexus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author florent
 */
@Component
public class NexusClientApi {

    private final static Logger LOG = LoggerFactory.getLogger(NexusClientApi.class);

    @Autowired
    private NexusProperties nexusProps;

    @Autowired
    @Qualifier("nexusRestTemplate")
    private RestTemplate nexusClient;

    /**
     * Retourne la ressource
     *
     * @param groupId
     * @param artifact
     * @param classifier
     * @param version
     * @param packaging
     * @return
     */
    public Resource getBinary(String groupId, String artifact, String packaging, String classifier, String version) {
        String template = nexusProps.getBaseurl() + "/service/local/artifact/maven/redirect?r={r}&g={g}&a={a}&v={v}&p={p}", url;
        Resource resource = null;
        for (String repo : nexusProps.getRepo()) {
            url = template.replace("{r}", repo).replace("{g}", groupId).replace("{a}", artifact).replace("{v}", version).replace("{p}", packaging);
            if(classifier != null){
                url = url.concat("&c="+classifier);
            }

            try {
                ResponseEntity<Resource> response = nexusClient.getForEntity(url, Resource.class);
                resource = response.getBody();
                
                if(HttpStatus.OK.equals(response.getStatusCode())){
                    break;
                }
            } catch (HttpClientErrorException hee) {
                if (!HttpStatus.NOT_FOUND.equals(hee.getStatusCode())) {
                    LOG.warn("Nexus : erreur cliente", hee);
                    throw hee;
                }
            }
        }

        /*Path tempFileBinary = null;
        try {
            tempFileBinary = Files.createTempFile("oo", "");
            
            URL oracle = new URL(template);
            URLConnection yc = oracle.openConnection();
            try (InputStream is = yc.getInputStream()) {
                byte[] buffer = new byte[10240]; // 10ko
                while (0 < is.read(buffer)) {
                    Files.write(tempFileBinary, buffer);
                }
                
                is.close();
            } 
        } catch (IOException ex) {
            if(tempFileBinary != null){
                try {
                    Files.delete(tempFileBinary);
                } catch (IOException e) {}
            }
        }
        try {
            InputStream i = new FileInputStream(tempFileBinary.toFile());
        } catch (FileNotFoundException ex) {
            
        }*/

        return resource;
    }

    /**
     * Retourne la donnée Nexus correspondant au binaire
     *
     * @param groupId
     * @param artifact
     * @param classifier
     * @param version
     * @param packaging
     * @return
     */
    public NexusData getData(String groupId, String artifact, String packaging, String classifier, String version) {
        NexusData data = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.parseMediaType(MediaType.APPLICATION_JSON.toString())));
        HttpEntity<NexusDataResult> entity = new HttpEntity<>(headers);

        String template = nexusProps.getBaseurl() + "/service/local/artifact/maven/resolve?r={r}&g={g}&a={a}&v={v}&p={p}", url;
        for (String repo : nexusProps.getRepo()) {
            url = template.replace("{r}", repo).replace("{g}", groupId).replace("{a}", artifact).replace("{v}", version).replace("{p}", packaging);
            if(classifier != null){
                url = url.concat("&c="+classifier);
            }

            try {
                ResponseEntity<NexusDataResult> response = this.nexusClient.exchange(url, HttpMethod.GET, entity, NexusDataResult.class);

                data = response.getBody().getData();
                break;
            } catch (HttpClientErrorException hee) {
                if (!HttpStatus.NOT_FOUND.equals(hee.getStatusCode())) {
                    LOG.warn("Nexus : erreur cliente", hee);
                    throw hee;
                }
            }
        }

        return data;
    }

    public NexusProperties getNexusProps() {
        return nexusProps;
    }

    public RestTemplate getNexusClient() {
        return nexusClient;
    }
}
