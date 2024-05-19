package com.tfg.scrumweb.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.scrumweb.controller.Autenticador;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.ServiceBlueprint;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.service.ServiceBlueprintService;

@RestController
@RequestMapping("/api/serviceblueprint/")
public class ApiServiceBlueprint {
    @Autowired
    private ServiceBlueprintService serviceBlueprintService;

    @PutMapping("{id}")
    public ResponseEntity<ServiceBlueprint> updateData(@PathVariable Long id, @RequestBody ServiceBlueprint data)
     throws ItemNotFoundException, NotAllowedException {
        ServiceBlueprint serviceBlueprint = serviceBlueprintService.findById(id);
        serviceBlueprintService.update(serviceBlueprint, data);
        return ResponseEntity.ok(serviceBlueprint);
    }
}
