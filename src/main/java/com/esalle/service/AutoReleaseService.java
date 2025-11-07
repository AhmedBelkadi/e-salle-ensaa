package com.esalle.service;

import com.esalle.entity.Reservation;
import com.esalle.entity.EmploiDuTemps;
import com.esalle.repository.ReservationRepository;
import com.esalle.repository.ReservationRepositoryImpl;
import com.esalle.repository.EmploiDuTempsRepository;
import com.esalle.repository.EmploiDuTempsRepositoryImpl;
import com.esalle.service.NotificationService;
import com.esalle.service.NotificationServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service pour la libération automatique des réservations et emplois du temps
 * selon le workflow : libération quand période se termine ou charge horaire atteinte
 */
public class AutoReleaseService {
    
    private static final Logger logger = Logger.getLogger(AutoReleaseService.class.getName());
    private final ReservationRepository reservationRepository;
    private final EmploiDuTempsRepository emploiRepository;
    private final NotificationService notificationService;
    
    public AutoReleaseService() {
        this.reservationRepository = new ReservationRepositoryImpl();
        this.emploiRepository = new EmploiDuTempsRepositoryImpl();
        this.notificationService = new NotificationServiceImpl();
    }
    
    /**
     * Libère automatiquement les réservations terminées
     * Appelé périodiquement par une tâche planifiée
     */
    public void libererReservationsTerminees() {
        logger.info("Début de la libération automatique des réservations terminées");
        
        LocalDate aujourdhui = LocalDate.now();
        LocalTime heureActuelle = LocalTime.now();
        
        // Récupérer toutes les réservations approuvées qui ne sont pas encore terminées
        List<Reservation> reservations = reservationRepository.findAll();
        
        int count = 0;
        for (Reservation reservation : reservations) {
            if (reservation.getStatut() == Reservation.StatutReservation.APPROUVEE) {
                // Vérifier si la réservation est passée
                if (reservation.isPassee()) {
                    // Marquer comme terminée
                    reservation.setStatut(Reservation.StatutReservation.TERMINEE);
                    reservationRepository.save(reservation);
                    count++;
                    
                    logger.info(String.format(
                        "Réservation %d libérée automatiquement (Salle: %s, Date: %s)",
                        reservation.getId(), reservation.getSalleNom(), reservation.getDateReservation()
                    ));
                    
                    // TODO: Notifier le responsable et le professeur
                    // notificationService.notifyReservationReleased(...);
                }
            }
        }
        
        logger.info(String.format("Libération automatique terminée: %d réservations libérées", count));
    }
    
    /**
     * Libère automatiquement les emplois du temps quand la charge horaire est atteinte
     * Cette méthode devrait être appelée après chaque séance ou périodiquement
     */
    public void libererEmploiDuTempsChargeAtteinte() {
        logger.info("Vérification des emplois du temps avec charge horaire atteinte");
        
        // Cette logique pourrait être plus complexe selon le workflow
        // Pour l'instant, on se base sur la date de fin de semestre ou autre critère
        // TODO: Implémenter selon les règles métier spécifiques
        
        logger.info("Vérification des charges horaires terminée");
    }
    
    /**
     * Supprime les réservations non confirmées après un délai (selon le workflow)
     */
    public void supprimerReservationsNonConfirmees() {
        logger.info("Suppression des réservations non confirmées après délai");
        
        LocalDateTime limite = LocalDateTime.now().minusHours(24); // 24h de délai
        
        List<Reservation> reservations = reservationRepository.findAll();
        int count = 0;
        
        for (Reservation reservation : reservations) {
            if (reservation.getStatut() == Reservation.StatutReservation.EN_ATTENTE) {
                if (reservation.getDateCreation() != null && 
                    reservation.getDateCreation().isBefore(limite)) {
                    
                    reservation.setStatut(Reservation.StatutReservation.ANNULEE);
                    reservationRepository.save(reservation);
                    count++;
                    
                    logger.info(String.format(
                        "Réservation %d annulée automatiquement (délai dépassé)",
                        reservation.getId()
                    ));
                    
                    // TODO: Notifier l'utilisateur
                }
            }
        }
        
        logger.info(String.format("Suppression terminée: %d réservations annulées", count));
    }
}

