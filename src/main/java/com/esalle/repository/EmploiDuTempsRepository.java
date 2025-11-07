package com.esalle.repository;

import com.esalle.entity.EmploiDuTemps;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface EmploiDuTempsRepository extends BaseRepository<EmploiDuTemps, Long> {
    
    /**
     * Trouver tous les emplois du temps d'une filière
     */
    List<EmploiDuTemps> findByFiliereId(Long filiereId);
    
    /**
     * Trouver tous les emplois du temps d'une filière et année
     */
    List<EmploiDuTemps> findByFiliereIdAndAnnee(Long filiereId, Integer annee);
    
    /**
     * Trouver tous les emplois du temps d'une matière
     */
    List<EmploiDuTemps> findByMatiereId(Long matiereId);
    
    /**
     * Trouver tous les emplois du temps d'un professeur
     */
    List<EmploiDuTemps> findByProfesseurId(Long professeurId);
    
    /**
     * Trouver tous les emplois du temps d'une salle
     */
    List<EmploiDuTemps> findBySalleId(Long salleId);
    
    /**
     * Trouver tous les emplois du temps pour un jour et une salle
     */
    List<EmploiDuTemps> findByJourSemaineAndSalleId(EmploiDuTemps.JourSemaine jour, Long salleId);
    
    /**
     * Vérifier les conflits horaires pour une salle
     */
    List<EmploiDuTemps> findConflitsSalle(Long salleId, EmploiDuTemps.JourSemaine jour, 
                                          LocalTime heureDebut, LocalTime heureFin);
    
    /**
     * Vérifier les conflits horaires pour un professeur
     */
    List<EmploiDuTemps> findConflitsProfesseur(Long professeurId, EmploiDuTemps.JourSemaine jour, 
                                               LocalTime heureDebut, LocalTime heureFin);
    
    /**
     * Trouver tous les emplois du temps pour un groupe
     */
    List<EmploiDuTemps> findByGroupe(String groupe);
    
    /**
     * Trouver tous les emplois du temps d'une filière, année et groupe
     */
    List<EmploiDuTemps> findByFiliereIdAndAnneeAndGroupe(Long filiereId, Integer annee, String groupe);
}

