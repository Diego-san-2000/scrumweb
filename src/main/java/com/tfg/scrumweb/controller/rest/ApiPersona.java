package com.tfg.scrumweb.controller.rest;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.scrumweb.controller.Autenticador;
import com.tfg.scrumweb.dto.ImagenDTO;
import com.tfg.scrumweb.dto.PersonaDTO;
import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Imagen;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Usuario;
import com.tfg.scrumweb.exception.EmailAlreadyExists;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.exception.ScrumViolation;
import com.tfg.scrumweb.service.UserService;

@RestController
@RequestMapping("/api/persona/")
public class ApiPersona {
    @Autowired
    private  UserService userService;

    Logger logger = LoggerFactory.getLogger(ApiPersona.class);
/*
    @GetMapping("{id}")
    public ResponseEntity<PersonaDTO> getData(@PathVariable Long id) throws ItemNotFoundException {
        Persona persona = (Persona) userService.findById(id);
        if (persona != null) 
            return ResponseEntity.ok(new PersonaDTO(persona));
        else 
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PersonaDTO> createData(@NonNull @RequestBody Persona data) throws EmailAlreadyExists {
        if(userService.findByEmail(data.getEmail()) != null)
            throw new EmailAlreadyExists("Ya existe un usuario con ese email registrado");
        Persona createdData = userService.crearPersona(data);
        PersonaDTO personaDTO = new PersonaDTO(createdData);
        return ResponseEntity.status(HttpStatus.CREATED).body(personaDTO);
    }
*/
    @PostMapping("{id}/imagen")
    public ResponseEntity<ImagenDTO> createImagen(@PathVariable Long id, @RequestParam MultipartFile multipartImage) throws IOException, ItemNotFoundException {
        Imagen createdData = userService.updateImagen(id, multipartImage);
        ImagenDTO imagenDTO = new ImagenDTO(createdData);
        return ResponseEntity.status(HttpStatus.CREATED).body(imagenDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<PersonaDTO> updateData(@PathVariable Long id, @RequestBody Persona data)
     throws ItemNotFoundException, EmailAlreadyExists, ScrumViolation, NotAllowedException {
        Persona updatedData = userService.updatePersona(id, data);
        return ResponseEntity.ok(new PersonaDTO(updatedData));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteData(@PathVariable Long id)
     throws ItemNotFoundException, ScrumViolation, NotAllowedException {
        Usuario usuario = Autenticador.autenticador();
        Persona persona = (Persona) userService.findById(id);
        if(!usuario.equals(persona) && !(usuario instanceof Empresa))
            throw new NotAllowedException("You can't perform that action");
        if(!persona.getEmpresa().equals(usuario))
            throw new NotAllowedException("You can't delete people of other business");
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

