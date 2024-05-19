package com.tfg.scrumweb.dto;

import com.tfg.scrumweb.entity.Imagen;

import lombok.Getter;

@Getter
public class ImagenDTO {
    private Long id;
    private byte[] content;
    private String name;
    private Long usuario;
    
    public ImagenDTO(Imagen i){
        this.id = i.getId();
        this.content = i.getContent();
        this.name = i.getName();
        this.usuario = i.getUsuario().getId();
    }
}
