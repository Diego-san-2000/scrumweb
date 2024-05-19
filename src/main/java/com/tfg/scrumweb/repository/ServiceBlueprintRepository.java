package com.tfg.scrumweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.scrumweb.entity.ServiceBlueprint;

@Repository
public interface ServiceBlueprintRepository  extends JpaRepository<ServiceBlueprint, Long>  {
    
}
