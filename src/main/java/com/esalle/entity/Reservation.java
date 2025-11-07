package com.esalle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entité représentant une Réservation de salle.
 * 
 * Gère les réservations avec priorités (PROF=100, CLUB=50), FIFO,
 * vérifications de conflits, blocage TP, et alternatives.
 */
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_nom", length = 200)
    private String userNom;

    @Column(name = "user_role", length = 50)
    @Enumerated(EnumType.STRING)
    private TypeReservateur typeReservateur;

    @Column(name = "salle_id", nullable = false)
    private Long salleId;

    @Column(name = "salle_nom", length = 100)
    private String salleNom;

    @Column(name = "salle_type", length = 20)
    @Enumerated(EnumType.STRING)
    private TypeSalleReservation typeSalle;

    @NotNull(message = "La date de réservation est obligatoire")
    @Column(name = "date_reservation", nullable = false)
    private LocalDate dateReservation;

    @NotNull(message = "L'heure de début est obligatoire")
    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est obligatoire")
    @Column(name = "heure_fin", nullable = false)
    private LocalTime heureFin;

    @Column(name = "motif", nullable = false, length = 500)
    private String motif;

    @Column(name = "priorite", nullable = false)
    private Integer priorite; // PROF=100, CLUB=50

    @Column(name = "statut", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StatutReservation statut = StatutReservation.EN_ATTENTE;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_traitement")
    private LocalDateTime dateTraitement;

    @Column(name = "traite_par")
    private Long traiteParId;

    @Column(name = "traite_par_nom", length = 200)
    private String traiteParNom;

    @Column(name = "salle_alternative_id")
    private Long salleAlternativeId;

    @Column(name = "salle_alternative_nom", length = 100)
    private String salleAlternativeNom;

    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;

    @Column(name = "est_liberation_exceptionnelle")
    private Boolean estLiberationExceptionnelle = false;

    /**
     * Enum pour le type de réservateur
     */
    public enum TypeReservateur {
        PROFESSEUR,    // Priorité 100
        MEMBRE_CLUB    // Priorité 50
    }

    /**
     * Enum pour le type de salle (référence à Salle.TypeSalle)
     */
    public enum TypeSalleReservation {
        COURS, TD, TP
    }

    /**
     * Enum pour le statut de réservation
     */
    public enum StatutReservation {
        EN_ATTENTE,      // En attente de validation
        APPROUVEE,       // Approuvée et confirmée
        REFUSEE,         // Refusée
        ANNULEE,         // Annulée par le réservateur
        TERMINEE         // Réservation terminée
    }

    // Constructors
    public Reservation() {
        this.dateCreation = LocalDateTime.now();
        this.statut = StatutReservation.EN_ATTENTE;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        if (statut == null) {
            statut = StatutReservation.EN_ATTENTE;
        }
        calculatePriorite();
    }

    @PreUpdate
    protected void onUpdate() {
        if (statut == StatutReservation.APPROUVEE && dateTraitement == null) {
            dateTraitement = LocalDateTime.now();
        }
    }

    // Business methods

    /**
     * Calcule la priorité selon le type de réservateur
     */
    private void calculatePriorite() {
        if (typeReservateur == TypeReservateur.PROFESSEUR) {
            this.priorite = 100;
        } else if (typeReservateur == TypeReservateur.MEMBRE_CLUB) {
            this.priorite = 50;
        } else {
            this.priorite = 0;
        }
    }

    /**
     * Vérifie si la réservation est pour une salle TP
     */
    public boolean isSalleTP() {
        return TypeSalleReservation.TP.equals(typeSalle);
    }

    /**
     * Vérifie si la réservation est approuvée
     */
    public boolean isApprouvee() {
        return StatutReservation.APPROUVEE.equals(statut);
    }

    /**
     * Vérifie si la réservation est en attente
     */
    public boolean isEnAttente() {
        return StatutReservation.EN_ATTENTE.equals(statut);
    }

    /**
     * Retourne la durée en heures
     */
    public Double getDureeHeures() {
        if (heureDebut != null && heureFin != null) {
            long minutes = java.time.Duration.between(heureDebut, heureFin).toMinutes();
            return minutes / 60.0;
        }
        return 0.0;
    }

    /**
     * Vérifie si la réservation est passée
     */
    public boolean isPassee() {
        if (dateReservation == null) return false;
        LocalDate aujourdhui = LocalDate.now();
        if (dateReservation.isBefore(aujourdhui)) return true;
        if (dateReservation.equals(aujourdhui) && heureFin != null) {
            return heureFin.isBefore(LocalTime.now());
        }
        return false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserNom() {
        return userNom;
    }

    public void setUserNom(String userNom) {
        this.userNom = userNom;
    }

    public TypeReservateur getTypeReservateur() {
        return typeReservateur;
    }

    public void setTypeReservateur(TypeReservateur typeReservateur) {
        this.typeReservateur = typeReservateur;
        calculatePriorite();
    }

    public Long getSalleId() {
        return salleId;
    }

    public void setSalleId(Long salleId) {
        this.salleId = salleId;
    }

    public String getSalleNom() {
        return salleNom;
    }

    public void setSalleNom(String salleNom) {
        this.salleNom = salleNom;
    }

    public TypeSalleReservation getTypeSalle() {
        return typeSalle;
    }

    public void setTypeSalle(TypeSalleReservation typeSalle) {
        this.typeSalle = typeSalle;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Integer getPriorite() {
        return priorite;
    }

    public void setPriorite(Integer priorite) {
        this.priorite = priorite;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
        if (statut == StatutReservation.APPROUVEE && dateTraitement == null) {
            dateTraitement = LocalDateTime.now();
        }
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateTraitement() {
        return dateTraitement;
    }

    public void setDateTraitement(LocalDateTime dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public Long getTraiteParId() {
        return traiteParId;
    }

    public void setTraiteParId(Long traiteParId) {
        this.traiteParId = traiteParId;
    }

    public String getTraiteParNom() {
        return traiteParNom;
    }

    public void setTraiteParNom(String traiteParNom) {
        this.traiteParNom = traiteParNom;
    }

    public Long getSalleAlternativeId() {
        return salleAlternativeId;
    }

    public void setSalleAlternativeId(Long salleAlternativeId) {
        this.salleAlternativeId = salleAlternativeId;
    }

    public String getSalleAlternativeNom() {
        return salleAlternativeNom;
    }

    public void setSalleAlternativeNom(String salleAlternativeNom) {
        this.salleAlternativeNom = salleAlternativeNom;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Boolean getEstLiberationExceptionnelle() {
        return estLiberationExceptionnelle;
    }

    public void setEstLiberationExceptionnelle(Boolean estLiberationExceptionnelle) {
        this.estLiberationExceptionnelle = estLiberationExceptionnelle;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", salleNom='" + salleNom + '\'' +
                ", dateReservation=" + dateReservation +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", statut=" + statut +
                ", priorite=" + priorite +
                '}';
    }
}

