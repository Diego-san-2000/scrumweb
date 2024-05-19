package com.tfg.scrumweb.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.scrumweb.controller.Autenticador;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.ServiceBlueprint;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.repository.ServiceBlueprintRepository;

@Service
public class ServiceBlueprintService {
    @Autowired
    private ServiceBlueprintRepository serviceBlueprintRepository;

    public ServiceBlueprint save(ServiceBlueprint s){
        return serviceBlueprintRepository.save(s);
    }

    public ServiceBlueprint findById(Long id) throws ItemNotFoundException{
        Optional<ServiceBlueprint> sb = serviceBlueprintRepository.findById(id);
        if(sb.isPresent())
            return sb.get();
        throw new ItemNotFoundException("Service blueprint not found");
    }
    public void update(ServiceBlueprint antiguo, ServiceBlueprint nuevo) throws NotAllowedException {
        Persona persona = (Persona) Autenticador.autenticador();
        if(!antiguo.getId().equals(persona.getProyecto().getServiceBlueprint().getId()))
            throw new NotAllowedException("You cannot modify other projects' service blueprint");
        antiguo.setA11(nuevo.getA11());
        antiguo.setA12(nuevo.getA12());
        antiguo.setA13(nuevo.getA13());
        antiguo.setA14(nuevo.getA14());
        antiguo.setA15(nuevo.getA15());
        antiguo.setA21(nuevo.getA21());
        antiguo.setA22(nuevo.getA22());
        antiguo.setA23(nuevo.getA23());
        antiguo.setA24(nuevo.getA24());
        antiguo.setA25(nuevo.getA25());
        antiguo.setA31(nuevo.getA31());
        antiguo.setA32(nuevo.getA32());
        antiguo.setA33(nuevo.getA33());
        antiguo.setA34(nuevo.getA34());
        antiguo.setA35(nuevo.getA35());
        antiguo.setA41(nuevo.getA41());
        antiguo.setA42(nuevo.getA42());
        antiguo.setA43(nuevo.getA43());
        antiguo.setA44(nuevo.getA44());
        antiguo.setA45(nuevo.getA45());
        antiguo.setA51(nuevo.getA51());
        antiguo.setA52(nuevo.getA52());
        antiguo.setA53(nuevo.getA53());
        antiguo.setA54(nuevo.getA54());
        antiguo.setA55(nuevo.getA55());
        antiguo.setA61(nuevo.getA61());
        antiguo.setA62(nuevo.getA62());
        antiguo.setA63(nuevo.getA63());
        antiguo.setA64(nuevo.getA64());
        antiguo.setA65(nuevo.getA65());
    }    
}
