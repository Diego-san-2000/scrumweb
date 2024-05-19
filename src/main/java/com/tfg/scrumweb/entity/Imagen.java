package com.tfg.scrumweb.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
public class Imagen implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "content", columnDefinition = "MEDIUMBLOB")
    private byte[] content;

    private String name;

    @OneToOne
    private Usuario usuario;
    public Imagen(byte[] content, String name, Usuario u){
        this.content = content;
        this.name = name;
        this.usuario = u;
    }
}