/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.node.tasks;

import com.github.ffremont.microservices.springboot.node.exceptions.TaskException;

/**
 *
 * @author florent
 */
public interface IMicroServiceTask {
    public void run(MicroServiceTask taks) throws TaskException;
}
