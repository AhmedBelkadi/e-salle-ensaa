# 🔒 Guide du Système de Sécurité - E-Salle ENSAA

## 📋 Vue d'ensemble

Le système de sécurité utilise **2 Filters Java** pour centraliser l'authentification et l'autorisation :

1. **`AuthFilter`** : Vérifie que l'utilisateur est connecté
2. **`RoleFilter`** : Vérifie les permissions selon le rôle

---

## 🏗️ Architecture

```
Client Request
     ↓
CharacterEncodingFilter (UTF-8)
     ↓
AuthFilter ← Vérifie la session utilisateur
     ↓
RoleFilter ← Vérifie les permissions par rôle
     ↓
ExceptionHandlerFilter
     ↓
Servlet (ex: SalleServlet, ReclamationServlet)
     ↓
Response
```

---

## 🔐 AuthFilter - Authentification

### Fonctionnalités
- ✅ Vérifie la présence d'une session utilisateur
- ✅ Vérifie que le compte est `APPROUVE`
- ✅ Sauvegarde l'URL demandée pour redirection après login
- ✅ Redirige vers `/auth/login` si non connecté

### URLs Publiques (sans authentification)
- `/auth/login`
- `/auth/register`
- `/auth/logout`
- `/` (page d'accueil)
- `/index.jsp`
- `/css/*`, `/js/*`, `/images/*`
- `/favicon.ico`

### Exemple de flux
```
User accède à: /salle/list
   ↓
AuthFilter vérifie session
   ↓
Pas de session → Redirect: /auth/login
   ↓
Session existe + statut APPROUVE → Continue
```

---

## 🎭 RoleFilter - Contrôle d'accès par rôle

### Règles d'accès

#### 1️⃣ Administration (ADMIN uniquement)
```java
/admin/*
/users/list
/users/approve
/users/reject
/users/delete
```

#### 2️⃣ Filière (ADMIN + COORDINATEUR)
```java
/filieres/new
/filieres/save
/filieres/edit
/filieres/delete

// ADMIN uniquement :
/filieres/assign-coordinateur
/filieres/remove-coordinateur

// Tous les utilisateurs authentifiés :
/filieres/list
/filieres/view
```

#### 3️⃣ Salle (ADMIN pour modification, tous pour consultation)
```java
// ADMIN uniquement :
/salle/new
/salle/save
/salle/edit
/salle/delete
/salle/toggle-disponibilite

// Tous les utilisateurs authentifiés :
/salle/list
/salle/view
```

#### 4️⃣ Réclamation (règles spéciales)
```java
// INTERDIT aux ADMIN (seulement COORDINATEUR/PROFESSEUR/MEMBRE_CLUB) :
/reclamations/new
/reclamations/create

// ADMIN uniquement :
/reclamations/traiter
/reclamations/delete

// Tous les utilisateurs authentifiés :
/reclamations/list
/reclamations/view
/reclamations/mes-reclamations
```

#### 5️⃣ Matière (ADMIN + COORDINATEUR) - Dev2
```java
/matieres/new
/matieres/save
/matieres/edit
/matieres/delete

// Tous pour consultation :
/matieres/list
/matieres/view
```

#### 6️⃣ Emploi du Temps (ADMIN + COORDINATEUR pour modification) - Dev2
```java
/emploi-temps/new
/emploi-temps/save
/emploi-temps/edit
/emploi-temps/delete

// Tous pour consultation :
/emploi-temps/list
/emploi-temps/view
```

#### 7️⃣ Réservation (règles métier) - Dev2
```java
// ADMIN + COORDINATEUR :
/reservations/approve
/reservations/reject
/reservations/cancel

// Tous pour création/consultation :
/reservations/new
/reservations/list
/reservations/mes-reservations
```

---

## 🧪 Tests de Sécurité

### Test 1 : Accès sans authentification
```bash
# Devrait rediriger vers /auth/login
curl -I http://localhost:8080/my-webapp/salle/list
```

### Test 2 : Accès avec mauvais rôle
```bash
# Se connecter en tant que PROFESSEUR
# Essayer d'accéder à /filieres/new
# Devrait rediriger vers / avec message d'erreur
```

### Test 3 : ADMIN ne peut pas créer de réclamation
```bash
# Se connecter en tant qu'ADMIN
# Le bouton "Nouvelle Réclamation" ne doit PAS apparaître
# Accès direct à /reclamations/new devrait être refusé
```

---

## 📝 Messages d'erreur

### Non authentifié
```
"Vous devez être connecté pour accéder à cette page"
```

### Compte non approuvé
```
"Votre compte est en attente d'approbation"
```

### Permissions insuffisantes
```
"Vous n'avez pas les permissions pour accéder à cette page"
```

---

## 🔧 Comment ajouter une nouvelle règle

### Exemple : Restreindre `/rapports/*` aux ADMIN uniquement

1. **Ouvrir `RoleFilter.java`**
2. **Ajouter dans le bloc `static {}`**
   ```java
   ACCESS_RULES.put("/rapports/new", Arrays.asList(UserRole.ADMIN));
   ACCESS_RULES.put("/rapports/generate", Arrays.asList(UserRole.ADMIN));
   ACCESS_RULES.put("/rapports/export", Arrays.asList(UserRole.ADMIN));
   ```
3. **Recompiler et redéployer**

### Exemple : Autoriser plusieurs rôles
```java
// COORDINATEUR et PROFESSEUR peuvent créer des emplois du temps
ACCESS_RULES.put("/emploi-temps/new", 
    Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR, UserRole.PROFESSEUR));
```

---

## ⚠️ Points d'attention

### 1. Ordre des Filters dans `web.xml`
L'ordre est **critique** :
```xml
CharacterEncodingFilter  ← Traite l'UTF-8 en premier
    ↓
AuthFilter              ← Vérifie l'authentification
    ↓
RoleFilter              ← Vérifie les permissions
    ↓
ExceptionHandlerFilter  ← Gère les erreurs
```

### 2. URLs publiques
Toujours vérifier que les ressources statiques (CSS, JS, images) sont dans `PUBLIC_URLS`.

### 3. Redirection après login
L'URL demandée est sauvegardée dans `session.redirectUrl` pour rediriger l'utilisateur après login.

### 4. Comptes non approuvés
Même si un utilisateur est connecté, s'il n'est pas `APPROUVE`, il ne peut accéder à aucune page.

---

## 🚀 Avantages du système

✅ **Centralisé** : Toute la sécurité en 2 fichiers  
✅ **DRY** : Pas de duplication dans les servlets  
✅ **Maintenable** : Facile d'ajouter/modifier des règles  
✅ **Sécurisé** : Impossible d'oublier de protéger une URL  
✅ **Flexible** : Support de règles complexes par rôle  
✅ **Testable** : Logique claire et isolée  

---

## 📊 Matrice des Permissions

| Rôle | Salle | Filière | Réclamation | Admin | Matière | Emploi du Temps | Réservation |
|------|-------|---------|-------------|-------|---------|-----------------|-------------|
| **ADMIN** | Tout | Tout | Traiter uniquement | Tout | Tout | Tout | Approuver/Rejeter |
| **COORDINATEUR** | Consultation | Tout sauf assignation | Créer/Voir | ❌ | Tout | Modifier | Créer/Approuver |
| **PROFESSEUR** | Consultation | Consultation | Créer/Voir | ❌ | Consultation | Consultation | Créer/Voir |
| **MEMBRE_CLUB** | Consultation | Consultation | Créer/Voir | ❌ | Consultation | Consultation | Créer/Voir |

---

## 🔍 Debugging

### Activer les logs de sécurité
Les Filters loggent automatiquement les tentatives d'accès refusées.

### Vérifier les règles
Ouvrir `RoleFilter.java` et consulter `ACCESS_RULES`.

### Tester manuellement
Utiliser différents comptes pour tester chaque scénario.

---

**Dernière mise à jour** : Semaine 4 - Module Réclamation  
**Créé par** : Dev1  
**Statut** : ✅ Production Ready

