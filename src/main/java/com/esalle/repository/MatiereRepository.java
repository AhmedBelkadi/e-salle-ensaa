package com.esalle.repository;

import com.esalle.entity.Matiere;
import java.util.List;
import java.util.Optional;

public interface MatiereRepository extends BaseRepository<Matiere, Long> {
    
    /**
     * Trouver toutes les matières d'une filière
     */
    List<Matiere> findByFiliereId(Long filiereId);
    
    /**
     * Trouver toutes les matières de plusieurs filières
     */
    List<Matiere> findByFiliereIds(List<Long> filiereIds);
    
    /**
     * Trouver toutes les matières d'un professeur
     */
    List<Matiere> findByProfesseurId(Long professeurId);
    
    /**
     * Trouver une matière par nom et filière
     */
    Optional<Matiere> findByNomAndFiliereId(String nom, Long filiereId);
    
    /**
     * Vérifier si une matière existe par nom et filière
     */
    boolean existsByNomAndFiliereId(String nom, Long filiereId);
    
    /**
     * Rechercher des matières par nom (LIKE)
     */
    List<Matiere> searchByNom(String keyword);
    
    /**
     * Trouver toutes les matières avec TP
     */
    List<Matiere> findByHeuresTPGreaterThan(Integer heuresTP);
}

