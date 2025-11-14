# 🚀 Guide de Déploiement - E-Salle ENSAA

**Guide complet pour déployer l'application E-Salle ENSAA en production**

---

## 📋 Table des Matières

1. [Prérequis](#-prérequis)
2. [Préparation](#-préparation)
3. [Configuration Production](#-configuration-production)
4. [Déploiement](#-déploiement)
5. [Vérification](#-vérification)
6. [Maintenance](#-maintenance)
7. [Sauvegarde](#-sauvegarde)
8. [Monitoring](#-monitoring)
9. [Troubleshooting](#-troubleshooting)

---

## 📦 Prérequis

### Infrastructure

- **Serveur** : Linux (Ubuntu 20.04+ recommandé) ou Windows Server
- **Docker** : 20.10+ et Docker Compose 2.0+
- **Ressources** :
  - CPU : 2+ cores
  - RAM : 4GB+ (8GB recommandé)
  - Disque : 20GB+ d'espace libre

### Logiciels

- Docker & Docker Compose
- Git (pour cloner le projet)
- OpenSSL (pour générer certificats)
- Node.js 16+ (pour builder Tailwind CSS)

### Accès

- Accès SSH au serveur
- Accès root ou sudo
- Ports ouverts : 80, 443, 8080 (ou selon votre configuration)

---

## 🔧 Préparation

### 1. Cloner le Projet

```bash
# Sur le serveur
git clone <repository-url>
cd my-webapp
```

### 2. Générer le Fichier .env

```bash
# Créer .env à partir du template
cp .env.example .env

# Éditer .env avec un éditeur sécurisé
nano .env  # ou vim .env
```

**⚠️ IMPORTANT :** Utiliser des mots de passe **forts** en production !

```env
# Exemple de configuration production
POSTGRES_DB=esalle_ensaa
POSTGRES_USER=esalle_admin
POSTGRES_PASSWORD=<GENERATE_STRONG_PASSWORD_HERE>

DB_URL=jdbc:postgresql://postgres:5432/esalle_ensaa
DB_USERNAME=esalle_admin
DB_PASSWORD=<SAME_STRONG_PASSWORD>

# Production : utiliser validate
HIBERNATE_DDL_AUTO=validate

# Java options pour production
JAVA_OPTS=-Xmx1g -Xms512m -Dspring.profiles.active=production

# Désactiver le seed en production
SEED_DB=false
```

### 3. Builder Tailwind CSS

```bash
# Installer les dépendances
npm install

# Builder le CSS optimisé
npm run build:css

# Vérifier que output.css est généré
ls -lh src/main/webapp/css/output.css
```

### 4. Générer les Certificats SSL

**Option A : Certificats Auto-signés (Développement/Test)**

```bash
# Générer certificats auto-signés
./generate-ssl.sh

# Ou manuellement
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout docker/ssl/nginx-selfsigned.key \
  -out docker/ssl/nginx-selfsigned.crt \
  -subj "/C=MA/ST=Agadir/L=Agadir/O=ENSAA/CN=yourdomain.com"
```

**Option B : Let's Encrypt (Production Recommandé)**

```bash
# Installer Certbot
sudo apt-get update
sudo apt-get install certbot

# Obtenir certificat
sudo certbot certonly --standalone -d yourdomain.com

# Copier les certificats
sudo cp /etc/letsencrypt/live/yourdomain.com/fullchain.pem docker/ssl/nginx.crt
sudo cp /etc/letsencrypt/live/yourdomain.com/privkey.pem docker/ssl/nginx.key
sudo chown $USER:$USER docker/ssl/*.pem
```

**Mettre à jour `docker/nginx/nginx.conf`** avec les noms de fichiers corrects.

---

## ⚙️ Configuration Production

### 1. Configuration Hibernate

**CRITIQUE :** Changer `HIBERNATE_DDL_AUTO` à `validate` en production :

```env
# Dans .env
HIBERNATE_DDL_AUTO=validate
```

Cela empêche Hibernate de modifier automatiquement le schéma de la base de données.

### 2. Configuration Nginx

Mettre à jour `docker/nginx/nginx.conf` :

```nginx
server {
    listen 443 ssl;
    server_name yourdomain.com;  # ← Changer ici
    
    ssl_certificate /etc/nginx/ssl/nginx.crt;  # ← Vérifier le chemin
    ssl_certificate_key /etc/nginx/ssl/nginx.key;  # ← Vérifier le chemin
    
    # ... reste de la configuration
}
```

### 3. Configuration Docker Compose

Pour la production, vous pouvez utiliser `docker-compose.prod.yml` ou modifier `docker-compose.yml` :

```yaml
services:
  webapp:
    environment:
      HIBERNATE_DDL_AUTO: validate  # ← Important !
      SEED_DB: "false"  # ← Désactiver le seed
      JAVA_OPTS: "-Xmx1g -Xms512m"  # ← Ajuster selon ressources
```

### 4. Sécurité

**Mots de passe :**
- Utiliser un générateur de mots de passe forts
- Minimum 20 caractères, mixte, avec symboles
- Ne jamais commiter `.env` (déjà dans `.gitignore`)

**Permissions :**
```bash
# Sécuriser le fichier .env
chmod 600 .env

# Sécuriser les certificats SSL
chmod 600 docker/ssl/*.key
chmod 644 docker/ssl/*.crt
```

---

## 🚀 Déploiement

### Étape 1 : Arrêter les Services Existants

```bash
# Si des conteneurs sont déjà en cours
docker-compose down
```

### Étape 2 : Nettoyer les Anciennes Images (Optionnel)

```bash
# Supprimer les anciennes images
docker-compose down --rmi all

# Nettoyer le système Docker
docker system prune -a
```

### Étape 3 : Build des Images

```bash
# Build sans cache pour garantir la fraîcheur
docker-compose build --no-cache
```

### Étape 4 : Démarrer les Services

```bash
# Démarrer en arrière-plan
docker-compose up -d

# Vérifier le statut
docker-compose ps
```

### Étape 5 : Vérifier les Logs

```bash
# Logs de l'application
docker-compose logs -f webapp

# Logs de la base de données
docker-compose logs -f postgres

# Tous les logs
docker-compose logs -f
```

---

## ✅ Vérification

### 1. Vérifier les Services

```bash
# Statut des conteneurs
docker-compose ps

# Tous doivent être "Up" et "healthy"
```

### 2. Tester l'Application

```bash
# Test HTTP
curl http://localhost:8080/health

# Test HTTPS (si Nginx est activé)
curl -k https://localhost/health

# Réponse attendue :
# {"status":"UP","database":"UP","hibernate":"UP","timestamp":...}
```

### 3. Vérifier la Base de Données

```bash
# Se connecter à PostgreSQL
docker-compose exec postgres psql -U postgres -d esalle_ensaa

# Vérifier les tables
\dt

# Vérifier les utilisateurs
SELECT email, role, statut FROM users;
```

### 4. Tester l'Accès Web

1. Ouvrir le navigateur
2. Accéder à `https://yourdomain.com` (ou `http://localhost:8080`)
3. Tester la connexion avec un compte admin
4. Vérifier les fonctionnalités principales

---

## 🔄 Maintenance

### Mise à Jour de l'Application

```bash
# 1. Sauvegarder la base de données (voir section Sauvegarde)
./backup-db.sh

# 2. Arrêter les services
docker-compose down

# 3. Pull les dernières modifications
git pull origin main  # ou develop

# 4. Rebuild Tailwind CSS (si changements UI)
npm run build:css

# 5. Rebuild les images
docker-compose build --no-cache

# 6. Redémarrer
docker-compose up -d

# 7. Vérifier les logs
docker-compose logs -f webapp
```

### Rotation des Logs

Les logs sont montés dans `./logs/`. Pour éviter qu'ils ne deviennent trop volumineux :

```bash
# Configurer logrotate
sudo nano /etc/logrotate.d/esalle

# Contenu :
/path/to/my-webapp/logs/*.log {
    daily
    rotate 7
    compress
    delaycompress
    notifempty
    create 0644 tomcat tomcat
}
```

### Mise à Jour des Dépendances

```bash
# Mettre à jour les packages npm
npm update

# Rebuilder Tailwind
npm run build:css

# Mettre à jour Maven (modifier pom.xml)
mvn versions:display-dependency-updates
```

---

## 💾 Sauvegarde

### Sauvegarde de la Base de Données

**Script de sauvegarde automatique :**

```bash
#!/bin/bash
# backup-db.sh

BACKUP_DIR="./backups"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/esalle_backup_$DATE.sql"

mkdir -p $BACKUP_DIR

docker-compose exec -T postgres pg_dump -U postgres esalle_ensaa > $BACKUP_FILE

# Compresser
gzip $BACKUP_FILE

# Garder seulement les 30 derniers backups
ls -t $BACKUP_DIR/*.gz | tail -n +31 | xargs rm -f

echo "Backup créé : $BACKUP_FILE.gz"
```

**Cron pour sauvegarde automatique :**

```bash
# Éditer crontab
crontab -e

# Ajouter (sauvegarde quotidienne à 2h du matin)
0 2 * * * /path/to/my-webapp/backup-db.sh
```

### Restauration

```bash
# Restaurer depuis un backup
gunzip < backups/esalle_backup_20251107_020000.sql.gz | \
  docker-compose exec -T postgres psql -U postgres -d esalle_ensaa
```

---

## 📊 Monitoring

### Health Checks

L'application expose un endpoint de santé :

```bash
# Vérifier la santé
curl http://localhost:8080/health

# Réponse JSON :
{
  "status": "UP",
  "timestamp": 1699372800000,
  "service": "E-Salle ENSAA",
  "database": "UP",
  "hibernate": "UP"
}
```

### Logs

```bash
# Logs en temps réel
docker-compose logs -f webapp

# Logs des 100 dernières lignes
docker-compose logs --tail=100 webapp

# Logs avec timestamps
docker-compose logs -f --timestamps webapp
```

### Métriques Docker

```bash
# Utilisation des ressources
docker stats

# Espace disque
docker system df

# Nettoyage
docker system prune
```

---

## 🐛 Troubleshooting

### Problème : Application ne démarre pas

**Symptômes :** Conteneur redémarre en boucle

**Solutions :**
```bash
# Vérifier les logs
docker-compose logs webapp

# Vérifier les variables d'environnement
docker-compose exec webapp env | grep DB_

# Vérifier la connexion à la base
docker-compose exec webapp psql -h postgres -U postgres -d esalle_ensaa
```

### Problème : Erreur 503 Service Unavailable

**Causes possibles :**
- Base de données non accessible
- Hibernate SessionFactory non initialisé
- Erreur de configuration

**Solutions :**
```bash
# Vérifier la santé
curl http://localhost:8080/health

# Vérifier les logs
docker-compose logs webapp | grep -i error

# Vérifier la base de données
docker-compose exec postgres pg_isready -U postgres
```

### Problème : Erreur de connexion à la base de données

**Solutions :**
```bash
# Vérifier que PostgreSQL est démarré
docker-compose ps postgres

# Vérifier les variables d'environnement
cat .env | grep DB_

# Tester la connexion
docker-compose exec postgres psql -U postgres -c "SELECT 1;"
```

### Problème : Certificats SSL expirés

**Solution :**
```bash
# Renouveler avec Let's Encrypt
sudo certbot renew

# Copier les nouveaux certificats
sudo cp /etc/letsencrypt/live/yourdomain.com/fullchain.pem docker/ssl/nginx.crt
sudo cp /etc/letsencrypt/live/yourdomain.com/privkey.pem docker/ssl/nginx.key

# Redémarrer Nginx
docker-compose restart nginx
```

### Problème : Performance dégradée

**Solutions :**
```bash
# Vérifier l'utilisation des ressources
docker stats

# Augmenter les ressources Java
# Dans .env :
JAVA_OPTS=-Xmx2g -Xms1g

# Redémarrer
docker-compose restart webapp
```

---

## 🔐 Checklist de Sécurité Production

Avant de mettre en production, vérifier :

- [ ] Mots de passe forts dans `.env`
- [ ] `HIBERNATE_DDL_AUTO=validate` en production
- [ ] `SEED_DB=false` en production
- [ ] Certificats SSL valides (pas auto-signés)
- [ ] Nginx configuré avec HTTPS
- [ ] Firewall configuré (ports 80, 443 uniquement)
- [ ] `.env` avec permissions 600
- [ ] Certificats SSL avec permissions correctes
- [ ] Backup automatique configuré
- [ ] Logs configurés avec rotation
- [ ] Monitoring en place
- [ ] Health checks fonctionnels

---

## 📞 Support

En cas de problème :

1. Consulter les logs : `docker-compose logs -f`
2. Vérifier la santé : `curl http://localhost:8080/health`
3. Consulter la documentation : `README.md`
4. Vérifier les issues GitHub

---

## 📚 Ressources Additionnelles

- [Documentation Docker](https://docs.docker.com/)
- [Documentation Nginx](https://nginx.org/en/docs/)
- [Let's Encrypt](https://letsencrypt.org/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

---

**Dernière mise à jour** : 7 Novembre 2025

