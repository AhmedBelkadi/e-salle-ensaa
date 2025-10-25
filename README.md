# E-Salle ENSAA - Système de Gestion des Salles

## 🎯 Vue d'Ensemble

Application de gestion des salles pour l'ENSAA développée avec **Java Servlets + JSP + Hibernate + PostgreSQL**.

**Architecture :** Clean Architecture (Domain, Repository, Service, Controller)

**Stratégie :** STRATÉGIE 3 - Par Fonctionnalité Métier (4 modules complets par développeur)

---

## 👥 Équipe de Développement

- **Dev1 (Ahmed)** : Auth & Users, Salle, Filière, Réclamation
- **Dev2 (Votre Ami)** : Administration, Matière, Emploi du Temps, Réservation

---

## 🚀 Installation et Configuration

### Prérequis

- **Java 11** ou supérieur
- **Maven 3.8+**
- **Docker** et **Docker Compose**
- **Git**
- **IDE** (IntelliJ IDEA, Eclipse, ou VS Code)

### 1. Cloner le Projet

```bash
git clone https://github.com/AhmedBelkadi/e-salle-ensaa.git
cd e-salle-ensaa
```

### 2. Configurer Git

```bash
# Vérifier les branches
git branch -a

# Vous devriez voir :
# * develop
#   remotes/origin/main
#   remotes/origin/develop
#   remotes/origin/dev1
#   remotes/origin/dev2

# Si vous êtes Dev2, checkout votre branche
git checkout dev2
```

### 3. Démarrer PostgreSQL

```bash
# Démarrer PostgreSQL avec Docker
docker-compose up -d postgres

# Vérifier que PostgreSQL fonctionne
docker ps
# Doit afficher : esalle-postgres (Up)

# Tester la connexion
docker exec -it esalle-postgres psql -U postgres -d esalle_ensaa
# Dans psql : \dt (pour voir les tables)
# Puis : \q (pour quitter)
```

### 4. Compiler le Projet

```bash
# Compiler avec Maven
mvn clean compile

# Ou compiler + packager
mvn clean package
```

### 5. Lancer l'Application

**Option A : Avec Docker (Recommandé)**

```bash
docker-compose up webapp
```

**Option B : Avec Maven (Développement)**

```bash
mvn tomcat7:run
```

**Option C : Avec votre IDE**

- Configurer Tomcat 9 dans votre IDE
- Déployer le WAR
- Démarrer Tomcat

### 6. Accéder à l'Application

Ouvrir dans le navigateur : **http://localhost:8080**

---

## 📚 Documentation

### Documents Essentiels (dans le dossier `documents/`)

1. **`LIRE_MOI_STRATEGIE_3.txt`** - Vue d'ensemble de la stratégie
2. **`PROJECT_MANAGEMENT_PLAN.tex`** - Plan de gestion complet
3. **`dev1.tex`** - Guide pour Développeur 1 (Ahmed)
4. **`dev2.tex`** - Guide pour Développeur 2 (Votre Ami) ⭐
5. **`cahier-de-charge.tex`** - Spécifications complètes

### Diagrammes (dans le dossier `diagrams/`)

- **Répartition du Travail** : `Répartition du Travail - STRATÉGIE 3.png`
- **Justification** : `Pourquoi STRATÉGIE 3 - Justification.png`
- **Entité-Relation** : `Diagramme Entité-Relation - E-Salle ENSAA.png`
- **Architecture Git** : `Architecture des Branches Git.png`

---

## 🏗️ Structure du Projet

```
my-webapp/
├── src/main/java/com/esalle/
│   ├── core/base/              # BaseRepository, BaseRepositoryImpl
│   ├── features/               # Modules métier (auth, salle, filiere, etc.)
│   │   ├── auth/               # Module Auth & Users (Dev1)
│   │   ├── admin/              # Module Administration (Dev2)
│   │   ├── salle/              # Module Salle (Dev1)
│   │   ├── filiere/            # Module Filière (Dev1)
│   │   ├── matiere/            # Module Matière (Dev2)
│   │   ├── emploi/             # Module Emploi du Temps (Dev2)
│   │   ├── reclamation/        # Module Réclamation (Dev1)
│   │   └── reservation/        # Module Réservation (Dev2)
│   └── shared/                 # Code partagé (config, filters, utils)
├── src/main/resources/
│   ├── db/schema.sql           # Schéma de base de données
│   ├── hibernate.cfg.xml       # Configuration Hibernate
│   └── application.properties  # Configuration application
├── src/main/webapp/
│   ├── WEB-INF/
│   │   ├── views/              # Pages JSP
│   │   └── web.xml             # Configuration Servlet
│   └── assets/                 # CSS, JS, images
├── docker/                     # Configuration Docker
├── documents/                  # Documentation LaTeX
├── diagrams/                   # Diagrammes PlantUML
├── pom.xml                     # Configuration Maven
└── docker-compose.yml          # Configuration Docker Compose
```

---

## 🔄 Workflow Git

### Workflow Quotidien

**Chaque matin :**

```bash
# 1. Checkout votre branche
git checkout dev2  # ou dev1

# 2. Pull les derniers changements
git pull origin develop
git pull origin dev2  # ou dev1

# 3. Commencer à coder !
```

**Pendant le développement :**

```bash
# Commits fréquents (2-3 fois par jour)
git add .
git commit -m "feat(admin): add user approval workflow"
git push origin dev2  # ou dev1
```

### Workflow Hebdomadaire (Chaque Vendredi)

```bash
# 1. Préparer le code
git checkout dev2  # ou dev1
git pull origin develop
# Résoudre conflits si nécessaire
git add .
git commit -m "merge: integrate develop changes"
git push origin dev2  # ou dev1

# 2. Créer Pull Request sur GitHub
# - Aller sur https://github.com/AhmedBelkadi/e-salle-ensaa
# - Créer PR : dev2 → develop (ou dev1 → develop)
# - Titre : [S1] Module Administration - Dev2
# - Demander review à l'autre dev

# 3. Après approbation, merger vers develop
```

### Conventions de Commit

```bash
feat(module): description      # Nouvelle fonctionnalité
fix(module): description       # Correction de bug
refactor(module): description  # Refactoring
docs(module): description      # Documentation
test(module): description      # Tests
```

**Exemples :**

```bash
feat(auth): add user registration
feat(admin): add approval workflow
fix(salle): fix capacity validation
refactor(filiere): improve query performance
```

---

## 📅 Planning - 7 Semaines

| Semaine | Dev1 (Ahmed) | Dev2 (Votre Ami) |
|---------|--------------|------------------|
| **S0** | ✅ Setup Git | ✅ Setup Git |
| **S1** | Auth & Users (5j) | Administration (5j) |
| **S2** | Salle (6j) | Admin (suite) |
| **S3** | Filière (6j) ⚠️ | Attend Filière (3j) + Matière (3j) |
| **S4** | Réclamation (6j) | Matière (5j) |
| **S5** | Aide Dev2 sur Emploi | Emploi du Temps (10j) |
| **S6** | Aide Dev2 sur Réservation | Emploi (suite) + Réservation (6j) |
| **S7** | Consultation + Intégration | Consultation + Intégration |

**⚠️ Point Critique :** Dev2 doit attendre que Dev1 termine **Filière** (fin S3) pour commencer **Matière**.

---

## 🤝 Communication

### Standup Quotidien (10 min)

**Chaque matin à 10h :**

- Qu'est-ce que j'ai fait hier ?
- Qu'est-ce que je vais faire aujourd'hui ?
- Y a-t-il des blocages ?

### Outils de Communication

- **WhatsApp** : Communication rapide
- **GitHub** : Code review, PRs, issues
- **Google Meet/Zoom** : Réunions si besoin

---

## 🛠️ Technologies Utilisées

- **Backend** : Java 11, Servlets 4.0, JSP 2.3
- **ORM** : Hibernate 5.6
- **Base de données** : PostgreSQL 15
- **Build** : Maven 3.8
- **Conteneurisation** : Docker, Docker Compose
- **Serveur** : Tomcat 9
- **Frontend** : HTML5, Tailwind CSS, JavaScript
- **Validation** : Bean Validation (JSR-303)

---

## 📞 Contact

**Dev1 (Ahmed)** : [Votre email/téléphone]

**Dev2** : [Email/téléphone de votre ami]

---

## 🎯 Prochaines Étapes

### Pour Dev1 (Ahmed)

1. ✅ Setup Git (fait)
2. 📖 Lire `documents/dev1.tex`
3. 💻 Commencer **Module Auth & Users** (Semaine 1)

### Pour Dev2 (Votre Ami)

1. 📥 Cloner le projet
2. 📖 Lire `documents/dev2.tex`
3. ⏳ Attendre que Dev1 merge **Auth & Users** (fin S1)
4. 💻 Commencer **Module Administration** (Semaine 1)

---

## ⚠️ Notes Importantes

- **Semaine 0 est terminée** ✅ (Setup Git)
- **Dev1 commence Auth & Users** (Semaine 1)
- **Dev2 commence Administration** (Semaine 1, après merge de Auth & Users)
- **Point de synchronisation critique** : Fin Semaine 3 (Filière doit être mergée)
- **Commits fréquents** : 2-3 fois par jour minimum
- **PR hebdomadaire** : Chaque vendredi
- **Communication quotidienne** : Standup 10 min

---

## 🚀 Bon Développement !

**STRATÉGIE 3 : 4 modules complets chacun = Équilibre parfait !**

