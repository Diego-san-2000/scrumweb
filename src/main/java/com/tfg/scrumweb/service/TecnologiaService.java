package com.tfg.scrumweb.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Tecnologia;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.repository.TecnologiaRepository;

@Service
public class TecnologiaService {
    Logger logger = LoggerFactory.getLogger(TecnologiaService.class);
    @Autowired
    private TecnologiaRepository tecnologiaRepository;
    
    public Tecnologia save(Tecnologia t){
        return tecnologiaRepository.save(t);
    }

    public Tecnologia findById(Long id) throws ItemNotFoundException {
        Optional<Tecnologia> oTecnologia = tecnologiaRepository.findById(id);
        if(oTecnologia.isPresent())
            return oTecnologia.get();
        else
            throw new ItemNotFoundException("Tecnología no encontrada.");
    }
    public void deleteAll(Persona p, List<Tecnologia> tecnologia){
        for(Tecnologia t: tecnologia){
            p.getTecnologias().remove(t);
            tecnologiaRepository.delete(t);
        }
        
    }
}
