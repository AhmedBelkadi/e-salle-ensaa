package com.esalle.util;

import com.esalle.entity.User;
import com.esalle.entity.User.UserRole;
import com.esalle.entity.User.UserStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.logging.Logger;

/**
 * DataInitializer (minimal): only ensures basic users exist on startup.
 * Full DB seeding should be performed via SQL script (src/main/resources/db/seed.sql)
 * This prevents accidental reseeding of environment data by the app on startup.
 */
@WebListener
public class DataInitializer implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());
    private static final String DEFAULT_PASSWORD = "password123"; // default for seeded users

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("DataInitializer (users-only) starting...");

        // Gate seeding via env var SEED_DB (default: enabled for users if table empty)
        String seedEnv = System.getenv("SEED_DB");
        boolean seedEnabled = seedEnv == null || seedEnv.isEmpty() || (
            "true".equalsIgnoreCase(seedEnv) || "1".equals(seedEnv) || "yes".equalsIgnoreCase(seedEnv)
        );

        if (!seedEnabled) {
            LOGGER.info("SEED_DB disabled. Skipping user initialization.");
            return;
        }

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        if (sessionFactory == null) {
            LOGGER.warning("SessionFactory not available; skipping user initialization.");
            return;
        }

        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Long usersCount = session.createQuery("SELECT COUNT(u) FROM com.esalle.entity.User u", Long.class).uniqueResult();
            LOGGER.info("Existing users count: " + usersCount);

            if (usersCount == null || usersCount == 0) {
                LOGGER.info("Creating minimal user accounts (admin, coordinateur, professeur, membre_club)");
                User admin = createUser("admin@ensaa.ma", "Admin", "System", "+212600000001", UserRole.ADMIN, UserStatus.ACTIF);
                session.persist(admin);

                User coordinateur = createUser("coordinateur@ensaa.ma", "Hassan", "Alami", "+212600000002", UserRole.COORDINATEUR, UserStatus.ACTIF);
                session.persist(coordinateur);

                User professeur = createUser("professeur@ensaa.ma", "Fatima", "Benani", "+212600000003", UserRole.PROFESSEUR, UserStatus.ACTIF);
                session.persist(professeur);

                User membreClub = createUser("club@ensaa.ma", "Ahmed", "Tazi", "+212600000004", UserRole.MEMBRE_CLUB, UserStatus.ACTIF);
                session.persist(membreClub);

                LOGGER.info("Minimal users created");
            } else {
                LOGGER.info("Users already present; skipping creation");
            }

            tx.commit();
        } catch (Throwable t) {
            LOGGER.severe("Error during user initialization: " + t.getMessage());
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

    private User createUser(String email, String nom, String prenom, String telephone,
                           UserRole role, UserStatus status) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(BCrypt.hashpw(DEFAULT_PASSWORD, BCrypt.gensalt(8)));
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setTelephone(telephone);
        user.setRole(role);
        user.setStatut(status);
        return user;
    }
}