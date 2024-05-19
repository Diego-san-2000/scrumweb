package com.tfg.scrumweb.controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.entity.Starfish;
import com.tfg.scrumweb.enumeradores.Estado;
import com.tfg.scrumweb.enumeradores.Rol;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.MessageNotSent;
import com.tfg.scrumweb.exception.ScrumViolation;
import com.tfg.scrumweb.service.BacklogItemService;
import com.tfg.scrumweb.service.EmailService;
import com.tfg.scrumweb.service.ProyectoService;
import com.tfg.scrumweb.service.SprintService;
import com.tfg.scrumweb.service.StarfishService;
import com.tfg.scrumweb.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Controller
public class ProyectoController {
    Logger logger = LoggerFactory.getLogger(ProyectoController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ProyectoService proyectoService;
    @Autowired
    private SprintService sprintService;
    @Autowired
    private StarfishService starfishService;
    @Autowired
    private BacklogItemService backlogItemService;

    @GetMapping("{idEmpresa}/proyecto/crear")
    public String crearProyecto(Model model, @PathVariable Long idEmpresa) {
        Empresa e = (Empresa) Autenticador.autenticador();
        List<Persona> p = userService.sinProyecto();
        model.addAttribute("listaPersonas", p);
        model.addAttribute("id_empresa", idEmpresa);
        model.addAttribute("fechaCrear", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        model.addAttribute("fechaFinalizar", LocalDate.now().plus(Period.ofMonths(1)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        ModelHelper.setModelEmpresa(model, e);
        return "proyecto/crear_proyecto";
    }

    @GetMapping("/proyecto/{idProyecto}")
    public String verProyectoEmpresa(@PathVariable Long idProyecto, Model model) throws ItemNotFoundException{
        Proyecto proyecto= proyectoService.findById(idProyecto);
        Sprint sprint = sprintService.getSprintActual(proyecto);

        ModelHelper.setModel(model, proyecto, sprint);
        return "proyecto/ver";
    }

    @GetMapping("/proyecto/empleado")
    public String verProyectoEmpleado(Model model) {
        Persona persona = (Persona) Autenticador.autenticador();
        Proyecto proyecto = persona.getProyecto();
        Sprint sprint = sprintService.getSprintActual(proyecto);
        String inicioSprint = sprint.getInicioSprint().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String finSprint = sprint.getFinSprint().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        
        //proyectoService.setAllDailyTo0();

        ModelHelper.setModel(model, proyecto, sprint);
        model.addAttribute("inicioSprint", inicioSprint);
        model.addAttribute("finSprint", finSprint);
        if(persona.getRol().equals(Rol.SCRUM_MASTER)){
            model.addAttribute("scrumMaster", true);
        }
        List<BacklogItem> increments = new ArrayList<>();
        List<BacklogItem> tareas = new ArrayList<>();
        for(BacklogItem b: sprint.getBacklog()){
            logger.info(b.toString());
            if(b.getEncargado()!=null){
                if(b.getEstado().equals(Estado.DONE))
                    increments.add(b);
                if(b.getEncargado().equals(persona) && b.getEstado().equals(Estado.DOING))
                    tareas.add(b);
            }
        }

        if(persona.getRol().equals(Rol.SCRUM_MASTER))
            model.addAttribute("increments", increments);

        model.addAttribute("tareas", tareas);
        model.addAttribute("hayTareas", !tareas.isEmpty());
        return "proyecto/developer";
    }

    @GetMapping("/proyecto/{idProyecto}/editar")
    public String anadirAProyecto(@PathVariable Long idProyecto, Model model) throws ItemNotFoundException{
        Proyecto proyecto = proyectoService.findById(idProyecto);
        Sprint sprint = sprintService.getSprintActual(proyecto);
        ModelHelper.setModel(model, proyecto, sprint);
        return "proyecto/editar";
    }

    //Finalizar sprint
    @GetMapping("/proyecto/{idProyecto}/finalizar")
    public String finalizarSprint(@PathVariable Long idProyecto, Model model) throws ItemNotFoundException{
        Proyecto proyecto = proyectoService.findById(idProyecto);
        Sprint sprint = sprintService.getSprintActual(proyecto);
        ModelHelper.setModel(model, proyecto, sprint);
        model.addAttribute("fechaCrear", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        model.addAttribute("fechaFinalizar", LocalDate.now().plus(Period.ofMonths(1)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return "proyecto/nuevo_sprint";
    }

    //Finalizar sprint
    @Transactional
    @PostMapping("/proyecto/{idProyecto}/finalizar")
    public String finalizarSprintPost(@PathVariable Long idProyecto,
        @RequestParam LocalDate inicioSprint, @RequestParam LocalDate finSprint)
        throws ItemNotFoundException {

        Proyecto proyecto = proyectoService.findById(idProyecto);
        Sprint sprintActual = sprintService.getSprintActual(proyecto);
        
        int numSprintActual = proyecto.getNumeroSprint();
        int numSprintNuevo = numSprintActual + 1;

        List<BacklogItem> backlogItems = new ArrayList<>();
        Starfish starfish = starfishService.save(new Starfish(null, null));
        Sprint nuevoSprint = sprintService.save(
            new Sprint(null, false, false, numSprintNuevo,
            proyecto, null, starfish, inicioSprint, finSprint, ""));

        for(BacklogItem b: sprintActual.getBacklog()){
            if(!b.getEstado().equals(Estado.DONE) && (!b.getEstado().equals(Estado.DONEANDSENT))){
                BacklogItem bNuevo = backlogItemService.save(
                    new BacklogItem(b.getOrden(), b.getItem(), nuevoSprint));
                backlogItems.add(bNuevo);
            }   
        }
        
        nuevoSprint.setBacklog(backlogItems);

        starfish.setSprint(nuevoSprint);
        proyecto.setDailyActual(false);
        proyecto.setReviewActual(false);
        proyecto.setRetrospectiveActual(false);
        
        proyecto.anadirSprint(nuevoSprint);
        proyecto.setNumeroSprint(numSprintNuevo);
        return "redirect:/rol";
    }

    //Reuniones
    @GetMapping("/proyecto/{idProyecto}/realizardaily")
    public String realizarDaily(@PathVariable Long idProyecto) throws ItemNotFoundException{
        Proyecto p = proyectoService.findById(idProyecto);
        p.setDailyActual(true);
        return "redirect:/proyecto/empleado";
    }
    @GetMapping("/proyecto/{idProyecto}/realizarreview")
    public String realizarReview(@PathVariable Long idProyecto) throws ItemNotFoundException{
        Proyecto p = proyectoService.findById(idProyecto);
        p.setReviewActual(true);
        return "redirect:/proyecto/empleado";
    }
    @GetMapping("/proyecto/{idProyecto}/realizarretrospective")
    public String realizarRetrospective(@PathVariable Long idProyecto) throws ItemNotFoundException {
        Proyecto p = proyectoService.findById(idProyecto);
        p.setRetrospectiveActual(true);
        return "redirect:/proyecto/empleado";
    }

    @GetMapping("/proyecto/{idProyecto}/enviarincrement")
    public String enviarIncrement(@PathVariable Long idProyecto)
     throws MessagingException, MessageNotSent, ItemNotFoundException, ScrumViolation{
        Proyecto proyecto = proyectoService.findById(idProyecto);
        if(proyecto.getReviewActual().equals(false)){
            throw new ScrumViolation("Primero se debe realizar el Sprint Review.");
        }
        List<BacklogItem> listaBacklogItems = new ArrayList<>();
        Sprint sprint = sprintService.getSprintActual(proyecto);
        for(BacklogItem item: sprint.getBacklog()){
            if(item.getEstado().equals(Estado.DONEANDSENT)){
                listaBacklogItems.add(item);
            }
        }
        EmailService.sendCorreo(proyecto, listaBacklogItems, proyecto.getStakeHolders());
        return "redirect:/proyecto/empleado";
    }
}
