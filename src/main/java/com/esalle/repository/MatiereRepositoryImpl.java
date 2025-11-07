package com.esalle.repository;

import com.esalle.entity.Matiere;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class MatiereRepositoryImpl extends BaseRepositoryImpl<Matiere, Long> implements MatiereRepository {

    public MatiereRepositoryImpl() {
        super(Matiere.class);
    }

    @Override
    public List<Matiere> findByFiliereId(Long filiereId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Matiere> query = session.createQuery(
                "FROM Matiere WHERE filiereId = :filiereId ORDER BY nom", Matiere.class);
            query.setParameter("filiereId", filiereId);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Matiere> findByProfesseurId(Long professeurId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Matiere> query = session.createQuery(
                "FROM Matiere WHERE professeurId = :professeurId ORDER BY nom", Matiere.class);
            query.setParameter("professeurId", professeurId);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<Matiere> findByNomAndFiliereId(String nom, Long filiereId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Matiere> query = session.createQuery(
                "FROM Matiere WHERE nom = :nom AND filiereId = :filiereId", Matiere.class);
            query.setParameter("nom", nom);
            query.setParameter("filiereId", filiereId);
            Matiere matiere = query.uniqueResult();
            return Optional.ofNullable(matiere);
        } finally {
            session.close();
        }
    }

    @Override
    public boolean existsByNomAndFiliereId(String nom, Long filiereId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Matiere WHERE nom = :nom AND filiereId = :filiereId", Long.class);
            query.setParameter("nom", nom);
            query.setParameter("filiereId", filiereId);
            return query.uniqueResult() > 0;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Matiere> searchByNom(String keyword) {
        Session session = sessionFactory.openSession();
        try {
            Query<Matiere> query = session.createQuery(
                "FROM Matiere WHERE nom LIKE :keyword ORDER BY nom", Matiere.class);
            query.setParameter("keyword", "%" + keyword + "%");
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Matiere> findByHeuresTPGreaterThan(Integer heuresTP) {
        Session session = sessionFactory.openSession();
        try {
            Query<Matiere> query = session.createQuery(
                "FROM Matiere WHERE heuresTP > :heuresTP ORDER BY nom", Matiere.class);
            query.setParameter("heuresTP", heuresTP);
            return query.list();
        } finally {
            session.close();
        }
    }
}

