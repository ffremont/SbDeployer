/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.exceptions.InvalidInstallationException;
import com.github.ffremont.microservices.springboot.node.exceptions.TaskException;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
public class InstallJarTask implements IMicroServiceTask {

    private final static Logger LOG = LoggerFactory.getLogger(InstallJarTask.class);

    @Value("${app.base}")
    private String nodeBase;

    @Override
    public void run(MicroServiceTask task) throws TaskException {
        Path msVersionFolder = Paths.get(this.nodeBase, task.getMs().getName(), task.getMs().getVersion());
        Path checksumPath = Paths.get(msVersionFolder.toString(), InstallTask.CHECKSUM_FILE_NAME + ".txt");
        Path jarTarget = Paths.get(msVersionFolder.toString(), task.getMs().getIdVersion() + ".jar");

        try {
            if(task.getJar() == null){
                throw new InvalidInstallationException("Jar indisponible dans l'objet MicroServiceTask");
            }
            
            Files.copy(task.getJar(), jarTarget);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            
            try (DigestInputStream digestIs = new DigestInputStream(new FileInputStream(jarTarget.toFile()), md)) {
                byte[] buffer = new byte[10240]; // 10ko
                while (0 < digestIs.read(buffer)) {}
                
                digestIs.close();
            } 
            byte[] checksumTarget = md.digest();

            if (!String.format("%032X", new BigInteger(1, checksumTarget)).equals(task.getMs().getSha1().toUpperCase())) {
                throw new InvalidInstallationException("Le checksum n'est pas valide suite à la copie : " + this.nodeBase);
            }

            Files.write(checksumPath, task.getMs().getSha1().getBytes());
            LOG.debug("Création du fichier checksum.txt");
        } catch (IOException | NoSuchAlgorithmException ex) {
            throw new InvalidInstallationException("Impossible d'installer le jar", ex);
        }
    }

}
