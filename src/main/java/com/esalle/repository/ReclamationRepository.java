package com.esalle.repository;

import com.esalle.entity.Reclamation;
import com.esalle.entity.Reclamation.Statut;
import com.esalle.entity.Reclamation.Urgence;

import java.util.List;
import java.util.Optional;

/**
 * Interface du repository pour les opérations sur les Réclamations
 */
public interface ReclamationRepository {

    /**
     * Sauvegarde une réclamation (création ou mise à jour)
     */
    Reclamation save(Reclamation reclamation);

    /**
     * Récupère une réclamation par son ID
     */
    Optional<Reclamation> findById(Long id);

    /**
     * Récupère toutes les réclamations
     */
    List<Reclamation> findAll();

    /**
     * Récupère les réclamations d'un utilisateur
     */
    List<Reclamation> findByUserId(Long userId);

    /**
     * Récupère les réclamations par statut
     */
    List<Reclamation> findByStatut(Statut statut);

    /**
     * Récupère les réclamations par urgence
     */
    List<Reclamation> findByUrgence(Urgence urgence);

    /**
     * Récupère les réclamations d'une salle
     */
    List<Reclamation> findBySalleId(Long salleId);

    /**
     * Récupère les réclamations urgentes en attente
     */
    List<Reclamation> findUrgentesEnAttente();

    /**
     * Filtre les réclamations selon plusieurs critères
     */
    List<Reclamation> filterReclamations(Statut statut, Urgence urgence, Long userId, Long salleId);

    /**
     * Supprime une réclamation par son ID
     */
    boolean deleteById(Long id);

    /**
     * Compte le nombre total de réclamations
     */
    long count();

    /**
     * Compte les réclamations par statut
     */
    long countByStatut(Statut statut);

    /**
     * Compte les réclamations urgentes en attente
     */
    long countUrgentesEnAttente();
}

