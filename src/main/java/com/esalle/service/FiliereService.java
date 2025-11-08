package com.esalle.service;

import com.esalle.entity.Filiere;
import com.esalle.entity.Filiere.Cycle;

import java.util.List;

/**
 * Interface du service métier pour les Filières
 */
public interface FiliereService {

    /**
     * Crée automatiquement 2 ou 3 filières selon le cycle
     * PREPARATOIRE: crée 2 filières (1ère et 2ème année)
     * INGENIEUR: crée 3 filières (DLA1, DLA2, DLA3)
     * 
     * @param nom Le nom de base de la filière
     * @param cycle Le cycle (PREPARATOIRE ou INGENIEUR)
     * @param effectif L'effectif moyen par année
     * @param description La description de base
     * @return Liste des filières créées
     */
    List<Filiere> createFilieres(String nom, Cycle cycle, Integer effectif, String description);
    
    /**
     * Sauvegarde/met à jour une filière existante
     */
    Filiere updateFiliere(Filiere filiere);

    /**
     * Récupère une filière par son ID
     */
    Filiere getFiliereById(Long id);

    /**
     * Récupère toutes les filières
     */
    List<Filiere> getAllFilieres();

    /**
     * Filtre les filières par cycle et/ou année
     */
    List<Filiere> filterFilieres(Cycle cycle, Integer annee, String keyword);

    /**
     * Récupère les filières d'un coordinateur
     */
    List<Filiere> getFilieresByCoordinateur(Long coordinateurId);

    /**
     * Assigne un coordinateur à une filière
     */
    Filiere assignCoordinateur(Long filiereId, Long coordinateurId);

    /**
     * Retire le coordinateur d'une filière
     */
    Filiere removeCoordinateur(Long filiereId);

    /**
     * Supprime une filière par ID
     */
    void deleteFiliere(Long id);
    
    /**
     * Supprime toutes les filières d'un cycle avec le même nom
     */
    void deleteFilieresByCycleAndNom(String nom, Cycle cycle);

    /**
     * Compte les filières par cycle
     */
    long countByCycle(Cycle cycle);

    /**
     * Récupère toutes les filières avec un nom exact (toutes années et cycles)
     */
    List<Filiere> getFilieresByNomExact(String nom);
}

