package com.tfg.scrumweb.enumeradores;

public enum Rol {
    DEVELOPER("Developer"),
    SCRUM_MASTER("Scrum Master"),
    PRODUCT_OWNER("Product Owner"),
    STAKEHOLDER("Stakeholder");

    private final String descripcion;

    Rol(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
