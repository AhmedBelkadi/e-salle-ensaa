# Script de sauvegarde automatique de la base de données E-Salle ENSAA (PowerShell)
# Usage: .\backup-db.ps1

$BackupDir = ".\backups"
$Date = Get-Date -Format "yyyyMMdd_HHmmss"
$BackupFile = "$BackupDir\esalle_backup_$Date.sql"

# Créer le dossier de backup s'il n'existe pas
if (-not (Test-Path $BackupDir)) {
    New-Item -ItemType Directory -Path $BackupDir | Out-Null
}

Write-Host "🔄 Création de la sauvegarde..." -ForegroundColor Cyan

# Exécuter pg_dump via Docker
docker-compose exec -T postgres pg_dump -U postgres esalle_ensaa | Out-File -FilePath $BackupFile -Encoding UTF8

# Vérifier si la sauvegarde a réussi
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Sauvegarde créée : $BackupFile" -ForegroundColor Green
    
    # Compresser avec PowerShell (nécessite .NET)
    try {
        $bytes = [System.IO.File]::ReadAllBytes($BackupFile)
        $compressed = [System.IO.Compression.GZipStream]::new(
            [System.IO.File]::Create("$BackupFile.gz"),
            [System.IO.Compression.CompressionLevel]::Optimal
        )
        $compressed.Write($bytes, 0, $bytes.Length)
        $compressed.Close()
        Remove-Item $BackupFile
        Write-Host "✅ Sauvegarde compressée : $BackupFile.gz" -ForegroundColor Green
    } catch {
        Write-Host "⚠️  Compression échouée, sauvegarde non compressée" -ForegroundColor Yellow
    }
    
    # Garder seulement les 30 derniers backups
    Get-ChildItem "$BackupDir\*.gz" | Sort-Object LastWriteTime -Descending | Select-Object -Skip 30 | Remove-Item
    Write-Host "✅ Anciens backups nettoyés (gardé 30 derniers)" -ForegroundColor Green
    
    # Afficher la taille
    $Size = (Get-Item "$BackupFile.gz").Length / 1MB
    Write-Host "📦 Taille : $([math]::Round($Size, 2)) MB" -ForegroundColor Cyan
} else {
    Write-Host "❌ Erreur lors de la sauvegarde" -ForegroundColor Red
    exit 1
}

