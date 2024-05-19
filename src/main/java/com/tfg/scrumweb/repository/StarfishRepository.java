package com.tfg.scrumweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.scrumweb.entity.Starfish;

@Repository
public interface StarfishRepository extends JpaRepository<Starfish, Long>  {
}