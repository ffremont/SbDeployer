/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.filters;

import com.github.ffremont.microservices.springboot.manager.annotations.ExpirationCache;
import java.io.IOException;
import java.lang.annotation.Annotation;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

@Provider
@ExpirationCache
public class ExpirationCacheFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        for (Annotation annotation : response.getEntityAnnotations()) {
            if (annotation.annotationType().equals(ExpirationCache.class)) {
                response.getHeaders().putSingle(HttpHeaders.CACHE_CONTROL, "public, max-age=" + ((ExpirationCache) annotation).maxAge());
                break;
            }
        }
    }
}
