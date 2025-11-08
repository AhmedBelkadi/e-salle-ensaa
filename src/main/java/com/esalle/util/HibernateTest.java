package com.esalle.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateTest {
    public static void main(String[] args) {
        // Load Hibernate configuration and build SessionFactory
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(com.esalle.entity.User.class)
                .buildSessionFactory();

        // Open a session
        Session session = sessionFactory.openSession();

        try {
            // Start transaction
            session.beginTransaction();

            // Create a new user
            com.esalle.entity.User user = new com.esalle.entity.User();
            user.setNom("Haidouch");
            user.setPrenom("Marouane");
            user.setEmail("marouane.haidouch@example.com");
            user.setPassword("password123");
            user.setRole(com.esalle.entity.User.UserRole.ADMIN);

            // Save user
            session.save(user);

            // Commit transaction
            session.getTransaction().commit();

            System.out.println("✅ User inserted successfully! ID: " + user.getId());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }
}
