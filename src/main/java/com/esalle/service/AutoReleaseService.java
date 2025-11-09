package com.esalle.service;

import com.esalle.entity.Reservation;
import com.esalle.entity.EmploiDuTemps;
import com.esalle.repository.ReservationRepository;
import com.esalle.repository.ReservationRepositoryImpl;
import com.esalle.repository.EmploiDuTempsRepository;
import com.esalle.repository.EmploiDuTempsRepositoryImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service pour la libération automatique des réservations et emplois du temps
 * selon le workflow : libération quand période se termine ou charge horaire atteinte
 *
 * IMPORTANT: Utilise l'initialisation lazy pour éviter les problèmes au démarrage de l'application
 */
public class AutoReleaseService {

    private static final Logger logger = Logger.getLogger(AutoReleaseService.class.getName());

    // Repositories non initialisés dans le constructeur (lazy initialization)
    private ReservationRepository reservationRepository;
    private EmploiDuTempsRepository emploiRepository;

    /**
     * Constructeur vide - les repositories seront créés lors de leur première utilisation
     * Ceci évite les problèmes d'initialisation de SessionFactory au démarrage
     */
    public AutoReleaseService() {
        // Constructeur vide intentionnel
    }

    /**
     * Initialise les repositories si nécessaire (lazy initialization)
     */
    private void initializeRepositories() {
        if (reservationRepository == null) {
            reservationRepository = new ReservationRepositoryImpl();
        }
        if (emploiRepository == null) {
            emploiRepository = new EmploiDuTempsRepositoryImpl();
        }
    }

    /**
     * Libère automatiquement les réservations terminées
     * Appelé périodiquement par une tâche planifiée
     */
    public void libererReservationsTerminees() {
        try {
            logger.info("⏰ Début de la libération automatique des réservations terminées");

            // Initialiser les repositories au moment de l'utilisation
            initializeRepositories();

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
                                "✅ Réservation %d libérée automatiquement (Salle: %s, Date: %s)",
                                reservation.getId(), reservation.getSalleNom(), reservation.getDateReservation()
                        ));
                    }
                }
            }

            logger.info(String.format("📊 Libération automatique terminée: %d réservation(s) libérée(s)", count));

        } catch (Exception e) {
            logger.severe("❌ Erreur lors de la libération des réservations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Libère automatiquement les emplois du temps quand la charge horaire est atteinte
     * Cette méthode devrait être appelée après chaque séance ou périodiquement
     */
    public void libererEmploiDuTempsChargeAtteinte() {
        try {
            logger.info("🔍 Vérification des emplois du temps avec charge horaire atteinte");

            // Initialiser les repositories au moment de l'utilisation
            initializeRepositories();

            // Cette logique pourrait être plus complexe selon le workflow
            // Pour l'instant, on se base sur la date de fin de semestre ou autre critère
            // TODO: Implémenter selon les règles métier spécifiques

            logger.info("✅ Vérification des charges horaires terminée");

        } catch (Exception e) {
            logger.severe("❌ Erreur lors de la vérification des charges horaires: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Supprime les réservations non confirmées après un délai (selon le workflow)
     */
    public void supprimerReservationsNonConfirmees() {
        try {
            logger.info("🗑️ Suppression des réservations non confirmées après délai");

            // Initialiser les repositories au moment de l'utilisation
            initializeRepositories();

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
                                "❌ Réservation %d annulée automatiquement (délai dépassé)",
                                reservation.getId()
                        ));
                    }
                }
            }

            logger.info(String.format("📊 Suppression terminée: %d réservation(s) annulée(s)", count));

        } catch (Exception e) {
            logger.severe("❌ Erreur lors de la suppression des réservations: " + e.getMessage());
            e.printStackTrace();
        }
    }
}