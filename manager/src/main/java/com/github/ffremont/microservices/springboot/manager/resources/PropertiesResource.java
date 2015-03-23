/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.resources;

import com.github.ffremont.microservices.springboot.manager.mappers.PropertyMapper;
import com.github.ffremont.microservices.springboot.manager.models.Property;
import com.github.ffremont.microservices.springboot.manager.models.repo.IPropertyRepo;
import com.github.ffremont.microservices.springboot.manager.security.Roles;
import com.github.ffremont.microservices.springboot.pojo.PropertyRest;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 *
 * @author florent
 */
@Path(PropertiesResource.PATH)
@Component
@Scope("request")
@Consumes(PropertiesResource.TYPE_MIME)
@Produces(PropertiesResource.TYPE_MIME)
public class PropertiesResource {

    private final static Logger LOG = LoggerFactory.getLogger(PropertiesResource.class);

    public final static String PATH = "properties";

    public final static String TYPE_MIME = "application/vnd.property+json";

    @Context
    private UriInfo uriInfo;

    @Autowired
    private IPropertyRepo propertyRepo;

    @GET
    @Path("{namespace}/{name}")
    @RolesAllowed({Roles.ADMIN, Roles.USER, Roles.GUEST})
    public Response getProperty(@PathParam("namespace") String namespace, @PathParam("name") String name) {
        Property prop = propertyRepo.findOneByNamespaceAndName(namespace, name);

        if (prop == null) {
            throw new WebApplicationException("Property not found", Status.NOT_FOUND);
        } else {
            return Response.ok((new PropertyMapper()).apply(prop)).build();
        }
    }

    @POST
    @Path("{namespace}/{name}")
    @RolesAllowed({Roles.ADMIN})
    public Response addProperty(@PathParam("namespace") String namespace, @PathParam("name") String name, @Valid PropertyRest prop) {
        try {
            this.propertyRepo.save(new Property(name, namespace, prop.getValue()));
        } catch (DuplicateKeyException dke) {
            LOG.error("Property already exists", dke);
            throw new WebApplicationException("Property already exists", Status.CONFLICT);
        }

        return Response.created(this.uriInfo.getAbsolutePath()).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed({Roles.ADMIN})
    public Response deleteProperty(@PathParam("id") String id) {
        this.propertyRepo.delete(id);
        
        return Response.noContent().build();
    }
}
