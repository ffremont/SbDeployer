/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.exceptions.InvalidInstallationException;
import com.github.ffremont.microservices.springboot.node.exceptions.TaskException;
import static com.github.ffremont.microservices.springboot.node.tasks.InstallTask.CHECKSUM_FILE_NAME;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
public class InstallJarTask implements IMicroServiceTask {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(InstallJarTask.class);

    @Value("${app.base}")
    private String nodeBase;

    @Override
    public void run(MicroServiceTask task) throws TaskException {
        Path msVersionFolder = Paths.get(this.nodeBase, task.getMs().getVersion());
        Path checksumPath = Paths.get(msVersionFolder.toString(), InstallTask.CHECKSUM_FILE_NAME + ".txt");
        Path jarTarget = Paths.get(msVersionFolder.toString(), task.getMs().getId() + ".jar");
        
        try {
            Files.copy(task.getJar(), jarTarget);

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            DigestInputStream digestIs = new DigestInputStream(new FileInputStream(jarTarget.toFile()), md);
            byte[] buffer = new byte[10240]; // 10ko
            while (0 < digestIs.read(buffer)) {}
            
            byte[] checksumTarget = digestIs.getMessageDigest().digest();

            if (!Arrays.toString(checksumTarget).equals(task.getChecksum())) {
                throw new InvalidInstallationException("Le checksum n'est pas valide suite à la copie : " + this.nodeBase);
            }

            Files.write(checksumPath, task.getChecksum().getBytes());
            LOG.debug("Création du fichier checksum.txt");
        } catch (IOException | NoSuchAlgorithmException ex) {
            throw new InvalidInstallationException("Impossible d'installer le jar", ex);
        }
    }

}
