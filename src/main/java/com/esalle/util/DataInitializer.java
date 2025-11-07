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
    private static final String DEFAULT_PASSWORD = "Test@2024"; // Même mot de passe pour tous les comptes de test

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("🚀 Initialisation des données de test...");
        
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction transaction = null;

        try {
            LOGGER.info("🔧 Tentative d'obtention de SessionFactory...");
            // Try to get session factory - may fail if Hibernate not ready yet
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

            // Vérifier si des données existent déjà
            Long userCount = (Long) session.createQuery("SELECT COUNT(u) FROM User u").uniqueResult();
            
            if (userCount > 0) {
                LOGGER.info("✅ Données déjà initialisées. Aucune action nécessaire.");
                transaction.commit();
                return;
            }

            LOGGER.info("📝 Création des utilisateurs de test...");

            // 1️⃣ ADMIN - admin@ensaa.ma
            User admin = createUser(
                "admin@ensaa.ma",
                "Admin",
                "Système",
                "+212600000001",
                UserRole.ADMIN,
                UserStatus.ACTIF
            );
            session.persist(admin);
            LOGGER.info("✅ Admin créé: admin@ensaa.ma / Test@2024");

            // 2️⃣ COORDINATEUR - coordinateur@ensaa.ma
            User coordinateur = createUser(
                "coordinateur@ensaa.ma",
                "Hassan",
                "Alami",
                "+212600000002",
                UserRole.COORDINATEUR,
                UserStatus.ACTIF
            );
            session.persist(coordinateur);
            LOGGER.info("✅ Coordinateur créé: coordinateur@ensaa.ma / Test@2024");

            // 3️⃣ PROFESSEUR - professeur@ensaa.ma
            User professeur = createUser(
                "professeur@ensaa.ma",
                "Fatima",
                "Benani",
                "+212600000003",
                UserRole.PROFESSEUR,
                UserStatus.ACTIF
            );
            session.persist(professeur);
            LOGGER.info("✅ Professeur créé: professeur@ensaa.ma / Test@2024");

            // 4️⃣ MEMBRE_CLUB - club@ensaa.ma
            User membreClub = createUser(
                "club@ensaa.ma",
                "Ahmed",
                "Tazi",
                "+212600000004",
                UserRole.MEMBRE_CLUB,
                UserStatus.ACTIF
            );
            session.persist(membreClub);
            LOGGER.info("✅ Membre Club créé: club@ensaa.ma / Test@2024");

            // 📚 Créer des salles de démonstration
            LOGGER.info("📝 Création des salles de démonstration...");
            
            Salle amphiA = createSalle("Amphithéâtre A", TypeSalle.COURS, 200, 
                "Projecteur HD\nSystème audio\nTableau interactif\nClimatisation", true);
            session.persist(amphiA);

            Salle labInfo = createSalle("Laboratoire Informatique 1", TypeSalle.TP, 30, 
                "30 PC avec double écran\nServeur local\nTableau blanc\nWiFi haut débit", true);
            session.persist(labInfo);

            Salle salleTD = createSalle("Salle TD B1", TypeSalle.TD, 40, 
                "Projecteur\nTableau blanc\nPrises électriques\nChaises mobiles", true);
            session.persist(salleTD);

            Salle labPhysique = createSalle("Laboratoire Physique", TypeSalle.TP, 25, 
                "Équipements de mesure\nOscilloscopes\nMultimètres\nGénérateurs de signaux", false);
            session.persist(labPhysique);

            LOGGER.info("✅ 4 salles créées");

            // 🎓 Créer des filières de démonstration
            LOGGER.info("📝 Création des filières de démonstration...");
            
            // Cycle Préparatoire (2 filières auto)
            Filiere prepa1 = createFiliere("MPSI", Cycle.PREPARATOIRE, 1, 45, 
                "Mathématiques, Physique et Sciences de l'Ingénieur - 1ère année");
            session.persist(prepa1);

            Filiere prepa2 = createFiliere("MPSI", Cycle.PREPARATOIRE, 2, 42, 
                "Mathématiques, Physique et Sciences de l'Ingénieur - 2ème année");
            session.persist(prepa2);

            // Cycle Ingénieur (3 filières auto)
            Filiere ing1 = createFiliere("Génie Informatique", Cycle.INGENIEUR, 1, 60, 
                "Formation en développement logiciel et systèmes - DLA1");
            session.persist(ing1);

            Filiere ing2 = createFiliere("Génie Informatique", Cycle.INGENIEUR, 2, 58, 
                "Formation en développement logiciel et systèmes - DLA2");
            session.persist(ing2);

            Filiere ing3 = createFiliere("Génie Informatique", Cycle.INGENIEUR, 3, 55, 
                "Formation en développement logiciel et systèmes - DLA3");
            session.persist(ing3);

            LOGGER.info("✅ 5 filières créées");

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
            // Catch all errors including Error and ExceptionInInitializerError
            LOGGER.severe("❌ ERREUR CRITIQUE lors de l'initialisation des données!");
            LOGGER.severe("Type: " + t.getClass().getName());
            LOGGER.severe("Message: " + (t.getMessage() != null ? t.getMessage() : "N/A"));
            
            if (t.getCause() != null) {
                LOGGER.severe("Cause: " + t.getCause().getClass().getName() + " - " + t.getCause().getMessage());
            }
            
            // Print full stack trace
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            t.printStackTrace(pw);
            LOGGER.severe("Stack trace:\n" + sw.toString());
            
            // Clean up transaction if active
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                    LOGGER.info("✅ Rollback effectué");
                } catch (Exception rollbackEx) {
                    LOGGER.warning("Erreur lors du rollback: " + rollbackEx.getMessage());
                }
            }
            
            // Don't rethrow - allow application to start even if data initialization fails
            LOGGER.warning("⚠️ L'application continuera à démarrer sans les données initiales.");
        } finally {
            if (session != null && session.isOpen()) {
                try {
                    session.close();
                    LOGGER.info("✅ Session fermée");
                } catch (Exception closeEx) {
                    LOGGER.warning("Erreur lors de la fermeture de la session: " + closeEx.getMessage());
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
            LOGGER.warning("⚠️ Erreur lors de la fermeture de SessionFactory: " + e.getMessage());
        }
    }

    /**
     * Crée un utilisateur avec mot de passe hashé
     */
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
        // Note: createdAt/updatedAt sont gérés automatiquement par @PrePersist/@PreUpdate
        return user;
    }

    /**
     * Crée une salle
     */
    private Salle createSalle(String nom, TypeSalle type, int capacite, String equipements, boolean disponible) {
        Salle salle = new Salle();
        salle.setNom(nom);
        salle.setType(type);
        salle.setCapacite(capacite);
        salle.setEquipements(equipements);
        salle.setDisponible(disponible);
        // Note: createdAt/updatedAt sont gérés automatiquement par @PrePersist/@PreUpdate
        return salle;
    }

    /**
     * Crée une filière
     */
    private Filiere createFiliere(String nom, Cycle cycle, int annee, int effectif, String description) {
        Filiere filiere = new Filiere();
        filiere.setNom(nom);
        filiere.setCycle(cycle);
        filiere.setAnnee(annee);
        filiere.setEffectif(effectif);
        filiere.setDescription(description);
        // Note: createdAt/updatedAt sont gérés automatiquement par @PrePersist/@PreUpdate
        return filiere;
    }
}

