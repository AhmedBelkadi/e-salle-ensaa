package com.esalle.features.salle.service;

import com.esalle.features.salle.domain.Salle;
import java.util.List;
import java.util.Optional;

public interface SalleService {
    
    /**
     * Créer une nouvelle salle
     */
    Salle create(Salle salle);
    
    /**
     * Mettre à jour une salle existante
     */
    Salle update(Long id, Salle salle);
    
    /**
     * Supprimer une salle par ID
     */
    boolean delete(Long id);
    
    /**
     * Trouver une salle par ID
     */
    Optional<Salle> findById(Long id);
    
    /**
     * Trouver toutes les salles
     */
    List<Salle> findAll();
    
    /**
     * Trouver une salle par nom
     */
    Optional<Salle> findByNom(String nom);
    
    /**
     * Trouver toutes les salles par type
     */
    List<Salle> findByType(Salle.TypeSalle type);
    
    /**
     * Trouver toutes les salles disponibles
     */
    List<Salle> findDisponibles();
    
    /**
     * Trouver toutes les salles par type et disponibilité
     */
    List<Salle> findByTypeAndDisponible(Salle.TypeSalle type, Boolean disponible);
    
    /**
     * Trouver toutes les salles avec capacité minimale
     */
    List<Salle> findByCapaciteMin(Integer capaciteMin);
    
    /**
     * Rechercher des salles par nom
     */
    List<Salle> search(String keyword);
    
    /**
     * Changer la disponibilité d'une salle
     */
    Salle changeDisponibilite(Long id, Boolean disponible);
}

