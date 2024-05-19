package com.tfg.scrumweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tfg.scrumweb.entity.BusinessCanvas;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.service.ProyectoService;
import com.tfg.scrumweb.service.SprintService;

@Controller
public class BusinessCanvasController {
    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private SprintService sprintService;
    
    @GetMapping("/{idProyecto}/businesscanvas")
    public String verBacklog(Model model, @PathVariable Long idProyecto) throws ItemNotFoundException{
        Proyecto proyecto = proyectoService.findById(idProyecto);
        Sprint sprint = sprintService.getSprintActual(proyecto);
        ModelHelper.setModel(model, proyecto, sprint);
        BusinessCanvas businessCanvas = proyecto.getBusinessCanvas();
        model.addAttribute("businessCanvas", businessCanvas);
        model.addAttribute("keyPartners", businessCanvas.getKeyPartners());
        model.addAttribute("keyActivities", businessCanvas.getKeyActivities());
        model.addAttribute("keyResources", businessCanvas.getKeyResources());
        model.addAttribute("valuePropositions", businessCanvas.getValuePropositions());
        model.addAttribute("customerRelationships", businessCanvas.getCustomerRelationships());
        model.addAttribute("customersSegments", businessCanvas.getCustomersSegments());
        model.addAttribute("channels", businessCanvas.getChannels());
        model.addAttribute("costStructure", businessCanvas.getCostStructure());
        model.addAttribute("revenueStreams", businessCanvas.getRevenueStreams());

        return "businesscanvas/index";
    }
}
