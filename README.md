# Compte-Rendu de TP : Gestion de l'Héritage en JPA / Hibernate

Ce projet illustre et compare les trois principales stratégies de mapping d'héritage objet-relationnel avec JPA et Hibernate :
1. **SINGLE_TABLE** (Une table par hiérarchie)
2. **JOINED** (Table par sous-classe avec jointure)
3. **TABLE_PER_CLASS** (Table par classe concrète avec duplication des colonnes)

---

## 1. Structure du Projet

```text
tp-hibernate-inheritance/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── tp/
        │           └── heritage/
        │               ├── App.java
        │               ├── singletable/
        │               │   ├── Vehicule.java     (@Inheritance(strategy = SINGLE_TABLE))
        │               │   ├── Voiture.java      (@DiscriminatorValue("VOITURE"))
        │               │   └── Moto.java         (@DiscriminatorValue("MOTO"))
        │               ├── joined/
        │               │   ├── Employe.java      (@Inheritance(strategy = JOINED))
        │               │   ├── Developpeur.java
        │               │   └── Manager.java
        │               └── tableperclass/
        │                   ├── Produit.java      (@Inheritance(strategy = TABLE_PER_CLASS))
        │                   ├── Livre.java
        │                   └── Electronique.java
        └── resources/
            └── META-INF/
                └── persistence.xml
```

---

## 2. Configuration Maven et JPA

### `pom.xml`
- `javax.persistence-api` (version 2.2)
- `hibernate-core` (version 5.4.30.Final)
- `h2` (version 1.4.200 en mémoire)
- `exec-maven-plugin` pour exécuter directement `App.main()`

### `persistence.xml`
- Unité de persistance : `tp-inheritance-pu`
- Pilote JDBC : `org.h2.Driver`
- URL : `jdbc:h2:mem:tp_heritage_db;DB_CLOSE_DELAY=-1`
- `hibernate.hbm2ddl.auto` : `create-drop`
- `hibernate.show_sql` et `hibernate.format_sql` activés pour visualiser les requêtes.

---

## 3. Analyse des Stratégies et Requêtes SQL Observées

### Stratégie 1 : `SINGLE_TABLE` (Modèle Véhicules)
* **Entités** : `Vehicule` (abstraite), `Voiture`, `Moto`.
* **Principe** : Toutes les données sont enregistrées dans une unique table `vehicules`. La colonne discriminante `type_vehicule` indique la nature de l'enregistrement (`VOITURE` ou `MOTO`).
* **Requêtes SQL générées** :
  - **Insertion** :
    ```sql
    insert into vehicules (id, marque, prix_achat, climatisation, nombre_portes, type_vehicule) 
    values (null, ?, ?, ?, ?, 'VOITURE');
    ```
  - **Lecture polymorphique (`SELECT v FROM Vehicule v`)** :
    ```sql
    select 
        id, marque, prix_achat, climatisation, nombre_portes, cylindree, sidecar, type_vehicule 
    from vehicules;
    ```
* **Observations** :
  - **Avantages** : Très performant en lecture car aucune jointure n'est nécessaire. Schéma très simple.
  - **Inconvénients** : Présence de valeurs `NULL` pour les colonnes spécifiques (ex: `sidecar` est `NULL` pour une voiture, `nombre_portes` est `NULL` pour une moto). Impossibilité de poser des contraintes `NOT NULL` sur les champs des sous-classes.

---

### Stratégie 2 : `JOINED` (Modèle Employés)
* **Entités** : `Employe` (abstraite), `Developpeur`, `Manager`.
* **Principe** : La table mère `employes` stocke les colonnes communes (`id`, `nom`, `salaire`). Les tables filles `developpeurs` et `managers` contiennent uniquement leurs colonnes spécifiques avec leur clé primaire qui est également une clé étrangère vers `employes(id)`.
* **Requêtes SQL générées** :
  - **Insertion** : Deux `INSERT` successifs sont nécessaires pour un même objet :
    ```sql
    insert into employes (id, nom, salaire) values (null, ?, ?);
    insert into developpeurs (annees_experience, langage_principal, id) values (?, ?, ?);
    ```
  - **Lecture polymorphique (`SELECT e FROM Employe e`)** :
    ```sql
    select 
        employe0_.id, employe0_.nom, employe0_.salaire,
        employe0_1_.annees_experience, employe0_1_.langage_principal,
        employe0_2_.departement, employe0_2_.nombre_equipes,
        case 
            when employe0_1_.id is not null then 1 
            when employe0_2_.id is not null then 2 
            else 0 
        end as clazz_ 
    from employes employe0_ 
    left outer join developpeurs employe0_1_ on employe0_.id = employe0_1_.id 
    left outer join managers employe0_2_ on employe0_.id = employe0_2_.id;
    ```
* **Observations** :
  - **Avantages** : Modèle relationnel normalisé (3NF). Pas de colonnes `NULL` superflues. Possibilité d'utiliser des contraintes d'intégrité strictes (`NOT NULL`).
  - **Inconvénients** : Requêtes plus lourdes impliquant des `LEFT OUTER JOIN` pour reconstituer les objets polymorphiques.

---

### Stratégie 3 : `TABLE_PER_CLASS` (Modèle Produits)
* **Entités** : `Produit` (abstraite), `Livre`, `Electronique`.
* **Principe** : Aucune table pour la classe abstraite `Produit`. Chaque sous-classe concrète a sa propre table indépendante (`livres`, `produits_electroniques`) qui contient l'ensemble des colonnes (les siennes plus celles héritées : `id`, `nom`, `prix`).
* **Requêtes SQL générées** :
  - **Identifiant** : Hibernate utilise un générateur de séquence global (`hibernate_sequence`) pour garantir l'unicité de l'`id` à travers les différentes tables concrètes.
  - **Insertion** :
    ```sql
    call next value for hibernate_sequence;
    insert into livres (nom, prix, auteur, isbn, id) values (?, ?, ?, ?, ?);
    ```
  - **Lecture polymorphique (`SELECT p FROM Produit p`)** :
    ```sql
    select ... from (
        select id, nom, prix, auteur, isbn, null as garantie_mois, null as marque_constructeur, 1 as clazz_ 
        from livres 
        union all 
        select id, nom, prix, null as auteur, null as isbn, garantie_mois, marque_constructeur, 2 as clazz_ 
        from produits_electroniques
    ) produit0_;
    ```
* **Observations** :
  - **Avantages** : Tables concrètes autonomes sans clé étrangère vers une table mère. Requêtes sur une seule sous-classe rapides sans jointure.
  - **Inconvénients** : Redondance des colonnes communes dans chaque table. Les requêtes polymorphiques sont très coûteuses car elles s'appuient sur un `UNION ALL` avec colonnes factices `NULL`. `GenerationType.IDENTITY` est interdit.

---

## 4. Tableau Récapitulatif

| Critère | SINGLE_TABLE | JOINED | TABLE_PER_CLASS |
| :--- | :--- | :--- | :--- |
| **Nombre de tables** | 1 | 1 par classe (mère + filles) | 1 par classe concrète |
| **Normalisation SQL** | Faible (colonnes NULL) | Forte (3ème forme normale) | Moyenne (colonnes dupliquées) |
| **Contraintes NOT NULL** | Non applicables aux sous-classes | Applicables partout | Applicables partout |
| **Performance d'écriture** | Excellente (1 INSERT) | Moyenne (N INSERTs) | Bonne (1 INSERT) |
| **Performance polymorphique** | Optimale (pas de jointure) | Dégradée (jointures multiples) | Dégradée (`UNION ALL`) |
| **Recommandation usuelle** | Hiérarchies simples / perf prioritaire | Modèle complexe / forte cohérence | Rares cas sans polymorphisme |

---

## 5. Comment exécuter le projet

### En ligne de commande :
```bash
mvn clean compile exec:java
```

### Dans un IDE (IntelliJ IDEA / Eclipse / VS Code) :
1. Importer le projet comme un projet Maven existant (`pom.xml`).
2. Exécuter la méthode `main()` dans la classe `com.tp.heritage.App`.
