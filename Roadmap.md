# Projet ALTARIS
Fin du refractoring des mappers, DTO, services, et contrôleurs de façon à rendre les réponses JSON plus essentielles.

Voici la structure des DTO que j'ai choisi d'adopter :
- ``ChaplainListDTO`` : toutes les infos 
- ``ProvinceListDTO`` : toutes les infos 
- ``DioceseListDTO`` : toutes les infos + id et nom Province parent 
- ``ZoneListDTO`` : toutes les infos + id et nom Diocese parent 
- ``ParishListDTO`` : toutes les infos + id et nom Zone parent
- ``<Unit>DetailsDTO`` : toutes les infos de l'unité + officeList + chaplainList 
- ``ServantListDTO`` : toutes les infos + id et nom Parish 
- ``OfficeListDTO`` : toutes les infos + List<AssignmentListDTO> (sera logiquement vide après la création)
- ``AssignmentListDTO`` : toutes les infos + id et nom Servant

J'ai également réécrit de 0 le script SQL de la BD (je laissais Hibernate le faire), et choisi d'avoir une BD totalement en anglais.

J'ai aussi exclu le dossier des tests du projet, question d'éviter les erreurs de compilation.


Ce qu'il reste à faire :
- Tests avec Bruno de tous les endpoints 
- Refractoring des tests unitaires et d'intégration
- Revérification de la documentation Springdoc
- Mise en place d'un pipline CI/CD
- Déploiement sur un serveur gratuit
