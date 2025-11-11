import io.github.cdimascio.dotenv.Dotenv;

public static SessionFactory getSessionFactory() {
    if (sessionFactory != null) {
        return sessionFactory;
    }

    synchronized (HibernateUtil.class) {
        if (initializationAttempted) {
            if (initializationException != null) {
                throw initializationException;
            }
            return sessionFactory;
        }

        initializationAttempted = true;

        try {
            LOGGER.info("🔧 Initializing Hibernate SessionFactory...");

            // Load environment variables from .env
            Dotenv dotenv = Dotenv.load();
            System.setProperty("DB_DRIVER", dotenv.get("DB_DRIVER"));
            System.setProperty("DB_URL", dotenv.get("DB_URL"));
            System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
            System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

            // Create registry
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // Load hibernate.cfg.xml
                    .build();

            try {
                MetadataSources sources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();

                LOGGER.info("✅ Hibernate SessionFactory initialized successfully");
                return sessionFactory;

            } catch (Exception e) {
                StandardServiceRegistryBuilder.destroy(registry);
                throw e;
            }

        } catch (Exception e) {
            LOGGER.severe("❌ Failed to create SessionFactory: " + e.getMessage());
            e.printStackTrace();
            initializationException = new RuntimeException("Failed to create SessionFactory", e);
            throw initializationException;
        }
    }
}
