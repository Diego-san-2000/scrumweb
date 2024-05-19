package com.tfg.scrumweb.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.scrumweb.controller.Autenticador;
import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.enumeradores.Estado;
import com.tfg.scrumweb.enumeradores.Rol;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.exception.ScrumViolation;
import com.tfg.scrumweb.repository.BacklogItemRepository;

@Service
public class BacklogItemService {
    @Autowired
    private BacklogItemRepository backlogItemRepository;
    Logger logger = LoggerFactory.getLogger(BacklogItemService.class);
    
    public BacklogItem findById(Long id) throws ItemNotFoundException{
        Optional<BacklogItem> oBacklogItem = backlogItemRepository.findById(id);
        if(!oBacklogItem.isPresent())
            throw new ItemNotFoundException("Item del Backlog no encontrado");
        else
            return oBacklogItem.get();
    }
    public BacklogItem save(BacklogItem u){
        return backlogItemRepository.save(u);
    }
    public void deleteById(Long id) throws ItemNotFoundException, ScrumViolation, NotAllowedException{
        Persona u = (Persona) Autenticador.autenticador();
        if(!u.getRol().equals(Rol.PRODUCT_OWNER) && !u.getRol().equals(Rol.DEVELOPER))
            throw new ScrumViolation("Only Developers and Product Owners can delete Backlog items");
        BacklogItem b = this.findById(id);
        if(!u.getProyecto().equals(b.getSprint().getProyecto()))
            throw new NotAllowedException("You can't delete Backlog items in other proyects");
        BacklogItem backlogItem = this.findById(id);
        Sprint sprint = backlogItem.getSprint();
        sprint.getBacklog().remove(backlogItem);
        backlogItem.setSprint(null);
        backlogItemRepository.deleteById(id);
    }

    public void deleteAll(List<BacklogItem> l){
        backlogItemRepository.deleteAllInBatch(l);
    }
    public BacklogItem updateData(Long id, BacklogItem backlogItem) throws ItemNotFoundException{
        BacklogItem backlogItemAntiguo = this.findById(id);
        backlogItemAntiguo.setOrden(backlogItem.getOrden());
        backlogItemAntiguo.setItem(backlogItem.getItem());
        backlogItemAntiguo.setDificultad(backlogItem.getDificultad());
        backlogItemAntiguo.setEncargado(backlogItem.getEncargado());
        if(backlogItem.getEncargado() != null)
            backlogItemAntiguo.setScrumSubTeam(backlogItem.getEncargado().getScrumSubTeam());
        backlogItemAntiguo.setEstado(backlogItem.getEstado());
        return this.save(backlogItemAntiguo);
    }

    public BacklogItem aceptar(BacklogItem b){
        b.setEstado(Estado.DONEANDSENT);
        b.setProyecto(b.getSprint().getProyecto());
        b.getProyecto().anadirDoneAndSent(b);
        return b;
    }
    public BacklogItem rechazar(BacklogItem b){
        b.setEstado(Estado.TODO);
        b.setDificultad(null);
        b.setDiaAcabado(null);
        b.getEncargado().getListaBacklogItem().remove(b);
        b.setEncargado(null);
        b.setScrumSubTeam(null);
        return b;
    }
    public void delete(BacklogItem b){
        b.getSprint().getBacklog().remove(b);
        b.setSprint(null);
        if((b.getEncargado()!= null) && (b.getEncargado().getListaBacklogItem() != null)){
            logger.info("Hemos entrado");
            b.getEncargado().getListaBacklogItem().remove(b);
        }
            
            
        
        b.setEncargado(null);
        if(b.getProyecto() != null)
            b.getProyecto().getDoneAndSent().remove(b);
        
        b.setProyecto(null);
        backlogItemRepository.delete(b);
    }
}
