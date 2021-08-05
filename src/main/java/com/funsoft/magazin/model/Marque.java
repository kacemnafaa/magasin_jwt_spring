package com.funsoft.magazin.model;

import javax.persistence.*;

@Entity //pour generer la table
@Table(name="marque")//renomer
public class Marque {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//creation cle premier
    private long id;
    private String nom_marque;

    public Marque() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom_marque() {
        return nom_marque;
    }

    public void setNom_marque(String nom_marque) {
        this.nom_marque = nom_marque;
    }
}
