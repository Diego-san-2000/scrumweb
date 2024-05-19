package com.tfg.scrumweb.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Tecnologia;
import com.tfg.scrumweb.enumeradores.Rol;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonaDTO {
    private Long id;
    private String username;
    private String email;
    private Long imagen;
    private Long proyecto;
    private Long empresa;
    private List<Long> listaBacklogItem = new ArrayList<>();
    private Rol rol;
    private Integer scrumSubTeam;
    private List<TecnologiaDTO> tecnologias = new ArrayList<>();
    

    public PersonaDTO(Persona p){
        this.id = p.getId();
        this.username = p.getUsername();
        this.email = p.getEmail();
        this.imagen = p.getImg() != null ? p.getImg().getId() : null;
        this.proyecto = p.getProyecto().getId();
        this.empresa = p.getEmpresa().getId();
        for(BacklogItem b: p.getListaBacklogItem())
            this.listaBacklogItem.add(b.getId());
        this.rol = p.getRol();
        this.scrumSubTeam = p.getScrumSubTeam();
        for(Tecnologia t: p.getTecnologias())
            this.tecnologias.add(new TecnologiaDTO(t));

    }    
}
