package com.esalle.service;

import com.esalle.entity.Reservation;
import com.esalle.entity.Salle;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationService {
    Reservation createReservation(Reservation reservation);
    Reservation approveReservation(Long id, Long adminId, String commentaire);
    Reservation refuseReservation(Long id, Long adminId, String commentaire);
    Reservation cancelReservation(Long id, Long userId);
    Optional<Reservation> getReservationById(Long id);
    List<Reservation> getAllReservations();
    List<Reservation> getReservationsByUser(Long userId);
    List<Reservation> getReservationsEnAttente();
    List<Reservation> getReservationsBySalle(Long salleId);
    List<Reservation> getReservationsByDate(LocalDate date);
    boolean verifierConflitSalle(Long salleId, LocalDate date, LocalTime heureDebut, LocalTime heureFin, Long excludeId);
    List<Salle> trouverSallesAlternatives(Long salleId, LocalDate date, LocalTime heureDebut, LocalTime heureFin, Integer capaciteMin);
    Reservation proposerSalleAlternative(Long reservationId, Long salleAlternativeId);
}

