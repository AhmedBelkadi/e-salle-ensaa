# 🏛️ E-Salle ENSAA - Système de Gestion des Salles

**Application web de gestion des salles pour l'École Nationale Supérieure d'Architecture et d'Art (ENSAA)**

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Hibernate](https://img.shields.io/badge/Hibernate-6.6.0-blue.svg)](https://hibernate.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)

---

## 📋 Table des Matières

1. [Description](#-description)
2. [Fonctionnalités](#-fonctionnalités)
3. [Technologies](#-technologies)
4. [Prérequis](#-prérequis)
5. [Installation](#-installation)
6. [Configuration](#-configuration)
7. [Démarrage](#-démarrage)
8. [Structure du Projet](#-structure-du-projet)
9. [Architecture](#-architecture)
10. [Documentation](#-documentation)
11. [Contribution](#-contribution)

---

## 🎯 Description

E-Salle ENSAA est une application web complète développée en **Java EE** pour la gestion des salles de l'ENSAA. L'application permet aux différents acteurs (Administrateurs, Coordinateurs, Professeurs, Membres de Club) de gérer les salles, les réservations, les réclamations, les matières, les filières et les emplois du temps.

### Caractéristiques Principales

- ✅ **Système d'authentification** avec rôles et workflow d'approbation
- ✅ **Gestion complète des salles** (CRUD, filtres, recherche)
- ✅ **Système de réservation** avec approbation
- ✅ **Gestion des réclamations** avec workflow de traitement
- ✅ **Gestion académique** (Filières, Matières, Emplois du Temps)
- ✅ **Dashboards personnalisés** par rôle
- ✅ **Interface moderne** avec Tailwind CSS
- ✅ **Sécurité renforcée** (filtres, HTTPS, rate limiting)

---

## 🚀 Fonctionnalités

### Modules Implémentés

| Module | Description | Rôles |
|--------|------------|-------|
| **Authentification** | Login, inscription, gestion des utilisateurs | Tous |
| **Salles** | CRUD complet, filtres, recherche | Tous (modification: ADMIN) |
| **Filières** | Gestion des cycles et années | ADMIN, COORDINATEUR |
| **Réclamations** | Création et traitement des réclamations | Tous (traitement: ADMIN) |
| **Matières** | Gestion des matières et charges horaires | ADMIN, COORDINATEUR |
| **Réservations** | Système de réservation avec approbation | Tous |
| **Emploi du Temps** | Planning et horaires | Tous (modification: ADMIN, COORDINATEUR) |
| **Administration** | Approbation des utilisateurs | ADMIN |
| **Consultation** | Dashboards par rôle | Tous |

### Rôles Utilisateurs

- **ADMIN** : Accès complet à toutes les fonctionnalités
- **COORDINATEUR** : Gestion des filières, matières, emplois du temps
- **PROFESSEUR** : Consultation, réservations, réclamations
- **MEMBRE_CLUB** : Réservations et réclamations

---

## 🛠️ Technologies

### Backend
- **Java 17** - Langage de programmation
- **Jakarta EE** - API Java Enterprise (Servlets, JSP, JSTL)
- **Hibernate 6.6.0** - ORM pour la persistance
- **PostgreSQL 15** - Base de données relationnelle
- **Maven 3.9.6** - Gestion des dépendances et build

### Frontend
- **JSP (JavaServer Pages)** - Templates de vues
- **JSTL** - Bibliothèque de tags
- **Tailwind CSS 3.4** - Framework CSS utility-first
- **Font Awesome 6.4** - Icônes
- **JavaScript** - Interactivité côté client

### Infrastructure
- **Docker & Docker Compose** - Containerisation
- **Tomcat 10.1** - Serveur d'application
- **Nginx** - Reverse proxy (optionnel)
- **PostgreSQL** - Base de données

### Outils
- **Git & GitHub** - Contrôle de version
- **BCrypt** - Hashage des mots de passe
- **Jackson** - Traitement JSON
- **SLF4J + Logback** - Logging

---

## 📦 Prérequis

### Pour Docker (Recommandé)

- **Docker** 20.10+
- **Docker Compose** 2.0+
- **Git** (pour cloner le projet)

### Pour Développement Local

- **Java 17** (JDK)
- **Maven 3.8+**
- **PostgreSQL 15+** (ou Docker pour la DB uniquement)
- **Node.js 16+** (pour builder Tailwind CSS)
- **Tomcat 10.1** (ou utiliser Maven Cargo Plugin)

---

## 🔧 Installation

### Option 1 : Docker (Recommandé - Plus Simple)

```bash
# 1. Cloner le projet
git clone <repository-url>
cd my-webapp

# 2. Générer le fichier .env
# Windows
.\generate-env.ps1

# Linux/Mac
chmod +x generate-env.sh
./generate-env.sh

# 3. Builder Tailwind CSS (optionnel mais recommandé)
npm install
npm run build:css

# 4. Démarrer avec Docker Compose
docker-compose up --build -d
```

L'application sera accessible sur : **http://localhost:8080**

### Option 2 : Développement Local

```bash
# 1. Cloner le projet
git clone <repository-url>
cd my-webapp

# 2. Configurer PostgreSQL
# Créer la base de données
createdb esalle_ensaa

# Ou utiliser Docker pour PostgreSQL uniquement
docker-compose up -d postgres

# 3. Configurer Hibernate
# Modifier src/main/resources/hibernate.cfg.xml
# avec vos paramètres de connexion

# 4. Builder Tailwind CSS
npm install
npm run build:css

# 5. Compiler avec Maven
mvn clean package

# 6. Déployer sur Tomcat
# Copier target/e-salle-ensaa.war vers $CATALINA_HOME/webapps/
```

---

## ⚙️ Configuration

### Variables d'Environnement

L'application utilise un fichier `.env` pour la configuration. Créer `.env` à partir de `.env.example` :

```bash
# Copier le template
cp .env.example .env

# Éditer .env et remplir les valeurs
```

**Variables importantes :**

```env
# Database Configuration
DB_NAME=esalle_ensaa
DB_USER=postgres
DB_PASSWORD=your_secure_password_here
DB_PORT=5432

# Application Configuration
APP_PORT=8080
TIMEZONE=Europe/Paris

# Hibernate (update pour dev, validate pour prod)
HIBERNATE_DDL_AUTO=update
```

**Note :** Pour Docker, les variables `POSTGRES_DB`, `POSTGRES_USER`, et `POSTGRES_PASSWORD` sont également utilisées par le conteneur PostgreSQL.

### Configuration Hibernate

Le fichier `src/main/resources/hibernate.cfg.xml` peut être configuré manuellement ou via variables d'environnement (recommandé).

**Modes DDL :**
- `update` : Met à jour le schéma (développement)
- `validate` : Valide le schéma (production)
- `create` : Recrée le schéma à chaque démarrage
- `create-drop` : Recrée et supprime à la fermeture

### Configuration SSL/HTTPS

Pour activer HTTPS en développement :

```bash
# Générer certificats auto-signés
# Windows
.\generate-ssl.ps1

# Linux/Mac
./generate-ssl.sh
```

Pour la production, utiliser des certificats d'une autorité de certification (Let's Encrypt, etc.).

---

## 🚀 Démarrage

### Avec Docker

```bash
# Démarrer tous les services
docker-compose up -d

# Voir les logs
docker-compose logs -f webapp

# Arrêter
docker-compose down

# Rebuild après modifications
docker-compose build --no-cache webapp
docker-compose up -d
```

### Accès à l'Application

- **Application** : http://localhost:8080
- **HTTPS** : https://localhost (si Nginx est activé)
- **Base de données** : localhost:5432

### Comptes de Test

L'application crée automatiquement des comptes de test au démarrage (via `DataInitializer`) :

- **Admin** : admin@ensaa.ma / password
- **Coordinateur** : coord@ensaa.ma / password
- **Professeur** : prof@ensaa.ma / password
- **Membre Club** : club@ensaa.ma / password

---

## 📁 Structure du Projet

```
my-webapp/
├── src/main/
│   ├── java/com/esalle/
│   │   ├── controller/          # Servlets (contrôleurs)
│   │   ├── service/             # Services métier
│   │   ├── repository/          # Accès aux données
│   │   ├── entity/              # Entités JPA
│   │   ├── filter/              # Filtres (Auth, Role, etc.)
│   │   ├── util/                # Utilitaires
│   │   └── exception/           # Exceptions personnalisées
│   ├── resources/
│   │   ├── db/schema.sql        # Schéma SQL initial
│   │   ├── hibernate.cfg.xml    # Configuration Hibernate
│   │   └── META-INF/
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── views/           # Pages JSP
│       │   └── web.xml          # Configuration web
│       ├── css/                  # CSS (Tailwind)
│       └── index.jsp            # Page d'accueil
├── docker/                      # Configuration Docker
│   ├── nginx/                   # Configuration Nginx
│   ├── tomcat/                  # Configuration Tomcat
│   └── healthcheck.sh           # Script de santé
├── docker-compose.yml           # Orchestration Docker
├── Dockerfile                   # Image Docker
├── pom.xml                      # Configuration Maven
├── package.json                 # Configuration npm (Tailwind)
├── tailwind.config.js           # Configuration Tailwind
└── README.md                    # Ce fichier
```

---

## 🏗️ Architecture

### Architecture en Couches

```
┌─────────────────────────────────────┐
│         View Layer (JSP)           │
│  - Pages JSP avec JSTL              │
│  - Composants réutilisables          │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│      Controller Layer (Servlets)     │
│  - Gestion des requêtes HTTP         │
│  - Validation des paramètres         │
│  - Redirection vers les vues        │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│        Service Layer                 │
│  - Logique métier                   │
│  - Validation des règles business   │
│  - Orchestration des opérations     │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│      Repository Layer                │
│  - Accès aux données                │
│  - Requêtes Hibernate               │
│  - Gestion des transactions         │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│      Entity Layer (JPA)              │
│  - Entités métier                   │
│  - Mapping objet-relationnel        │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│         Database (PostgreSQL)        │
└─────────────────────────────────────┘
```

### Pattern de Design

- **MVC (Model-View-Controller)** : Séparation des responsabilités
- **Repository Pattern** : Abstraction de l'accès aux données
- **Service Layer** : Encapsulation de la logique métier
- **Filter Chain** : Filtres pour sécurité et traitement

---

## 📚 Documentation

### Documentation Disponible

- **`docker/nginx/README.md`** - Documentation Nginx et configuration SSL
- **`README.md`** - Ce fichier (documentation principale)
- **Diagrammes PlantUML** - Disponibles dans le dossier `diagrams/` :
  - Diagrammes de cas d'utilisation (Use Case)
  - Diagramme de classes UML
  - Diagrammes d'architecture

---

## 🔒 Sécurité

### Mesures Implémentées

- ✅ **Authentification** : Hashage BCrypt des mots de passe
- ✅ **Autorisation** : Contrôle d'accès basé sur les rôles (RBAC)
- ✅ **Filtres de sécurité** : `AuthFilter`, `RoleFilter`
- ✅ **HTTPS** : Configuration SSL/TLS avec Nginx
- ✅ **Rate Limiting** : Protection contre brute force (Nginx)
- ✅ **Sessions sécurisées** : Cookies http-only
- ✅ **Validation** : Validation côté serveur et client

### Configuration de Production

Pour la production, suivre ces recommandations :
- Configuration de variables d'environnement sécurisées dans `.env`
- Certificats SSL/TLS de production (voir `docker/nginx/ssl/README.md`)
- Configuration Hibernate en mode `validate`
- Backup automatique de la base de données
- Utiliser `docker-compose.prod.yml` pour la configuration de production

---

## 🧪 Tests

### Comptes de Test

L'application crée automatiquement des comptes de test au démarrage :

| Rôle | Email | Mot de passe |
|------|-------|--------------|
| Admin | admin@ensaa.ma | password |
| Coordinateur | coord@ensaa.ma | password |
| Professeur | prof@ensaa.ma | password |
| Membre Club | club@ensaa.ma | password |

### Tests Manuels

1. **Authentification** : Tester login avec différents rôles
2. **Autorisation** : Vérifier l'accès aux pages selon les rôles
3. **CRUD** : Tester création, lecture, modification, suppression
4. **Filtres** : Tester les filtres de recherche
5. **Pagination** : Tester la navigation entre pages

---

## 🐛 Dépannage

### Problème : Application ne démarre pas

```bash
# Vérifier les logs
docker-compose logs webapp

# Vérifier la base de données
docker-compose logs postgres

# Vérifier les variables d'environnement
docker-compose exec webapp env | grep DB_
```

### Problème : Erreur 404

- Vérifier que les servlets sont bien mappés dans `web.xml`
- Vérifier les logs Tomcat pour les erreurs de déploiement
- Rebuild l'image Docker : `docker-compose build --no-cache webapp`

### Problème : CSS ne s'affiche pas

```bash
# Builder Tailwind CSS
npm install
npm run build:css

# Vérifier que output.css existe
ls src/main/webapp/css/output.css

# Rebuild Docker
docker-compose build webapp
docker-compose up -d
```

### Problème : Erreur de connexion à la base de données

- Vérifier que PostgreSQL est démarré : `docker-compose ps`
- Vérifier les variables d'environnement dans `.env`
- Vérifier les logs : `docker-compose logs postgres`

---

## 🤝 Contribution

### Workflow Git

1. **Créer une branche** : `git checkout -b feature/ma-fonctionnalite`
2. **Faire des commits** : `git commit -m "feat: description"`
3. **Pousser** : `git push origin feature/ma-fonctionnalite`
4. **Créer une Pull Request** sur GitHub

### Conventions de Commit

- `feat:` - Nouvelle fonctionnalité
- `fix:` - Correction de bug
- `docs:` - Documentation
- `refactor:` - Refactoring
- `test:` - Tests
- `style:` - Formatage
- `chore:` - Tâches de maintenance

### Code Style

- Suivre les conventions Java standard
- Commenter le code complexe
- Utiliser des noms de variables explicites
- Respecter l'architecture en couches

---

## 📞 Support

Pour toute question ou problème :

1. Consulter ce README et la documentation dans `docker/nginx/README.md`
2. Vérifier les issues sur GitHub
3. Consulter les diagrammes PlantUML dans le dossier `diagrams/`
4. Contacter l'équipe de développement

---

## 📄 Licence

Ce projet est développé dans le cadre d'un projet académique pour l'ENSAA.

---

## 🎓 Équipe de Développement

- **Ahmed Belkadi** - Développeur 1
- **Maroune Haidouch** - Développeur 2

**Encadrant** : Madame Zahra Benider  
**Établissement** : ENSA Agadir  
**Année académique** : 2025-2026

**Dernière mise à jour** : Novembre 2025
