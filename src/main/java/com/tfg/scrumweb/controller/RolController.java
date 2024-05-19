package com.tfg.scrumweb.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.entity.Usuario;
import com.tfg.scrumweb.enumeradores.Rol;
import com.tfg.scrumweb.exception.ScrumViolation;
import com.tfg.scrumweb.service.ProyectoService;
import com.tfg.scrumweb.service.SprintService;

@Controller
public class RolController {
    Logger logger = LoggerFactory.getLogger(RolController.class);

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private SprintService sprintService;

    @GetMapping("/rol")
    public String rol(Model model) throws ScrumViolation {
        Usuario user = Autenticador.autenticador();
        //Es empleado
        if(user instanceof Persona){
            Persona p = (Persona) user;
            model.addAttribute("u", p);
            Proyecto proyecto = p.getProyecto();
            Sprint sprint = sprintService.getSprintActual(proyecto);
            Boolean backlogCreado = sprint.getSprintPlanningDone();
            logger.info(backlogCreado.toString());
            //Es el product owner
            if((p.getRol() == Rol.PRODUCT_OWNER) && Boolean.FALSE.equals((backlogCreado)))
                return "redirect:/backlog/"+proyecto.getId()+"/editar";
            
            if((p.getRol() == Rol.PRODUCT_OWNER) && Boolean.TRUE.equals((backlogCreado)))
                return "redirect:/proyecto/empleado";

            //Es el Stakeholder
            if(p.getRol().equals(Rol.STAKEHOLDER))
                return "redirect:/stakeholder";

            //Developer y Scrum Master
            else{
                if(Boolean.FALSE.equals(backlogCreado))
                    throw new ScrumViolation("The Product Owner has not made the Product Backlog yet");
                return "redirect:/proyecto/empleado";
            }
        }
        //Es empresa
        else {
            Empresa empresa = (Empresa) user;
            model.addAttribute("empresa", empresa);

            List<Proyecto> p = proyectoService.findAllByEmpresa(empresa).get();
            model.addAttribute("listaProyecto", p);
            ModelHelper.setModelEmpresa(model, empresa);
            return "proyectos";
        }
    }
}
