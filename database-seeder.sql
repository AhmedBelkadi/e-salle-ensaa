-- =====================================================
-- E-Salle ENSAA - Database Seeder SQL
-- =====================================================
-- This script inserts sample data for testing
-- 
-- USAGE INSTRUCTIONS:
-- 1. Open pgAdmin and connect to your PostgreSQL database
-- 2. Right-click on your database and select "Query Tool"
-- 3. Copy and paste this entire SQL file into the query editor
-- 4. Execute the script (F5 or click Execute)
-- 
-- IMPORTANT NOTES:
-- - Password for all users: password123 (BCrypt hash)
-- - Make sure your database tables are created first (run migrations/schema)
-- - If you get foreign key errors, check that sequences are reset correctly
-- - User IDs are auto-incremented, so coordinateur assignments use IDs 2-4
-- 
-- DATA INCLUDED:
-- - 1 Admin user
-- - 4 Coordinateurs (assigned to different filieres)
-- - 8 Professeurs
-- - 13 Filieres (GI, GC, DLA - Preparatoire and Ingenieur cycles)
-- - 18 Matieres (subjects) across different filieres
-- - 14 Salles (classrooms: Amphi, Salles de cours, TD, TP)
-- - Sample Emploi du Temps entries for GI Préparatoire 1 and GI DLA1
-- =====================================================

-- Reset sequences (optional, if you want to start from 1)
-- ALTER SEQUENCE users_id_seq RESTART WITH 1;
-- ALTER SEQUENCE filieres_id_seq RESTART WITH 1;
-- ALTER SEQUENCE matieres_id_seq RESTART WITH 1;
-- ALTER SEQUENCE salles_id_seq RESTART WITH 1;
-- ALTER SEQUENCE emploi_du_temps_id_seq RESTART WITH 1;

-- =====================================================
-- 1. USERS (Admin, Coordinateurs, Professeurs)
-- =====================================================

-- Admin User
INSERT INTO users (nom, prenom, email, telephone, password, role, statut, date_inscription, date_approbation, approved_by)
VALUES (
    'Admin',
    'System',
    'admin@ensaa.ma',
    '0612345678',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- password123
    'ADMIN',
    'ACTIF',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);

-- Coordinateurs
INSERT INTO users (nom, prenom, email, telephone, password, role, statut, date_inscription, date_approbation, approved_by)
VALUES 
    ('Alaoui', 'Ahmed', 'ahmed.alaoui@ensaa.ma', '0611111111', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'COORDINATEUR', 'ACTIF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('Bennani', 'Fatima', 'fatima.bennani@ensaa.ma', '0622222222', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'COORDINATEUR', 'ACTIF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('Chraibi', 'Hassan', 'hassan.chraibi@ensaa.ma', '0633333333', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'COORDINATEUR', 'ACTIF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('Idrissi', 'Khadija', 'khadija.idrissi@ensaa.ma', '0644444444', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'COORDINATEUR', 'ACTIF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);

-- Professeurs
INSERT INTO users (nom, prenom, email, telephone, password, role, statut, date_inscription, date_approbation, approved_by)
VALUES 
    ('Amrani', 'Mohammed', 'mohammed.amrani@ensaa.ma', '0655555555', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'PROFESSEUR', 'ACTIF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('Berrada', 'Aicha', 'aicha.berrada@ensaa.ma', '0666666666', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'PROFESSEUR', 'ACTIF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('Dakir', 'Youssef', 'youssef.dakir@ensaa.ma', '0677777777', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'PROFESSEUR', 'ACTIF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('El Fassi', 'Sanae', 'sanae.elfassi@ensaa.ma', '0688888888', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'PROFESSEUR', 'ACTIF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('Fikri', 'Omar', 'omar.fikri@ensaa.ma', '0699999999', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'PROFESSEUR', 'ACTIF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('Ghanmi', 'Nadia', 'nadia.ghanmi@ensaa.ma', '0600000000', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'PROFESSEUR', 'ACTIF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('Hamdi', 'Rachid', 'rachid.hamdi@ensaa.ma', '0611111112', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'PROFESSEUR', 'ACTIF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('Ibrahimi', 'Laila', 'laila.ibrahimi@ensaa.ma', '0611111113', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'PROFESSEUR', 'ACTIF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);

-- =====================================================
-- 2. FILIERES
-- =====================================================

-- Filières Préparatoires
INSERT INTO filieres (nom, cycle, annee, effectif, coordinateur_id, coordinateur_nom, description, created_at, updated_at)
VALUES 
    -- GI (Génie Informatique) - Préparatoire
    ('GI', 'PREPARATOIRE', 1, 60, 2, 'Fatima Bennani', 'Génie Informatique - 1ère année préparatoire', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('GI', 'PREPARATOIRE', 2, 55, 2, 'Fatima Bennani', 'Génie Informatique - 2ème année préparatoire', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- GC (Génie Civil) - Préparatoire
    ('GC', 'PREPARATOIRE', 1, 50, 3, 'Hassan Chraibi', 'Génie Civil - 1ère année préparatoire', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('GC', 'PREPARATOIRE', 2, 48, 3, 'Hassan Chraibi', 'Génie Civil - 2ème année préparatoire', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Filières Ingénieur (DLA)
    -- GI - Ingénieur
    ('GI', 'INGENIEUR', 1, 45, 2, 'Fatima Bennani', 'Génie Informatique - DLA1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('GI', 'INGENIEUR', 2, 42, 2, 'Fatima Bennani', 'Génie Informatique - DLA2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('GI', 'INGENIEUR', 3, 40, 2, 'Fatima Bennani', 'Génie Informatique - DLA3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- GC - Ingénieur
    ('GC', 'INGENIEUR', 1, 38, 3, 'Hassan Chraibi', 'Génie Civil - DLA1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('GC', 'INGENIEUR', 2, 35, 3, 'Hassan Chraibi', 'Génie Civil - DLA2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('GC', 'INGENIEUR', 3, 33, 3, 'Hassan Chraibi', 'Génie Civil - DLA3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Autres filières
    ('DLA', 'INGENIEUR', 1, 30, 4, 'Khadija Idrissi', 'Développement Logiciel et Applications - DLA1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('DLA', 'INGENIEUR', 2, 28, 4, 'Khadija Idrissi', 'Développement Logiciel et Applications - DLA2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('DLA', 'INGENIEUR', 3, 25, 4, 'Khadija Idrissi', 'Développement Logiciel et Applications - DLA3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 3. SALLES
-- =====================================================

INSERT INTO salles (nom, type, capacite, equipements, disponible, date_creation, date_modification)
VALUES 
    -- Salles de Cours
    ('Amphi A', 'COURS', 150, 'Projecteur, Tableau interactif, Sonorisation', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Amphi B', 'COURS', 120, 'Projecteur, Tableau interactif', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Salle 101', 'COURS', 60, 'Projecteur, Tableau blanc', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Salle 102', 'COURS', 60, 'Projecteur, Tableau blanc', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Salle 103', 'COURS', 50, 'Projecteur', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Salles TD
    ('TD 201', 'TD', 30, 'Tableau blanc, Projecteur', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TD 202', 'TD', 30, 'Tableau blanc, Projecteur', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TD 203', 'TD', 25, 'Tableau blanc', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TD 204', 'TD', 25, 'Tableau blanc', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Salles TP
    ('TP Informatique 1', 'TP', 20, '20 ordinateurs, Serveur, Réseau', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TP Informatique 2', 'TP', 20, '20 ordinateurs, Serveur, Réseau', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TP Informatique 3', 'TP', 15, '15 ordinateurs, Serveur', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TP Électronique 1', 'TP', 15, 'Oscilloscopes, Multimètres, Composants', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TP Mécanique 1', 'TP', 12, 'Machines-outils, Équipements de mesure', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 4. MATIERES
-- =====================================================

-- Matières pour GI Préparatoire 1
INSERT INTO matieres (nom, filiere_id, filiere_nom, professeur_id, professeur_nom, heures_cours, heures_td, heures_tp, total_heures, description, created_at, updated_at)
VALUES 
    -- GI Préparatoire 1 (filiere_id = 1)
    ('Mathématiques', 1, 'GI - Préparatoire 1', 5, 'Omar Fikri', 30, 20, 0, 50, 'Mathématiques générales', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Algorithmique', 1, 'GI - Préparatoire 1', 6, 'Nadia Ghanmi', 20, 15, 20, 55, 'Introduction à l''algorithmique et programmation', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Base de données', 1, 'GI - Préparatoire 1', 7, 'Rachid Hamdi', 15, 10, 15, 40, 'Introduction aux bases de données', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Systèmes d''exploitation', 1, 'GI - Préparatoire 1', 8, 'Laila Ibrahimi', 20, 10, 15, 45, 'Fonctionnement des systèmes d''exploitation', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- GI Préparatoire 2 (filiere_id = 2)
    ('Structures de données', 2, 'GI - Préparatoire 2', 6, 'Nadia Ghanmi', 25, 20, 20, 65, 'Structures de données avancées', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Réseaux informatiques', 2, 'GI - Préparatoire 2', 7, 'Rachid Hamdi', 20, 15, 20, 55, 'Architecture des réseaux', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Programmation orientée objet', 2, 'GI - Préparatoire 2', 8, 'Laila Ibrahimi', 25, 20, 25, 70, 'POO avec Java', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- GI Ingénieur DLA1 (filiere_id = 5)
    ('Développement Web', 5, 'GI - DLA1', 5, 'Omar Fikri', 30, 20, 30, 80, 'Développement web full-stack', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Architecture logicielle', 5, 'GI - DLA1', 6, 'Nadia Ghanmi', 25, 15, 20, 60, 'Patterns et architectures', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Base de données avancées', 5, 'GI - DLA1', 7, 'Rachid Hamdi', 20, 15, 20, 55, 'Bases de données relationnelles et NoSQL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- GI Ingénieur DLA2 (filiere_id = 6)
    ('Intelligence Artificielle', 6, 'GI - DLA2', 5, 'Omar Fikri', 30, 20, 25, 75, 'IA et Machine Learning', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Sécurité informatique', 6, 'GI - DLA2', 7, 'Rachid Hamdi', 25, 15, 20, 60, 'Sécurité des systèmes informatiques', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- GC Préparatoire 1 (filiere_id = 3)
    ('Mathématiques', 3, 'GC - Préparatoire 1', 5, 'Omar Fikri', 30, 20, 0, 50, 'Mathématiques pour génie civil', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Résistance des matériaux', 3, 'GC - Préparatoire 1', 9, 'Mohammed Amrani', 25, 20, 15, 60, 'Mécanique des matériaux', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Topographie', 3, 'GC - Préparatoire 1', 10, 'Aicha Berrada', 15, 10, 20, 45, 'Techniques de topographie', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- DLA Ingénieur DLA1 (filiere_id = 11)
    ('Développement Web Frontend', 11, 'DLA - DLA1', 5, 'Omar Fikri', 30, 20, 30, 80, 'React, Vue.js, Angular', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Développement Backend', 11, 'DLA - DLA1', 6, 'Nadia Ghanmi', 30, 20, 30, 80, 'Spring Boot, Node.js', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Mobile Development', 11, 'DLA - DLA1', 8, 'Laila Ibrahimi', 25, 15, 25, 65, 'Android, iOS, React Native', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =====================================================
-- 5. EMPLOI DU TEMPS
-- =====================================================

-- Emploi du temps pour GI Préparatoire 1 - 1ère année (filiere_id = 1, annee = 1)
-- Note: Using matiere IDs directly for clarity (you may need to adjust IDs based on actual insert order)

-- First, let's get the matiere IDs for reference (these will be used in the INSERT statements)
-- Mathématiques for filiere_id = 1 should be matiere ID around 1-4
-- Algorithmique for filiere_id = 1 should be matiere ID around 2
-- Base de données for filiere_id = 1 should be matiere ID around 3
-- Systèmes d'exploitation for filiere_id = 1 should be matiere ID around 4

INSERT INTO emploi_du_temps (filiere_id, filiere_nom, annee, matiere_id, matiere_nom, professeur_id, professeur_nom, salle_id, salle_nom, groupe, jour_semaine, heure_debut, heure_fin, type_seance, created_at, updated_at)
SELECT 
    1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 1, 'Amphi A', NULL, 'LUNDI', '08:30'::time, '10:30'::time, 'COURS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Mathématiques' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 3, 'Salle 101', NULL, 'LUNDI', '10:30'::time, '12:30'::time, 'COURS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Algorithmique' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 9, 'TP Informatique 1', '1', 'LUNDI', '14:30'::time, '16:30'::time, 'TP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Algorithmique' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 9, 'TP Informatique 1', '2', 'LUNDI', '16:30'::time, '18:30'::time, 'TP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Algorithmique' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 3, 'Salle 101', NULL, 'MARDI', '08:30'::time, '10:30'::time, 'COURS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Base de données' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 7, 'TD 201', '1', 'MARDI', '10:30'::time, '12:30'::time, 'TD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Base de données' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 10, 'TP Informatique 2', '1', 'MARDI', '14:30'::time, '16:30'::time, 'TP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Base de données' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 3, 'Salle 101', NULL, 'MARDI', '16:30'::time, '18:30'::time, 'COURS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Systèmes d''exploitation' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 7, 'TD 201', '1', 'MERCREDI', '08:30'::time, '10:30'::time, 'TD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Mathématiques' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 7, 'TD 201', '2', 'MERCREDI', '10:30'::time, '12:30'::time, 'TD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Mathématiques' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 7, 'TD 201', '1', 'MERCREDI', '14:30'::time, '16:30'::time, 'TD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Algorithmique' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 7, 'TD 201', '2', 'MERCREDI', '16:30'::time, '18:30'::time, 'TD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Algorithmique' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 7, 'TD 201', '1', 'JEUDI', '08:30'::time, '10:30'::time, 'TD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Systèmes d''exploitation' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 11, 'TP Informatique 3', '1', 'JEUDI', '10:30'::time, '12:30'::time, 'TP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Systèmes d''exploitation' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 10, 'TP Informatique 2', '2', 'JEUDI', '14:30'::time, '16:30'::time, 'TP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Base de données' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 11, 'TP Informatique 3', '2', 'JEUDI', '16:30'::time, '18:30'::time, 'TP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Systèmes d''exploitation' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 1, 'Amphi A', NULL, 'VENDREDI', '08:30'::time, '10:30'::time, 'COURS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Mathématiques' AND m.filiere_id = 1
UNION ALL
SELECT 1, 'GI - Préparatoire 1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 3, 'Salle 101', NULL, 'VENDREDI', '10:30'::time, '12:30'::time, 'COURS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Base de données' AND m.filiere_id = 1;

-- Emploi du temps pour GI Ingénieur DLA1 - 1ère année (filiere_id = 5, annee = 1)
INSERT INTO emploi_du_temps (filiere_id, filiere_nom, annee, matiere_id, matiere_nom, professeur_id, professeur_nom, salle_id, salle_nom, groupe, jour_semaine, heure_debut, heure_fin, type_seance, created_at, updated_at)
SELECT 
    5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 2, 'Amphi B', NULL, 'LUNDI', '08:30'::time, '10:30'::time, 'COURS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Développement Web' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 9, 'TP Informatique 1', '1', 'LUNDI', '10:30'::time, '12:30'::time, 'TP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Développement Web' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 9, 'TP Informatique 1', '2', 'LUNDI', '14:30'::time, '16:30'::time, 'TP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Développement Web' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 4, 'Salle 102', NULL, 'LUNDI', '16:30'::time, '18:30'::time, 'COURS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Architecture logicielle' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 4, 'Salle 102', NULL, 'MARDI', '08:30'::time, '10:30'::time, 'COURS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Base de données avancées' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 8, 'TD 203', '1', 'MARDI', '10:30'::time, '12:30'::time, 'TD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Base de données avancées' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 8, 'TD 203', '1', 'MARDI', '14:30'::time, '16:30'::time, 'TD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Architecture logicielle' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 8, 'TD 203', '1', 'MARDI', '16:30'::time, '18:30'::time, 'TD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Développement Web' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 10, 'TP Informatique 2', '1', 'MERCREDI', '08:30'::time, '10:30'::time, 'TP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Architecture logicielle' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 10, 'TP Informatique 2', '1', 'MERCREDI', '10:30'::time, '12:30'::time, 'TP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Base de données avancées' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 10, 'TP Informatique 2', '2', 'MERCREDI', '14:30'::time, '16:30'::time, 'TP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Développement Web' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 10, 'TP Informatique 2', '2', 'MERCREDI', '16:30'::time, '18:30'::time, 'TP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Architecture logicielle' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 4, 'Salle 102', NULL, 'JEUDI', '08:30'::time, '10:30'::time, 'COURS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Base de données avancées' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 4, 'Salle 102', NULL, 'JEUDI', '10:30'::time, '12:30'::time, 'COURS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Développement Web' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 8, 'TD 203', '2', 'JEUDI', '14:30'::time, '16:30'::time, 'TD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Architecture logicielle' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 11, 'TP Informatique 3', '1', 'JEUDI', '16:30'::time, '18:30'::time, 'TP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Base de données avancées' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 8, 'TD 203', '2', 'VENDREDI', '08:30'::time, '10:30'::time, 'TD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Développement Web' AND m.filiere_id = 5
UNION ALL
SELECT 5, 'GI - DLA1', 1, m.id, m.nom, m.professeur_id, m.professeur_nom, 2, 'Amphi B', NULL, 'VENDREDI', '10:30'::time, '12:30'::time, 'COURS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM matieres m WHERE m.nom = 'Architecture logicielle' AND m.filiere_id = 5;

-- =====================================================
-- NOTES IMPORTANTES
-- =====================================================
-- 1. Password pour tous les utilisateurs: password123
-- 2. Les coordinateurs sont assignés aux filières:
--    - Fatima Bennani (ID: 2) -> GI (toutes les années)
--    - Hassan Chraibi (ID: 3) -> GC (toutes les années)
--    - Khadija Idrissi (ID: 4) -> DLA (toutes les années)
-- 3. Les matières sont liées aux filières et aux professeurs
-- 4. L'emploi du temps est créé pour:
--    - GI Préparatoire 1 (1ère année)
--    - GI Ingénieur DLA1 (1ère année)
-- 5. Les créneaux horaires standards sont:
--    - 08:30-10:30
--    - 10:30-12:30
--    - 14:30-16:30
--    - 16:30-18:30
--    - 18:30-20:30
-- =====================================================

-- Verification queries (optional)
-- SELECT COUNT(*) FROM users WHERE role = 'ADMIN';
-- SELECT COUNT(*) FROM users WHERE role = 'COORDINATEUR';
-- SELECT COUNT(*) FROM users WHERE role = 'PROFESSEUR';
-- SELECT COUNT(*) FROM filieres;
-- SELECT COUNT(*) FROM matieres;
-- SELECT COUNT(*) FROM salles;
-- SELECT COUNT(*) FROM emploi_du_temps;

