package com.esalle.util;

import com.esalle.entity.User;
import com.esalle.entity.User.UserRole;
import com.esalle.entity.User.UserStatus;
import com.esalle.entity.Salle;
import com.esalle.entity.Salle.TypeSalle;
import com.esalle.entity.Filiere;
import com.esalle.entity.Filiere.Cycle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;
import com.esalle.entity.Reservation;
import com.esalle.entity.Matiere;
import com.esalle.entity.EmploiDuTemps;
import com.esalle.entity.Reclamation;
import com.esalle.entity.AuditLog;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.logging.Logger;

/**
 * Initialise les données de test au démarrage de l'application
 * Crée 4 utilisateurs (un par rôle) + données de démonstration
 */
@WebListener
public class DataInitializer implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());
    private static final String DEFAULT_PASSWORD = "ahmed2003";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("🚀 Initialisation des données de test...");

        // Control seeding via environment variable SEED_DB
        // Default: seeding is DISABLED unless SEED_DB is set to true/1/yes
        String seedEnv = System.getenv("SEED_DB");
    // If SEED_DB is not set (null or empty), default to ENABLED so the DB will be seeded.
    // If you want to disable seeding, set SEED_DB to false/0/no in the service env.
    boolean seedEnabled = seedEnv == null || seedEnv.isEmpty() || (
        "true".equalsIgnoreCase(seedEnv) ||
        "1".equals(seedEnv) ||
        "yes".equalsIgnoreCase(seedEnv)
    );

    if (!seedEnabled) {
        LOGGER.info("⚪ SEED_DB not enabled (SEED_DB='" + seedEnv + "'). Skipping data seeding on startup.");
        return;
    }
        
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction transaction = null;

        try {
            LOGGER.info("🔧 Tentative d'obtention de SessionFactory...");
            sessionFactory = HibernateUtil.getSessionFactory();
            LOGGER.info("✅ SessionFactory obtenue avec succès");
            
            if (sessionFactory == null) {
                LOGGER.warning("⚠️ SessionFactory n'est pas disponible. L'initialisation des données sera ignorée.");
                return;
            }
            
            LOGGER.info("🔧 Ouverture de session...");
            session = sessionFactory.openSession();
            LOGGER.info("✅ Session ouverte avec succès");
            
            LOGGER.info("🔧 Début de transaction...");
            transaction = session.beginTransaction();
            LOGGER.info("✅ Transaction démarrée");

            // Count existing rows for each key table and seed only missing tables
            Long usersCount = session.createQuery("SELECT COUNT(u) FROM com.esalle.entity.User u", Long.class).uniqueResult();
            Long sallesCount = session.createQuery("SELECT COUNT(s) FROM com.esalle.entity.Salle s", Long.class).uniqueResult();
            Long filieresCount = session.createQuery("SELECT COUNT(f) FROM com.esalle.entity.Filiere f", Long.class).uniqueResult();
            Long matieresCount = session.createQuery("SELECT COUNT(m) FROM com.esalle.entity.Matiere m", Long.class).uniqueResult();
            Long emploisCount = session.createQuery("SELECT COUNT(e) FROM com.esalle.entity.EmploiDuTemps e", Long.class).uniqueResult();
            Long reservationsCount = session.createQuery("SELECT COUNT(r) FROM com.esalle.entity.Reservation r", Long.class).uniqueResult();
            Long reclamationsCount = session.createQuery("SELECT COUNT(r) FROM com.esalle.entity.Reclamation r", Long.class).uniqueResult();
            Long auditCount = session.createQuery("SELECT COUNT(a) FROM com.esalle.entity.AuditLog a", Long.class).uniqueResult();

            LOGGER.info("ℹ️ Rows present => users:" + usersCount + " salles:" + sallesCount + " filieres:" + filieresCount + " matieres:" + matieresCount + " emplois:" + emploisCount + " reservations:" + reservationsCount + " reclamations:" + reclamationsCount + " audit:" + auditCount);

            // Prepare holder variables (may be fetched or created)
            User admin = null, coordinateur = null, professeur = null, membreClub = null;
            Salle amphiA = null, labInfo = null, salleTD = null, labPhysique = null;
            Filiere prepa1 = null, prepa2 = null, ing1 = null, ing2 = null, ing3 = null;

            // --- USERS ---
            if (usersCount == 0) {
                LOGGER.info("📝 Création des utilisateurs de test...");
                admin = createUser("admin@ensaa.ma","Admin","Système","+212600000001",UserRole.ADMIN,UserStatus.ACTIF);
                session.persist(admin);
                coordinateur = createUser("coordinateur@ensaa.ma","Hassan","Alami","+212600000002",UserRole.COORDINATEUR,UserStatus.ACTIF);
                session.persist(coordinateur);
                professeur = createUser("professeur@ensaa.ma","Fatima","Benani","+212600000003",UserRole.PROFESSEUR,UserStatus.ACTIF);
                session.persist(professeur);
                membreClub = createUser("club@ensaa.ma","Ahmed","Tazi","+212600000004",UserRole.MEMBRE_CLUB,UserStatus.ACTIF);
                session.persist(membreClub);
                LOGGER.info("✅ Utilisateurs créés");
            } else {
                // fetch by emails (best-effort)
                java.util.List<User> list;
                list = session.createQuery("FROM com.esalle.entity.User u WHERE u.email = :e", User.class).setParameter("e", "admin@ensaa.ma").setMaxResults(1).getResultList();
                admin = list.isEmpty() ? null : list.get(0);
                list = session.createQuery("FROM com.esalle.entity.User u WHERE u.email = :e", User.class).setParameter("e", "coordinateur@ensaa.ma").setMaxResults(1).getResultList();
                coordinateur = list.isEmpty() ? null : list.get(0);
                list = session.createQuery("FROM com.esalle.entity.User u WHERE u.email = :e", User.class).setParameter("e", "professeur@ensaa.ma").setMaxResults(1).getResultList();
                professeur = list.isEmpty() ? null : list.get(0);
                list = session.createQuery("FROM com.esalle.entity.User u WHERE u.email = :e", User.class).setParameter("e", "club@ensaa.ma").setMaxResults(1).getResultList();
                membreClub = list.isEmpty() ? null : list.get(0);
                LOGGER.info("ℹ️ Utilisateurs existants récupérés");
            }

            // --- SALLES ---
            if (sallesCount == 0) {
                LOGGER.info("📝 Création des salles de démonstration...");
                amphiA = createSalle("Amphithéâtre A", TypeSalle.COURS, 200, "Projecteur HD\nSystème audio\nTableau interactif\nClimatisation", true);
                session.persist(amphiA);
                labInfo = createSalle("Laboratoire Informatique 1", TypeSalle.TP, 30, "30 PC avec double écran\nServeur local\nTableau blanc\nWiFi haut débit", true);
                session.persist(labInfo);
                salleTD = createSalle("Salle TD B1", TypeSalle.TD, 40, "Projecteur\nTableau blanc\nPrises électriques\nChaises mobiles", true);
                session.persist(salleTD);
                labPhysique = createSalle("Laboratoire Physique", TypeSalle.TP, 25, "Équipements de mesure\nOscilloscopes\nMultimètres\nGénérateurs de signaux", false);
                session.persist(labPhysique);
                LOGGER.info("✅ 4 salles créées");
            } else {
                java.util.List<Salle> sList;
                sList = session.createQuery("FROM com.esalle.entity.Salle s WHERE s.nom = :n", Salle.class).setParameter("n","Amphithéâtre A").setMaxResults(1).getResultList();
                amphiA = sList.isEmpty() ? null : sList.get(0);
                sList = session.createQuery("FROM com.esalle.entity.Salle s WHERE s.nom = :n", Salle.class).setParameter("n","Laboratoire Informatique 1").setMaxResults(1).getResultList();
                labInfo = sList.isEmpty() ? null : sList.get(0);
                sList = session.createQuery("FROM com.esalle.entity.Salle s WHERE s.nom = :n", Salle.class).setParameter("n","Salle TD B1").setMaxResults(1).getResultList();
                salleTD = sList.isEmpty() ? null : sList.get(0);
                sList = session.createQuery("FROM com.esalle.entity.Salle s WHERE s.nom = :n", Salle.class).setParameter("n","Laboratoire Physique").setMaxResults(1).getResultList();
                labPhysique = sList.isEmpty() ? null : sList.get(0);
                LOGGER.info("ℹ️ Salles existantes récupérées");
            }

            // --- FILIERES ---
            if (filieresCount == 0) {
                LOGGER.info("📝 Création des filières de démonstration...");
                prepa1 = createFiliere("MPSI", Cycle.PREPARATOIRE, 1, 45, "Mathématiques, Physique et Sciences de l'Ingénieur - 1ère année");
                session.persist(prepa1);
                prepa2 = createFiliere("MPSI", Cycle.PREPARATOIRE, 2, 42, "Mathématiques, Physique et Sciences de l'Ingénieur - 2ème année");
                session.persist(prepa2);
                ing1 = createFiliere("Génie Informatique", Cycle.INGENIEUR, 1, 60, "Formation en développement logiciel et systèmes - DLA1");
                session.persist(ing1);
                ing2 = createFiliere("Génie Informatique", Cycle.INGENIEUR, 2, 58, "Formation en développement logiciel et systèmes - DLA2");
                session.persist(ing2);
                ing3 = createFiliere("Génie Informatique", Cycle.INGENIEUR, 3, 55, "Formation en développement logiciel et systèmes - DLA3");
                session.persist(ing3);
                LOGGER.info("✅ 5 filières créées");
            } else {
                java.util.List<Filiere> fList;
                fList = session.createQuery("FROM com.esalle.entity.Filiere f WHERE f.nom = :n AND f.annee = :a", Filiere.class).setParameter("n","MPSI").setParameter("a",1).setMaxResults(1).getResultList();
                prepa1 = fList.isEmpty() ? null : fList.get(0);
                fList = session.createQuery("FROM com.esalle.entity.Filiere f WHERE f.nom = :n AND f.annee = :a", Filiere.class).setParameter("n","MPSI").setParameter("a",2).setMaxResults(1).getResultList();
                prepa2 = fList.isEmpty() ? null : fList.get(0);
                fList = session.createQuery("FROM com.esalle.entity.Filiere f WHERE f.nom = :n AND f.annee = :a", Filiere.class).setParameter("n","Génie Informatique").setParameter("a",1).setMaxResults(1).getResultList();
                ing1 = fList.isEmpty() ? null : fList.get(0);
                fList = session.createQuery("FROM com.esalle.entity.Filiere f WHERE f.nom = :n AND f.annee = :a", Filiere.class).setParameter("n","Génie Informatique").setParameter("a",2).setMaxResults(1).getResultList();
                ing2 = fList.isEmpty() ? null : fList.get(0);
                fList = session.createQuery("FROM com.esalle.entity.Filiere f WHERE f.nom = :n AND f.annee = :a", Filiere.class).setParameter("n","Génie Informatique").setParameter("a",3).setMaxResults(1).getResultList();
                ing3 = fList.isEmpty() ? null : fList.get(0);
                LOGGER.info("ℹ️ Filières existantes récupérées");
            }

            // ---------- MATIERES ----------
            java.util.List<Matiere> createdMatieres = new java.util.ArrayList<>();
            if (matieresCount == 0) {
                LOGGER.info("📝 Création des matières de démonstration (jeu plus conséquent)...");

                java.util.List<Filiere> filieres = new java.util.ArrayList<>();
                if (prepa1 != null) filieres.add(prepa1);
                if (prepa2 != null) filieres.add(prepa2);
                if (ing1 != null) filieres.add(ing1);
                if (ing2 != null) filieres.add(ing2);
                if (ing3 != null) filieres.add(ing3);

                int matCounter = 1;
                for (Filiere f : filieres) {
                    // create 2 matieres per filiere
                    for (int i = 0; i < 2; i++) {
                        String matName = "Matière " + matCounter + " - " + f.getNom();
                        Matiere m = createMatiere(matName, f.getId(), professeur != null ? professeur.getId() : null, 20 + (i * 5), 6 + i, 2 + i);
                        m.setFiliereNom(f.getNom());
                        if (professeur != null) m.setProfesseurNom(professeur.getPrenom() + " " + professeur.getNom());
                        session.persist(m);
                        createdMatieres.add(m);
                        matCounter++;
                    }
                }

                LOGGER.info("✅ Matières créées: " + createdMatieres.size());
            } else {
                createdMatieres = session.createQuery("FROM com.esalle.entity.Matiere m", Matiere.class).setMaxResults(20).getResultList();
                LOGGER.info("ℹ️ Matières existantes récupérées: " + createdMatieres.size());
            }

            // ---------- EMPLOIS DU TEMPS ----------
            java.util.List<EmploiDuTemps> createdEmplois = new java.util.ArrayList<>();
            // If there are few emplois in DB, populate a fuller timetable
            final int MIN_EMPLOIS_DESIRED = 50;
            if (emploisCount == 0 || emploisCount < MIN_EMPLOIS_DESIRED) {
                LOGGER.info("📝 Création d'emplois du temps de démonstration (grand jeu de données)...");

                // collect filieres
                java.util.List<Filiere> filieres = new java.util.ArrayList<>();
                if (prepa1 != null) filieres.add(prepa1);
                if (prepa2 != null) filieres.add(prepa2);
                if (ing1 != null) filieres.add(ing1);
                if (ing2 != null) filieres.add(ing2);
                if (ing3 != null) filieres.add(ing3);

                // collect salles
                java.util.List<Salle> salles = session.createQuery("FROM com.esalle.entity.Salle s", Salle.class).setMaxResults(50).getResultList();
                if (salles.isEmpty()) {
                    if (amphiA != null) salles.add(amphiA);
                    if (labInfo != null) salles.add(labInfo);
                    if (salleTD != null) salles.add(salleTD);
                    if (labPhysique != null) salles.add(labPhysique);
                }

                // days (Mon-Sat) to match the view which shows Saturday as well
                EmploiDuTemps.JourSemaine[] jours = new EmploiDuTemps.JourSemaine[] {
                    EmploiDuTemps.JourSemaine.LUNDI,
                    EmploiDuTemps.JourSemaine.MARDI,
                    EmploiDuTemps.JourSemaine.MERCREDI,
                    EmploiDuTemps.JourSemaine.JEUDI,
                    EmploiDuTemps.JourSemaine.VENDREDI,
                    EmploiDuTemps.JourSemaine.SAMEDI
                };

                // time slots MUST match the JSP time labels so calendar keys align exactly
                LocalTime[][] slots = new LocalTime[][] {
                    { LocalTime.of(8,30), LocalTime.of(10,30) },   // 08:30-10:30
                    { LocalTime.of(10,30), LocalTime.of(12,30) },  // 10:30-12:30
                    { LocalTime.of(14,30), LocalTime.of(16,30) },  // 14:30-16:30
                    { LocalTime.of(16,30), LocalTime.of(18,30) },  // 16:30-18:30
                    { LocalTime.of(18,30), LocalTime.of(20,30) }   // 18:30-20:30
                };

                // map filiereId -> matieres
                java.util.Map<Long, java.util.List<Matiere>> matieresByFiliere = new java.util.HashMap<>();
                for (Matiere m : createdMatieres) {
                    if (m.getFiliereId() == null) continue;
                    matieresByFiliere.computeIfAbsent(m.getFiliereId(), k -> new java.util.ArrayList<>()).add(m);
                }

                int seanceNum = 1;
                for (Filiere f : filieres) {
                    java.util.List<Matiere> mList = matieresByFiliere.getOrDefault(f.getId(), new java.util.ArrayList<>());
                    if (mList.isEmpty()) {
                        // try to fetch matieres for this filiere from DB if none in createdMatieres
                        mList = session.createQuery("FROM com.esalle.entity.Matiere m WHERE m.filiereId = :fid", Matiere.class).setParameter("fid", f.getId()).getResultList();
                    }
                    if (mList.isEmpty()) continue; // can't schedule without matiere

                    // for each day and slot create a session, rotating matieres and salles
                    for (EmploiDuTemps.JourSemaine jour : jours) {
                        for (int sIdx = 0; sIdx < slots.length; sIdx++) {
                            LocalTime start = slots[sIdx][0];
                            LocalTime end = slots[sIdx][1];
                            Matiere chosen = mList.get((seanceNum - 1) % mList.size());
                            Salle chosenSalle = salles.isEmpty() ? null : salles.get((seanceNum - 1) % salles.size());

                            EmploiDuTemps e = createEmploi(f.getId(), f.getNom(), f.getAnnee(), chosen.getId(), chosen.getNom(), chosen.getProfesseurId(), chosen.getProfesseurNom(), chosenSalle != null ? chosenSalle.getId() : null, chosenSalle != null ? chosenSalle.getNom() : null, jour, start, end, sIdx % 2 == 0 ? EmploiDuTemps.TypeSeance.COURS : EmploiDuTemps.TypeSeance.TP, seanceNum);
                            session.persist(e);
                            createdEmplois.add(e);
                            seanceNum++;
                        }
                    }
                }

                LOGGER.info("✅ Emplois du temps créés: " + createdEmplois.size());
            } else {
                createdEmplois = session.createQuery("FROM com.esalle.entity.EmploiDuTemps e", EmploiDuTemps.class).setMaxResults(500).getResultList();
                LOGGER.info("ℹ️ Emplois existants récupérés: " + createdEmplois.size());
            }

            // ---------- RESERVATIONS ----------
            if (reservationsCount == 0) {
                LOGGER.info("📝 Création des réservations de démonstration (jeu plus large)...");
                java.util.List<User> users = session.createQuery("FROM com.esalle.entity.User u", User.class).setMaxResults(10).getResultList();
                java.util.List<Salle> salles = session.createQuery("FROM com.esalle.entity.Salle s", Salle.class).setMaxResults(10).getResultList();

                int resNum = 1;
                for (User u : users) {
                    // create up to 2 reservations per user (or until we reach some limit)
                    for (int i = 0; i < 2 && !salles.isEmpty(); i++) {
                        Salle s = salles.get((resNum - 1) % salles.size());
                        Reservation r = createReservation(u.getId(), u.getPrenom() + " " + u.getNom(), u.getRole() == UserRole.PROFESSEUR ? Reservation.TypeReservateur.PROFESSEUR : Reservation.TypeReservateur.MEMBRE_CLUB, s.getId(), s.getNom(), s.getType() == Salle.TypeSalle.COURS ? Reservation.TypeSalleReservation.COURS : Reservation.TypeSalleReservation.TP, LocalDate.now().plusDays(resNum), LocalTime.of(9 + (i * 2),0), LocalTime.of(11 + (i * 2),0), "Réservation test " + resNum, Reservation.StatutReservation.EN_ATTENTE);
                        session.persist(r);
                        resNum++;
                        if (resNum > 10) break;
                    }
                    if (resNum > 10) break;
                }

                LOGGER.info("✅ Réservations créées (jusqu'à 10)");
            } else {
                LOGGER.info("ℹ️ Réservations déjà présentes, aucune création nécessaire");
            }

            // ---------- RECLAMATIONS ----------
            if (reclamationsCount == 0) {
                LOGGER.info("📝 Création de réclamations de démonstration (plusieurs)...");
                java.util.List<User> users = session.createQuery("FROM com.esalle.entity.User u", User.class).setMaxResults(10).getResultList();
                java.util.List<Salle> salles = session.createQuery("FROM com.esalle.entity.Salle s", Salle.class).setMaxResults(10).getResultList();

                int recNum = 1;
                for (User u : users) {
                    if (salles.isEmpty()) break;
                    Salle s = salles.get((recNum - 1) % salles.size());
                    Reclamation rec = new Reclamation(u.getId(), s.getId(), "Problème automatique #" + recNum, recNum % 2 == 0 ? Reclamation.Urgence.FAIBLE : Reclamation.Urgence.MOYEN);
                    rec.setUserNom(u.getPrenom() + " " + u.getNom());
                    rec.setSalleNom(s.getNom());
                    session.persist(rec);
                    recNum++;
                    if (recNum > 6) break;
                }

                LOGGER.info("✅ Réclamations créées (jusqu'à 6)");
            } else {
                LOGGER.info("ℹ️ Réclamations déjà présentes, aucune création nécessaire");
            }

            // ---------- AUDIT LOGS ----------
            if (auditCount == 0) {
                LOGGER.info("📝 Création d'entrées de journal (audit) complémentaires...");
                int aNum = 1;
                // create an audit log for each salle
                java.util.List<Salle> sAll = session.createQuery("FROM com.esalle.entity.Salle s", Salle.class).setMaxResults(20).getResultList();
                for (Salle s : sAll) {
                    if (admin != null) {
                        AuditLog a = new AuditLog(admin.getId(), admin.getPrenom() + " " + admin.getNom(), "ADD", "SALLE", s.getId(), "Ajout de la salle: " + s.getNom());
                        session.persist(a);
                        aNum++;
                    }
                }
                // create some generic audit entries
                if (admin != null && prepa1 != null) {
                    AuditLog a2 = new AuditLog(admin.getId(), admin.getPrenom() + " " + admin.getNom(), "UPDATE", "FILIERE", prepa1.getId(), "Mise à jour de la filière " + prepa1.getNom());
                    session.persist(a2);
                }

                LOGGER.info("✅ Audit logs créés (" + aNum + ")");
            } else {
                LOGGER.info("ℹ️ Audit logs déjà présents, aucune création nécessaire");
            }

            transaction.commit();
            
            LOGGER.info("✅ ========================================");
            LOGGER.info("✅ DONNÉES DE TEST CRÉÉES AVEC SUCCÈS !");
            LOGGER.info("✅ ========================================");
            LOGGER.info("📧 Comptes de test (mot de passe: Test@2024):");
            LOGGER.info("   1️⃣  admin@ensaa.ma (ADMIN)");
            LOGGER.info("   2️⃣  coordinateur@ensaa.ma (COORDINATEUR)");
            LOGGER.info("   3️⃣  professeur@ensaa.ma (PROFESSEUR)");
            LOGGER.info("   4️⃣  club@ensaa.ma (MEMBRE_CLUB)");
            LOGGER.info("✅ ========================================");

        } catch (Throwable t) {
            LOGGER.severe("❌ ERREUR lors de l'initialisation: " + t.getMessage());
            t.printStackTrace();
            
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                    LOGGER.info("✅ Rollback effectué");
                } catch (Exception rollbackEx) {
                    LOGGER.warning("Erreur lors du rollback: " + rollbackEx.getMessage());
                }
            }
            
            LOGGER.warning("⚠️ L'application continuera à démarrer sans les données initiales.");
        } finally {
            if (session != null && session.isOpen()) {
                try {
                    session.close();
                    LOGGER.info("✅ Session fermée");
                } catch (Exception closeEx) {
                    LOGGER.warning("Erreur lors de la fermeture: " + closeEx.getMessage());
                }
            }
            LOGGER.info("🏁 Fin de l'initialisation des données");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("🔌 Arrêt de l'application...");
        try {
            HibernateUtil.closeSessionFactory();
            LOGGER.info("✅ SessionFactory fermé avec succès");
        } catch (Exception e) {
            LOGGER.warning("⚠️ Erreur lors de la fermeture: " + e.getMessage());
        }
    }

    private User createUser(String email, String nom, String prenom, String telephone, 
                           UserRole role, UserStatus status) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(BCrypt.hashpw(DEFAULT_PASSWORD, BCrypt.gensalt(12)));
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setTelephone(telephone);
        user.setRole(role);
        user.setStatut(status);
        return user;
    }

    private Salle createSalle(String nom, TypeSalle type, int capacite, String equipements, boolean disponible) {
        Salle salle = new Salle();
        salle.setNom(nom);
        salle.setType(type);
        salle.setCapacite(capacite);
        salle.setEquipements(equipements);
        salle.setDisponible(disponible);
        return salle;
    }

    private Filiere createFiliere(String nom, Cycle cycle, int annee, int effectif, String description) {
        Filiere filiere = new Filiere();
        filiere.setNom(nom);
        filiere.setCycle(cycle);
        filiere.setAnnee(annee);
        filiere.setEffectif(effectif);
        filiere.setDescription(description);
        return filiere;
    }

    private Matiere createMatiere(String nom, Long filiereId, Long professeurId, Integer heuresCours, Integer heuresTD, Integer heuresTP) {
        Matiere m = new Matiere();
        m.setNom(nom);
        m.setFiliereId(filiereId);
        m.setProfesseurId(professeurId);
        m.setHeuresCours(heuresCours != null ? heuresCours : 0);
        m.setHeuresTD(heuresTD != null ? heuresTD : 0);
        m.setHeuresTP(heuresTP != null ? heuresTP : 0);
        m.setDescription("Matière de démonstration");
        return m;
    }

    private EmploiDuTemps createEmploi(Long filiereId, String filiereNom, int annee, Long matiereId, String matiereNom, Long professeurId, String professeurNom, Long salleId, String salleNom, EmploiDuTemps.JourSemaine jour, LocalTime debut, LocalTime fin, EmploiDuTemps.TypeSeance type, int numero) {
        EmploiDuTemps e = new EmploiDuTemps();
        e.setFiliereId(filiereId);
        e.setFiliereNom(filiereNom);
        e.setAnnee(annee);
        e.setMatiereId(matiereId);
        e.setMatiereNom(matiereNom);
        e.setProfesseurId(professeurId);
        e.setProfesseurNom(professeurNom);
        e.setSalleId(salleId);
        e.setSalleNom(salleNom);
        e.setJourSemaine(jour);
        e.setHeureDebut(debut);
        e.setHeureFin(fin);
        e.setTypeSeance(type);
        e.setNumeroSeance(numero);
        return e;
    }

    private Reservation createReservation(Long userId, String userNom, Reservation.TypeReservateur typeReservateur, Long salleId, String salleNom, Reservation.TypeSalleReservation typeSalle, LocalDate dateResa, LocalTime debut, LocalTime fin, String motif, Reservation.StatutReservation statut) {
        Reservation r = new Reservation();
        r.setUserId(userId);
        r.setUserNom(userNom);
        r.setTypeReservateur(typeReservateur);
        r.setSalleId(salleId);
        r.setSalleNom(salleNom);
        r.setTypeSalle(typeSalle);
        r.setDateReservation(dateResa);
        r.setHeureDebut(debut);
        r.setHeureFin(fin);
        r.setMotif(motif);
        r.setStatut(statut);
        return r;
    }
}