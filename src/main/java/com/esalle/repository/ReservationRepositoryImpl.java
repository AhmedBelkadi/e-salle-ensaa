package com.esalle.repository;

import com.esalle.entity.Reservation;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationRepositoryImpl extends BaseRepositoryImpl<Reservation, Long> implements ReservationRepository {

    public ReservationRepositoryImpl() {
        super(Reservation.class);
    }

    @Override
    public List<Reservation> findByUserId(Long userId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Reservation> query = session.createQuery(
                "FROM " + com.esalle.entity.Reservation.class.getName() + " WHERE userId = :userId ORDER BY dateReservation DESC, heureDebut", Reservation.class);
            query.setParameter("userId", userId);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> findBySalleId(Long salleId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Reservation> query = session.createQuery(
                "FROM " + com.esalle.entity.Reservation.class.getName() + " WHERE salleId = :salleId ORDER BY dateReservation DESC, heureDebut", Reservation.class);
            query.setParameter("salleId", salleId);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> findByStatut(Reservation.StatutReservation statut) {
        Session session = sessionFactory.openSession();
        try {
            Query<Reservation> query = session.createQuery(
                "FROM " + com.esalle.entity.Reservation.class.getName() + " WHERE statut = :statut ORDER BY dateReservation DESC", Reservation.class);
            query.setParameter("statut", statut);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> findEnAttenteOrderByPrioriteAndDateCreation() {
        Session session = sessionFactory.openSession();
        try {
            // FIFO: Trier par priorité (desc) puis par date de création (asc)
            Query<Reservation> query = session.createQuery(
                "FROM " + com.esalle.entity.Reservation.class.getName() + " WHERE statut = :statut ORDER BY priorite DESC, dateCreation ASC", Reservation.class);
            query.setParameter("statut", Reservation.StatutReservation.EN_ATTENTE);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> findConflitsSalle(Long salleId, LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        Session session = sessionFactory.openSession();
        try {
            // Trouver les conflits: réservations approuvées qui se chevauchent
            Query<Reservation> query = session.createQuery(
                "FROM " + com.esalle.entity.Reservation.class.getName() + " WHERE salleId = :salleId AND dateReservation = :date " +
                "AND statut = :statut " +
                "AND ((heureDebut < :heureFin AND heureFin > :heureDebut)) " +
                "ORDER BY heureDebut", Reservation.class);
            query.setParameter("salleId", salleId);
            query.setParameter("date", date);
            query.setParameter("statut", Reservation.StatutReservation.APPROUVEE);
            query.setParameter("heureDebut", heureDebut);
            query.setParameter("heureFin", heureFin);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> findByDateReservation(LocalDate date) {
        Session session = sessionFactory.openSession();
        try {
            Query<Reservation> query = session.createQuery(
                "FROM " + com.esalle.entity.Reservation.class.getName() + " WHERE dateReservation = :date ORDER BY heureDebut", Reservation.class);
            query.setParameter("date", date);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> findBySalleIdAndDateReservation(Long salleId, LocalDate date) {
        Session session = sessionFactory.openSession();
        try {
            Query<Reservation> query = session.createQuery(
                "FROM " + com.esalle.entity.Reservation.class.getName() + " WHERE salleId = :salleId AND dateReservation = :date " +
                "AND statut = :statut ORDER BY heureDebut", Reservation.class);
            query.setParameter("salleId", salleId);
            query.setParameter("date", date);
            query.setParameter("statut", Reservation.StatutReservation.APPROUVEE);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> findApprouveesBySalleId(Long salleId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Reservation> query = session.createQuery(
                "FROM " + com.esalle.entity.Reservation.class.getName() + " WHERE salleId = :salleId AND statut = :statut " +
                "ORDER BY dateReservation, heureDebut", Reservation.class);
            query.setParameter("salleId", salleId);
            query.setParameter("statut", Reservation.StatutReservation.APPROUVEE);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> findByTypeReservateur(Reservation.TypeReservateur type) {
        Session session = sessionFactory.openSession();
        try {
            Query<Reservation> query = session.createQuery(
                "FROM " + com.esalle.entity.Reservation.class.getName() + " WHERE typeReservateur = :type ORDER BY dateReservation DESC", Reservation.class);
            query.setParameter("type", type);
            return query.list();
        } finally {
            session.close();
        }
    }
}

