package com.esalle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entité représentant un Emploi du Temps.
 * 
 * Gère les emplois du temps avec calendrier, onglets années, groupes (1/2),
 * et génération de 7 séances par matière.
 */
@Entity
@Table(name = "emploi_du_temps")
public class EmploiDuTemps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filiere_id", nullable = false)
    private Long filiereId;

    @Column(name = "filiere_nom", length = 100)
    private String filiereNom;

    @Column(name = "annee", nullable = false)
    private Integer annee; // 1, 2, 3 pour INGENIEUR ou 1, 2 pour PREPARATOIRE

    @Column(name = "matiere_id", nullable = false)
    private Long matiereId;

    @Column(name = "matiere_nom", length = 150)
    private String matiereNom;

    @Column(name = "professeur_id", nullable = false)
    private Long professeurId;

    @Column(name = "professeur_nom", length = 200)
    private String professeurNom;

    @Column(name = "salle_id")
    private Long salleId;

    @Column(name = "salle_nom", length = 100)
    private String salleNom;

    @Column(name = "groupe", length = 10)
    private String groupe; // "1", "2", ou null pour tous

    @NotNull(message = "Le jour de la semaine est obligatoire")
    @Column(name = "jour_semaine", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private JourSemaine jourSemaine;

    @NotNull(message = "L'heure de début est obligatoire")
    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est obligatoire")
    @Column(name = "heure_fin", nullable = false)
    private LocalTime heureFin;

    @Column(name = "type_seance", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TypeSeance typeSeance; // COURS, TD, TP

    @Column(name = "numero_seance")
    private Integer numeroSeance; // 1 à 7 (ou plus selon besoins)

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Enum pour les jours de la semaine
     */
    public enum JourSemaine {
        LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI
    }

    /**
     * Enum pour le type de séance
     */
    public enum TypeSeance {
        COURS, TD, TP
    }

    // Constructors
    public EmploiDuTemps() {
        this.createdAt = LocalDateTime.now();
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Business methods

    /**
     * Vérifie si c'est une séance de TP
     */
    public boolean isTP() {
        return TypeSeance.TP.equals(typeSeance);
    }

    /**
     * Vérifie si c'est pour un groupe spécifique
     */
    public boolean isGroupeSpecifique() {
        return groupe != null && !groupe.trim().isEmpty();
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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFiliereId() {
        return filiereId;
    }

    public void setFiliereId(Long filiereId) {
        this.filiereId = filiereId;
    }

    public String getFiliereNom() {
        return filiereNom;
    }

    public void setFiliereNom(String filiereNom) {
        this.filiereNom = filiereNom;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Long getMatiereId() {
        return matiereId;
    }

    public void setMatiereId(Long matiereId) {
        this.matiereId = matiereId;
    }

    public String getMatiereNom() {
        return matiereNom;
    }

    public void setMatiereNom(String matiereNom) {
        this.matiereNom = matiereNom;
    }

    public Long getProfesseurId() {
        return professeurId;
    }

    public void setProfesseurId(Long professeurId) {
        this.professeurId = professeurId;
    }

    public String getProfesseurNom() {
        return professeurNom;
    }

    public void setProfesseurNom(String professeurNom) {
        this.professeurNom = professeurNom;
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

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public JourSemaine getJourSemaine() {
        return jourSemaine;
    }

    public void setJourSemaine(JourSemaine jourSemaine) {
        this.jourSemaine = jourSemaine;
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

    public TypeSeance getTypeSeance() {
        return typeSeance;
    }

    public void setTypeSeance(TypeSeance typeSeance) {
        this.typeSeance = typeSeance;
    }

    public Integer getNumeroSeance() {
        return numeroSeance;
    }

    public void setNumeroSeance(Integer numeroSeance) {
        this.numeroSeance = numeroSeance;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "EmploiDuTemps{" +
                "id=" + id +
                ", matiereNom='" + matiereNom + '\'' +
                ", jourSemaine=" + jourSemaine +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", groupe='" + groupe + '\'' +
                '}';
    }
}

