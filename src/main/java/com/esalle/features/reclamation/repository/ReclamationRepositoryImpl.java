package com.esalle.features.reclamation.repository;

import com.esalle.core.base.BaseRepositoryImpl;
import com.esalle.features.reclamation.domain.Reclamation;
import com.esalle.features.reclamation.domain.Reclamation.Statut;
import com.esalle.features.reclamation.domain.Reclamation.Urgence;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du repository pour les Réclamations
 */
public class ReclamationRepositoryImpl extends BaseRepositoryImpl<Reclamation, Long> 
        implements ReclamationRepository {

    public ReclamationRepositoryImpl() {
        super(Reclamation.class);
    }

    @Override
    public Reclamation save(Reclamation reclamation) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            if (reclamation.getId() == null) {
                session.persist(reclamation);
            } else {
                reclamation = (Reclamation) session.merge(reclamation);
            }
            transaction.commit();
            return reclamation;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving reclamation", e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reclamation> findByUserId(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM Reclamation r WHERE r.userId = :userId ORDER BY r.dateCreation DESC", 
                Reclamation.class)
                .setParameter("userId", userId)
                .getResultList();
        }
    }

    @Override
    public List<Reclamation> findByStatut(Statut statut) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM Reclamation r WHERE r.statut = :statut ORDER BY r.dateCreation DESC", 
                Reclamation.class)
                .setParameter("statut", statut)
                .getResultList();
        }
    }

    @Override
    public List<Reclamation> findByUrgence(Urgence urgence) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM Reclamation r WHERE r.urgence = :urgence ORDER BY r.dateCreation DESC", 
                Reclamation.class)
                .setParameter("urgence", urgence)
                .getResultList();
        }
    }

    @Override
    public List<Reclamation> findBySalleId(Long salleId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM Reclamation r WHERE r.salleId = :salleId ORDER BY r.dateCreation DESC", 
                Reclamation.class)
                .setParameter("salleId", salleId)
                .getResultList();
        }
    }

    @Override
    public List<Reclamation> findUrgentesEnAttente() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM Reclamation r WHERE r.urgence = :urgence AND r.statut = :statut ORDER BY r.dateCreation DESC", 
                Reclamation.class)
                .setParameter("urgence", Urgence.ELEVEE)
                .setParameter("statut", Statut.EN_ATTENTE)
                .getResultList();
        }
    }

    @Override
    public List<Reclamation> filterReclamations(Statut statut, Urgence urgence, Long userId, Long salleId) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM Reclamation r WHERE 1=1");
            
            if (statut != null) {
                hql.append(" AND r.statut = :statut");
            }
            if (urgence != null) {
                hql.append(" AND r.urgence = :urgence");
            }
            if (userId != null) {
                hql.append(" AND r.userId = :userId");
            }
            if (salleId != null) {
                hql.append(" AND r.salleId = :salleId");
            }
            
            hql.append(" ORDER BY r.dateCreation DESC");
            
            var query = session.createQuery(hql.toString(), Reclamation.class);
            
            if (statut != null) {
                query.setParameter("statut", statut);
            }
            if (urgence != null) {
                query.setParameter("urgence", urgence);
            }
            if (userId != null) {
                query.setParameter("userId", userId);
            }
            if (salleId != null) {
                query.setParameter("salleId", salleId);
            }
            
            return query.getResultList();
        }
    }

    @Override
    public long countByStatut(Statut statut) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "SELECT COUNT(r) FROM Reclamation r WHERE r.statut = :statut", 
                Long.class)
                .setParameter("statut", statut)
                .getSingleResult();
        }
    }

    @Override
    public long countUrgentesEnAttente() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "SELECT COUNT(r) FROM Reclamation r WHERE r.urgence = :urgence AND r.statut = :statut", 
                Long.class)
                .setParameter("urgence", Urgence.ELEVEE)
                .setParameter("statut", Statut.EN_ATTENTE)
                .getSingleResult();
        }
    }
}

