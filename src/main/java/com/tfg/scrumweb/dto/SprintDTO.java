package com.tfg.scrumweb.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.entity.Sprint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SprintDTO {
    private Long id;
    private Boolean sprintPlanningDone;
    private Boolean planningPokerRealizado;
    private int numeroSprint;
    private Long proyecto;
    private List<Long> backlog;
    private Long starfish;
    private LocalDate inicioSprint;
    private LocalDate finSprint;
    private String sprintGoal;

    public SprintDTO(Sprint sprint){
        this.id = sprint.getId();
        this.sprintPlanningDone = sprint.getSprintPlanningDone();
        this.planningPokerRealizado = sprint.getPlanningPokerRealizado();
        this.numeroSprint = sprint.getNumeroSprint();
        this.proyecto = sprint.getProyecto().getId();
        this.backlog = new ArrayList<>();
        for(BacklogItem b: sprint.getBacklog()){
            this.backlog.add(b.getId());
        }
        this.starfish = sprint.getStarfish().getId();
        this.inicioSprint = sprint.getInicioSprint();
        this.finSprint = sprint.getFinSprint();
        this.sprintGoal = sprint.getSprintGoal();
    }
}
