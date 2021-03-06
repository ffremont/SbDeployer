/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.services;

import com.github.ffremont.microservices.springboot.pojo.MicroServiceRest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.ws.rs.NotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author florent
 */
@Component
public class MsService {

    private final static Logger LOG = LoggerFactory.getLogger(MsService.class);

    private final static String MS_JSON_TYPE_MIME = "application/vnd.microservice+json";

    @Value("${app.cluster}")
    private String cluster;

    @Value("${app.node}")
    private String node;

    @Value("${app.master.host}")
    private String masterhost;

    @Value("${app.master.port}")
    private String masterPort;

    @Value("${app.master.contextRoot}")
    private String masterCR;

    @Value("${app.master.user}")
    private String username;

    @Value("${app.master.pwd}")
    private String password;

    @Autowired
    private RestTemplate restTempate;

    public List<MicroServiceRest> getMicroServices() {
        MasterUrlBuilder builder = new MasterUrlBuilder(cluster, node, masterhost, masterPort, masterCR);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.parseMediaType(MS_JSON_TYPE_MIME)));
        HttpEntity<MicroServiceRest> entity = new HttpEntity<>(headers);

        ResponseEntity<MicroServiceRest[]> response = restTempate.exchange(builder.build(), HttpMethod.GET, entity, MicroServiceRest[].class);

        return HttpStatus.OK.equals(response.getStatusCode()) ? new ArrayList<>(Arrays.asList(response.getBody())) : null;
    }

    /**
     * Retourne un MS
     *
     * @param msName
     * @return
     */
    public MicroServiceRest getMs(String msName) {
        MasterUrlBuilder builder = new MasterUrlBuilder(cluster, node, masterhost, masterPort, masterCR);
        builder.setUri(msName);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.parseMediaType(MS_JSON_TYPE_MIME)));
        HttpEntity<MicroServiceRest> entity = new HttpEntity<>(headers);

        ResponseEntity<MicroServiceRest> response = restTempate.exchange(builder.build(), HttpMethod.GET, entity, MicroServiceRest.class);

        return HttpStatus.OK.equals(response.getStatusCode()) ? response.getBody() : null;
    }

    /**
     * Retourne le flux à lire
     *
     * @param msName
     * @return
     */
    public Path getBinary(String msName) {
        MicroServiceRest ms = this.getMs(msName);

        if (ms == null) {
            return null;
        }

        MasterUrlBuilder builder = new MasterUrlBuilder(cluster, node, masterhost, masterPort, masterCR);
        builder.setUri(msName + "/binary");

        Path tempFileBinary = null;
        try {
            tempFileBinary = Files.createTempFile("node", "jarBinary");
            HttpURLConnection managerConnection = (HttpURLConnection) (new URL(builder.build())).openConnection();
            managerConnection.setRequestProperty("Authorization", "Basic ".concat(new String(Base64.getEncoder().encode((username + ":" + password).getBytes()))));
            managerConnection.setRequestProperty("Accept", "application/java-archive");
            managerConnection.connect();

            if (managerConnection.getResponseCode() != 200) {
                LOG.warn("Manager : récupération impossible, statut {}", managerConnection.getResponseCode());
                return tempFileBinary;
            }

            FileOutputStream fos = new FileOutputStream(tempFileBinary.toFile());
            try (InputStream is = managerConnection.getInputStream()) {
                byte[] buffer = new byte[10240]; // 10ko
                int read;
                while (-1 != (read = is.read(buffer))) {
                    fos.write(buffer, 0, read);
                }
                fos.flush();
                is.close();
            }
        } catch (IOException ex) {
            LOG.error("Impossible de récupérer le binaire", ex);
            if (tempFileBinary != null) {
                try {
                    Files.delete(tempFileBinary);
                } catch (IOException e) {
                }
            }
        }

        return tempFileBinary;
    }

    public byte[] getContentOfProperties(String msName) {
        MasterUrlBuilder builder = new MasterUrlBuilder(cluster, node, masterhost, masterPort, masterCR);
        builder.setUri(msName + "/properties");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
        HttpEntity<byte[]> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTempate.exchange(builder.build(), HttpMethod.GET, entity, byte[].class);

        return HttpStatus.OK.equals(response.getStatusCode()) ? response.getBody() : null;
    }
}
