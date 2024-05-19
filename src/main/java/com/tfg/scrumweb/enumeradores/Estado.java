package com.tfg.scrumweb.enumeradores;

public enum Estado {
    TODO("To Do"),
    DOING("Doing"),
    DONE("Done"),
    DONEANDSENT("Done");

    private final String descripcion;
    Estado(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
