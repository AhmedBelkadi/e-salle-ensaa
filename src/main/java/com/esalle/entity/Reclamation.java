package com.esalle.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité représentant une Réclamation.
 * 
 * Permet aux utilisateurs de signaler des problèmes concernant les salles.
 * Workflow: EN_ATTENTE → TRAITEE
 * Notifications envoyées à l'admin lors de création et au user lors du traitement.
 */
@Entity
@Table(name = "reclamations")
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_nom", length = 200)
    private String userNom;

    @Column(name = "salle_id", nullable = false)
    private Long salleId;

    @Column(name = "salle_nom", length = 100)
    private String salleNom;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Urgence urgence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Statut statut = Statut.EN_ATTENTE;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_traitement")
    private LocalDateTime dateTraitement;

    @Column(name = "traite_par")
    private Long traiteParId;

    @Column(name = "traite_par_nom", length = 200)
    private String traiteParNom;

    @Column(name = "commentaire_traitement", columnDefinition = "TEXT")
    private String commentaireTraitement;

    /**
     * Enum pour les niveaux d'urgence
     */
    public enum Urgence {
        FAIBLE,   // Urgence faible (ex: confort)
        MOYEN,    // Urgence moyenne (ex: matériel défectueux)
        URGENT    // Urgence élevée (ex: sécurité, urgent)
    }

    /**
     * Enum pour les statuts de traitement
     */
    public enum Statut {
        EN_ATTENTE,  // En attente de traitement
        TRAITEE      // Traitée par un admin
    }

    // Constructors
    public Reclamation() {
        this.dateCreation = LocalDateTime.now();
        this.statut = Statut.EN_ATTENTE;
    }

    public Reclamation(Long userId, Long salleId, String description, Urgence urgence) {
        this();
        this.userId = userId;
        this.salleId = salleId;
        this.description = description;
        this.urgence = urgence;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }
        if (statut == null) {
            statut = Statut.EN_ATTENTE;
        }
    }

    // Business methods

    /**
     * Vérifie si la réclamation est en attente de traitement
     */
    public boolean isEnAttente() {
        return statut == Statut.EN_ATTENTE;
    }

    /**
     * Vérifie si la réclamation a été traitée
     */
    public boolean isTraitee() {
        return statut == Statut.TRAITEE;
    }

    /**
     * Vérifie si la réclamation est urgente (URGENT)
     */
    public boolean isUrgente() {
        return urgence == Urgence.URGENT;
    }

    /**
     * Retourne le libellé de l'urgence en français
     */
    public String getUrgenceLibelle() {
        switch (urgence) {
            case FAIBLE:
                return "Faible";
            case MOYEN:
                return "Moyen";
            case URGENT:
                return "Urgent";
            default:
                return urgence.toString();
        }
    }

    /**
     * Retourne le libellé du statut en français
     */
    public String getStatutLibelle() {
        switch (statut) {
            case EN_ATTENTE:
                return "En attente";
            case TRAITEE:
                return "Traitée";
            default:
                return statut.toString();
        }
    }

    /**
     * Retourne la classe CSS pour l'urgence (pour le badge)
     */
    public String getUrgenceCssClass() {
        switch (urgence) {
            case FAIBLE:
                return "bg-green-100 text-green-800";
            case MOYEN:
                return "bg-yellow-100 text-yellow-800";
            case URGENT:
                return "bg-red-100 text-red-800";
            default:
                return "bg-gray-100 text-gray-800";
        }
    }

    /**
     * Retourne la classe CSS pour le statut (pour le badge)
     */
    public String getStatutCssClass() {
        switch (statut) {
            case EN_ATTENTE:
                return "bg-orange-100 text-orange-800";
            case TRAITEE:
                return "bg-green-100 text-green-800";
            default:
                return "bg-gray-100 text-gray-800";
        }
    }

    /**
     * Calcule le délai de traitement en heures
     */
    public Long getDelaiTraitementHeures() {
        if (dateTraitement != null && dateCreation != null) {
            return java.time.Duration.between(dateCreation, dateTraitement).toHours();
        }
        return null;
    }

    /**
     * Formate la date de création (ex: "26/10/2025 à 13:27")
     */
    public String getDateCreationFormatee() {
        if (dateCreation == null) {
            return "";
        }
        return dateCreation.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm"));
    }

    /**
     * Formate la date de traitement (ex: "26/10/2025 à 14:30")
     */
    public String getDateTraitementFormatee() {
        if (dateTraitement == null) {
            return "";
        }
        return dateTraitement.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm"));
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Urgence getUrgence() {
        return urgence;
    }

    public void setUrgence(Urgence urgence) {
        this.urgence = urgence;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
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

    public String getCommentaireTraitement() {
        return commentaireTraitement;
    }

    public void setCommentaireTraitement(String commentaireTraitement) {
        this.commentaireTraitement = commentaireTraitement;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", userId=" + userId +
                ", salleId=" + salleId +
                ", urgence=" + urgence +
                ", statut=" + statut +
                ", dateCreation=" + dateCreation +
                '}';
    }
}

