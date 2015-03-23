/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.manager.models.repo;

import com.github.ffremont.microservices.springboot.manager.models.MicroService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author florent
 */
public interface IMicroServiceRepo extends PagingAndSortingRepository<MicroService, String>{
    public MicroService findOneByClusterAndNodeAndName(String cluster, String node, String name);
    
    public Page<MicroService> findByClusterAndNode(String cluster, String node, Pageable pageable);
}
