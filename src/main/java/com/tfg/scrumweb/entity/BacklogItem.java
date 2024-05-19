package com.tfg.scrumweb.entity;

import java.io.Serializable;

import com.tfg.scrumweb.enumeradores.Estado;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BacklogItem implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer orden;
    private String item;
    private Integer dificultad;
    private Estado estado;

    private Integer diaAcabado;
    
    @ManyToOne
    private Persona encargado;
    
    @ManyToOne
    private Sprint sprint;

    private Integer scrumSubTeam;

    @ManyToOne
    private Proyecto proyecto;

    public BacklogItem(Integer orden, String item) {
        this.estado = Estado.TODO;
        this.orden = orden;
        this.item = item;
    }
    public BacklogItem(Integer orden, String item, Sprint sprint) {
        this(orden, item);
        this.sprint=sprint;
    }
    public BacklogItem(Integer orden, String item, Persona encargado, Estado estado, Integer dificultad, Sprint sprint) {
        this(orden, item);
        this.encargado = encargado;
        if(encargado != null)
            this.scrumSubTeam = encargado.getScrumSubTeam();
        this.estado  = estado;
        this.dificultad = dificultad;
        this.sprint = sprint;
    }
    @Override
    public String toString() {
        return "BacklogItem [id=" + id + ", orden=" + orden + ", item=" + item + ", "
                + ", dificultad=" + dificultad + ", estado=" + estado + ", diaAcabado=" + diaAcabado + "]";
    }
    public String toStringEmail(){
        return "Item: " + item + " done by: " + encargado.getUsername() + " the day: " + diaAcabado + ".\n";
    }
}
