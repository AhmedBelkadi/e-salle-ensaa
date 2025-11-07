package com.esalle.repository;

import com.esalle.entity.Salle;
import java.util.List;
import java.util.Optional;

public interface SalleRepository extends BaseRepository<Salle, Long> {
    
    /**
     * Trouver une salle par son nom
     */
    Optional<Salle> findByNom(String nom);
    
    /**
     * Vérifier si une salle existe par son nom
     */
    boolean existsByNom(String nom);
    
    /**
     * Trouver toutes les salles par type
     */
    List<Salle> findByType(Salle.TypeSalle type);
    
    /**
     * Trouver toutes les salles disponibles
     */
    List<Salle> findByDisponible(Boolean disponible);
    
    /**
     * Trouver toutes les salles par type et disponibilité
     */
    List<Salle> findByTypeAndDisponible(Salle.TypeSalle type, Boolean disponible);
    
    /**
     * Trouver toutes les salles avec capacité minimale
     */
    List<Salle> findByCapaciteGreaterThanEqual(Integer capaciteMin);
    
    /**
     * Rechercher des salles par nom (LIKE)
     */
    List<Salle> searchByNom(String keyword);
}

