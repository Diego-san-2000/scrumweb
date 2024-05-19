package com.tfg.scrumweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.scrumweb.entity.BusinessCanvas;

@Repository
public interface BusinessCanvasRepository  extends JpaRepository<BusinessCanvas, Long>  {
    BusinessCanvas findById(long id);
}