package com.tp.heritage;

import com.tp.heritage.joined.Developpeur;
import com.tp.heritage.joined.Employe;
import com.tp.heritage.joined.Manager;
import com.tp.heritage.singletable.Moto;
import com.tp.heritage.singletable.Vehicule;
import com.tp.heritage.singletable.Voiture;
import com.tp.heritage.tableperclass.Electronique;
import com.tp.heritage.tableperclass.Livre;
import com.tp.heritage.tableperclass.Produit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Classe principale pour tester et observer le comportement des 3 stratégies d'héritage JPA :
 * 1. SINGLE_TABLE (Véhicule -> Voiture, Moto)
 * 2. JOINED (Employe -> Developpeur, Manager)
 * 3. TABLE_PER_CLASS (Produit -> Livre, Electronique)
 */
public class App {

    public static void main(String[] args) {
        System.out.println("=== INITIALISATION DE L'ENTITY MANAGER FACTORY ===");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tp-inheritance-pu");
        EntityManager em = emf.createEntityManager();

        try {
            // ----------------------------------------------------
            // Étape 1 : Insertion des entités (Transaction en écriture)
            // ----------------------------------------------------
            em.getTransaction().begin();

            System.out.println("\n>>> [1] INSERTION DES DONNÉES SINGLE_TABLE (Vehicule)");
            Voiture v1 = new Voiture("Peugeot 308", 22500.0, 5, true);
            Voiture v2 = new Voiture("Renault Clio", 17200.0, 5, false);
            Moto m1 = new Moto("Yamaha MT-07", 7900.0, false, 689);
            em.persist(v1);
            em.persist(v2);
            em.persist(m1);

            System.out.println("\n>>> [2] INSERTION DES DONNÉES JOINED (Employe)");
            Developpeur dev1 = new Developpeur("Alice Dupont", 3400.0, "Java / Hibernate", 4);
            Developpeur dev2 = new Developpeur("Lucas Martin", 2900.0, "React / TypeScript", 2);
            Manager mgr1 = new Manager("Claire Bernard", 4900.0, 3, "Direction Technique");
            em.persist(dev1);
            em.persist(dev2);
            em.persist(mgr1);

            System.out.println("\n>>> [3] INSERTION DES DONNÉES TABLE_PER_CLASS (Produit)");
            Livre livre1 = new Livre("Clean Code", 38.50, "Robert C. Martin", "978-0132350884");
            Livre livre2 = new Livre("Effective Java", 44.00, "Joshua Bloch", "978-0134685991");
            Electronique ecran = new Electronique("Écran 27\" UltraHD", 289.90, 24, "Dell");
            em.persist(livre1);
            em.persist(livre2);
            em.persist(ecran);

            em.getTransaction().commit();
            System.out.println("\n=== PERSISTANCE TERMINÉE AVEC SUCCÈS ===");

            // Vider le cache de premier niveau (session) pour forcer les requêtes SQL SELECT
            em.clear();

            // ----------------------------------------------------
            // Étape 2 : Requêtes polymorphiques et observation SQL
            // ----------------------------------------------------

            System.out.println("\n==================================================");
            System.out.println("--- 1. LECTURE POLYMORPHIQUE : STRATÉGIE SINGLE_TABLE ---");
            System.out.println("==================================================");
            List<Vehicule> vehicules = em.createQuery("SELECT v FROM Vehicule v", Vehicule.class)
                    .getResultList();
            for (Vehicule v : vehicules) {
                System.out.println("  -> Type: " + v.getClass().getSimpleName() + " | Détails: " + v);
            }

            System.out.println("\n==================================================");
            System.out.println("--- 2. LECTURE POLYMORPHIQUE : STRATÉGIE JOINED ---");
            System.out.println("==================================================");
            List<Employe> employes = em.createQuery("SELECT e FROM Employe e", Employe.class)
                    .getResultList();
            for (Employe e : employes) {
                System.out.println("  -> Type: " + e.getClass().getSimpleName() + " | Détails: " + e);
            }

            System.out.println("\n==================================================");
            System.out.println("--- 3. LECTURE POLYMORPHIQUE : STRATÉGIE TABLE_PER_CLASS ---");
            System.out.println("==================================================");
            List<Produit> produits = em.createQuery("SELECT p FROM Produit p", Produit.class)
                    .getResultList();
            for (Produit p : produits) {
                System.out.println("  -> Type: " + p.getClass().getSimpleName() + " | Détails: " + p);
            }

            // Requête ciblée sur une sous-classe spécifique
            System.out.println("\n==================================================");
            System.out.println("--- 4. REQUÊTE CIBLÉE SUR UNE SOUS-CLASSE (Developpeur) ---");
            System.out.println("==================================================");
            List<Developpeur> devs = em.createQuery(
                    "SELECT d FROM Developpeur d WHERE d.langage LIKE :pattern", Developpeur.class)
                    .setParameter("pattern", "%Java%")
                    .getResultList();
            for (Developpeur d : devs) {
                System.out.println("  -> Développeur spécialisé Java : " + d.getNom() + " (" + d.getLangage() + ")");
            }

        } catch (Exception ex) {
            System.err.println("Une erreur est survenue lors de l'exécution : " + ex.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
            System.out.println("\n=== FERMETURE DE L'ENTITY MANAGER ET DE L'EMF ===");
        }
    }
}
