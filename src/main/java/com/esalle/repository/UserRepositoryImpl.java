package com.esalle.repository;

import com.esalle.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;
import java.util.List;

public class UserRepositoryImpl extends BaseRepositoryImpl<User, Long> implements UserRepository {
    
    public UserRepositoryImpl() {
        super(User.class);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        Session session = sessionFactory.openSession();
        try {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.uniqueResultOptional();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<User> findByStatut(User.UserStatus statut) {
        Session session = sessionFactory.openSession();
        try {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.statut = :statut ORDER BY u.dateInscription DESC", User.class);
            query.setParameter("statut", statut);
            return query.list();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<User> findByRole(User.UserRole role) {
        Session session = sessionFactory.openSession();
        try {
            Query<User> query = session.createQuery(
                "FROM User u WHERE u.role = :role ORDER BY u.nom, u.prenom", User.class);
            query.setParameter("role", role);
            return query.list();
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}

