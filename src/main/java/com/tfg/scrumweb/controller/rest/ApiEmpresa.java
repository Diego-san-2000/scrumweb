package com.tfg.scrumweb.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.scrumweb.controller.Autenticador;
import com.tfg.scrumweb.dto.EmpresaDTO;
import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.Usuario;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.exception.ScrumViolation;
import com.tfg.scrumweb.service.ProyectoService;
import com.tfg.scrumweb.service.UserService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/empresa/")
public class ApiEmpresa {
    Logger logger = LoggerFactory.getLogger(ApiEmpresa.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProyectoService proyectoService;
/*
    @GetMapping("{id}")
    public ResponseEntity<EmpresaDTO> getData(@PathVariable Long id) throws ItemNotFoundException {
        Empresa empresa = (Empresa) userService.findById(id);
        if (empresa != null)
            return ResponseEntity.ok(new EmpresaDTO(empresa));
        else 
            return ResponseEntity.notFound().build();
    }
*/
    
    @PutMapping("{id}")
    public ResponseEntity<EmpresaDTO> updateData(@PathVariable Long id, @RequestBody EmpresaDTO data)
     throws ItemNotFoundException, NotAllowedException {
        Empresa updatedData = userService.updateEmpresa(id, data);
        return ResponseEntity.ok(new EmpresaDTO(updatedData));
    }

    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteData(@PathVariable Long id)
     throws ItemNotFoundException, NotAllowedException, ScrumViolation {
        Usuario usuario = Autenticador.autenticador();
        Empresa empresa = (Empresa) userService.findById(id);
        if(!usuario.equals(empresa))
            throw new NotAllowedException("You can't delete other business");
        List<Proyecto> backUpProyecto = new ArrayList<>(empresa.getListaProyecto());
        
        for (Proyecto p : backUpProyecto) {
            proyectoService.deleteById(p.getId());
        }

        empresa.setListaProyecto(null);
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
