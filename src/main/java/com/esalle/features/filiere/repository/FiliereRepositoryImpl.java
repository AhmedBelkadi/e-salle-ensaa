package com.esalle.features.filiere.repository;

import com.esalle.features.filiere.domain.Filiere;
import com.esalle.features.filiere.domain.Filiere.Cycle;
import com.esalle.core.base.BaseRepositoryImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du repository pour les Filières
 */
public class FiliereRepositoryImpl extends BaseRepositoryImpl<Filiere, Long> implements FiliereRepository {

    public FiliereRepositoryImpl() {
        super(Filiere.class);
    }

    @Override
    public Filiere save(Filiere filiere) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            if (filiere.getId() == null) {
                session.persist(filiere);
            } else {
                filiere = (Filiere) session.merge(filiere);
            }
            transaction.commit();
            return filiere;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving filiere", e);
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<Filiere> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Filiere filiere = session.get(Filiere.class, id);
            return Optional.ofNullable(filiere);
        }
    }

    @Override
    public List<Filiere> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Filiere f ORDER BY f.cycle, f.annee, f.nom", Filiere.class)
                    .getResultList();
        }
    }

    @Override
    public List<Filiere> findByCycle(Cycle cycle) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Filiere f WHERE f.cycle = :cycle ORDER BY f.annee, f.nom", Filiere.class)
                    .setParameter("cycle", cycle)
                    .getResultList();
        }
    }

    @Override
    public List<Filiere> findByCycleAndAnnee(Cycle cycle, Integer annee) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Filiere f WHERE f.cycle = :cycle AND f.annee = :annee ORDER BY f.nom", Filiere.class)
                    .setParameter("cycle", cycle)
                    .setParameter("annee", annee)
                    .getResultList();
        }
    }

    @Override
    public List<Filiere> findByCoordinateurId(Long coordinateurId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Filiere f WHERE f.coordinateurId = :coordinateurId ORDER BY f.cycle, f.annee, f.nom", Filiere.class)
                    .setParameter("coordinateurId", coordinateurId)
                    .getResultList();
        }
    }

    @Override
    public List<Filiere> searchByNom(String keyword) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Filiere f WHERE LOWER(f.nom) LIKE LOWER(:keyword) ORDER BY f.nom", Filiere.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        }
    }

    @Override
    public boolean existsByNomAndCycleAndAnnee(String nom, Cycle cycle, Integer annee) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                    "SELECT COUNT(f) FROM Filiere f WHERE f.nom = :nom AND f.cycle = :cycle AND f.annee = :annee", 
                    Long.class)
                    .setParameter("nom", nom)
                    .setParameter("cycle", cycle)
                    .setParameter("annee", annee)
                    .getSingleResult();
            return count > 0;
        }
    }

    @Override
    public boolean existsByNomAndCycleAndAnneeAndIdNot(String nom, Cycle cycle, Integer annee, Long id) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                    "SELECT COUNT(f) FROM Filiere f WHERE f.nom = :nom AND f.cycle = :cycle AND f.annee = :annee AND f.id != :id", 
                    Long.class)
                    .setParameter("nom", nom)
                    .setParameter("cycle", cycle)
                    .setParameter("annee", annee)
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Filiere filiere = session.get(Filiere.class, id);
            if (filiere != null) {
                session.remove(filiere);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting filiere", e);
        } finally {
            session.close();
        }
    }

    @Override
    public long count() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT COUNT(f) FROM Filiere f", Long.class)
                    .getSingleResult();
        }
    }

    @Override
    public long countByCycle(Cycle cycle) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT COUNT(f) FROM Filiere f WHERE f.cycle = :cycle", Long.class)
                    .setParameter("cycle", cycle)
                    .getSingleResult();
        }
    }
}

