package com.esalle.features.filiere.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité représentant une Filière académique.
 * 
 * Gère les filières avec cycles (PREPARATOIRE/INGENIEUR) et années.
 * Chaque filière peut avoir un coordinateur (User avec role COORDINATEUR).
 */
@Entity
@Table(name = "filieres", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom", "cycle", "annee"})
})
public class Filiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Cycle cycle;

    @Column(nullable = false)
    private Integer annee;

    @Column(nullable = false)
    private Integer effectif;

    @Column(name = "coordinateur_id")
    private Long coordinateurId;

    @Column(name = "coordinateur_nom", length = 200)
    private String coordinateurNom;

    @Column(length = 500)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Enum pour les cycles de formation
     */
    public enum Cycle {
        PREPARATOIRE,  // 2 ans (1ère et 2ème année prépa)
        INGENIEUR      // 3 ans (DLA1, DLA2, DLA3)
    }

    // Constructors
    public Filiere() {
        this.createdAt = LocalDateTime.now();
    }

    public Filiere(String nom, Cycle cycle, Integer annee, Integer effectif) {
        this();
        this.nom = nom;
        this.cycle = cycle;
        this.annee = annee;
        this.effectif = effectif;
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
     * Retourne le nom complet de la filière avec cycle et année
     * Ex: "Informatique - Préparatoire 1", "Génie Civil - Ingénieur DLA2"
     */
    public String getNomComplet() {
        String cycleStr = cycle == Cycle.PREPARATOIRE ? "Préparatoire" : "Ingénieur";
        String anneeStr = cycle == Cycle.PREPARATOIRE ? 
            annee + "ère année" : 
            "DLA" + annee;
        return nom + " - " + cycleStr + " " + anneeStr;
    }

    /**
     * Vérifie si l'année est valide pour le cycle
     */
    public boolean isAnneeValide() {
        if (cycle == Cycle.PREPARATOIRE) {
            return annee >= 1 && annee <= 2;
        } else if (cycle == Cycle.INGENIEUR) {
            return annee >= 1 && annee <= 3;
        }
        return false;
    }

    /**
     * Retourne le libellé de l'année (ex: "DLA1", "2ème année prépa")
     */
    public String getAnneeLibelle() {
        if (cycle == Cycle.PREPARATOIRE) {
            return annee == 1 ? "1ère année" : "2ème année";
        } else {
            return "DLA" + annee;
        }
    }

    /**
     * Vérifie si la filière a un coordinateur assigné
     */
    public boolean hasCoordinateur() {
        return coordinateurId != null;
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

    public Cycle getCycle() {
        return cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Integer getEffectif() {
        return effectif;
    }

    public void setEffectif(Integer effectif) {
        this.effectif = effectif;
    }

    public Long getCoordinateurId() {
        return coordinateurId;
    }

    public void setCoordinateurId(Long coordinateurId) {
        this.coordinateurId = coordinateurId;
    }

    public String getCoordinateurNom() {
        return coordinateurNom;
    }

    public void setCoordinateurNom(String coordinateurNom) {
        this.coordinateurNom = coordinateurNom;
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
        return "Filiere{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", cycle=" + cycle +
                ", annee=" + annee +
                ", effectif=" + effectif +
                ", coordinateurId=" + coordinateurId +
                ", coordinateurNom='" + coordinateurNom + '\'' +
                '}';
    }
}

