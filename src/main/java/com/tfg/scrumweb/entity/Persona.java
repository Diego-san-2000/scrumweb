package com.tfg.scrumweb.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tfg.scrumweb.enumeradores.Rol;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Persona extends Usuario{
    @ManyToOne
    @JoinColumn(name = "proyecto_id", nullable = true)
    @JsonIgnore
    private Proyecto proyecto;

    @ManyToOne
    private Empresa empresa;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "encargado", fetch = FetchType.EAGER)
    private List<BacklogItem> listaBacklogItem;

    private Rol rol;

    private Integer scrumSubTeam;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "persona", fetch = FetchType.EAGER)
    private List<Tecnologia> tecnologias;
    

    public Persona(Long id, String name, String email, String pass, Proyecto proyecto, Rol rol) {
        super(id, name, email, pass);
        this.tecnologias = new ArrayList<>();
        this.proyecto = proyecto;
        this.rol = rol;
        this.listaBacklogItem = new ArrayList<>();
       
    }
    public Persona(Long id, String name, String email, String pass, Empresa empresa, Rol rol) {
        super(id, name, email, pass);
        this.tecnologias = new ArrayList<>();
        this.empresa = empresa;
        this.rol = rol;
        this.listaBacklogItem = new ArrayList<>();
    }
    
    public Persona(Long id, String name, String email, String pass, Empresa empresa, Rol rol, Integer i) {
        this(id, name, email, pass, empresa, rol);
        this.scrumSubTeam = i;
    }

    public Persona(Long id, String name, String email, String pass, Rol rol) {
        super(id, name, email, pass);
        this.tecnologias = new ArrayList<>();
        this.rol = rol;
        this.setPass(pass);
        this.listaBacklogItem = new ArrayList<>();
    }
    public void anadirTecnologia(Tecnologia t){
        this.tecnologias.add(t);
    }
    @Override
    public String toString() {
        return "Persona [proyecto=" + proyecto + ", empresa=" + empresa + ", listaBacklogItem=" + listaBacklogItem
                + ", rol=" + rol + ", scrumSubTeam=" + scrumSubTeam + ", tecnologias=" + tecnologias + "]";
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getEmail() == null) ? 0 : this.getEmail().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Persona other = (Persona) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }
}
