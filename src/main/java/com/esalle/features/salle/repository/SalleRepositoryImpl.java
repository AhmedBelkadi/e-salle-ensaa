package com.esalle.features.salle.repository;

import com.esalle.core.base.BaseRepositoryImpl;
import com.esalle.features.salle.domain.Salle;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class SalleRepositoryImpl extends BaseRepositoryImpl<Salle, Long> implements SalleRepository {
    
    public SalleRepositoryImpl() {
        super(Salle.class);
    }
    
    @Override
    public Optional<Salle> findByNom(String nom) {
        Session session = sessionFactory.openSession();
        try {
            Query<Salle> query = session.createQuery(
                "FROM Salle s WHERE s.nom = :nom", Salle.class);
            query.setParameter("nom", nom);
            return query.uniqueResultOptional();
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean existsByNom(String nom) {
        return findByNom(nom).isPresent();
    }
    
    @Override
    public List<Salle> findByType(Salle.TypeSalle type) {
        Session session = sessionFactory.openSession();
        try {
            Query<Salle> query = session.createQuery(
                "FROM Salle s WHERE s.type = :type ORDER BY s.nom", Salle.class);
            query.setParameter("type", type);
            return query.list();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Salle> findByDisponible(Boolean disponible) {
        Session session = sessionFactory.openSession();
        try {
            Query<Salle> query = session.createQuery(
                "FROM Salle s WHERE s.disponible = :disponible ORDER BY s.nom", Salle.class);
            query.setParameter("disponible", disponible);
            return query.list();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Salle> findByTypeAndDisponible(Salle.TypeSalle type, Boolean disponible) {
        Session session = sessionFactory.openSession();
        try {
            Query<Salle> query = session.createQuery(
                "FROM Salle s WHERE s.type = :type AND s.disponible = :disponible ORDER BY s.nom", 
                Salle.class);
            query.setParameter("type", type);
            query.setParameter("disponible", disponible);
            return query.list();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Salle> findByCapaciteGreaterThanEqual(Integer capaciteMin) {
        Session session = sessionFactory.openSession();
        try {
            Query<Salle> query = session.createQuery(
                "FROM Salle s WHERE s.capacite >= :capaciteMin ORDER BY s.capacite, s.nom", 
                Salle.class);
            query.setParameter("capaciteMin", capaciteMin);
            return query.list();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Salle> searchByNom(String keyword) {
        Session session = sessionFactory.openSession();
        try {
            Query<Salle> query = session.createQuery(
                "FROM Salle s WHERE LOWER(s.nom) LIKE LOWER(:keyword) ORDER BY s.nom", 
                Salle.class);
            query.setParameter("keyword", "%" + keyword + "%");
            return query.list();
        } finally {
            session.close();
        }
    }
}

