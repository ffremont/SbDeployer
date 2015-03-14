/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ffremont.microservices.springboot.manager.security;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author florent
 */
@Provider
public class ApplicationExceptionProvider implements ExceptionMapper<Throwable>{

    private final static Logger LOG = LoggerFactory.getLogger(ApplicationExceptionProvider.class);
    
    @Inject
    private DicoError dico;
    
    @Override
    public Response toResponse(Throwable e) {
        if(WebApplicationException.class.isAssignableFrom(e.getClass())){
            LOG.debug("Exception Jaxrs", e);
            return ((WebApplicationException)e).getResponse();
        }else{
            LOG.error("Exception Jaxrs", e);
            return Response.serverError().entity(dico.findError(DicoError.DEFAULT_ERROR_CODE)).type(Error.TYPE_MIME).build();
        }
    }
    
}
