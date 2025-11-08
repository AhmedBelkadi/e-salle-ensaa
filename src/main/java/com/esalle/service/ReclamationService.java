package com.esalle.service;

import com.esalle.entity.Reclamation;
import com.esalle.entity.Reclamation.Statut;
import com.esalle.entity.Reclamation.Urgence;

import java.util.List;

/**
 * Interface du service métier pour les Réclamations
 */
public interface ReclamationService {

    /**
     * Crée une nouvelle réclamation et notifie l'admin
     * 
     * @param userId ID de l'utilisateur créant la réclamation
     * @param salleId ID de la salle concernée
     * @param description Description du problème
     * @param urgence Niveau d'urgence
     * @return Réclamation créée
     */
    Reclamation createReclamation(Long userId, Long salleId, String description, Urgence urgence);

    /**
     * Traite une réclamation et notifie l'utilisateur
     * 
     * @param reclamationId ID de la réclamation
     * @param adminId ID de l'admin traitant la réclamation
     * @param commentaire Commentaire de traitement
     * @return Réclamation mise à jour
     */
    Reclamation traiterReclamation(Long reclamationId, Long adminId, String commentaire);

    /**
     * Récupère une réclamation par son ID
     */
    Reclamation getReclamationById(Long id);

    /**
     * Récupère toutes les réclamations
     */
    List<Reclamation> getAllReclamations();

    /**
     * Récupère les réclamations d'un utilisateur
     */
    List<Reclamation> getReclamationsByUser(Long userId);

    /**
     * Récupère les réclamations d'une salle
     */
    List<Reclamation> getReclamationsBySalle(Long salleId);

    /**
     * Filtre les réclamations selon plusieurs critères
     */
    List<Reclamation> filterReclamations(Statut statut, Urgence urgence, Long userId, Long salleId);

    /**
     * Récupère les réclamations urgentes en attente
     */
    List<Reclamation> getReclamationsUrgentesEnAttente();

    /**
     * Supprime une réclamation
     */
    void deleteReclamation(Long id);

    /**
     * Compte les réclamations par statut
     */
    long countByStatut(Statut statut);

    /**
     * Compte les réclamations urgentes en attente
     */
    long countUrgentesEnAttente();

    /**
     * Récupère toutes les salles disponibles (pour le formulaire de création)
     */
    List<com.esalle.entity.Salle> getAllSallesForForm();
}

