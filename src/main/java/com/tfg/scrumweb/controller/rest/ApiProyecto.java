package com.tfg.scrumweb.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.scrumweb.controller.Autenticador;
import com.tfg.scrumweb.dto.ProyectoDTO;
import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.exception.EmailAlreadyExists;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.exception.ScrumViolation;
import com.tfg.scrumweb.service.ProyectoService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/proyecto/")
public class ApiProyecto {
    @Autowired
    private ProyectoService proyectoService;

    Logger logger = LoggerFactory.getLogger(ApiProyecto.class);
/*
    @GetMapping("{id}")
    public ResponseEntity<ProyectoDTO> getData(@PathVariable Long id) throws ItemNotFoundException {
        Proyecto proyecto = proyectoService.findById(id);
        if (proyecto != null)
            return ResponseEntity.ok(new ProyectoDTO(proyecto));
        else
            return ResponseEntity.notFound().build();
    }
    */
    @Transactional
    @PostMapping
    public ResponseEntity<ProyectoDTO> createData(@RequestBody Proyecto data) throws EmailAlreadyExists, ScrumViolation {
        Proyecto createdData = proyectoService.crearProyecto(data);
        ProyectoDTO proyectoDTO = new ProyectoDTO(createdData);
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProyectoDTO> updateData(@PathVariable Long id, @RequestBody Proyecto data)
     throws EmailAlreadyExists, ItemNotFoundException, NotAllowedException, ScrumViolation {
        Proyecto updatedData = proyectoService.updateData(id, data);
        return ResponseEntity.ok(new ProyectoDTO(updatedData));
    }

    @PutMapping("{id}/atributos")
    public ResponseEntity<ProyectoDTO> updateProductGoal(@PathVariable Long id, @RequestBody Proyecto data) throws ItemNotFoundException {
        Proyecto updatedData = proyectoService.updateProductGoal(id, data);
        if (updatedData != null) {
            return ResponseEntity.ok(new ProyectoDTO(updatedData));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteData(@PathVariable Long id) throws ItemNotFoundException, NotAllowedException {
        proyectoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
