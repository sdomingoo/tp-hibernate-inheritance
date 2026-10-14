package com.tp.heritage.singletable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Sous-classe Moto stockée dans la table 'vehicules' avec le discriminateur 'MOTO'.
 */
@Entity
@DiscriminatorValue("MOTO")
public class Moto extends Vehicule {

    @Column(name = "sidecar")
    private boolean sidecar;

    @Column(name = "cylindree")
    private int cylindree;

    public Moto() {
        super();
    }

    public Moto(String marque, Double prixAchat, boolean sidecar, int cylindree) {
        super(marque, prixAchat);
        this.sidecar = sidecar;
        this.cylindree = cylindree;
    }

    public boolean isSidecar() {
        return sidecar;
    }

    public void setSidecar(boolean sidecar) {
        this.sidecar = sidecar;
    }

    public int getCylindree() {
        return cylindree;
    }

    public void setCylindree(int cylindree) {
        this.cylindree = cylindree;
    }

    @Override
    public String toString() {
        return "Moto [id=" + getId() + ", marque=" + getMarque() + ", prix=" + getPrixAchat()
                + "€, sidecar=" + sidecar + ", cylindree=" + cylindree + "cc]";
    }
}
