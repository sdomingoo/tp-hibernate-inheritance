package com.tp.heritage.joined;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Sous-classe Manager pour la stratégie JOINED.
 * Génère une table 'managers' liée par clé étrangère à la table 'employes'.
 */
@Entity
@Table(name = "managers")
public class Manager extends Employe {

    @Column(name = "nombre_equipes")
    private int nombreEquipes;

    @Column(name = "departement")
    private String departement;

    public Manager() {
        super();
    }

    public Manager(String nom, Double salaire, int nombreEquipes, String departement) {
        super(nom, salaire);
        this.nombreEquipes = nombreEquipes;
        this.departement = departement;
    }

    public int getNombreEquipes() {
        return nombreEquipes;
    }

    public void setNombreEquipes(int nombreEquipes) {
        this.nombreEquipes = nombreEquipes;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    @Override
    public String toString() {
        return "Manager [id=" + getId() + ", nom=" + getNom() + ", salaire=" + getSalaire()
                + "€, équipes=" + nombreEquipes + ", dept=" + departement + "]";
    }
}
