package com.tfg.scrumweb.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.scrumweb.controller.Autenticador;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.entity.Starfish;
import com.tfg.scrumweb.enumeradores.Rol;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.exception.ScrumViolation;
import com.tfg.scrumweb.repository.SprintRepository;

@Service
public class SprintService {
    @Autowired
    private SprintRepository sprintRepository;
    public Sprint save(Sprint u){
        return sprintRepository.save(u);
    }
    public Sprint getSprintActual(Proyecto p){
        return p.getSprint().get(p.getNumeroSprint()-1);
    }

    public Sprint findById(Long id) throws ItemNotFoundException{
        Optional<Sprint> oSprint = sprintRepository.findById(id);
        if(!oSprint.isPresent()){
            throw new ItemNotFoundException("Sprint no encontrado");
        }
        else
            return oSprint.get();
    }
    public Sprint crearSprint(Sprint s){
        return sprintRepository.save(s);
    }
    public Sprint actualizarSprint(Long id, Sprint s) throws ItemNotFoundException{
        Sprint sAntiguo = this.findById(id);
        sAntiguo.setFinSprint(s.getFinSprint());
        sAntiguo.setInicioSprint(s.getInicioSprint());
        sAntiguo.setSprintGoal(s.getSprintGoal());
        return sAntiguo;
    }
    public Sprint updateSprintGoal(Sprint sAntiguo, Sprint s) throws ScrumViolation, NotAllowedException {
        Persona p = (Persona) Autenticador.autenticador();
        if(!p.getRol().equals(Rol.DEVELOPER))
            throw new ScrumViolation("Only Developers can modify the Sprint Goal");

        if(!p.getProyecto().equals(s.getProyecto()))
            throw new NotAllowedException("You cannot modify other projects Sprint Goal");
        sAntiguo.setSprintGoal(s.getSprintGoal());
        sAntiguo.setPlanningPokerRealizado(true);
        return sAntiguo;
    }
    public Sprint updateSprintPlanning(Sprint s) throws NotAllowedException, ScrumViolation {
        Persona p = (Persona) Autenticador.autenticador();
        if(!p.getRol().equals(Rol.PRODUCT_OWNER))
            throw new ScrumViolation("Only the Product Owner can modify the Sprint Planning");
        if(!p.getProyecto().equals(s.getProyecto()))
            throw new NotAllowedException("You cannot modify other projects Sprint Planning");
        s.setSprintPlanningDone(true);
        return s;
    }
    public void deleteById(Long id){
        sprintRepository.deleteById(id);
    }
    public void delete(Sprint s){
        sprintRepository.delete(s);
    }
}
