package com.tfg.scrumweb.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.enumeradores.Estado;
import com.tfg.scrumweb.service.SprintService;

@Controller
public class StakeholderController {
    Logger logger = LoggerFactory.getLogger(StakeholderController.class);

    @Autowired
    private SprintService sprintService;

    @GetMapping("/stakeholder")
    public String mostrarIncremet(Model model) {
        Persona user = (Persona) Autenticador.autenticador();
        Proyecto proyecto = user.getProyecto();
        ModelHelper.setModel(model, proyecto, sprintService.getSprintActual(proyecto));
        
        List<BacklogItem> lbacklogs = new ArrayList<>();
        for(Sprint s: proyecto.getSprint()){
            for(BacklogItem b: s.getBacklog())
                if(b.getEstado().equals(Estado.DONEANDSENT))
                    lbacklogs.add(b);
        }
        model.addAttribute("hayIncrement", !lbacklogs.isEmpty());
        model.addAttribute("listaItems", lbacklogs);

        logger.info(proyecto.getDoneAndSent().toString());
        return "/stakeholder/increment";
    }
    @GetMapping("/stakeholdermap")
    public String mostrarMapa(Model model) {
        Persona user = (Persona) Autenticador.autenticador();
        Proyecto proyecto = user.getProyecto();
        ModelHelper.setModel(model, proyecto, sprintService.getSprintActual(proyecto));

        model.addAttribute("stakeholders", proyecto.getStakeHolders());

        return "/stakeholder/stakeholdermap";
    }
}
