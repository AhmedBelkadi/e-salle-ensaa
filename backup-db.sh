#!/bin/bash

# Script de sauvegarde automatique de la base de données E-Salle ENSAA
# Usage: ./backup-db.sh

BACKUP_DIR="./backups"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/esalle_backup_$DATE.sql"

# Créer le dossier de backup s'il n'existe pas
mkdir -p $BACKUP_DIR

echo "🔄 Création de la sauvegarde..."

# Exécuter pg_dump via Docker
docker-compose exec -T postgres pg_dump -U postgres esalle_ensaa > $BACKUP_FILE

# Vérifier si la sauvegarde a réussi
if [ $? -eq 0 ]; then
    echo "✅ Sauvegarde créée : $BACKUP_FILE"
    
    # Compresser le fichier
    gzip $BACKUP_FILE
    echo "✅ Sauvegarde compressée : $BACKUP_FILE.gz"
    
    # Garder seulement les 30 derniers backups
    ls -t $BACKUP_DIR/*.gz 2>/dev/null | tail -n +31 | xargs rm -f 2>/dev/null
    echo "✅ Anciens backups nettoyés (gardé 30 derniers)"
    
    # Afficher la taille
    SIZE=$(du -h "$BACKUP_FILE.gz" | cut -f1)
    echo "📦 Taille : $SIZE"
else
    echo "❌ Erreur lors de la sauvegarde"
    exit 1
fi

