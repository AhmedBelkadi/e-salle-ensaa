package com.esalle.service;

import com.esalle.entity.Reservation;
import com.esalle.entity.Salle;
import com.esalle.entity.User;
import com.esalle.entity.EmploiDuTemps;
import com.esalle.repository.ReservationRepository;
import com.esalle.repository.ReservationRepositoryImpl;
import com.esalle.repository.SalleRepository;
import com.esalle.repository.SalleRepositoryImpl;
import com.esalle.repository.UserRepository;
import com.esalle.repository.UserRepositoryImpl;
import com.esalle.service.EmploiDuTempsService;
import com.esalle.service.EmploiDuTempsServiceImpl;
import com.esalle.exception.BusinessException;
import com.esalle.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final SalleRepository salleRepository;
    private final UserRepository userRepository;
    private final EmploiDuTempsService emploiDuTempsService;

    public ReservationServiceImpl() {
        this.reservationRepository = new ReservationRepositoryImpl();
        this.salleRepository = new SalleRepositoryImpl();
        this.userRepository = new UserRepositoryImpl();
        this.emploiDuTempsService = new EmploiDuTempsServiceImpl();
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        // Validation
        if (reservation.getUserId() == null) {
            throw new BusinessException("L'utilisateur est obligatoire.");
        }
        if (reservation.getSalleId() == null) {
            throw new BusinessException("La salle est obligatoire.");
        }
        if (reservation.getDateReservation() == null) {
            throw new BusinessException("La date de réservation est obligatoire.");
        }
        if (reservation.getHeureDebut() == null || reservation.getHeureFin() == null) {
            throw new BusinessException("Les heures de début et de fin sont obligatoires.");
        }
        if (reservation.getDateReservation().isBefore(LocalDate.now())) {
            throw new BusinessException("La date de réservation ne peut pas être dans le passé.");
        }
        if (reservation.getHeureDebut().isAfter(reservation.getHeureFin()) || 
            reservation.getHeureDebut().equals(reservation.getHeureFin())) {
            throw new BusinessException("L'heure de début doit être antérieure à l'heure de fin.");
        }

        // Vérifier que l'utilisateur existe
        User user = userRepository.findById(reservation.getUserId())
                .orElseThrow(() -> new BusinessException("L'utilisateur spécifié n'existe pas."));
        reservation.setUserNom(user.getNom() + " " + user.getPrenom());

        // Déterminer le type de réservateur et la priorité
        if (user.getRole() == User.UserRole.PROFESSEUR || user.getRole() == User.UserRole.COORDINATEUR) {
            reservation.setTypeReservateur(Reservation.TypeReservateur.PROFESSEUR);
            reservation.setPriorite(100); // Professeurs et coordinateurs ont la même priorité
        } else if (user.getRole() == User.UserRole.MEMBRE_CLUB) {
            reservation.setTypeReservateur(Reservation.TypeReservateur.MEMBRE_CLUB);
            reservation.setPriorite(50);
        } else {
            throw new BusinessException("Seuls les professeurs, coordinateurs et membres de club peuvent réserver des salles.");
        }

        // Vérifier que la salle existe
        Salle salle = salleRepository.findById(reservation.getSalleId())
                .orElseThrow(() -> new BusinessException("La salle spécifiée n'existe pas."));
        reservation.setSalleNom(salle.getNom());
        reservation.setTypeSalle(convertTypeSalle(salle.getType()));

        // Vérifier le blocage TP (les membres de club ne peuvent pas réserver les salles TP)
        if (salle.getType() == Salle.TypeSalle.TP && 
            reservation.getTypeReservateur() == Reservation.TypeReservateur.MEMBRE_CLUB &&
            !reservation.getEstLiberationExceptionnelle()) {
            throw new BusinessException("Les salles TP ne peuvent pas être réservées par les clubs (sauf libération exceptionnelle).");
        }

        // Vérifier les conflits avec les réservations existantes
        if (verifierConflitSalle(reservation.getSalleId(), reservation.getDateReservation(),
                                reservation.getHeureDebut(), reservation.getHeureFin(), null)) {
            throw new BusinessException("Conflit d'horaire : la salle est déjà réservée à cet horaire.");
        }

        // Vérifier les conflits avec l'emploi du temps (pour PROFESSEUR et COORDINATEUR uniquement)
        if (user.getRole() == User.UserRole.PROFESSEUR || user.getRole() == User.UserRole.COORDINATEUR) {
            if (verifierConflitEmploiDuTemps(reservation.getSalleId(), reservation.getDateReservation(),
                                            reservation.getHeureDebut(), reservation.getHeureFin())) {
                throw new BusinessException("Conflit d'horaire : la salle est déjà utilisée dans l'emploi du temps à cet horaire.");
            }
        }

        reservation.setStatut(Reservation.StatutReservation.EN_ATTENTE);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation approveReservation(Long id, Long adminId, String commentaire) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Réservation introuvable."));
        
        if (reservation.getStatut() != Reservation.StatutReservation.EN_ATTENTE) {
            throw new BusinessException("Seules les réservations en attente peuvent être approuvées.");
        }

        // Vérifier à nouveau les conflits (au cas où)
        if (verifierConflitSalle(reservation.getSalleId(), reservation.getDateReservation(),
                                reservation.getHeureDebut(), reservation.getHeureFin(), id)) {
            throw new BusinessException("Conflit d'horaire : la salle est déjà réservée à cet horaire.");
        }

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new BusinessException("L'administrateur spécifié n'existe pas."));
        
        reservation.setStatut(Reservation.StatutReservation.APPROUVEE);
        reservation.setTraiteParId(adminId);
        reservation.setTraiteParNom(admin.getNom() + " " + admin.getPrenom());
        reservation.setCommentaire(commentaire);

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation refuseReservation(Long id, Long adminId, String commentaire) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Réservation introuvable."));
        
        if (reservation.getStatut() != Reservation.StatutReservation.EN_ATTENTE) {
            throw new BusinessException("Seules les réservations en attente peuvent être refusées.");
        }

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new BusinessException("L'administrateur spécifié n'existe pas."));
        
        reservation.setStatut(Reservation.StatutReservation.REFUSEE);
        reservation.setTraiteParId(adminId);
        reservation.setTraiteParNom(admin.getNom() + " " + admin.getPrenom());
        reservation.setCommentaire(commentaire);

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation cancelReservation(Long id, Long userId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Réservation introuvable."));
        
        if (!reservation.getUserId().equals(userId)) {
            throw new BusinessException("Vous ne pouvez annuler que vos propres réservations.");
        }
        
        if (reservation.getStatut() == Reservation.StatutReservation.TERMINEE) {
            throw new BusinessException("Une réservation terminée ne peut pas être annulée.");
        }

        reservation.setStatut(Reservation.StatutReservation.ANNULEE);
        return reservationRepository.save(reservation);
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isPresent()) {
            enrichirReservation(reservationOpt.get());
        }
        return reservationOpt;
    }

    @Override
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        enrichirReservations(reservations);
        return reservations;
    }

    @Override
    public List<Reservation> getReservationsByUser(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        enrichirReservations(reservations);
        return reservations;
    }

    @Override
    public List<Reservation> getReservationsEnAttente() {
        List<Reservation> reservations = reservationRepository.findEnAttenteOrderByPrioriteAndDateCreation();
        enrichirReservations(reservations);
        return reservations;
    }

    @Override
    public List<Reservation> getReservationsBySalle(Long salleId) {
        List<Reservation> reservations = reservationRepository.findBySalleId(salleId);
        enrichirReservations(reservations);
        return reservations;
    }

    @Override
    public List<Reservation> getReservationsByDate(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByDateReservation(date);
        enrichirReservations(reservations);
        return reservations;
    }

    @Override
    public boolean verifierConflitSalle(Long salleId, LocalDate date, LocalTime heureDebut, 
                                       LocalTime heureFin, Long excludeId) {
        List<Reservation> conflits = reservationRepository.findConflitsSalle(salleId, date, heureDebut, heureFin);
        if (excludeId != null) {
            conflits.removeIf(r -> r.getId().equals(excludeId));
        }
        return !conflits.isEmpty();
    }

    @Override
    public List<Salle> trouverSallesAlternatives(Long salleId, LocalDate date, 
                                                 LocalTime heureDebut, LocalTime heureFin, Integer capaciteMin) {
        Salle salleOriginale = salleRepository.findById(salleId)
                .orElseThrow(() -> new BusinessException("La salle spécifiée n'existe pas."));
        
        List<Salle> sallesDisponibles = salleRepository.findByType(salleOriginale.getType())
                .stream()
                .filter(s -> s.isDisponible())
                .filter(s -> capaciteMin == null || s.getCapacite() >= capaciteMin)
                .filter(s -> !s.getId().equals(salleId))
                .filter(s -> !verifierConflitSalle(s.getId(), date, heureDebut, heureFin, null))
                .collect(Collectors.toList());
        
        return sallesDisponibles;
    }

    @Override
    public Reservation proposerSalleAlternative(Long reservationId, Long salleAlternativeId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException("Réservation introuvable."));
        
        Salle salleAlternative = salleRepository.findById(salleAlternativeId)
                .orElseThrow(() -> new BusinessException("La salle alternative spécifiée n'existe pas."));
        
        reservation.setSalleAlternativeId(salleAlternativeId);
        reservation.setSalleAlternativeNom(salleAlternative.getNom());
        
        return reservationRepository.save(reservation);
    }

    /**
     * Enrichit une liste de réservations avec les noms de salle et d'utilisateur
     */
    private void enrichirReservations(List<Reservation> reservations) {
        if (reservations == null) {
            return;
        }
        for (Reservation reservation : reservations) {
            enrichirReservation(reservation);
        }
    }

    /**
     * Enrichit une réservation avec les noms de salle et d'utilisateur si manquants
     */
    private void enrichirReservation(Reservation reservation) {
        if (reservation == null) {
            return;
        }
        
        // Enrichir le nom de l'utilisateur si manquant
        if (reservation.getUserNom() == null || reservation.getUserNom().trim().isEmpty()) {
            if (reservation.getUserId() != null) {
                userRepository.findById(reservation.getUserId()).ifPresent(user -> {
                    reservation.setUserNom(user.getNom() + " " + user.getPrenom());
                });
            }
        }
        
        // Enrichir le nom de la salle si manquant
        if (reservation.getSalleNom() == null || reservation.getSalleNom().trim().isEmpty()) {
            if (reservation.getSalleId() != null) {
                salleRepository.findById(reservation.getSalleId()).ifPresent(salle -> {
                    reservation.setSalleNom(salle.getNom());
                    if (reservation.getTypeSalle() == null) {
                        reservation.setTypeSalle(convertTypeSalle(salle.getType()));
                    }
                });
            }
        }
    }

    private Reservation.TypeSalleReservation convertTypeSalle(Salle.TypeSalle type) {
        switch (type) {
            case COURS:
                return Reservation.TypeSalleReservation.COURS;
            case TD:
                return Reservation.TypeSalleReservation.TD;
            case TP:
                return Reservation.TypeSalleReservation.TP;
            default:
                return Reservation.TypeSalleReservation.COURS;
        }
    }

    /**
     * Vérifie si une réservation entre en conflit avec l'emploi du temps
     * @param salleId ID de la salle
     * @param date Date de la réservation
     * @param heureDebut Heure de début
     * @param heureFin Heure de fin
     * @return true si conflit, false sinon
     */
    private boolean verifierConflitEmploiDuTemps(Long salleId, LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        // Convertir la date en jour de la semaine
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        EmploiDuTemps.JourSemaine jourSemaine = convertDayOfWeekToJourSemaine(dayOfWeek);
        
        // Si c'est dimanche, il n'y a pas de cours dans l'emploi du temps
        if (jourSemaine == null) {
            return false;
        }
        
        // Vérifier les conflits avec l'emploi du temps
        return emploiDuTempsService.verifierConflitSalle(salleId, jourSemaine, heureDebut, heureFin, null);
    }

    /**
     * Convertit DayOfWeek en JourSemaine de l'emploi du temps
     */
    private EmploiDuTemps.JourSemaine convertDayOfWeekToJourSemaine(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return EmploiDuTemps.JourSemaine.LUNDI;
            case TUESDAY:
                return EmploiDuTemps.JourSemaine.MARDI;
            case WEDNESDAY:
                return EmploiDuTemps.JourSemaine.MERCREDI;
            case THURSDAY:
                return EmploiDuTemps.JourSemaine.JEUDI;
            case FRIDAY:
                return EmploiDuTemps.JourSemaine.VENDREDI;
            case SATURDAY:
                return EmploiDuTemps.JourSemaine.SAMEDI;
            default:
                // Dimanche n'est pas un jour de cours
                return null;
        }
    }
}