package com.esalle.repository;

import com.esalle.entity.EmploiDuTemps;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.time.LocalTime;
import java.util.List;

public class EmploiDuTempsRepositoryImpl extends BaseRepositoryImpl<EmploiDuTemps, Long> implements EmploiDuTempsRepository {

    public EmploiDuTempsRepositoryImpl() {
        super(EmploiDuTemps.class);
    }

    @Override
    public List<EmploiDuTemps> findByFiliereId(Long filiereId) {
        Session session = sessionFactory.openSession();
        try {
            Query<EmploiDuTemps> query = session.createQuery(
                "FROM EmploiDuTemps WHERE filiereId = :filiereId ORDER BY jourSemaine, heureDebut", EmploiDuTemps.class);
            query.setParameter("filiereId", filiereId);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<EmploiDuTemps> findByFiliereIdAndAnnee(Long filiereId, Integer annee) {
        Session session = sessionFactory.openSession();
        try {
            Query<EmploiDuTemps> query = session.createQuery(
                "FROM EmploiDuTemps WHERE filiereId = :filiereId AND annee = :annee ORDER BY jourSemaine, heureDebut", EmploiDuTemps.class);
            query.setParameter("filiereId", filiereId);
            query.setParameter("annee", annee);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<EmploiDuTemps> findByMatiereId(Long matiereId) {
        Session session = sessionFactory.openSession();
        try {
            Query<EmploiDuTemps> query = session.createQuery(
                "FROM EmploiDuTemps WHERE matiereId = :matiereId ORDER BY jourSemaine, heureDebut", EmploiDuTemps.class);
            query.setParameter("matiereId", matiereId);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<EmploiDuTemps> findByProfesseurId(Long professeurId) {
        Session session = sessionFactory.openSession();
        try {
            Query<EmploiDuTemps> query = session.createQuery(
                "FROM EmploiDuTemps WHERE professeurId = :professeurId ORDER BY jourSemaine, heureDebut", EmploiDuTemps.class);
            query.setParameter("professeurId", professeurId);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<EmploiDuTemps> findBySalleId(Long salleId) {
        Session session = sessionFactory.openSession();
        try {
            Query<EmploiDuTemps> query = session.createQuery(
                "FROM EmploiDuTemps WHERE salleId = :salleId ORDER BY jourSemaine, heureDebut", EmploiDuTemps.class);
            query.setParameter("salleId", salleId);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<EmploiDuTemps> findByJourSemaineAndSalleId(EmploiDuTemps.JourSemaine jour, Long salleId) {
        Session session = sessionFactory.openSession();
        try {
            Query<EmploiDuTemps> query = session.createQuery(
                "FROM EmploiDuTemps WHERE jourSemaine = :jour AND salleId = :salleId ORDER BY heureDebut", EmploiDuTemps.class);
            query.setParameter("jour", jour);
            query.setParameter("salleId", salleId);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<EmploiDuTemps> findConflitsSalle(Long salleId, EmploiDuTemps.JourSemaine jour, 
                                                  LocalTime heureDebut, LocalTime heureFin) {
        Session session = sessionFactory.openSession();
        try {
            // Trouver les conflits: les séances qui se chevauchent
            Query<EmploiDuTemps> query = session.createQuery(
                "FROM EmploiDuTemps WHERE salleId = :salleId AND jourSemaine = :jour " +
                "AND ((heureDebut < :heureFin AND heureFin > :heureDebut)) " +
                "ORDER BY heureDebut", EmploiDuTemps.class);
            query.setParameter("salleId", salleId);
            query.setParameter("jour", jour);
            query.setParameter("heureDebut", heureDebut);
            query.setParameter("heureFin", heureFin);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<EmploiDuTemps> findConflitsProfesseur(Long professeurId, EmploiDuTemps.JourSemaine jour, 
                                                      LocalTime heureDebut, LocalTime heureFin) {
        Session session = sessionFactory.openSession();
        try {
            // Trouver les conflits: les séances qui se chevauchent pour ce professeur
            Query<EmploiDuTemps> query = session.createQuery(
                "FROM EmploiDuTemps WHERE professeurId = :professeurId AND jourSemaine = :jour " +
                "AND ((heureDebut < :heureFin AND heureFin > :heureDebut)) " +
                "ORDER BY heureDebut", EmploiDuTemps.class);
            query.setParameter("professeurId", professeurId);
            query.setParameter("jour", jour);
            query.setParameter("heureDebut", heureDebut);
            query.setParameter("heureFin", heureFin);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<EmploiDuTemps> findByGroupe(String groupe) {
        Session session = sessionFactory.openSession();
        try {
            Query<EmploiDuTemps> query = session.createQuery(
                "FROM EmploiDuTemps WHERE groupe = :groupe ORDER BY jourSemaine, heureDebut", EmploiDuTemps.class);
            query.setParameter("groupe", groupe);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<EmploiDuTemps> findByFiliereIdAndAnneeAndGroupe(Long filiereId, Integer annee, String groupe) {
        Session session = sessionFactory.openSession();
        try {
            Query<EmploiDuTemps> query = session.createQuery(
                "FROM EmploiDuTemps WHERE filiereId = :filiereId AND annee = :annee AND groupe = :groupe " +
                "ORDER BY jourSemaine, heureDebut", EmploiDuTemps.class);
            query.setParameter("filiereId", filiereId);
            query.setParameter("annee", annee);
            query.setParameter("groupe", groupe);
            return query.list();
        } finally {
            session.close();
        }
    }
}

