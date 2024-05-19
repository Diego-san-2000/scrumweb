package com.tfg.scrumweb.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.scrumweb.dto.StarfishDTO;
import com.tfg.scrumweb.entity.Starfish;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.service.StarfishService;

@RestController
@RequestMapping("/api/starfish/")
public class ApiStarfish {
    @Autowired
    private StarfishService starfishService;

    Logger logger = LoggerFactory.getLogger(ApiStarfish.class);
/*
    @GetMapping("{id}")
    public ResponseEntity<StarfishDTO> getData(@PathVariable Long id) throws ItemNotFoundException {
        Starfish starfish = starfishService.findById(id);
        if (starfish != null) {
            return ResponseEntity.ok(new StarfishDTO(starfish));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<StarfishDTO> createData(@NonNull @RequestBody Starfish data) {
        Starfish createdData = starfishService.crearStarfish(data);
        StarfishDTO starfishDTO = new StarfishDTO(createdData);
        return ResponseEntity.status(HttpStatus.CREATED).body(starfishDTO);
    }
*/
    @PutMapping("{id}")
    public ResponseEntity<StarfishDTO> updateData(@PathVariable Long id, @RequestBody Starfish data)
     throws ItemNotFoundException, NotAllowedException {
        Starfish updatedData = starfishService.updateStarfish(id, data);
        return ResponseEntity.ok(new StarfishDTO(updatedData));
    }
}
