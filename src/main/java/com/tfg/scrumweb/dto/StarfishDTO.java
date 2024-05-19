package com.tfg.scrumweb.dto;

import java.util.List;

import com.tfg.scrumweb.entity.Starfish;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StarfishDTO {
    private Long id;
    private Long sprint;
    private List<String> seguirHaciendo;
    private List<String> masDe;
    private List<String> menosDe;
    private List<String> empezarAHacer;
    private List<String> dejarDeHacer;

    public StarfishDTO(Starfish starfish){
        this.id = starfish.getId();
        this.sprint = starfish.getSprint().getId();
        this.seguirHaciendo = starfish.getSeguirHaciendo();
        this.masDe = starfish.getMasDe();
        this.menosDe = starfish.getMenosDe();
        this.empezarAHacer = starfish.getEmpezarAHacer();
        this.dejarDeHacer = starfish.getDejarDeHacer();
    }
}
