package com.tfg.scrumweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.scrumweb.entity.BacklogItem;

@Repository
public interface BacklogItemRepository extends JpaRepository<BacklogItem, Long>  {
    BacklogItem findById(long id);
}

