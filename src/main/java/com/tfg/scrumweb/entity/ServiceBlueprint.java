package com.tfg.scrumweb.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceBlueprint implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String a11;
    private String a12;
    private String a13;
    private String a14;
    private String a15;
    private String a21;
    private String a22;
    private String a23;
    private String a24;
    private String a25;
    private String a31;
    private String a32;
    private String a33;
    private String a34;
    private String a35;
    private String a41;
    private String a42;
    private String a43;
    private String a44;
    private String a45;
    private String a51;
    private String a52;
    private String a53;
    private String a54;
    private String a55;
    private String a61;
    private String a62;
    private String a63;
    private String a64;
    private String a65;    
}
