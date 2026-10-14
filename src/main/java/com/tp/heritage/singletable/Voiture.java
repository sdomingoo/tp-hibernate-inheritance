package com.tp.heritage.singletable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Sous-classe Voiture stockée dans la table 'vehicules' avec le discriminateur 'VOITURE'.
 */
@Entity
@DiscriminatorValue("VOITURE")
public class Voiture extends Vehicule {

    @Column(name = "nombre_portes")
    private int nombrePortes;

    @Column(name = "climatisation")
    private boolean climatisation;

    public Voiture() {
        super();
    }

    public Voiture(String marque, Double prixAchat, int nombrePortes, boolean climatisation) {
        super(marque, prixAchat);
        this.nombrePortes = nombrePortes;
        this.climatisation = climatisation;
    }

    public int getNombrePortes() {
        return nombrePortes;
    }

    public void setNombrePortes(int nombrePortes) {
        this.nombrePortes = nombrePortes;
    }

    public boolean isClimatisation() {
        return climatisation;
    }

    public void setClimatisation(boolean climatisation) {
        this.climatisation = climatisation;
    }

    @Override
    public String toString() {
        return "Voiture [id=" + getId() + ", marque=" + getMarque() + ", prix=" + getPrixAchat()
                + "€, portes=" + nombrePortes + ", clim=" + climatisation + "]";
    }
}
