/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.security;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author florent
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    private final static Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

    @Inject
    private AppSecurityProperties appSecurity;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOG.debug("AuthFilter process...");
        
        String authValue = requestContext.getHeaderString("authorization");
        String username, password;

        if ((authValue != null) && !authValue.isEmpty()) {
            String userPwd = new String(Base64.getDecoder().decode(authValue.replace("Basic ", "")));
            String[] userPwdArray = userPwd.contains(":") ? userPwd.split(":") : new String[]{};
            if (userPwdArray.length == 2) {
                username = userPwdArray[0];
                password = userPwdArray[1];

                if (appSecurity.getUsers().containsKey(username)) {
                    Map<String, String> configUser = (Map<String, String>) appSecurity.getUsers().get(username);

                    if (password.equals(configUser.get("pwd"))) {
                        requestContext.setSecurityContext(new UserSecurityContext(new UserPrincipal(new UserDetail(username, configUser.get("pwd"), configUser.get("role")))));
                    }
                }
            }
        }

        if (requestContext.getSecurityContext().getUserPrincipal() == null) {
            requestContext.setSecurityContext(new UserSecurityContext(new UserPrincipal(null)));
        }
    }

}
