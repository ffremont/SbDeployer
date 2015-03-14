/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ffremont.microservices.springboot.manager.security;

import java.security.Principal;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author florent
 */
public class UserSecurityContext implements SecurityContext{

    private final UserPrincipal userPrincipal;
    
    public UserSecurityContext(UserPrincipal userPrincipal){
        this.userPrincipal = userPrincipal;
    }

    @Override
    public Principal getUserPrincipal() {
        return this.userPrincipal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return (this.userPrincipal == null) || (this.userPrincipal.getUser() == null) ? false : this.userPrincipal.getUser().getRole().equals(role);
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }
}
