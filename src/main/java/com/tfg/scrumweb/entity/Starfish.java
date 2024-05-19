package com.tfg.scrumweb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Starfish implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Sprint sprint;

    @ElementCollection
    private List<String> seguirHaciendo;

    @ElementCollection
    private List<String> masDe;

    @ElementCollection
    private List<String> menosDe;

    @ElementCollection
    private List<String> empezarAHacer;

    @ElementCollection
    private List<String> dejarDeHacer;
    
    public Starfish(Long id, Sprint sprint){
        this.id = id;
        this.sprint = sprint;
        this.seguirHaciendo = new ArrayList<>();
        this.masDe = new ArrayList<>();
        this.menosDe = new ArrayList<>();
        this.empezarAHacer = new ArrayList<>();
        this.dejarDeHacer = new ArrayList<>();
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
        Starfish other = (Starfish) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
}
