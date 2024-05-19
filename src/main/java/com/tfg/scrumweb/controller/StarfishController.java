package com.tfg.scrumweb.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.entity.Starfish;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.service.ProyectoService;
import com.tfg.scrumweb.service.SprintService;
import com.tfg.scrumweb.service.StarfishService;

@Controller
public class StarfishController {
    Logger logger = LoggerFactory.getLogger(StarfishController.class);
    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private SprintService sprintService;

    @Autowired
    private StarfishService starfishService;
    
    @GetMapping("/proyecto/{proyectoId}/starfish")
    public String starfishIndex(Model model, @PathVariable Long proyectoId) throws ItemNotFoundException {
        Proyecto proyecto = proyectoService.findById(proyectoId);
        Sprint sprint = sprintService.getSprintActual(proyecto);
        Starfish starfish = sprint.getStarfish();

        ModelHelper.setModel(model, proyecto, sprint);
        construirStarfish(model, starfish);
        return "/starfish/index";
    }

    @GetMapping("/proyecto/{proyectoId}/starfish/historial")
    public String starfishHistorial(Model model, @PathVariable Long proyectoId) throws ItemNotFoundException {
        Proyecto proyecto = proyectoService.findById(proyectoId);
        Sprint sprint = sprintService.getSprintActual(proyecto);
        List<Starfish> listaStarfish = new ArrayList<>();
        
        for(Sprint s: proyecto.getSprint())
            listaStarfish.add(s.getStarfish());

        model.addAttribute("listaStarfish", listaStarfish);
        ModelHelper.setModel(model, proyecto, sprint);

        return "/starfish/historial";
    }

    @GetMapping("/starfish/{starfishId}")
    public String verStarfish(Model model, @PathVariable Long starfishId) throws ItemNotFoundException {
        Starfish starfish = starfishService.findById(starfishId);
        Sprint sprint = starfish.getSprint();
        Proyecto proyecto = sprint.getProyecto();

        ModelHelper.setModel(model, proyecto, sprint);
        construirStarfish(model, starfish);
        return "/starfish/ver";
    }

    private void construirStarfish(Model model, Starfish starfish){
        model.addAttribute("seguirHaciendo", starfish.getSeguirHaciendo());
        model.addAttribute("masDe", starfish.getMasDe());
        model.addAttribute("menosDe", starfish.getMenosDe());
        model.addAttribute("empezarAHacer", starfish.getEmpezarAHacer());
        model.addAttribute("dejarDeHacer", starfish.getDejarDeHacer());
    }
}
