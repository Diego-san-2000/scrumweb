package com.tfg.scrumweb.enumeradores;

public enum Categoria {
    
    SOCIAL("Social"),
    TECNOLOGIA("Tecnología"),
    NEGOCIO("Negocio"),
    CREATIVA("Creativa"),
    EDUCATIVA("Educativa");

    private final String descripcion;
    Categoria(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
