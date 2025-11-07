package com.esalle.repository;

import com.esalle.entity.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends BaseRepository<Reservation, Long> {
    
    /**
     * Trouver toutes les réservations d'un utilisateur
     */
    List<Reservation> findByUserId(Long userId);
    
    /**
     * Trouver toutes les réservations d'une salle
     */
    List<Reservation> findBySalleId(Long salleId);
    
    /**
     * Trouver toutes les réservations par statut
     */
    List<Reservation> findByStatut(Reservation.StatutReservation statut);
    
    /**
     * Trouver toutes les réservations en attente, triées par priorité (FIFO)
     */
    List<Reservation> findEnAttenteOrderByPrioriteAndDateCreation();
    
    /**
     * Vérifier les conflits horaires pour une salle
     */
    List<Reservation> findConflitsSalle(Long salleId, LocalDate date, LocalTime heureDebut, LocalTime heureFin);
    
    /**
     * Trouver toutes les réservations pour une date
     */
    List<Reservation> findByDateReservation(LocalDate date);
    
    /**
     * Trouver toutes les réservations d'une salle pour une date
     */
    List<Reservation> findBySalleIdAndDateReservation(Long salleId, LocalDate date);
    
    /**
     * Trouver toutes les réservations approuvées d'une salle
     */
    List<Reservation> findApprouveesBySalleId(Long salleId);
    
    /**
     * Trouver toutes les réservations par type de réservateur
     */
    List<Reservation> findByTypeReservateur(Reservation.TypeReservateur type);
}

