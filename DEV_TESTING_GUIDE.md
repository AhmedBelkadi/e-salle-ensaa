# 🧪 Guide de Test - E-Salle ENSAA

## 📦 Comptes de Test Pré-configurés

L'application initialise automatiquement **4 comptes de test** au premier démarrage :

| Rôle | Email | Mot de passe | Nom | Téléphone |
|------|-------|--------------|-----|-----------|
| **👑 ADMIN** | `admin@ensaa.ma` | `Test@2024` | Admin Système | +212600000001 |
| **📚 COORDINATEUR** | `coordinateur@ensaa.ma` | `Test@2024` | Hassan Alami | +212600000002 |
| **👨‍🏫 PROFESSEUR** | `professeur@ensaa.ma` | `Test@2024` | Fatima Benani | +212600000003 |
| **🎭 MEMBRE_CLUB** | `club@ensaa.ma` | `Test@2024` | Ahmed Tazi | +212600000004 |

---

## 🚀 Démarrage Rapide

### 1️⃣ **Lancer l'application**
```bash
docker-compose up --build -d
```

### 2️⃣ **Accéder à l'application**
Ouvrir : `http://localhost:8080`

### 3️⃣ **Se connecter avec un compte de test**
Sur la page de login, cliquer sur un des **4 badges de compte** pour remplir automatiquement les champs !

---

## 🎯 Données de Démonstration

### 🚪 **4 Salles**
1. **Amphithéâtre A** (COURS, 200 places) - Disponible ✅
2. **Laboratoire Informatique 1** (TP, 30 places) - Disponible ✅
3. **Salle TD B1** (TD, 40 places) - Disponible ✅
4. **Laboratoire Physique** (TP, 25 places) - Non disponible ❌

### 📚 **5 Filières**
#### Cycle Préparatoire
1. **MPSI** - 1ère année (45 étudiants)
2. **MPSI** - 2ème année (42 étudiants)

#### Cycle Ingénieur
3. **Génie Informatique** - DLA1 (60 étudiants)
4. **Génie Informatique** - DLA2 (58 étudiants)
5. **Génie Informatique** - DLA3 (55 étudiants)

---

## 🧪 Scénarios de Test

### **Test 1 : Permissions ADMIN**
1. Se connecter avec `admin@ensaa.ma`
2. Vérifier :
   - ✅ Boutons "Nouvelle Salle" / "Modifier" / "Supprimer" visibles
   - ✅ Boutons "Nouvelle Filière" / "Modifier" / "Supprimer" visibles
   - ✅ Menu "Utilisateurs" visible
   - ✅ Formulaire de traitement des réclamations visible
   - ❌ **Pas de bouton** "Nouvelle Réclamation" (Admin ne peut pas créer)

### **Test 2 : Permissions COORDINATEUR**
1. Se connecter avec `coordinateur@ensaa.ma`
2. Vérifier :
   - ✅ Boutons Filières : créer/modifier/supprimer ✅
   - ❌ Boutons Salles : **PAS** de créer/modifier/supprimer
   - ✅ Peut créer des réclamations
   - ❌ **Pas de menu** "Utilisateurs"
   - ❌ **Pas de formulaire** de traitement des réclamations

### **Test 3 : Permissions PROFESSEUR**
1. Se connecter avec `professeur@ensaa.ma`
2. Vérifier :
   - ❌ **Aucun bouton** de création/modification/suppression
   - ✅ Consultation de Salles et Réclamations seulement
   - ✅ Peut créer des réclamations
   - ❌ **Pas de menu** Filières

### **Test 4 : Permissions MEMBRE_CLUB**
1. Se connecter avec `club@ensaa.ma`
2. Vérifier :
   - ❌ **Aucun bouton** de création/modification/suppression
   - ✅ Consultation de Salles et Réclamations seulement
   - ✅ Peut créer des réclamations
   - ❌ **Pas de menu** Filières

---

## 🔄 Réinitialiser les Données

### Méthode 1 : Supprimer la base de données
```bash
docker-compose down -v
docker-compose up --build -d
```

### Méthode 2 : Supprimer manuellement via PostgreSQL
```bash
docker exec -it esalle-postgres psql -U esalle_user -d esalle_db

DELETE FROM reclamations;
DELETE FROM filieres;
DELETE FROM salles;
DELETE FROM users;

\q
```

Les données seront recréées automatiquement au prochain démarrage.

---

## 📊 Matrice des Permissions

| Action | ADMIN | COORDINATEUR | PROFESSEUR | MEMBRE_CLUB |
|--------|-------|--------------|-----------|-------------|
| **Salles - CRUD** | ✅ | ❌ | ❌ | ❌ |
| **Salles - Consultation** | ✅ | ✅ | ✅ | ✅ |
| **Filières - CRUD** | ✅ | ✅ | ❌ | ❌ |
| **Filières - Consultation** | ✅ | ✅ | ❌ | ❌ |
| **Réclamations - Créer** | ❌ | ✅ | ✅ | ✅ |
| **Réclamations - Traiter** | ✅ | ❌ | ❌ | ❌ |
| **Réclamations - Consulter** | ✅ | ✅ | ✅ | ✅ |
| **Utilisateurs - Gérer** | ✅ | ❌ | ❌ | ❌ |

---

## 🐛 Debugging

### Vérifier les logs d'initialisation
```bash
docker logs esalle-webapp | grep "Initialisation des données"
```

Vous devriez voir :
```
✅ ========================================
✅ DONNÉES DE TEST CRÉÉES AVEC SUCCÈS !
✅ ========================================
📧 Comptes de test (mot de passe: Test@2024):
   1️⃣  admin@ensaa.ma (ADMIN)
   2️⃣  coordinateur@ensaa.ma (COORDINATEUR)
   3️⃣  professeur@ensaa.ma (PROFESSEUR)
   4️⃣  club@ensaa.ma (MEMBRE_CLUB)
✅ ========================================
```

### Vérifier la base de données
```bash
docker exec -it esalle-postgres psql -U esalle_user -d esalle_db

-- Compter les utilisateurs
SELECT COUNT(*) FROM users;

-- Voir tous les utilisateurs
SELECT email, role, statut FROM users;

-- Compter les salles
SELECT COUNT(*) FROM salles;

-- Compter les filières
SELECT COUNT(*) FROM filieres;

\q
```

---

## 💡 Conseils de Développement

### 1️⃣ **Tester Rapidement les Rôles**
Au lieu de se déconnecter/reconnecter, utilisez plusieurs navigateurs ou onglets incognito :
- **Chrome Normal** : ADMIN
- **Chrome Incognito** : COORDINATEUR
- **Firefox** : PROFESSEUR
- **Edge** : MEMBRE_CLUB

### 2️⃣ **Modifier le Mot de Passe**
Dans `DataInitializer.java`, changez :
```java
private static final String DEFAULT_PASSWORD = "VotreNouveauMDP";
```

### 3️⃣ **Désactiver l'Auto-initialisation en Production**
Ajoutez une condition dans `contextInitialized()` :
```java
String env = System.getenv("APP_ENV");
if (!"production".equals(env)) {
    // Initialiser les données de test
}
```

---

## 📝 Notes

- Les données sont créées **UNIQUEMENT** au premier démarrage
- Si la base de données contient déjà des utilisateurs, l'initialisation est **ignorée**
- Les mots de passe sont **hashés avec BCrypt** (12 rounds)
- Tous les comptes ont le statut `ACTIF` par défaut

---

**🎉 Bon développement !**

