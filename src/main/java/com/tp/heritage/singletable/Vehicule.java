package com.tp.heritage.singletable;

import javax.persistence.*;

/**
 * Classe de base Vehicule utilisant la stratégie SINGLE_TABLE.
 * Toutes les sous-classes seront stockées dans une seule table 'vehicules'.
 */
@Entity
@Table(name = "vehicules")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_vehicule", discriminatorType = DiscriminatorType.STRING, length = 20)
public abstract class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marque", nullable = false)
    private String marque;

    @Column(name = "prix_achat")
    private Double prixAchat;

    // Constructeur sans argument obligatoire pour JPA
    public Vehicule() {
    }

    public Vehicule(String marque, Double prixAchat) {
        this.marque = marque;
        this.prixAchat = prixAchat;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Double getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(Double prixAchat) {
        this.prixAchat = prixAchat;
    }

    @Override
    public String toString() {
        return "Vehicule [id=" + id + ", marque=" + marque + ", prixAchat=" + prixAchat + "]";
    }
}
