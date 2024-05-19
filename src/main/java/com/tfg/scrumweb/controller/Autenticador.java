package com.tfg.scrumweb.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.tfg.scrumweb.entity.Usuario;

public class Autenticador {

    private Autenticador() {
        throw new IllegalStateException("Utility class");
    }
    
    public static Usuario autenticador(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return (Usuario) userDetails;
    }
}
