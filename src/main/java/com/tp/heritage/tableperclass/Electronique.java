package com.tp.heritage.tableperclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Sous-classe Electronique dans la stratégie TABLE_PER_CLASS.
 * Possède sa propre table 'produits_electroniques' avec toutes les colonnes :
 * id, nom, prix, garantie_mois, marque_constructeur.
 */
@Entity
@Table(name = "produits_electroniques")
public class Electronique extends Produit {

    @Column(name = "garantie_mois")
    private int garantie; // Durée de garantie en mois

    @Column(name = "marque_constructeur")
    private String marque;

    public Electronique() {
        super();
    }

    public Electronique(String nom, Double prix, int garantie, String marque) {
        super(nom, prix);
        this.garantie = garantie;
        this.marque = marque;
    }

    public int getGarantie() {
        return garantie;
    }

    public void setGarantie(int garantie) {
        this.garantie = garantie;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    @Override
    public String toString() {
        return "Electronique [id=" + getId() + ", nom=" + getNom() + ", prix=" + getPrix()
                + "€, garantie=" + garantie + " mois, marque=" + marque + "]";
    }
}
