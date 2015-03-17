/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.models.repo;

import com.github.ffremont.microservices.springboot.manager.models.Property;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author florent
 */
public interface IPropertyRepo extends PagingAndSortingRepository<Property, String>{
    public Property findOneByNamespaceAndName(String namespace, String name);
    
    public Property findByNamespaceRegex(String namespace);
}
