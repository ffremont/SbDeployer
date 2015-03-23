/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.mappers;

import com.github.ffremont.microservices.springboot.manager.models.Property;
import com.github.ffremont.microservices.springboot.pojo.PropertyRest;
import java.util.function.Function;

/**
 *
 * @author florent
 */
public class PropertyMapper implements Function<Property, PropertyRest> {

    @Override
    public PropertyRest apply(Property t) {
        return new PropertyRest(t.getId(), t.getName(), t.getNamespace(), t.getValue());
    }
    
}
