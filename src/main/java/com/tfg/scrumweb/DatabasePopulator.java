package com.tfg.scrumweb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.entity.BusinessCanvas;
import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.ServiceBlueprint;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.entity.Starfish;
import com.tfg.scrumweb.enumeradores.Estado;
import com.tfg.scrumweb.enumeradores.Rol;
import com.tfg.scrumweb.repository.ProyectoRepository;
import com.tfg.scrumweb.repository.SprintRepository;
import com.tfg.scrumweb.repository.StarfishRepository;
import com.tfg.scrumweb.repository.UserRepository;
import com.tfg.scrumweb.service.BacklogItemService;
import com.tfg.scrumweb.service.BusinessCanvasService;
import com.tfg.scrumweb.service.ServiceBlueprintService;

import jakarta.transaction.Transactional;

@Component
public class DatabasePopulator implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private BacklogItemService backlogItemService;

    @Autowired
    private StarfishRepository starfishRepository;

    @Autowired
    private BusinessCanvasService businessCanvasService;
    
    @Autowired
    private ServiceBlueprintService serviceBlueprintService;

    private final Boolean usar = true;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(userRepository.findAll().isEmpty() && Boolean.TRUE.equals(usar)){
            //ID, Nombre, Email, pass
            Empresa e = userRepository.save(new Empresa(null, "Company", "e@scrumweb.es", "e"));

            //ID, Nombre, Emal, Pass, Rol
            Persona po = userRepository.save(
                new Persona(null, "Product Owner", "po@scrumweb.es", "p", e, Rol.PRODUCT_OWNER));
            Persona s = userRepository.save(
                new Persona(null, "Scrum Master", "sm@scrumweb.es", "s", e, Rol.SCRUM_MASTER));
            Persona d = userRepository.save(
                new Persona(null, "Developer", "d@scrumweb.es", "d", e, Rol.DEVELOPER, 1));
            Persona d1 = userRepository.save(
                new Persona(null, "Developer1", "d1@scrumweb.es", "d1", e, Rol.DEVELOPER, 1));
            Persona sk = userRepository.save(
                new Persona(null, "Stakeholder", "diegosanchezguerrero@gmail.com", "sk", e, Rol.STAKEHOLDER, 1));

            
            LocalDate now = LocalDate.now();
            LocalDate fin = now.plusDays(30);
            //Constructor proyectos:
            //ID, Nombre, Nº Sprint, ListaPersonas, Empresa, Backlog, InicioSprint, FinSprint, Daily, Review, Retrospective
            Starfish starfish = starfishRepository.save(new Starfish(null, null));

            starfish.setDejarDeHacer(new ArrayList<>(
                Arrays.asList("Ignoring team feedback", "Accepting unrealistic deadlines")));
            starfish.setEmpezarAHacer(new ArrayList<>(
                Arrays.asList("Using time tracking tools", "Scheduling brainstorming sessions", "Create a knowledge base")));
            starfish.setMasDe(new ArrayList<>(
                Arrays.asList("Time for professional development", "Regular updates on project progress", "Pair programming sessions")));
            starfish.setMenosDe(new ArrayList<>(
                Arrays.asList("Micromanaging team members' tasks", "Using email for urgent communication")));
            starfish.setSeguirHaciendo(new ArrayList<>(
                Arrays.asList("Encouraging team members", "Using clear and open channels of communication", "Celebrating team successes")));

            Sprint sprint = sprintRepository.save(new Sprint(null, true, true, 
            1, null, null, starfish, now, fin, "This is the Sprint 1 Goal"));
            starfish.setSprint(sprint);
            sprint.setStarfish(starfish);
            List<Sprint> listaSprint = new ArrayList<>();
            listaSprint.add(sprint);
            
            BusinessCanvas bc = businessCanvasService.save(new BusinessCanvas());
            ServiceBlueprint sc = serviceBlueprintService.save(new ServiceBlueprint());

            Proyecto py = new Proyecto(null, "Test project",
             1, null, e, listaSprint, true,
             false, false, new ArrayList<>(),
              "This is the Product Goal", "This is the Definition of Done",
               bc, sc);
            
            proyectoRepository.save(py);
            sprint.setProyecto(py);
            po.setProyecto(py);
            s.setProyecto(py);
            d.setProyecto(py);
            d1.setProyecto(py);
            sk.setProyecto(py);

            List<Persona> lp = new ArrayList<>();
            lp.add(po);
            lp.add(s);
            lp.add(d);
            lp.add(d1);
            lp.add(sk);

            py.setPersonas(lp);
            py.setEmpresa(e);
            proyectoRepository.save(py); //NUEVO

            //Id, orden, item, numeroSprint, dificultad, estado, encargado, proyecto
             
            BacklogItem b0 = new BacklogItem(null, 0,
            "Implement SSL", 20, Estado.DONEANDSENT, 1, d, sprint, 1, null);
            BacklogItem b1 = new BacklogItem(null, 1,
            "Redirect broken links", 40, Estado.DONEANDSENT, 2, d, sprint, 1, null);
            BacklogItem b2 = new BacklogItem(null, 2,
            "Migrate database", 100, Estado.DONEANDSENT, 3, d, sprint, 1, null);
            backlogItemService.save(b0);
            backlogItemService.save(b1);
            backlogItemService.save(b2);
            d.getListaBacklogItem().add(b0);
            d.getListaBacklogItem().add(b2);
            d1.getListaBacklogItem().add(b1);
            List<BacklogItem> bi = new ArrayList<>();
            bi.add(b0);
            bi.add(b1);
            bi.add(b2);
            py.anadirDoneAndSent(b0);
            py.anadirDoneAndSent(b1);
            py.anadirDoneAndSent(b2);
            proyectoRepository.save(py);
            sprint.setBacklog(bi);
            sprintRepository.save(sprint);
            
            //Creamos segundo sprint
            
            py.setNumeroSprint(2);
            Starfish starfish2 = starfishRepository.save(new Starfish(null, null));
            Sprint sprint2 = sprintRepository.save(new Sprint(null, true,
            true, 2, py, null, starfish2, now, fin, "This is the Sprint 2 Goal"));
            starfish2.setSprint(sprint2);
            starfish2.setDejarDeHacer(new ArrayList<>(
                Arrays.asList("Ignoring team feedback", "Accepting unrealistic deadlines")));
                starfish2.setEmpezarAHacer(new ArrayList<>(
                Arrays.asList("Using time tracking tools", "Scheduling brainstorming sessions", "Create a knowledge base")));
                starfish2.setMasDe(new ArrayList<>(
                Arrays.asList("Time for professional development", "Regular updates on project progress", "Pair programming sessions")));
                starfish2.setMenosDe(new ArrayList<>(
                Arrays.asList("Micromanaging team members' tasks", "Using email for urgent communication")));
                starfish2.setSeguirHaciendo(new ArrayList<>(
                Arrays.asList("Encouraging team members", "Using clear and open channels of communication", "Celebrating team successes")));
            starfishRepository.save(starfish2);
            py.anadirSprint(sprint2);

            BacklogItem b10 = new BacklogItem(null, 0,
            "Resize images", 100, Estado.TODO, null, null, sprint2, null, null);
            BacklogItem b11 = new BacklogItem(null, 1,
            "Modify hosting provider", 40, Estado.TODO, null, null, sprint2, null, null);
            BacklogItem b12 = new BacklogItem(null, 2,
            "Create cookies policy", 20, Estado.TODO, null, null, sprint2, null, null);
            backlogItemService.save(b10);
            backlogItemService.save(b11);
            backlogItemService.save(b12);
            List<BacklogItem> bi2 = new ArrayList<>();
            bi2.add(b0);
            bi2.add(b1);
            bi2.add(b2);
            sprint.setBacklog(bi2);
            sprintRepository.save(sprint2);
            
        }
    }
}
