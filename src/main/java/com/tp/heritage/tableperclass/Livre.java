package com.tp.heritage.tableperclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Sous-classe Livre dans la stratégie TABLE_PER_CLASS.
 * Possède sa propre table 'livres' avec toutes les colonnes : id, nom, prix, auteur, isbn.
 */
@Entity
@Table(name = "livres")
public class Livre extends Produit {

    @Column(name = "auteur")
    private String auteur;

    @Column(name = "isbn")
    private String isbn;

    public Livre() {
        super();
    }

    public Livre(String nom, Double prix, String auteur, String isbn) {
        super(nom, prix);
        this.auteur = auteur;
        this.isbn = isbn;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "Livre [id=" + getId() + ", nom=" + getNom() + ", prix=" + getPrix()
                + "€, auteur=" + auteur + ", isbn=" + isbn + "]";
    }
}
