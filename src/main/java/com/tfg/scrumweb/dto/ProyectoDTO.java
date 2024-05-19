package com.tfg.scrumweb.dto;

import java.util.ArrayList;
import java.util.List;

import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProyectoDTO {
    private Long id;
    private String nombre;
    private int numeroSprint;
    private List<Long> personas = new ArrayList<>();
    private Long empresa;
    private Long sprint;
    private Boolean dailyActual;
    private Boolean reviewActual;
    private Boolean retrospectiveActual;
    private List<Long> doneAndSent = new ArrayList<>();
    private String productGoal;
    private String definitionOfDone;

    public ProyectoDTO(Proyecto proyecto) {
        this.id = proyecto.getId();
        this.nombre = proyecto.getNombre();
        this.numeroSprint = proyecto.getNumeroSprint();
       
        for(Persona p: proyecto.getPersonas())
            personas.add(p.getId());
        this.empresa = proyecto.getEmpresa().getId();
        this.sprint = proyecto.getSprint().get(proyecto.getNumeroSprint()-1).getId();
        this.dailyActual = proyecto.getDailyActual();
        this.reviewActual = proyecto.getReviewActual();
        this.retrospectiveActual = proyecto.getRetrospectiveActual();
        for(BacklogItem b: proyecto.getDoneAndSent())
            this.doneAndSent.add(b.getId());
        this.productGoal = proyecto.getProductGoal();
        this.definitionOfDone = proyecto.getDefinitionOfDone();
    }
}

