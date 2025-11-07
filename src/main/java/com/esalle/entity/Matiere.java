package com.esalle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Entité représentant une Matière académique.
 * 
 * Gère les matières avec charges horaires (cours/TD/TP), professeur et filière.
 * Dépend de Filière (module Dev1).
 */
@Entity
@Table(name = "matieres")
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de la matière est obligatoire")
    @Column(nullable = false, length = 150)
    private String nom;

    @Column(name = "filiere_id", nullable = false)
    private Long filiereId;

    @Column(name = "filiere_nom", length = 100)
    private String filiereNom;

    @Column(name = "professeur_id", nullable = false)
    private Long professeurId;

    @Column(name = "professeur_nom", length = 200)
    private String professeurNom;

    @NotNull(message = "Les heures de cours sont obligatoires")
    @Min(value = 0, message = "Les heures de cours doivent être positives ou nulles")
    @Column(name = "heures_cours", nullable = false)
    private Integer heuresCours = 0;

    @NotNull(message = "Les heures de TD sont obligatoires")
    @Min(value = 0, message = "Les heures de TD doivent être positives ou nulles")
    @Column(name = "heures_td", nullable = false)
    private Integer heuresTD = 0;

    @NotNull(message = "Les heures de TP sont obligatoires")
    @Min(value = 0, message = "Les heures de TP doivent être positives ou nulles")
    @Column(name = "heures_tp", nullable = false)
    private Integer heuresTP = 0;

    @Column(name = "total_heures", nullable = false)
    private Integer totalHeures;

    @Column(length = 500)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Matiere() {
        this.createdAt = LocalDateTime.now();
        calculateTotalHeures();
    }

    public Matiere(String nom, Long filiereId, Long professeurId, Integer heuresCours, Integer heuresTD, Integer heuresTP) {
        this();
        this.nom = nom;
        this.filiereId = filiereId;
        this.professeurId = professeurId;
        this.heuresCours = heuresCours != null ? heuresCours : 0;
        this.heuresTD = heuresTD != null ? heuresTD : 0;
        this.heuresTP = heuresTP != null ? heuresTP : 0;
        calculateTotalHeures();
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateTotalHeures();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateTotalHeures();
    }

    // Business methods
    private void calculateTotalHeures() {
        this.totalHeures = (heuresCours != null ? heuresCours : 0) +
                          (heuresTD != null ? heuresTD : 0) +
                          (heuresTP != null ? heuresTP : 0);
    }

    /**
     * Vérifie si la matière a des heures de TP
     */
    public boolean hasTP() {
        return heuresTP != null && heuresTP > 0;
    }

    /**
     * Retourne le nombre total de séances estimées (1 séance = 2h)
     */
    public Integer getNombreSeancesEstime() {
        return totalHeures != null ? (totalHeures + 1) / 2 : 0; // Arrondi supérieur
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public Integer getHeuresCours() {
        return heuresCours;
    }

    public void setHeuresCours(Integer heuresCours) {
        this.heuresCours = heuresCours;
        calculateTotalHeures();
    }

    public Integer getHeuresTD() {
        return heuresTD;
    }

    public void setHeuresTD(Integer heuresTD) {
        this.heuresTD = heuresTD;
        calculateTotalHeures();
    }

    public Integer getHeuresTP() {
        return heuresTP;
    }

    public void setHeuresTP(Integer heuresTP) {
        this.heuresTP = heuresTP;
        calculateTotalHeures();
    }

    public Integer getTotalHeures() {
        return totalHeures;
    }

    public void setTotalHeures(Integer totalHeures) {
        this.totalHeures = totalHeures;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Matiere{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", filiereId=" + filiereId +
                ", professeurId=" + professeurId +
                ", totalHeures=" + totalHeures +
                '}';
    }
}

