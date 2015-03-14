/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ffremont.microservices.springboot.manager.security;

import java.security.Principal;

/**
 *
 * @author florent
 */
public class UserPrincipal implements Principal{

    private UserDetail user;

    public UserPrincipal(UserDetail user) {
        this.user = user;
    }

    public UserDetail getUser() {
        return user;
    }

    public void setUser(UserDetail user) {
        this.user = user;
    }
    
    @Override
    public String getName() {
        return this.user.getName();
    }
    
}
