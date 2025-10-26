package com.esalle.features.salle.domain;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "salles")
public class Salle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le nom de la salle est obligatoire")
    @Column(nullable = false, unique = true, length = 100)
    private String nom;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type de salle est obligatoire")
    @Column(nullable = false, length = 20)
    private TypeSalle type;
    
    @NotNull(message = "La capacité est obligatoire")
    @Min(value = 1, message = "La capacité doit être supérieure à 0")
    @Column(nullable = false)
    private Integer capacite;
    
    @Column(length = 500)
    private String equipements;
    
    @Column(nullable = false)
    private Boolean disponible = true;
    
    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
    
    @Column(name = "date_modification")
    private LocalDateTime dateModification;
    
    // Constructeurs
    public Salle() {
    }
    
    public Salle(String nom, TypeSalle type, Integer capacite) {
        this.nom = nom;
        this.type = type;
        this.capacite = capacite;
    }
    
    // Getters et Setters
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
    
    public TypeSalle getType() {
        return type;
    }
    
    public void setType(TypeSalle type) {
        this.type = type;
    }
    
    public Integer getCapacite() {
        return capacite;
    }
    
    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }
    
    public String getEquipements() {
        return equipements;
    }
    
    public void setEquipements(String equipements) {
        this.equipements = equipements;
    }
    
    public Boolean getDisponible() {
        return disponible;
    }
    
    public boolean isDisponible() {
        return disponible != null && disponible;
    }
    
    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
    
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public LocalDateTime getDateModification() {
        return dateModification;
    }
    
    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
    
    // Enum pour les types de salle
    public enum TypeSalle {
        COURS("Cours magistral"),
        TP("Travaux pratiques"),
        TD("Travaux dirigés");
        
        private final String description;
        
        TypeSalle(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    @Override
    public String toString() {
        return "Salle{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type=" + type +
                ", capacite=" + capacite +
                ", disponible=" + disponible +
                '}';
    }
}

