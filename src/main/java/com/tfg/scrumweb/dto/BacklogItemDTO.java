package com.tfg.scrumweb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.enumeradores.Estado;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BacklogItemDTO {
    private Long id;
    private Integer orden;
    private String item;
    private Integer dificultad;
    private Estado estado;
    private Integer diaAcabado;
    private Long sprint;
    private String encargado;

    public BacklogItemDTO(BacklogItem b) {
        this.id = b.getId();
        this.orden = b.getOrden();
        this.item = b.getItem();
        this.sprint = b.getSprint().getId();
        this.estado = b.getEstado();
        if(b.getDificultad()!= null)
            this.dificultad = b.getDificultad();
        if(b.getEncargado() != null)
            this.encargado = b.getEncargado().getEmail();
    }    
}
