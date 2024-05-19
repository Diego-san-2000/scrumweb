package com.tfg.scrumweb.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.scrumweb.controller.Autenticador;
import com.tfg.scrumweb.dto.SprintDTO;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.enumeradores.Rol;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.exception.ScrumViolation;
import com.tfg.scrumweb.service.SprintService;

@RestController
@RequestMapping("/api/sprint/")
public class ApiSprint {
    @Autowired
    private SprintService sprintService;
/*
    @GetMapping("{id}")
    public ResponseEntity<SprintDTO> getData(@PathVariable Long id) throws ItemNotFoundException {
        Sprint proyecto = sprintService.findById(id);
        if (proyecto != null)
            return ResponseEntity.ok(new SprintDTO(proyecto));
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<SprintDTO> createData(@RequestBody Sprint data) {
        Sprint createdData = sprintService.crearSprint(data);
        SprintDTO proyectoDTO = new SprintDTO(createdData);
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<SprintDTO> updateData(@PathVariable Long id, @RequestBody Sprint data) throws ItemNotFoundException {
        Sprint updatedData = sprintService.actualizarSprint(id, data);
        if (updatedData != null)
            return ResponseEntity.ok(new SprintDTO(updatedData));
        else
            return ResponseEntity.notFound().build();
    }
*/
    @PutMapping("{id}/sprintgoal")
    public ResponseEntity<SprintDTO> updateSprintGoal(@PathVariable Long id, @RequestBody Sprint data)
     throws ItemNotFoundException, ScrumViolation, NotAllowedException {
        Sprint s = sprintService.findById(id);
        Sprint updatedData = sprintService.updateSprintGoal(s, data);
        return ResponseEntity.ok(new SprintDTO(updatedData));
    }

    @PutMapping("{id}/sprintplanningdone")
    public ResponseEntity<SprintDTO> updateSprintplanning(@PathVariable Long id)
     throws ItemNotFoundException, NotAllowedException, ScrumViolation {
        Sprint s = sprintService.findById(id);
        Sprint updatedData = sprintService.updateSprintPlanning(s);
        return ResponseEntity.ok(new SprintDTO(updatedData));
    }
}
