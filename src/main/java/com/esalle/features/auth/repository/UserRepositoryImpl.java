package com.esalle.features.auth.repository;

import com.esalle.core.base.BaseRepositoryImpl;
import com.esalle.features.auth.domain.User;
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
        Session session = getSession();
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
        Session session = getSession();
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
        Session session = getSession();
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

