/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.services;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author florent
 */
public class PsCommand {

    private final static Logger LOG = LoggerFactory.getLogger(PsCommand.class);
    
    private ProcessBuilder ps;

    public PsCommand() {
        this.ps = new ProcessBuilder("ps", "-Af");
    }

    public PsCommandResult exec() {
        try {
            Process process = this.ps.start();

            return new PsCommandResult(IOUtils.toString(process.getInputStream()));
        } catch (IOException io) {
            LOG.error("Impossible de lancer la command 'ps'",io);
        }
        return null;
    }

    public ProcessBuilder getPs() {
        return ps;
    }

    public static class PsCommandResult {

        private String output;

        public PsCommandResult(String output) {
            this.output = output;
        }

        public boolean contains(String id) {
            return this.output.contains(id);
        }
    }
}
