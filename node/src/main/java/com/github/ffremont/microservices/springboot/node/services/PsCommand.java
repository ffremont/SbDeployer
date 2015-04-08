/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.services;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        this.ps = new ProcessBuilder("ps", "-eo", "pid,cmd");
    }

    public PsCommandResult exec() {
        try {
            Process process = this.ps.start();

            return new PsCommandResult(IOUtils.toString(process.getInputStream()));
        } catch (IOException io) {
            LOG.error("Impossible de lancer la command 'ps'", io);
        }
        return null;
    }

    public ProcessBuilder getPs() {
        return ps;
    }

    public static class PsCommandResult {

        private Pattern pattern = Pattern.compile("([0-9]+) (.*)");

        private String output;

        public PsCommandResult(String output) {
            this.output = output;
        }

        public boolean isRunning(String id) {
            return this.output.contains(id);
        }

        public String[] lines() {
            return this.output.split("\n");
        }

        /**
         * Retourne le PID à partir d'un identifiant
         *
         * @param id
         * @return
         */
        public String pid(String id) {
            Matcher rr;
            for (String line : this.lines()) {
                if (line.contains(id)) {
                    rr = this.pattern.matcher(line);
                    if (rr.find()) {
                        return rr.group(1);
                    }
                }
            }
            return null;
        }

    }
}
