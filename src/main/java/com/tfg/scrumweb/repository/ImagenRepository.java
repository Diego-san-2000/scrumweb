package com.tfg.scrumweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.scrumweb.entity.Imagen;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {
    Optional<Imagen> findById(Long id);
}