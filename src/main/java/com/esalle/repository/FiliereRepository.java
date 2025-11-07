package com.esalle.repository;

import com.esalle.entity.Filiere;
import com.esalle.entity.Filiere.Cycle;

import java.util.List;
import java.util.Optional;

/**
 * Interface du repository pour les opérations sur les Filières
 */
public interface FiliereRepository {

    /**
     * Sauvegarde une filière (création ou mise à jour)
     */
    Filiere save(Filiere filiere);

    /**
     * Récupère une filière par son ID
     */
    Optional<Filiere> findById(Long id);

    /**
     * Récupère toutes les filières
     */
    List<Filiere> findAll();

    /**
     * Récupère les filières par cycle
     */
    List<Filiere> findByCycle(Cycle cycle);

    /**
     * Récupère les filières par cycle et année
     */
    List<Filiere> findByCycleAndAnnee(Cycle cycle, Integer annee);

    /**
     * Récupère les filières d'un coordinateur
     */
    List<Filiere> findByCoordinateurId(Long coordinateurId);

    /**
     * Recherche des filières par nom (LIKE)
     */
    List<Filiere> searchByNom(String keyword);

    /**
     * Récupère toutes les filières avec un nom exact (toutes années et cycles)
     */
    List<Filiere> findByNomExact(String nom);

    /**
     * Vérifie si une filière existe avec le même nom, cycle et année
     */
    boolean existsByNomAndCycleAndAnnee(String nom, Cycle cycle, Integer annee);

    /**
     * Vérifie si une filière existe avec le même nom, cycle et année, en excluant un ID
     */
    boolean existsByNomAndCycleAndAnneeAndIdNot(String nom, Cycle cycle, Integer annee, Long id);

    /**
     * Supprime une filière par son ID
     * @return true si supprimé, false sinon
     */
    boolean deleteById(Long id);

    /**
     * Compte le nombre total de filières
     */
    long count();

    /**
     * Compte le nombre de filières par cycle
     */
    long countByCycle(Cycle cycle);
}

