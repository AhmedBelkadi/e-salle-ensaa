package com.esalle.util;

import com.esalle.entity.*;
import com.esalle.entity.User.UserRole;
import com.esalle.entity.User.UserStatus;
import com.esalle.entity.Filiere.Cycle;
import com.esalle.entity.Salle.TypeSalle;
import com.esalle.entity.Reservation.TypeReservateur;
import com.esalle.entity.Reservation.TypeSalleReservation;
import com.esalle.entity.Reservation.StatutReservation;
import com.esalle.entity.Reclamation.Urgence;
import com.esalle.entity.Reclamation.Statut;
import com.esalle.entity.EmploiDuTemps.JourSemaine;
import com.esalle.entity.EmploiDuTemps.TypeSeance;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * DataInitializer: Seeds all database tables with comprehensive test data.
 * Only seeds if tables are empty (to prevent accidental reseeding).
 * Controlled via SEED_DB environment variable (default: true).
 */
@WebListener
public class DataInitializer implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());
    private static final String DEFAULT_PASSWORD = "password123";
    private static final Random random = new Random();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("DataInitializer starting...");

        // Gate seeding via env var SEED_DB (default: enabled)
        String seedEnv = System.getenv("SEED_DB");
        boolean seedEnabled = seedEnv == null || seedEnv.isEmpty() || (
            "true".equalsIgnoreCase(seedEnv) || "1".equals(seedEnv) || "yes".equalsIgnoreCase(seedEnv)
        );

        if (!seedEnabled) {
            LOGGER.info("SEED_DB disabled. Skipping data initialization.");
            return;
        }

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        if (sessionFactory == null) {
            LOGGER.warning("SessionFactory not available; skipping data initialization.");
            return;
        }

        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            // Check if database is already seeded
            Long usersCount = session.createQuery("SELECT COUNT(u) FROM com.esalle.entity.User u", Long.class).uniqueResult();
            if (usersCount != null && usersCount > 0) {
                LOGGER.info("Database already contains data. Skipping seeding.");
                tx.commit();
                return;
            }

            LOGGER.info("Starting comprehensive database seeding...");

            // Seed in order: Users -> Filieres -> Salles -> Matieres -> Reservations -> Reclamations -> EmploiDuTemps -> AuditLogs
            List<User> users = seedUsers(session);
            List<Filiere> filieres = seedFilieres(session, users);
            List<Salle> salles = seedSalles(session);
            List<Matiere> matieres = seedMatieres(session, filieres, users);
            seedReservations(session, users, salles);
            seedReclamations(session, users, salles);
            seedEmploiDuTemps(session, filieres, matieres, users, salles);
            seedAuditLogs(session, users);

            tx.commit();
            LOGGER.info("Database seeding completed successfully!");
        } catch (Throwable t) {
            LOGGER.severe("Error during data initialization: " + t.getMessage());
            t.printStackTrace();
            if (tx != null && tx.isActive()) tx.rollback();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            HibernateUtil.closeSessionFactory();
        } catch (Exception e) {
            LOGGER.warning("Error closing SessionFactory: " + e.getMessage());
        }
    }

    // ========== SEEDING METHODS ==========

    private List<User> seedUsers(Session session) {
        LOGGER.info("Seeding Users...");
        List<User> users = new ArrayList<>();

        // Admins
        users.add(createUser("admin@ensaa.ma", "Admin", "System", "+212600000001", UserRole.ADMIN, UserStatus.ACTIF, null, null));
        users.add(createUser("admin2@ensaa.ma", "Mohammed", "Alaoui", "+212600000002", UserRole.ADMIN, UserStatus.ACTIF, null, null));

        // Coordinateurs
        users.add(createUser("coordinateur.info@ensaa.ma", "Hassan", "Alami", "+212600000010", UserRole.COORDINATEUR, UserStatus.ACTIF, null, null));
        users.add(createUser("coordinateur.gc@ensaa.ma", "Fatima", "Benani", "+212600000011", UserRole.COORDINATEUR, UserStatus.ACTIF, null, null));
        users.add(createUser("coordinateur.meca@ensaa.ma", "Ahmed", "Tazi", "+212600000012", UserRole.COORDINATEUR, UserStatus.ACTIF, null, null));

        // Professeurs
        users.add(createUser("prof.alami@ensaa.ma", "Karim", "Alami", "+212600000020", UserRole.PROFESSEUR, UserStatus.ACTIF, null, null));
        users.add(createUser("prof.benani@ensaa.ma", "Sanae", "Benani", "+212600000021", UserRole.PROFESSEUR, UserStatus.ACTIF, null, null));
        users.add(createUser("prof.tazi@ensaa.ma", "Youssef", "Tazi", "+212600000022", UserRole.PROFESSEUR, UserStatus.ACTIF, null, null));
        users.add(createUser("prof.alaoui@ensaa.ma", "Nadia", "Alaoui", "+212600000023", UserRole.PROFESSEUR, UserStatus.ACTIF, null, null));
        users.add(createUser("prof.berrada@ensaa.ma", "Omar", "Berrada", "+212600000024", UserRole.PROFESSEUR, UserStatus.ACTIF, null, null));
        users.add(createUser("prof.idrissi@ensaa.ma", "Leila", "Idrissi", "+212600000025", UserRole.PROFESSEUR, UserStatus.ACTIF, null, null));

        // Membres de clubs
        users.add(createUser("club.robotique@ensaa.ma", "Ahmed", "Tazi", "+212600000030", UserRole.MEMBRE_CLUB, UserStatus.ACTIF, null, "Club Robotique"));
        users.add(createUser("club.informatique@ensaa.ma", "Sara", "Bennani", "+212600000031", UserRole.MEMBRE_CLUB, UserStatus.ACTIF, null, "Club Informatique"));
        users.add(createUser("club.entrepreneuriat@ensaa.ma", "Mehdi", "Alaoui", "+212600000032", UserRole.MEMBRE_CLUB, UserStatus.ACTIF, null, "Club Entrepreneuriat"));
        users.add(createUser("club.culturel@ensaa.ma", "Aicha", "Idrissi", "+212600000033", UserRole.MEMBRE_CLUB, UserStatus.ACTIF, null, "Club Culturel"));
        users.add(createUser("club.sport@ensaa.ma", "Youssef", "Berrada", "+212600000034", UserRole.MEMBRE_CLUB, UserStatus.ACTIF, null, "Club Sport"));

        // Some pending users
        users.add(createUser("pending.prof@ensaa.ma", "Pending", "Professor", "+212600000040", UserRole.PROFESSEUR, UserStatus.EN_ATTENTE, null, null));
        users.add(createUser("pending.club@ensaa.ma", "Pending", "Club", "+212600000041", UserRole.MEMBRE_CLUB, UserStatus.EN_ATTENTE, null, "Club Pending"));

        for (User user : users) {
            session.persist(user);
        }
        session.flush(); // Ensure IDs are generated

        LOGGER.info("Created " + users.size() + " users");
        return users;
    }

    private List<Filiere> seedFilieres(Session session, List<User> users) {
        LOGGER.info("Seeding Filieres...");
        List<Filiere> filieres = new ArrayList<>();
        List<User> coordinateurs = users.stream()
            .filter(u -> u.getRole() == UserRole.COORDINATEUR)
            .toList();

        // Préparatoire
        Filiere prepInfo1 = createFiliere("Informatique", Cycle.PREPARATOIRE, 1, 45, coordinateurs.get(0));
        Filiere prepInfo2 = createFiliere("Informatique", Cycle.PREPARATOIRE, 2, 42, coordinateurs.get(0));
        Filiere prepGC1 = createFiliere("Génie Civil", Cycle.PREPARATOIRE, 1, 40, coordinateurs.get(1));
        Filiere prepGC2 = createFiliere("Génie Civil", Cycle.PREPARATOIRE, 2, 38, coordinateurs.get(1));
        Filiere prepMeca1 = createFiliere("Mécanique", Cycle.PREPARATOIRE, 1, 35, coordinateurs.get(2));
        Filiere prepMeca2 = createFiliere("Mécanique", Cycle.PREPARATOIRE, 2, 33, coordinateurs.get(2));

        // Ingénieur
        Filiere ingInfo1 = createFiliere("Informatique", Cycle.INGENIEUR, 1, 38, coordinateurs.get(0));
        Filiere ingInfo2 = createFiliere("Informatique", Cycle.INGENIEUR, 2, 35, coordinateurs.get(0));
        Filiere ingInfo3 = createFiliere("Informatique", Cycle.INGENIEUR, 3, 32, coordinateurs.get(0));
        Filiere ingGC1 = createFiliere("Génie Civil", Cycle.INGENIEUR, 1, 35, coordinateurs.get(1));
        Filiere ingGC2 = createFiliere("Génie Civil", Cycle.INGENIEUR, 2, 33, coordinateurs.get(1));
        Filiere ingGC3 = createFiliere("Génie Civil", Cycle.INGENIEUR, 3, 30, coordinateurs.get(1));
        Filiere ingMeca1 = createFiliere("Mécanique", Cycle.INGENIEUR, 1, 30, coordinateurs.get(2));
        Filiere ingMeca2 = createFiliere("Mécanique", Cycle.INGENIEUR, 2, 28, coordinateurs.get(2));
        Filiere ingMeca3 = createFiliere("Mécanique", Cycle.INGENIEUR, 3, 25, coordinateurs.get(2));

        filieres.add(prepInfo1);
        filieres.add(prepInfo2);
        filieres.add(prepGC1);
        filieres.add(prepGC2);
        filieres.add(prepMeca1);
        filieres.add(prepMeca2);
        filieres.add(ingInfo1);
        filieres.add(ingInfo2);
        filieres.add(ingInfo3);
        filieres.add(ingGC1);
        filieres.add(ingGC2);
        filieres.add(ingGC3);
        filieres.add(ingMeca1);
        filieres.add(ingMeca2);
        filieres.add(ingMeca3);

        // Persist filieres first
        for (Filiere f : filieres) {
            session.persist(f);
        }
        session.flush();

        // Update coordinateurs with their primary filiere ID (first filiere they coordinate)
        for (int i = 0; i < coordinateurs.size() && i < 3; i++) {
            User coord = coordinateurs.get(i);
            Long filiereId = filieres.stream()
                .filter(f -> f.getCoordinateurId() != null && f.getCoordinateurId().equals(coord.getId()))
                .findFirst()
                .map(Filiere::getId)
                .orElse(null);
            if (filiereId != null) {
                coord.setFiliereId(filiereId);
                session.merge(coord);
            }
        }
        
        // Assign some professeurs to filieres
        List<User> professeurs = users.stream()
            .filter(u -> u.getRole() == UserRole.PROFESSEUR)
            .toList();
        for (int i = 0; i < professeurs.size() && i < filieres.size(); i++) {
            User prof = professeurs.get(i);
            Filiere filiere = filieres.get(i % filieres.size());
            prof.setFiliereId(filiere.getId());
            session.merge(prof);
        }

        LOGGER.info("Created " + filieres.size() + " filieres");
        return filieres;
    }

    private List<Salle> seedSalles(Session session) {
        LOGGER.info("Seeding Salles...");
        List<Salle> salles = new ArrayList<>();

        // Salles de cours
        salles.add(createSalle("Amphi A", TypeSalle.COURS, 150, "Projecteur, Tableau interactif, Sonorisation"));
        salles.add(createSalle("Amphi B", TypeSalle.COURS, 120, "Projecteur, Tableau interactif"));
        salles.add(createSalle("Salle C101", TypeSalle.COURS, 60, "Projecteur, Tableau"));
        salles.add(createSalle("Salle C102", TypeSalle.COURS, 60, "Projecteur, Tableau"));
        salles.add(createSalle("Salle C201", TypeSalle.COURS, 80, "Projecteur, Tableau interactif"));
        salles.add(createSalle("Salle C202", TypeSalle.COURS, 80, "Projecteur, Tableau"));

        // Salles TD
        salles.add(createSalle("TD 101", TypeSalle.TD, 30, "Tableau, Projecteur"));
        salles.add(createSalle("TD 102", TypeSalle.TD, 30, "Tableau, Projecteur"));
        salles.add(createSalle("TD 103", TypeSalle.TD, 25, "Tableau"));
        salles.add(createSalle("TD 201", TypeSalle.TD, 35, "Tableau, Projecteur"));
        salles.add(createSalle("TD 202", TypeSalle.TD, 35, "Tableau, Projecteur"));
        salles.add(createSalle("TD 203", TypeSalle.TD, 30, "Tableau"));

        // Salles TP
        salles.add(createSalle("TP Info 1", TypeSalle.TP, 25, "25 PC, Réseau, Serveur"));
        salles.add(createSalle("TP Info 2", TypeSalle.TP, 25, "25 PC, Réseau, Serveur"));
        salles.add(createSalle("TP Info 3", TypeSalle.TP, 20, "20 PC, Réseau"));
        salles.add(createSalle("TP Meca 1", TypeSalle.TP, 15, "Équipements mécaniques, Outils"));
        salles.add(createSalle("TP Meca 2", TypeSalle.TP, 15, "Équipements mécaniques, Outils"));
        salles.add(createSalle("TP GC 1", TypeSalle.TP, 20, "Équipements génie civil, Matériaux"));
        salles.add(createSalle("TP GC 2", TypeSalle.TP, 20, "Équipements génie civil, Matériaux"));

        // One unavailable salle
        Salle salleIndispo = createSalle("Salle Maintenance", TypeSalle.COURS, 50, "En maintenance");
        salleIndispo.setDisponible(false);
        salles.add(salleIndispo);

        for (Salle salle : salles) {
            session.persist(salle);
        }
        session.flush();

        LOGGER.info("Created " + salles.size() + " salles");
        return salles;
    }

    private List<Matiere> seedMatieres(Session session, List<Filiere> filieres, List<User> users) {
        LOGGER.info("Seeding Matieres...");
        List<Matiere> matieres = new ArrayList<>();
        List<User> professeurs = users.stream()
            .filter(u -> u.getRole() == UserRole.PROFESSEUR)
            .toList();

        // Matières pour Informatique
        Filiere info1 = filieres.stream().filter(f -> f.getNom().equals("Informatique") && f.getCycle() == Cycle.INGENIEUR && f.getAnnee() == 1).findFirst().orElse(null);
        Filiere info2 = filieres.stream().filter(f -> f.getNom().equals("Informatique") && f.getCycle() == Cycle.INGENIEUR && f.getAnnee() == 2).findFirst().orElse(null);
        Filiere info3 = filieres.stream().filter(f -> f.getNom().equals("Informatique") && f.getCycle() == Cycle.INGENIEUR && f.getAnnee() == 3).findFirst().orElse(null);

        if (info1 != null && professeurs.size() >= 6) {
            matieres.add(createMatiere("Algorithmes et Structures de Données", info1, professeurs.get(0), 20, 10, 20));
            matieres.add(createMatiere("Base de Données", info1, professeurs.get(1), 18, 12, 18));
            matieres.add(createMatiere("Programmation Orientée Objet", info1, professeurs.get(2), 15, 10, 25));
        }
        if (info2 != null && professeurs.size() >= 6) {
            matieres.add(createMatiere("Architecture Logicielle", info2, professeurs.get(3), 20, 8, 20));
            matieres.add(createMatiere("Réseaux et Sécurité", info2, professeurs.get(4), 18, 10, 18));
            matieres.add(createMatiere("Intelligence Artificielle", info2, professeurs.get(5), 15, 10, 15));
        }
        if (info3 != null && professeurs.size() >= 3) {
            matieres.add(createMatiere("Projet de Fin d'Études", info3, professeurs.get(0), 10, 0, 0));
            matieres.add(createMatiere("Gestion de Projet", info3, professeurs.get(1), 15, 5, 0));
        }

        // Matières pour Génie Civil
        Filiere gc1 = filieres.stream().filter(f -> f.getNom().equals("Génie Civil") && f.getCycle() == Cycle.INGENIEUR && f.getAnnee() == 1).findFirst().orElse(null);
        if (gc1 != null && professeurs.size() >= 2) {
            matieres.add(createMatiere("Résistance des Matériaux", gc1, professeurs.get(0), 25, 15, 10));
            matieres.add(createMatiere("Béton Armé", gc1, professeurs.get(1), 20, 12, 15));
        }

        // Matières pour Mécanique
        Filiere meca1 = filieres.stream().filter(f -> f.getNom().equals("Mécanique") && f.getCycle() == Cycle.INGENIEUR && f.getAnnee() == 1).findFirst().orElse(null);
        if (meca1 != null && professeurs.size() >= 2) {
            matieres.add(createMatiere("Mécanique des Fluides", meca1, professeurs.get(2), 22, 10, 12));
            matieres.add(createMatiere("Thermodynamique", meca1, professeurs.get(3), 20, 10, 10));
        }

        for (Matiere matiere : matieres) {
            session.persist(matiere);
        }
        session.flush();

        LOGGER.info("Created " + matieres.size() + " matieres");
        return matieres;
    }

    private void seedReservations(Session session, List<User> users, List<Salle> salles) {
        LOGGER.info("Seeding Reservations...");
        List<User> professeurs = users.stream()
            .filter(u -> u.getRole() == UserRole.PROFESSEUR && u.getStatut() == UserStatus.ACTIF)
            .toList();
        List<User> membresClub = users.stream()
            .filter(u -> u.getRole() == UserRole.MEMBRE_CLUB && u.getStatut() == UserStatus.ACTIF)
            .toList();
        List<Salle> sallesCours = salles.stream().filter(s -> s.getType() == TypeSalle.COURS && s.isDisponible()).toList();
        List<Salle> sallesTD = salles.stream().filter(s -> s.getType() == TypeSalle.TD && s.isDisponible()).toList();
        List<Salle> sallesTP = salles.stream().filter(s -> s.getType() == TypeSalle.TP && s.isDisponible()).toList();

        LocalDate today = LocalDate.now();
        int count = 0;

        // Past reservations (APPROUVEE, TERMINEE)
        for (int i = 0; i < 10 && professeurs.size() > 0 && sallesCours.size() > 0; i++) {
            LocalDate date = today.minusDays(random.nextInt(30) + 1);
            Reservation res = createReservation(
                professeurs.get(random.nextInt(professeurs.size())),
                sallesCours.get(random.nextInt(sallesCours.size())),
                date,
                LocalTime.of(8 + random.nextInt(4), 0),
                LocalTime.of(10 + random.nextInt(4), 0),
                "Cours magistral - " + date,
                StatutReservation.TERMINEE
            );
            res.setDateTraitement(date.atTime(LocalTime.of(9, 0)));
            session.persist(res);
            count++;
        }

        // Current reservations (APPROUVEE)
        for (int i = 0; i < 15 && professeurs.size() > 0; i++) {
            LocalDate date = today.plusDays(random.nextInt(14));
            List<Salle> availableSalles = i % 3 == 0 ? sallesTP : (i % 2 == 0 ? sallesTD : sallesCours);
            if (availableSalles.isEmpty()) continue;
            
            Reservation res = createReservation(
                professeurs.get(random.nextInt(professeurs.size())),
                availableSalles.get(random.nextInt(availableSalles.size())),
                date,
                LocalTime.of(8 + random.nextInt(6), 0),
                LocalTime.of(10 + random.nextInt(4), 0),
                "Réservation approuvée - " + date,
                StatutReservation.APPROUVEE
            );
            res.setDateTraitement(LocalDateTime.now().minusDays(random.nextInt(5)));
            session.persist(res);
            count++;
        }

        // Pending reservations (EN_ATTENTE)
        for (int i = 0; i < 10 && professeurs.size() > 0; i++) {
            LocalDate date = today.plusDays(random.nextInt(21));
            List<Salle> availableSalles = i % 2 == 0 ? sallesTD : sallesCours;
            if (availableSalles.isEmpty()) continue;
            
            Reservation res = createReservation(
                professeurs.get(random.nextInt(professeurs.size())),
                availableSalles.get(random.nextInt(availableSalles.size())),
                date,
                LocalTime.of(8 + random.nextInt(6), 0),
                LocalTime.of(10 + random.nextInt(4), 0),
                "Réservation en attente - " + date,
                StatutReservation.EN_ATTENTE
            );
            session.persist(res);
            count++;
        }

        // Club reservations
        for (int i = 0; i < 8 && membresClub.size() > 0 && sallesCours.size() > 0; i++) {
            LocalDate date = today.plusDays(random.nextInt(30));
            Reservation res = createReservation(
                membresClub.get(random.nextInt(membresClub.size())),
                sallesCours.get(random.nextInt(sallesCours.size())),
                date,
                LocalTime.of(14 + random.nextInt(4), 0),
                LocalTime.of(16 + random.nextInt(2), 0),
                "Événement club - " + date,
                random.nextBoolean() ? StatutReservation.APPROUVEE : StatutReservation.EN_ATTENTE
            );
            if (res.getStatut() == StatutReservation.APPROUVEE) {
                res.setDateTraitement(LocalDateTime.now().minusDays(random.nextInt(3)));
            }
            session.persist(res);
            count++;
        }

        LOGGER.info("Created " + count + " reservations");
    }

    private void seedReclamations(Session session, List<User> users, List<Salle> salles) {
        LOGGER.info("Seeding Reclamations...");
        List<User> allUsers = users.stream()
            .filter(u -> u.getStatut() == UserStatus.ACTIF)
            .toList();
        List<Salle> availableSalles = salles.stream().filter(Salle::isDisponible).toList();

        int count = 0;
        String[] descriptions = {
            "Projecteur ne fonctionne plus correctement",
            "Climatisation en panne",
            "Tableau effaçable endommagé",
            "Chaises manquantes dans la salle",
            "Problème de connexion réseau",
            "Éclairage défectueux",
            "Fenêtres qui ne ferment pas",
            "Bruit excessif dans la salle",
            "Équipements TP manquants",
            "Porte qui ne se ferme pas correctement"
        };

        Urgence[] urgences = {Urgence.FAIBLE, Urgence.MOYEN, Urgence.URGENT};

        // Pending reclamations
        for (int i = 0; i < 12 && allUsers.size() > 0 && availableSalles.size() > 0; i++) {
            Reclamation rec = createReclamation(
                allUsers.get(random.nextInt(allUsers.size())),
                availableSalles.get(random.nextInt(availableSalles.size())),
                descriptions[random.nextInt(descriptions.length)],
                urgences[random.nextInt(urgences.length)],
                Statut.EN_ATTENTE
            );
            session.persist(rec);
            count++;
        }

        // Treated reclamations
        List<User> admins = users.stream()
            .filter(u -> u.getRole() == UserRole.ADMIN)
            .toList();
        for (int i = 0; i < 8 && allUsers.size() > 0 && availableSalles.size() > 0 && admins.size() > 0; i++) {
            Reclamation rec = createReclamation(
                allUsers.get(random.nextInt(allUsers.size())),
                availableSalles.get(random.nextInt(availableSalles.size())),
                descriptions[random.nextInt(descriptions.length)],
                urgences[random.nextInt(urgences.length)],
                Statut.TRAITEE
            );
            User admin = admins.get(random.nextInt(admins.size()));
            rec.setTraiteParId(admin.getId());
            rec.setTraiteParNom(admin.getNom() + " " + admin.getPrenom());
            rec.setDateTraitement(LocalDateTime.now().minusDays(random.nextInt(10)));
            rec.setCommentaireTraitement("Problème résolu. Maintenance effectuée.");
            session.persist(rec);
            count++;
        }

        LOGGER.info("Created " + count + " reclamations");
    }

    private void seedEmploiDuTemps(Session session, List<Filiere> filieres, List<Matiere> matieres, 
                                   List<User> users, List<Salle> salles) {
        LOGGER.info("Seeding EmploiDuTemps...");
        List<Salle> availableSalles = salles.stream().filter(Salle::isDisponible).toList();
        JourSemaine[] jours = JourSemaine.values();
        int count = 0;

        for (Matiere matiere : matieres) {
            // Create 7 sessions per matiere
            for (int seance = 1; seance <= 7; seance++) {
                EmploiDuTemps edt = new EmploiDuTemps();
                edt.setFiliereId(matiere.getFiliereId());
                edt.setFiliereNom(matiere.getFiliereNom());
                edt.setMatiereId(matiere.getId());
                edt.setMatiereNom(matiere.getNom());
                edt.setProfesseurId(matiere.getProfesseurId());
                edt.setProfesseurNom(matiere.getProfesseurNom());
                
                // Assign salle based on type
                TypeSeance typeSeance;
                Salle salle = null;
                if (matiere.getHeuresTP() > 0 && seance <= 3) {
                    typeSeance = TypeSeance.TP;
                    List<Salle> sallesTP = availableSalles.stream()
                        .filter(s -> s.getType() == TypeSalle.TP)
                        .toList();
                    if (!sallesTP.isEmpty()) {
                        salle = sallesTP.get(random.nextInt(sallesTP.size()));
                    }
                } else if (matiere.getHeuresTD() > 0 && seance <= 5) {
                    typeSeance = TypeSeance.TD;
                    List<Salle> sallesTD = availableSalles.stream()
                        .filter(s -> s.getType() == TypeSalle.TD)
                        .toList();
                    if (!sallesTD.isEmpty()) {
                        salle = sallesTD.get(random.nextInt(sallesTD.size()));
                    }
                } else {
                    typeSeance = TypeSeance.COURS;
                    List<Salle> sallesCours = availableSalles.stream()
                        .filter(s -> s.getType() == TypeSalle.COURS)
                        .toList();
                    if (!sallesCours.isEmpty()) {
                        salle = sallesCours.get(random.nextInt(sallesCours.size()));
                    }
                }

                if (salle != null) {
                    edt.setSalleId(salle.getId());
                    edt.setSalleNom(salle.getNom());
                }

                edt.setAnnee(matiere.getFiliereId() != null ? 
                    filieres.stream()
                        .filter(f -> f.getId().equals(matiere.getFiliereId()))
                        .findFirst()
                        .map(Filiere::getAnnee)
                        .orElse(1) : 1);

                // Group assignment (1 or 2 for TD/TP)
                if (typeSeance == TypeSeance.TD || typeSeance == TypeSeance.TP) {
                    edt.setGroupe(random.nextBoolean() ? "1" : "2");
                }

                edt.setJourSemaine(jours[random.nextInt(jours.length - 1)]); // Exclude SAMEDI sometimes
                edt.setHeureDebut(LocalTime.of(8 + random.nextInt(6), random.nextBoolean() ? 0 : 30));
                edt.setHeureFin(edt.getHeureDebut().plusHours(2));
                edt.setTypeSeance(typeSeance);
                edt.setNumeroSeance(seance);
                edt.setDateDebut(LocalDate.now().minusWeeks(2));
                edt.setDateFin(LocalDate.now().plusWeeks(16));

                session.persist(edt);
                count++;
            }
        }

        LOGGER.info("Created " + count + " emploi du temps entries");
    }

    private void seedAuditLogs(Session session, List<User> users) {
        LOGGER.info("Seeding AuditLogs...");
        List<User> admins = users.stream()
            .filter(u -> u.getRole() == UserRole.ADMIN)
            .toList();

        String[] actionTypes = {"CREATE", "UPDATE", "DELETE", "APPROVE", "REFUSE"};
        String[] entityTypes = {"USER", "SALLE", "FILIERE", "RESERVATION", "RECLAMATION", "MATIERE"};

        for (int i = 0; i < 20 && admins.size() > 0; i++) {
            AuditLog log = new AuditLog();
            User admin = admins.get(random.nextInt(admins.size()));
            log.setUserId(admin.getId());
            log.setUserNom(admin.getNom() + " " + admin.getPrenom());
            log.setActionType(actionTypes[random.nextInt(actionTypes.length)]);
            log.setEntityType(entityTypes[random.nextInt(entityTypes.length)]);
            log.setEntityId((long) (random.nextInt(100) + 1));
            log.setDescription("Action " + log.getActionType() + " sur " + log.getEntityType() + " #" + log.getEntityId());
            log.setIpAddress("192.168.1." + (random.nextInt(255) + 1));
            log.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));

            session.persist(log);
        }

        LOGGER.info("Created 20 audit logs");
    }

    // ========== HELPER METHODS ==========

    private User createUser(String email, String nom, String prenom, String telephone,
                           UserRole role, UserStatus status, Long filiereId, String nomClub) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(BCrypt.hashpw(DEFAULT_PASSWORD, BCrypt.gensalt(8)));
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setTelephone(telephone);
        user.setRole(role);
        user.setStatut(status);
        user.setFiliereId(filiereId);
        user.setNomClub(nomClub);
        user.setDateInscription(LocalDateTime.now().minusDays(random.nextInt(90)));
        if (status == UserStatus.ACTIF) {
            user.setDateApprobation(LocalDateTime.now().minusDays(random.nextInt(60)));
            user.setApprovedBy(1L); // First admin
        }
        return user;
    }

    private Filiere createFiliere(String nom, Cycle cycle, Integer annee, Integer effectif, User coordinateur) {
        Filiere filiere = new Filiere();
        filiere.setNom(nom);
        filiere.setCycle(cycle);
        filiere.setAnnee(annee);
        filiere.setEffectif(effectif);
        filiere.setCoordinateurId(coordinateur.getId());
        filiere.setCoordinateurNom(coordinateur.getNom() + " " + coordinateur.getPrenom());
        filiere.setDescription("Filière " + nom + " - " + cycle + " année " + annee);
        return filiere;
    }

    private Salle createSalle(String nom, TypeSalle type, Integer capacite, String equipements) {
        Salle salle = new Salle();
        salle.setNom(nom);
        salle.setType(type);
        salle.setCapacite(capacite);
        salle.setEquipements(equipements);
        salle.setDisponible(true);
        salle.setDateCreation(LocalDateTime.now().minusMonths(random.nextInt(12)));
        return salle;
    }

    private Matiere createMatiere(String nom, Filiere filiere, User professeur, 
                                  Integer heuresCours, Integer heuresTD, Integer heuresTP) {
        Matiere matiere = new Matiere();
        matiere.setNom(nom);
        matiere.setFiliereId(filiere.getId());
        matiere.setFiliereNom(filiere.getNom());
        matiere.setProfesseurId(professeur.getId());
        matiere.setProfesseurNom(professeur.getNom() + " " + professeur.getPrenom());
        matiere.setHeuresCours(heuresCours);
        matiere.setHeuresTD(heuresTD);
        matiere.setHeuresTP(heuresTP);
        matiere.setDescription("Matière " + nom + " pour " + filiere.getNomComplet());
        return matiere;
    }

    private Reservation createReservation(User user, Salle salle, LocalDate date,
                                          LocalTime heureDebut, LocalTime heureFin,
                                          String motif, StatutReservation statut) {
        Reservation res = new Reservation();
        res.setUserId(user.getId());
        res.setUserNom(user.getNom() + " " + user.getPrenom());
        res.setTypeReservateur(user.getRole() == UserRole.PROFESSEUR ? 
            TypeReservateur.PROFESSEUR : TypeReservateur.MEMBRE_CLUB);
        res.setSalleId(salle.getId());
        res.setSalleNom(salle.getNom());
        res.setTypeSalle(convertTypeSalle(salle.getType()));
        res.setDateReservation(date);
        res.setHeureDebut(heureDebut);
        res.setHeureFin(heureFin);
        res.setMotif(motif);
        res.setStatut(statut);
        res.setDateCreation(LocalDateTime.now().minusDays(random.nextInt(10)));
        return res;
    }

    private Reclamation createReclamation(User user, Salle salle, String description,
                                         Urgence urgence, Statut statut) {
        Reclamation rec = new Reclamation();
        rec.setUserId(user.getId());
        rec.setUserNom(user.getNom() + " " + user.getPrenom());
        rec.setSalleId(salle.getId());
        rec.setSalleNom(salle.getNom());
        rec.setDescription(description);
        rec.setUrgence(urgence);
        rec.setStatut(statut);
        rec.setDateCreation(LocalDateTime.now().minusDays(random.nextInt(15)));
        return rec;
    }

    private TypeSalleReservation convertTypeSalle(TypeSalle type) {
        return switch (type) {
            case COURS -> TypeSalleReservation.COURS;
            case TD -> TypeSalleReservation.TD;
            case TP -> TypeSalleReservation.TP;
        };
    }
}
