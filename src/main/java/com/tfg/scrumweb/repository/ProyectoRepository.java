package com.tfg.scrumweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Proyecto;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long>  {
    Optional<List<Proyecto>> findAllByEmpresa(Empresa e);
    Proyecto findByNombre(String nombre);

    @Modifying
    @Query(value = "UPDATE Proyecto p SET p.daily_actual = 0", nativeQuery = true)
    void setAllDailyTo0();
}
