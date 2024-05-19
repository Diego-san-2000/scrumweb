package com.tfg.scrumweb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.ServiceBlueprint;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.service.ProyectoService;
import com.tfg.scrumweb.service.SprintService;

@Controller
public class ServiceBlueprintController {
    Logger logger = LoggerFactory.getLogger(ServiceBlueprintController.class);
    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private SprintService sprintService;

    @GetMapping("/{idProyecto}/blueprint")
    public String mostrarBlueprint(Model model, @PathVariable Long idProyecto) throws ItemNotFoundException {
        Persona user = (Persona) Autenticador.autenticador();
        Proyecto proyecto = proyectoService.findById(idProyecto);
        ServiceBlueprint sc = proyecto.getServiceBlueprint();
        ModelHelper.setModel(model, proyecto, sprintService.getSprintActual(proyecto));
        model.addAttribute("idServiceBlueprint", proyecto.getServiceBlueprint().getId());
        model.addAttribute("sc", proyecto.getServiceBlueprint());
        model.addAttribute("a11", sc.getA11());
        model.addAttribute("a12", sc.getA12());
        model.addAttribute("a13", sc.getA13());
        model.addAttribute("a14", sc.getA14());
        model.addAttribute("a15", sc.getA15());

        model.addAttribute("a21", sc.getA21());
        model.addAttribute("a22", sc.getA22());
        model.addAttribute("a23", sc.getA23());
        model.addAttribute("a24", sc.getA24());
        model.addAttribute("a25", sc.getA25());

        model.addAttribute("a31", sc.getA31());
        model.addAttribute("a32", sc.getA32());
        model.addAttribute("a33", sc.getA33());
        model.addAttribute("a34", sc.getA34());
        model.addAttribute("a35", sc.getA35());

        model.addAttribute("a41", sc.getA41());
        model.addAttribute("a42", sc.getA42());
        model.addAttribute("a43", sc.getA43());
        model.addAttribute("a44", sc.getA44());
        model.addAttribute("a45", sc.getA45());

        model.addAttribute("a51", sc.getA51());
        model.addAttribute("a52", sc.getA52());
        model.addAttribute("a53", sc.getA53());
        model.addAttribute("a54", sc.getA54());
        model.addAttribute("a55", sc.getA55());

        model.addAttribute("a61", sc.getA61());
        model.addAttribute("a62", sc.getA62());
        model.addAttribute("a63", sc.getA63());
        model.addAttribute("a64", sc.getA64());
        model.addAttribute("a65", sc.getA65());

        logger.info(sc.toString());
        return "/blueprint/index";
    }
}
