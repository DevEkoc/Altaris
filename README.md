# 🙏 Altaris API

![Spring](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Apache_Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-a571a5?style=for-the-badge&logo=hibernate&logoColor=white)

Ce dépôt contient le code source de **Altaris**, une application backend développée en **Java 21** avec **Spring Boot**. Elle est conçue pour la **gestion centralisée des servants d'autel (acolytes) et des structures ecclésiastiques au Cameroun**.
Le projet expose une **API REST** orientée métier, permettant une gestion structurée et fiable des informations, de la province ecclésiastique jusqu'à la paroisse.

---

## 🎯 Objectifs du projet
- Centraliser les données relatives aux servants d'autel dans une base de données unique.
- Modéliser et gérer la hiérarchie des entités ecclésiastiques du Cameroun (Provinces, Diocèses, Zones, Paroisses).
- Offrir une API REST performante et sécurisée pour l'administration et la consultation de ces données.
- Garantir la cohérence et l'intégrité des informations à travers les différents niveaux hiérarchiques.
- Mettre en place une architecture backend maintenable, évolutive et prête pour de futures fonctionnalités.

---

## 🛠️ Stack technique

- **Langage** : Java 21+
- **Framework** : Spring Boot 4
- **Persistance** : Spring Data JPA / Hibernate
- **Base de données** : MySQL
- **Sécurité** : Spring Security (en cours d'implémentation)
- **Build** : Maven
- **Documentation API** : OpenAPI (prévu)
- **Outils** : Git, GitHub

---

## ⚙️ Fonctionnalités principales

### ⛪ Gestion de la hiérarchie ecclésiastique
- CRUD complet pour les **Provinces**.
- CRUD complet pour les **Diocèses**, rattachés à une province.
- CRUD complet pour les **Zones**, rattachées à un diocèse.
- CRUD complet pour les **Paroisses**, rattachées à une zone.

### 🧑 Gestion des servants d'autel
- Création, consultation, mise à jour et suppression (CRUD) des profils de servants.
- Rattachement d'un servant à une paroisse spécifique.
- Gestion des informations personnelles et du parcours du servant.

### 🔍 Recherche & Spécifications
- Utilisation de **Spring Data JPA Specifications** pour permettre des recherches et des filtrages dynamiques sur les entités.

### 🖼️ Gestion des médias
- Service de gestion pour l'upload et la suppression d'images associées aux entités (ex: photo d'une province).

---

## 🏗️ Architecture

Le projet adopte une **architecture en couches**, claire et découplée, pour assurer une séparation nette des responsabilités :

- **Controller** : Exposition des endpoints REST et gestion des requêtes/réponses HTTP.
- **Service** : Implémentation de toute la logique métier et orchestration des opérations.
- **Repository** : Interface pour l'accès aux données via Spring Data JPA.
- **Entity** : Modélisation des objets du domaine (tables de la base de données).
- **DTO (Data Transfer Object)** : Objets dédiés au transport des données entre les couches, notamment pour l'API.
- **Mapper** : Conversion manuelle et statique entre les `Entity` et les `DTO`.

Un point clé du modèle de données est l'utilisation d'une classe de base abstraite `EcclesiasticalUnit` avec une stratégie d'héritage `@Inheritance(strategy = InheritanceType.JOINED)`, permettant de mutualiser les attributs communs et de créer une hiérarchie claire entre `Province`, `Diocese`, `Zone` et `Parish`.

### 🔁 Schéma de fonctionnement
`Client ➡️ Controller ➡️ Service ➡️ Repository ➡️ Base de données`

---
## 📘 Documentation API
Une documentation interactive via Swagger/OpenAPI est prévue. Une fois active, elle sera accessible sur `http://localhost:8080/swagger-ui.html` et permettra de :
- Visualiser et tester l'ensemble des endpoints.
- Comprendre les modèles de données attendus en entrée et en sortie.
---
## ▶️ Lancer le projet en local

1.  **Cloner le dépôt :**
    ```bash 
    git clone <votre-repo-url>
    cd Altaris
    ```
2.  **Configurer la base de données dans `src/main/resources/application.properties` :**
    Assurez-vous d'avoir une base de données MySQL nommée `altaris_db` (ou de votre choix) et mettez à jour les identifiants.
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/altaris_db?createDatabaseIfNotExist=true
    spring.datasource.username=votre_user
    spring.datasource.password=votre_mot_de_passe
    ```
3.  **Lancez l'application :**
    Utilisez Maven pour compiler le projet et démarrer le serveur.
    ```bash
    mvn clean install
    mvn spring-boot:run
    ``` 
L'API devrait maintenant être accessible à l'adresse `http://localhost:8080`.

---
## 🧪 Tests
Le projet inclut des tests unitaires pour les couches Service et Controller, en utilisant :
- **JUnit 5** : Framework de test.
- **Mockito** : Pour la création de mocks et l'isolation des dépendances.
- **Spring Boot Test** : Pour les tests d'intégration et le chargement du contexte de l'application.

---

## 🚀 Améliorations prévues
- Implémentation complète de la sécurité avec **Spring Security et JWT** pour l'authentification et la gestion des rôles (Admin, Utilisateur standard).
- Intégration de **Swagger/OpenAPI** pour la documentation automatique de l'API.
- Ajout de tests d’intégration pour couvrir les flux de bout en bout.
- Mise en place d'un pipeline **CI/CD** avec des outils comme GitHub Actions.
- Conteneurisation de l'application avec **Docker**.

---

## 🧑‍💻 Auteur
**Christophe Cédric EKOBENA OMGBA**

---
## 📄 Licence

Copyright © 2025 **Christophe Cédric EKOBENA OMGBA**. Tous droits réservés.