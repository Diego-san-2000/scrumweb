package com.tfg.scrumweb.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.exception.EmailAlreadyExists;
import com.tfg.scrumweb.service.UserService;

@Controller
public class GreetingController {
    Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String greeting() {
        return "home";
    }
    @GetMapping("/registrarse")
    public String login() {
        return "registrarse";
    }

    @PostMapping("/registrarse")
    public String registrarseFormulario(@RequestParam("username") String username, 
        @RequestParam("email") String email, @RequestParam("pass") String pass) throws EmailAlreadyExists, IOException {
            
            //Usuario ya registrado
            if(userService.findByEmail(email) != null)
                throw new EmailAlreadyExists("El correo " + email + " ya está en uso.");
            
            userService.save(new Empresa(null, username, email, pass));

            return "redirect:/login";
    }
    
    /* @PostMapping("/login")
    public String loginFormulario(@RequestParam("email") String email, 
        @RequestParam("pass") String pass, Model model) {
            logger.info("En login");
            model.addAttribute("email", email);
            model.addAttribute("pass", pass);
            return "redirect:rol";
    } */
    
}
