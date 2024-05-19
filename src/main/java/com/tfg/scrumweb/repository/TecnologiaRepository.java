package com.tfg.scrumweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.scrumweb.entity.Tecnologia;

@Repository
public interface TecnologiaRepository extends JpaRepository<Tecnologia, Long>  {
}
