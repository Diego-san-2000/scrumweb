package com.tfg.scrumweb.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.scrumweb.controller.Autenticador;
import com.tfg.scrumweb.entity.BusinessCanvas;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.enumeradores.Rol;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.service.BusinessCanvasService;

@RestController
@RequestMapping("/api/businesscanvas/")
public class ApiBusinessCanvas {
    @Autowired
    private BusinessCanvasService businessCanvasService;

    @PutMapping("{id}")
    public ResponseEntity<BusinessCanvas> updateData(@PathVariable Long id, @RequestBody BusinessCanvas data)
     throws  NotAllowedException, ItemNotFoundException {
        BusinessCanvas businessCanvas = businessCanvasService.findById(id);
        businessCanvasService.updateData(businessCanvas, data);
        return ResponseEntity.ok(businessCanvas);
    }
}
