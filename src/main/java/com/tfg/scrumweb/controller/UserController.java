package com.tfg.scrumweb.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.scrumweb.dto.ImagenDTO;
import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Imagen;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.Sprint;
import com.tfg.scrumweb.entity.Usuario;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.service.ImagenService;
import com.tfg.scrumweb.service.SprintService;
import com.tfg.scrumweb.service.UserService;

@Controller
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SprintService sprintService;

    @GetMapping("/usuario/{idUsuario}/editar")
    public String editarUsuario(@NonNull @PathVariable Long idUsuario, Model model) throws ItemNotFoundException{
        Usuario usuarioAEditar = userService.findById(idUsuario);
        Usuario u = Autenticador.autenticador();
        model.addAttribute("usuario", usuarioAEditar);
        model.addAttribute("u", u);
        if(u instanceof Empresa)
            model.addAttribute("esEmpresa", true);

        String image = null;
        if(usuarioAEditar.getImg() != null){
            byte[] imageBytes = usuarioAEditar.getImg().getContent();
            image = Base64.getEncoder().encodeToString(imageBytes);
        }
        model.addAttribute("imagen", image);
        
        //Editamos una persona
        if(usuarioAEditar instanceof Persona){
            Persona p = (Persona) usuarioAEditar;
            Proyecto proyecto = p.getProyecto();
            Sprint sprint = sprintService.getSprintActual(proyecto);
            switch (p.getRol()) {
                case SCRUM_MASTER:
                    model.addAttribute("SCRUM_MASTER", true);
                    break;
                case PRODUCT_OWNER:
                    model.addAttribute("PRODUCT_OWNER", true);
                    break;
                case STAKEHOLDER:
                    model.addAttribute("STAKEHOLDER", true);
                    break;
                
                default: //developer
                    model.addAttribute("DEVELOPER", true);
                    break;
            }
            ModelHelper.setModel(model, proyecto, sprintService.getSprintActual(proyecto));
            String inicioSprint = sprint.getInicioSprint().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String finSprint = sprint.getFinSprint().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            
            model.addAttribute("inicioSprint", inicioSprint);
            model.addAttribute("finSprint", finSprint);
            
            model.addAttribute("empresa", p.getEmpresa());
            return "usuario/editar_persona";
        }
        //Editamos una empresa
        else{
            Empresa e = (Empresa) Autenticador.autenticador();
            ModelHelper.setModelEmpresa(model, e);
            return "usuario/editar_empresa";
        }            
    }

    @PostMapping("/usuario/{usuarioId}/imagen")
    public String editarImagen(@PathVariable Long usuarioId, @RequestParam MultipartFile multipartImage) throws IOException, ItemNotFoundException{
        Imagen createdData = userService.updateImagen(usuarioId, multipartImage);
        ImagenDTO imagenDTO = new ImagenDTO(createdData);
        return "redirect:/usuario/"+usuarioId+"/editar";
    }
}
