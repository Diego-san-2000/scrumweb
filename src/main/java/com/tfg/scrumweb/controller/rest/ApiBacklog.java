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
import com.tfg.scrumweb.dto.BacklogItemDTO;
import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.entity.Usuario;
import com.tfg.scrumweb.enumeradores.Rol;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.exception.ScrumViolation;
import com.tfg.scrumweb.service.BacklogItemService;
import com.tfg.scrumweb.service.SprintService;
import com.tfg.scrumweb.service.UserService;

@RestController
@RequestMapping("/api/backlog/")
public class ApiBacklog {
    @Autowired
    private UserService userService;

    @Autowired
    private BacklogItemService backlogItemService;

    @Autowired
    private SprintService sprintService;

    Logger logger = LoggerFactory.getLogger(ApiBacklog.class);
/*
    @GetMapping("/{id}")
    public ResponseEntity<BacklogItemDTO> getData(@PathVariable Long id) throws ItemNotFoundException {
        BacklogItem backlogItem = backlogItemService.findById(id);
        if (backlogItem != null) {
            return ResponseEntity.ok(new BacklogItemDTO(backlogItem));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
*/
    @PostMapping
    public ResponseEntity<BacklogItemDTO> createData(@RequestBody BacklogItemDTO data)
     throws ItemNotFoundException, NotAllowedException, ScrumViolation {
        Persona u = (Persona) Autenticador.autenticador();
        if(!u.getRol().equals(Rol.PRODUCT_OWNER) && !u.getRol().equals(Rol.DEVELOPER))
            throw new ScrumViolation("Only Developers and Product Owners can create Backlog items");
        Sprint sprint = sprintService.findById(data.getSprint());
        if(!u.getProyecto().equals(sprint.getProyecto()))
            throw new NotAllowedException("You can't create Backlog items in other proyects");
        Persona p;
        if(data.getEncargado() == null)
            p = null;
        else
            p = (Persona) userService.findByEmail(data.getEncargado());
        BacklogItem backlogItem = 
         new BacklogItem(data.getOrden(), data.getItem(), p,
         data.getEstado(), data.getDificultad(), sprint);
        BacklogItem createdData = backlogItemService.save(backlogItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BacklogItemDTO(createdData));
    }

    @PutMapping("{id}")
    public ResponseEntity<BacklogItemDTO> updateData(@PathVariable Long id,
     @RequestBody BacklogItemDTO data) throws ItemNotFoundException, NotAllowedException, ScrumViolation {
        Persona persona = (Persona) Autenticador.autenticador();
        if(!persona.getRol().equals(Rol.PRODUCT_OWNER) && !persona.getRol().equals(Rol.DEVELOPER))
            throw new ScrumViolation("Only Developers and Product Owners can create Backlog items");

        Sprint sprint = sprintService.findById(data.getSprint());
        if(!persona.getProyecto().equals(sprint.getProyecto()))
            throw new NotAllowedException("You can't modify Backlog items in other proyects");

        Persona encargado;
        if(data.getEncargado() != null)
            encargado = (Persona) userService.findByEmail(data.getEncargado());
        else
            encargado = null;
        BacklogItem backlogItem =
         new BacklogItem(data.getOrden(), data.getItem(), encargado, data.getEstado(), data.getDificultad(), sprint);
        BacklogItem updatedData = backlogItemService.updateData(id, backlogItem);
        return ResponseEntity.ok(new BacklogItemDTO(updatedData));
    }

    //Aceptar increment como definition of done
    @PutMapping("{id}/aceptar")
    public ResponseEntity<BacklogItemDTO> aceptar(@PathVariable Long id) throws ItemNotFoundException {
        BacklogItem updatedData = backlogItemService.findById(id);
        backlogItemService.aceptar(updatedData);
        return ResponseEntity.ok(new BacklogItemDTO(updatedData));
    }

    //Rechazar increment como definition of done
    @PutMapping("{id}/rechazar")
    public ResponseEntity<BacklogItemDTO> rechazar(@PathVariable Long id) throws ItemNotFoundException {
        BacklogItem updatedData = backlogItemService.findById(id);
        backlogItemService.rechazar(updatedData);
        return ResponseEntity.ok(new BacklogItemDTO(updatedData));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteData(@PathVariable Long id)
     throws ItemNotFoundException, NotAllowedException, ScrumViolation {
        backlogItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
