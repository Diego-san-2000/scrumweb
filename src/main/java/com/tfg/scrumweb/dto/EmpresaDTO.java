package com.tfg.scrumweb.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmpresaDTO {
    private Long id;
    private String username;
    private String email;

    @JsonIgnoreProperties
    private String pass;

    private Long imagen;
    private List<Long> listaProyecto = new ArrayList<>();
    private List<Long> empleados = new ArrayList<>();

    public EmpresaDTO(Empresa empresa){
        this.id = empresa.getId();
        this.username = empresa.getUsername();
        this.email = empresa.getEmail();
        this.imagen = empresa.getImg() != null ? empresa.getImg().getId() : null;
        for(Proyecto p: empresa.getListaProyecto())
            listaProyecto.add(p.getId());
        for(Persona p: empresa.getEmpleados())
            empleados.add(p.getId());
    }
}
