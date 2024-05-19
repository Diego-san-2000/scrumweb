package com.tfg.scrumweb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tfg.scrumweb.enumeradores.Rol;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Proyecto implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private int numeroSprint;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyecto", orphanRemoval = true,  fetch = FetchType.EAGER)
    private List<Persona> personas;
    
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyecto", fetch = FetchType.EAGER)
    private List<Sprint> sprint;

    @Column(name = "daily_actual")
    private Boolean dailyActual;
    private Boolean reviewActual;
    private Boolean retrospectiveActual;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyecto", fetch = FetchType.EAGER)
    private List<BacklogItem> doneAndSent;

    private String productGoal;

    private String definitionOfDone;

    @OneToOne
    private BusinessCanvas businessCanvas;

    @OneToOne
    private ServiceBlueprint serviceBlueprint;

    public List<Persona> getStakeHolders(){
        List<Persona> toReturn = new ArrayList<>();
        for(Persona p: this.personas){
            if(p.getRol().equals(Rol.STAKEHOLDER))
                toReturn.add(p);
        }
        return toReturn;
    }
    
    public void anadirSprint(Sprint s){
        this.sprint.add(s);
    }
    public void anadirDoneAndSent(BacklogItem b){
        this.doneAndSent.add(b);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Proyecto other = (Proyecto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
