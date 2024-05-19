package com.tfg.scrumweb.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.scrumweb.controller.Autenticador;
import com.tfg.scrumweb.entity.BusinessCanvas;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.enumeradores.Rol;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.repository.BusinessCanvasRepository;

@Service
public class BusinessCanvasService {
    @Autowired
    private BusinessCanvasRepository businessCanvasRepository;
    Logger logger = LoggerFactory.getLogger(BusinessCanvasService.class);

    public BusinessCanvas findById(Long id) throws ItemNotFoundException{
        Optional<BusinessCanvas> businessCanvas = businessCanvasRepository.findById(id);
        if(!businessCanvas.isPresent())
            throw new ItemNotFoundException("Business canvas not found");
        else
            return businessCanvas.get();
    }
    public BusinessCanvas save(BusinessCanvas u){
        return businessCanvasRepository.save(u);
    }
    public void deleteById(Long id) {
        businessCanvasRepository.deleteById(id);
    }
    public void updateData(BusinessCanvas businessCanvas, BusinessCanvas data) throws NotAllowedException{
        
        Persona persona = (Persona) Autenticador.autenticador();
        if(!persona.getRol().equals(Rol.SCRUM_MASTER))
            throw new NotAllowedException("Only the Scrum Master can modify the business canvas");
        if(!businessCanvas.getId().equals(persona.getProyecto().getBusinessCanvas().getId()))
            throw new NotAllowedException("You cannot modify other projects' business canvas");
        businessCanvas.setKeyPartners(data.getKeyPartners());
        businessCanvas.setKeyActivities(data.getKeyActivities());
        businessCanvas.setKeyResources(data.getKeyResources());
        businessCanvas.setValuePropositions(data.getValuePropositions());
        businessCanvas.setCustomerRelationships(data.getCustomerRelationships());
        businessCanvas.setChannels(data.getChannels());
        businessCanvas.setCustomersSegments(data.getCustomersSegments());
        businessCanvas.setCostStructure(data.getCostStructure());
        businessCanvas.setRevenueStreams(data.getRevenueStreams());
    }

}
