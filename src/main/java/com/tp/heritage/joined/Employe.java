package com.tp.heritage.joined;

import javax.persistence.*;

/**
 * Classe de base Employe utilisant la stratégie JOINED.
 * Une table 'employes' contiendra les colonnes communes (id, nom, salaire).
 * Les sous-classes auront chacune leur propre table liée par clé étrangère.
 */
@Entity
@Table(name = "employes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "salaire")
    private Double salaire;

    public Employe() {
    }

    public Employe(String nom, Double salaire) {
        this.nom = nom;
        this.salaire = salaire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getSalaire() {
        return salaire;
    }

    public void setSalaire(Double salaire) {
        this.salaire = salaire;
    }

    @Override
    public String toString() {
        return "Employe [id=" + id + ", nom=" + nom + ", salaire=" + salaire + "€]";
    }
}
