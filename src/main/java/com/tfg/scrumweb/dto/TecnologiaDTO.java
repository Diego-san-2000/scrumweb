package com.tfg.scrumweb.dto;

import com.tfg.scrumweb.entity.Tecnologia;
import com.tfg.scrumweb.enumeradores.Categoria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TecnologiaDTO {
    private Long id;
    private String item;
    private Categoria categoria;
    private Long persona;

    public TecnologiaDTO(Tecnologia tecnologia) {
        this.id = tecnologia.getId();
        this.item = tecnologia.getItem();
        this.categoria = tecnologia.getCategoria();
        this.persona = tecnologia.getPersona().getId();
    }    
}
