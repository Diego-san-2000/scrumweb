package com.tfg.scrumweb.controller;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.entity.Starfish;
import com.tfg.scrumweb.entity.Usuario;

public class ModelHelper {
    static Logger logger = LoggerFactory.getLogger(ModelHelper.class);

    private ModelHelper(){
        throw new IllegalStateException("Utility class");
      }

    protected static void setModel(Model model, Proyecto proyecto, Sprint sprint){
        Usuario u = Autenticador.autenticador();
        model.addAttribute("u", u);
        if(u instanceof Persona){
            model.addAttribute("menuEmpleado", true);
        }
        Starfish starfish = sprint.getStarfish();
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("sprint", sprint);
       
        model.addAttribute("starfish", starfish);
        String image = null;
        if(u.getImg() != null){
            byte[] imageBytes = u.getImg().getContent();
            image = Base64.getEncoder().encodeToString(imageBytes);
        }
        model.addAttribute("imagenUsuario", image);
    }
    protected static void setModelEmpresa(Model model, Empresa e){
        Usuario u = Autenticador.autenticador();
        model.addAttribute("u", u);
        String image = null;
        if(e.getImg() != null){
            byte[] imageBytes = u.getImg().getContent();
            image = Base64.getEncoder().encodeToString(imageBytes);
        }
        model.addAttribute("imagenUsuario", image);
    }

}
