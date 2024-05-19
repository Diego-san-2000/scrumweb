package com.tfg.scrumweb.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.entity.Usuario;
import com.tfg.scrumweb.enumeradores.Estado;
import com.tfg.scrumweb.enumeradores.Rol;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.service.BacklogItemService;
import com.tfg.scrumweb.service.ProyectoService;
import com.tfg.scrumweb.service.SprintService;

@Controller
public class BacklogController {
    Logger logger = LoggerFactory.getLogger(BacklogController.class);
    private Usuario user;

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private SprintService sprintService;

    @Autowired
    private BacklogItemService backlogItemService;

    @GetMapping("/backlog/{idProyecto}")
    public String verBacklog(Model model, @PathVariable Long idProyecto) throws ItemNotFoundException{
        Persona p = (Persona) Autenticador.autenticador();

        model.addAttribute("u", p);
        Proyecto proyecto = proyectoService.findById(idProyecto);
        Sprint sprint = sprintService.getSprintActual(proyecto);
        List<BacklogItem> toDo = new ArrayList<>();
        List<BacklogItem> doing = new ArrayList<>();
        List<BacklogItem> done = new ArrayList<>();

        for(BacklogItem item: sprint.getBacklog()){
            switch (item.getEstado()) {
                case DONE:
                    done.add(item);
                    break;
                case DOING:
                    if ((p.getScrumSubTeam()==null) || (p.getScrumSubTeam().equals(item.getScrumSubTeam())))
                        doing.add(item);
                    break;
                case TODO:
                    toDo.add(item);
                    break;
                case DONEANDSENT:
                    break;
            }
        }
        model.addAttribute("todo", toDo);
        model.addAttribute("doing", doing);
        model.addAttribute("done", done);
        model.addAttribute("idProyecto", proyecto.getId());
        ModelHelper.setModel(model, proyecto, sprint);
        return "/backlog/backlog";
    }
    
/*
    @GetMapping("/backlogpo/{idProyecto}")
    public String crearBacklog(Model model, @PathVariable Long idProyecto) throws ItemNotFoundException{
        Proyecto proyecto = proyectoService.findById(idProyecto);
        Sprint sprint = sprintService.getSprintActual(proyecto);
        user = Autenticador.autenticador();

        String inicioSprint = sprint.getInicioSprint().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String finSprint = sprint.getInicioSprint().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        model.addAttribute("inicioSprint", inicioSprint);
        model.addAttribute("finSprint", finSprint);
        ModelHelper.setModel(model, proyecto, sprint);
        return "/backlog/po_crear_backlog";
    }
*/
    //Editar Product Backlog
    @GetMapping("/backlog/{proyectoId}/editar")
    public String editarBacklog(@PathVariable Long proyectoId, Model model) throws ItemNotFoundException, NotAllowedException{
        Persona persona = (Persona) Autenticador.autenticador();
        if(!persona.getRol().equals(Rol.PRODUCT_OWNER))
            throw new NotAllowedException("Only the Product Owner can edit the Product Backlog directly");
        Proyecto proyecto = proyectoService.findById(proyectoId);
        Sprint sprint = sprintService.getSprintActual(proyecto);
        
        List<BacklogItem> listaBacklog = new ArrayList<>();
        for(BacklogItem b: sprint.getBacklog()){
            if(b.getEstado().equals(Estado.TODO))
                listaBacklog.add(b);
        }

        Collections.sort(listaBacklog, Comparator.comparingInt(BacklogItem::getOrden));

        String inicioSprint = sprint.getInicioSprint().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String finSprint = sprint.getInicioSprint().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        model.addAttribute("inicioSprint", inicioSprint);
        model.addAttribute("finSprint", finSprint);
        model.addAttribute("listaBacklog", listaBacklog);
        ModelHelper.setModel(model, proyecto, sprint);
        return "backlog/po_crear_backlog";
    }
/*
    @GetMapping("/backlog/guardado")
    public String backlogGuardado(Model model){
        user = Autenticador.autenticador();

        model.addAttribute("u", user);
        return "/backlog/guardado";
    }
*/
    @GetMapping("/backlog/{idProyecto}/finalizar/{idBacklog}")
    public String finalizarBacklogItem(Model model,@PathVariable Long idProyecto,
     @PathVariable Long idBacklog) throws ItemNotFoundException{
        Persona p = (Persona) Autenticador.autenticador();
        Proyecto proyecto = p.getProyecto();
        Sprint sprint = sprintService.getSprintActual(proyecto);
        BacklogItem backlogItem = backlogItemService.findById(idBacklog);
        if(esMismaPersona(p, backlogItem)){
            backlogItem.setEstado(Estado.DONE);
            backlogItem.setDiaAcabado((int) ChronoUnit.DAYS.between(sprint.getInicioSprint(), LocalDate.now()));
            backlogItemService.save(backlogItem);
        }
        return "redirect:/backlog/"+idProyecto;
    }

    @GetMapping("/backlog/{idProyecto}/historial")
    public String historialBacklog(Model model,@PathVariable Long idProyecto) throws ItemNotFoundException {
        Proyecto proyecto = proyectoService.findById(idProyecto);
        Sprint sprint = sprintService.getSprintActual(proyecto);
        List<BacklogItem> listaItems = new ArrayList<>();
        for(Sprint s: proyecto.getSprint()){
            listaItems.addAll(s.getBacklog());
        }
        model.addAttribute("listaItems", listaItems);
        ModelHelper.setModel(model, proyecto, sprint);
        return "backlog/historial";
     }
    private boolean esMismaPersona(Persona p, BacklogItem b){
        return p.equals(b.getEncargado());
    }
}
