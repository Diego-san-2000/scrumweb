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

import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.entity.Usuario;
import com.tfg.scrumweb.enumeradores.Rol;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.exception.ScrumViolation;
import com.tfg.scrumweb.service.ProyectoService;
import com.tfg.scrumweb.service.SprintService;

@Controller
public class PlanningPokerController {
    Logger logger = LoggerFactory.getLogger(PlanningPokerController.class);

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private SprintService sprintService;

    @GetMapping("/planningpoker/{idProyecto}")
    public String planningPoker(@PathVariable Long idProyecto, Model model) throws ItemNotFoundException, NotAllowedException, ScrumViolation {
        Usuario user = Autenticador.autenticador();
        Proyecto proyecto = proyectoService.findById(idProyecto);
        Sprint sprint = sprintService.getSprintActual(proyecto);

        if(user instanceof Empresa)
            throw new NotAllowedException("The planning poker is only accessible by Developers");

        Persona p = (Persona) user;
        if(p.getRol().equals(Rol.PRODUCT_OWNER) || p.getRol().equals(Rol.STAKEHOLDER))
            throw new NotAllowedException("The planning poker is only accessible by Developers");

        
        if(sprint.getSprintPlanningDone().equals(false))
            throw new ScrumViolation("The Product owner must review the Product Backlog first");

        else{
            List<Persona> listaUsuarios = new ArrayList<>();
            for(Persona empleado: proyecto.getPersonas()){
                if(empleado.getRol().equals(Rol.DEVELOPER)
                && empleado.getScrumSubTeam().equals(p.getScrumSubTeam())){
                    listaUsuarios.add(empleado);
                }
            }
            List<BacklogItem> todo = new ArrayList<>();
            List<BacklogItem> doing = new ArrayList<>();
            for(BacklogItem item: sprint.getBacklog()){
                switch (item.getEstado()) {
                    case TODO:
                        todo.add(item);
                        break;
                    case DOING:
                        if((p.getScrumSubTeam()==null) || (p.getScrumSubTeam().equals(item.getScrumSubTeam())))
                            doing.add(item);
                        break;
                    default:
                        break;
                }
            }
            ModelHelper.setModel(model, proyecto, sprint);
            model.addAttribute("todo", todo);
            model.addAttribute("doing", doing);
            model.addAttribute("id_usuario", p.getId());
            model.addAttribute("listaUsuarios", listaUsuarios);

            return "planningpoker/index";
        }
    }
}
