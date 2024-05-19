package com.tfg.scrumweb.service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.scrumweb.controller.Autenticador;
import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.entity.BusinessCanvas;
import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.ServiceBlueprint;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.entity.Starfish;
import com.tfg.scrumweb.entity.Usuario;
import com.tfg.scrumweb.enumeradores.Estado;
import com.tfg.scrumweb.enumeradores.Rol;
import com.tfg.scrumweb.exception.EmailAlreadyExists;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.exception.ScrumViolation;
import com.tfg.scrumweb.repository.ProyectoRepository;

import jakarta.transaction.Transactional;

@Service
public class ProyectoService {
    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private SprintService sprintService;

    @Autowired
    private BacklogItemService backlogItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private StarfishService starfishService;

    @Autowired
    private BusinessCanvasService businessCanvasService;

    @Autowired
    private ServiceBlueprintService serviceBlueprintService;

    Logger logger = LoggerFactory.getLogger(ProyectoService.class);

    public Proyecto save(Proyecto u){
        return proyectoRepository.save(u);
    }
    
    @Transactional
    public Proyecto findById(Long id) throws ItemNotFoundException {
        return proyectoRepository.findById(id)
            .orElseThrow(() -> new ItemNotFoundException("Proyecto no encontrado con ID: " + id));
    }

    public Optional<List<Proyecto>> findAllByEmpresa(Empresa e){
        return proyectoRepository.findAllByEmpresa(e);
    }
    public void deleteById(Long  idProyecto) throws ItemNotFoundException, NotAllowedException{
        if(idProyecto == null)
            throw new ItemNotFoundException("Project not found");
        Usuario u = Autenticador.autenticador();
        if(u instanceof Persona)
            throw new NotAllowedException("Only businesses can delete projects");
        Empresa e = (Empresa) u;
        
        Proyecto proyecto = this.findById(idProyecto);
        if(!proyecto.getEmpresa().equals(e))
            throw new NotAllowedException("You can't delete another business project");
        proyecto.setDoneAndSent(null);
        List<Persona> backUpPersona = new ArrayList<>(proyecto.getPersonas());
        proyecto.setPersonas(null);
        for(Persona p: backUpPersona){
            p.setListaBacklogItem(null);
            p.setProyecto(null);
            p.getEmpresa().getEmpleados().remove(p);
            p.setEmpresa(null);
        }
            
        List<Sprint> backUpSprint = new ArrayList<>(proyecto.getSprint());
        proyecto.setSprint(null);

        //Para cada Sprint
        for(Sprint s: backUpSprint) {
            //Borramos backlog
            for(BacklogItem b: new ArrayList<>(s.getBacklog())) {
                backlogItemService.delete(b);
            }
            starfishService.delete(s.getStarfish());
            sprintService.delete(s);
        }
        
        proyectoRepository.deleteById(idProyecto);
        userService.deleteAll(backUpPersona);
    }
    @Transactional
    public void setAllDailyTo0() {
        proyectoRepository.setAllDailyTo0();
    }
    private void comprobarRoles(List<Persona> l) throws ScrumViolation{
        boolean sm = false;
        boolean po = false;
        for(Persona p: l){
            if(p.getRol().equals(Rol.SCRUM_MASTER)){
                if(sm)
                    throw new ScrumViolation("There must be only one Scrum Master in every project");
                sm = true;
            }
            if(p.getRol().equals(Rol.PRODUCT_OWNER)){
                if(po)
                    throw new ScrumViolation("There must be only one Product Owner in every project");
                po = true;
            }
        }
    }
    public Proyecto crearProyecto(Proyecto proyecto2) throws EmailAlreadyExists, ScrumViolation{
        Sprint sprint = proyecto2.getSprint().get(0);
        if(ChronoUnit.DAYS.between(sprint.getInicioSprint(), sprint.getFinSprint()) > 32)
            throw new ScrumViolation("A Sprint cannot last longer than 30 days");
        comprobarRoles(proyecto2.getPersonas());
        Empresa empresa = (Empresa) Autenticador.autenticador();
        logger.info("1");
        buscarEmailCoincidente(proyecto2.getPersonas());
        proyecto2.setDoneAndSent(new ArrayList<>());
        Proyecto proyecto = this.save(proyecto2);
        proyecto.setNumeroSprint(1);
        proyecto.setDailyActual(false);
        proyecto.setReviewActual(false);
        proyecto.setRetrospectiveActual(false);
        proyecto.setBusinessCanvas(businessCanvasService.save(new BusinessCanvas()));
        proyecto.setServiceBlueprint(serviceBlueprintService.save(new ServiceBlueprint()));
        logger.info("2");
        
        logger.info("3");
        sprint.setBacklog(new ArrayList<>());
        sprint.setPlanningPokerRealizado(false);
        sprint.setSprintPlanningDone(false);
        sprint.setNumeroSprint(1);
        logger.info("4");
        Starfish starfish = starfishService.save(new Starfish(null, sprint));
        sprint.setStarfish(starfish);
        
        logger.info("5");
        for(Persona p: proyecto.getPersonas()){
            p.setEmpresa(empresa);
            p.setProyecto(proyecto);
            userService.save(p);
        }
        logger.info("6");
        proyecto.setEmpresa(empresa);
        sprint.setProyecto(proyecto);
        logger.info("7");
        return this.save(proyecto);
    }
    
    public Proyecto updateData(Long id, Proyecto p) throws EmailAlreadyExists, ItemNotFoundException, ScrumViolation, NotAllowedException{
        Empresa empresa = (Empresa) Autenticador.autenticador();
        Proyecto modificar = this.findById(id);
        if(!modificar.getEmpresa().equals(empresa))
            throw new NotAllowedException("You can't modify other business projects");
        Sprint sprint = p.getSprint().get(0);
        if(ChronoUnit.DAYS.between(sprint.getInicioSprint(), sprint.getFinSprint()) > 32)
            throw new ScrumViolation("A Sprint cannot last longer than 30 days");
        comprobarRoles(p.getPersonas());
        Proyecto pAntiguo = this.findById(id);
        Sprint sprintAntiguo = sprintService.getSprintActual(pAntiguo);
        buscarEmailCoincidente(p.getPersonas());
        pAntiguo.setNombre(p.getNombre());
        sprintAntiguo.setInicioSprint(sprint.getInicioSprint());
        sprintAntiguo.setFinSprint(sprint.getFinSprint());
        List<Persona> listaNoEstan = new ArrayList<>(pAntiguo.getPersonas());
        for(Persona i: p.getPersonas()){
            if(i.getId() != 0){
                listaNoEstan.remove(i);
                Persona pAntigua = (Persona) userService.findById(i.getId());
                pAntigua.setUsername(i.getUsername());
                pAntigua.setEmail(i.getEmail());
                pAntigua.setScrumSubTeam(i.getScrumSubTeam());
                if(i.getPass() != null)
                    pAntigua.setPassYaEncodeada(i.getPass());
                pAntigua.setRol(i.getRol());
            }
            else{
                i.setId(null);
                Persona persona = (Persona) userService.save(i);
                persona.setEmpresa(pAntiguo.getEmpresa());
                persona.setProyecto(pAntiguo);
            }
        }
        this.save(pAntiguo);

        //No funciona
        for (Persona i : listaNoEstan) {
            for(BacklogItem b: i.getListaBacklogItem()){
                if(b.getEstado().equals(Estado.DOING)){
                    b.setEstado(Estado.TODO);
                    b.setEncargado(null);
                }
                    
            }
            i.setEmail(null);
            i.setRol(null);
            i.setProyecto(null);
            userService.delete(i); // Eliminar personas huérfanas
        }
        return pAntiguo;
    }
    public Proyecto updateProductGoal(Long id, Proyecto proyecto) throws ItemNotFoundException{
        Proyecto pAntiguo = this.findById(id);
        pAntiguo.setProductGoal(proyecto.getProductGoal());
        pAntiguo.setDefinitionOfDone(proyecto.getDefinitionOfDone());
        return pAntiguo;
    }
    private void buscarEmailCoincidente(List<Persona> listaPersonas) throws EmailAlreadyExists{
        for(Persona p: listaPersonas){
            Persona pEncontrada = (Persona) userService.findByEmail(p.getEmail());
            if(pEncontrada != null){
                if(!pEncontrada.getId().equals(p.getId()))
                    throw new EmailAlreadyExists("The email is already in use: " + p.getEmail());
            }
            
        }
    }
    
}
