package com.tfg.scrumweb.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.tfg.scrumweb.controller.Autenticador;
import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Starfish;
import com.tfg.scrumweb.entity.Usuario;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.repository.StarfishRepository;

@Service
public class StarfishService {

    @Autowired
    private StarfishRepository starfishRepository;

    public Starfish save(@NonNull Starfish u){
        return starfishRepository.save(u);
    }
    public Starfish findById(Long id) throws ItemNotFoundException{
        Optional<Starfish> oStarfish = starfishRepository.findById(id);
        if(!oStarfish.isPresent()){
            throw new ItemNotFoundException("Starfish no encontrada");
        }
        else
            return oStarfish.get();
    }
    public Starfish crearStarfish(@NonNull Starfish starfish){
        return starfishRepository.save(starfish);
    }
    public Starfish updateStarfish(Long id, Starfish starfish) throws ItemNotFoundException, NotAllowedException{
        Usuario u = Autenticador.autenticador();
        if(u instanceof Empresa)
            throw new NotAllowedException("The Starfish retrospective is only accesible by the Scrum Team");
        Persona p =  (Persona) u;
        
        Starfish starfishAntigua = this.findById(id);
        if(!p.getProyecto().equals(starfishAntigua.getSprint().getProyecto()))
            throw new NotAllowedException("You cannot modify other projects starfish retrospectives");
        if(!starfishAntigua.getSprint().equals(p.getProyecto().getSprint().get(p.getProyecto().getNumeroSprint()-1)))
            throw new NotAllowedException("You cannot modify a starfish retrospective of a previous Sprint");

        starfishAntigua.setDejarDeHacer(starfish.getDejarDeHacer());
        starfishAntigua.setEmpezarAHacer(starfish.getEmpezarAHacer());
        starfishAntigua.setMasDe(starfish.getMasDe());
        starfishAntigua.setMenosDe(starfish.getMenosDe());
        starfishAntigua.setSeguirHaciendo(starfish.getSeguirHaciendo());
        return starfishRepository.save(starfishAntigua);
    }
    public void deleteById(@NonNull Long id){
        starfishRepository.deleteById(id);
    }
    public void delete(Starfish s){
        s.getSprint().setStarfish(null);
        starfishRepository.delete(s);
    }
}
