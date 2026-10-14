package com.tp.heritage.joined;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Sous-classe Developpeur pour la stratégie JOINED.
 * Génère une table 'developpeurs' contenant 'id' (clé primaire et clé étrangère vers 'employes'),
 * ainsi que les attributs spécifiques.
 */
@Entity
@Table(name = "developpeurs")
public class Developpeur extends Employe {

    @Column(name = "langage_principal")
    private String langage;

    @Column(name = "annees_experience")
    private int anneesExperience;

    public Developpeur() {
        super();
    }

    public Developpeur(String nom, Double salaire, String langage, int anneesExperience) {
        super(nom, salaire);
        this.langage = langage;
        this.anneesExperience = anneesExperience;
    }

    public String getLangage() {
        return langage;
    }

    public void setLangage(String langage) {
        this.langage = langage;
    }

    public int getAnneesExperience() {
        return anneesExperience;
    }

    public void setAnneesExperience(int anneesExperience) {
        this.anneesExperience = anneesExperience;
    }

    @Override
    public String toString() {
        return "Developpeur [id=" + getId() + ", nom=" + getNom() + ", salaire=" + getSalaire()
                + "€, langage=" + langage + ", exp=" + anneesExperience + " ans]";
    }
}
