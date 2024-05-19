package com.tfg.scrumweb.controller;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.ScrumViolation;
import com.tfg.scrumweb.service.ProyectoService;
import com.tfg.scrumweb.service.SprintService;

@Controller
public class GraficaController {

    Logger logger = LoggerFactory.getLogger(GraficaController.class);

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private SprintService sprintService;

    private List<BacklogItem> itemsPorHacer(List<BacklogItem> b) {
        return b.stream()
                .filter(i -> i.getDiaAcabado() != null && i.getDificultad() != null)
                .sorted(Comparator.comparing(BacklogItem::getDiaAcabado))
                .collect(Collectors.toList());
    }
    
    private int todosItems(List<BacklogItem> b) {
        return b.stream()
                .filter(i -> i.getDificultad() != null)
                .mapToInt(BacklogItem::getDificultad).sum();
    }

    @GetMapping("/grafica/{proyectoId}")
    public String getGrafica(@PathVariable Long proyectoId, Model model) throws ItemNotFoundException, ScrumViolation {
    Proyecto proyecto = proyectoService.findById(proyectoId);
    Sprint sprint = sprintService.getSprintActual(proyecto);
    if (Boolean.FALSE.equals(sprint.getSprintPlanningDone())) {
        throw new ScrumViolation("Se debe crear el backlog primero");
    }

    List<BacklogItem> listaItems = itemsPorHacer(sprint.getBacklog());
    int total = todosItems(sprint.getBacklog());

    Integer numeroDiasSprint = (int) ChronoUnit.DAYS.between(sprint.getInicioSprint(), sprint.getFinSprint());
    int[] burnDownProgreso = new int[numeroDiasSprint];
    int[] burnUpProgreso = new int[numeroDiasSprint];
    for(BacklogItem b: listaItems){
        burnDownProgreso[b.getDiaAcabado()] += (b.getDificultad());
        burnUpProgreso[b.getDiaAcabado()] += (b.getDificultad());
    }

    //Normalizamos gráfica para que no haya días con 0
    for (int index = 1; index < burnUpProgreso.length; index++) {
        burnDownProgreso[index] += burnDownProgreso[index - 1];
        burnUpProgreso[index] += burnUpProgreso[index - 1];
    }

    for (int index = 0; index < burnDownProgreso.length; index++) 
        burnDownProgreso[index] = total - burnDownProgreso[index];

    
    List<Integer> progresoIdeal = IntStream.range(0, numeroDiasSprint + 1)
            .mapToObj(i -> total - (total / numeroDiasSprint) * i)
            .collect(Collectors.toList());

    String[] label = IntStream.rangeClosed(0, numeroDiasSprint-1)
            .mapToObj(i -> "Day " + i)
            .toArray(String[]::new);

    List<Integer> burnUpTotal = IntStream.range(0, numeroDiasSprint + 1)
            .mapToObj(i -> total)
            .collect(Collectors.toList());

    ModelHelper.setModel(model, proyecto, sprint);
    model.addAttribute("ideal", progresoIdeal);
    model.addAttribute("total", total);
    model.addAttribute("burnDownProgreso", burnDownProgreso);
    model.addAttribute("label", label);
    model.addAttribute("burnUpTotal", burnUpTotal);
    model.addAttribute("burnUpProgreso", burnUpProgreso);

    return "/grafica/index";
}

    @GetMapping("/grafica/{proyectoId}/historial")
    public String getHistorialGrafica(@PathVariable Long proyectoId, Model model) throws ItemNotFoundException {
        Proyecto proyecto = proyectoService.findById(proyectoId);
        Sprint sprint = sprintService.getSprintActual(proyecto);
        model.addAttribute("listaSprint", proyecto.getSprint());
        ModelHelper.setModel(model, proyecto, sprint);
        return "/grafica/historial";
    }

    @GetMapping("/grafica/{proyectoId}/historial/{sprintId}")
    public String getHistorialGraficaVer(@PathVariable Long proyectoId, @PathVariable Long sprintId, Model model) throws ItemNotFoundException {
        Proyecto proyecto = proyectoService.findById(proyectoId);
        Sprint sprint = sprintService.findById(sprintId);
        if(sprintService.getSprintActual(proyecto).equals(sprint))
            return "redirect:/grafica/"+proyectoId;
        int total = todosItems(sprint.getBacklog());
        
        List<BacklogItem> listaItems = itemsPorHacer(sprint.getBacklog());
        Integer numeroDiasSprint = (int) ChronoUnit.DAYS.between(sprint.getInicioSprint(), sprint.getFinSprint());
        int[] burnDownProgreso = new int[numeroDiasSprint];
        int[] burnUpProgreso = new int[numeroDiasSprint];
        for(BacklogItem b: listaItems){
            burnDownProgreso[b.getDiaAcabado()] += (b.getDificultad());
            burnUpProgreso[b.getDiaAcabado()] += (b.getDificultad());
        }

        //Normalizamos gráfica para que no haya días con 0
        for (int index = 1; index < burnUpProgreso.length; index++) {
            burnDownProgreso[index] += burnDownProgreso[index - 1];
            burnUpProgreso[index] += burnUpProgreso[index - 1];
        }

        for (int index = 0; index < burnDownProgreso.length; index++) 
            burnDownProgreso[index] = total - burnDownProgreso[index];

    
        List<Integer> progresoIdeal = IntStream.range(0, numeroDiasSprint + 1)
            .mapToObj(i -> total - (total / numeroDiasSprint) * i)
            .collect(Collectors.toList());

        String[] label = IntStream.rangeClosed(0, numeroDiasSprint-1)
            .mapToObj(i -> "Day " + i)
            .toArray(String[]::new);

        List<Integer> burnUpTotal = IntStream.range(0, numeroDiasSprint + 1)
            .mapToObj(i -> total)
            .collect(Collectors.toList());
            
        ModelHelper.setModel(model, proyecto, sprint);
        model.addAttribute("ideal", progresoIdeal);
        model.addAttribute("total", total);
        model.addAttribute("burnDownProgreso", burnDownProgreso);
        model.addAttribute("label", label);
        model.addAttribute("burnUpTotal", burnUpTotal);
        model.addAttribute("burnUpProgreso", burnUpProgreso);
        return "/grafica/index";
    }
}
