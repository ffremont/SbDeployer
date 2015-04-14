/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.exceptions.FailStopedException;
import com.github.ffremont.microservices.springboot.node.services.PsCommand;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Component
public class ShutdownTask implements IMicroServiceTask {

    private final static Logger LOG = LoggerFactory.getLogger(ShutdownTask.class);

    @Override
    public void run(MicroServiceTask task) throws FailStopedException {
        PsCommand ps = new PsCommand();
        PsCommand.PsCommandResult r = ps.exec();

        String pid = r.pid(task.getMs().getIdVersion());
        LOG.info("Arrêt du micro service {}", pid);

        if (pid != null) {
            try {
                new ProcessBuilder("kill", "-2", pid).start();
            } catch (IOException ex) {
                throw new FailStopedException("Impossible de supprimer le processus : "+pid, ex);
            }
        } else {
            throw new FailStopedException("Impossible de supprimer le processus : "+pid);
        }
    }
}
