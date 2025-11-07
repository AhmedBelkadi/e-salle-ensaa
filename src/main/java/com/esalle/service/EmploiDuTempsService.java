package com.esalle.service;

import com.esalle.entity.EmploiDuTemps;
import java.util.List;
import java.util.Optional;

public interface EmploiDuTempsService {
    EmploiDuTemps saveEmploiDuTemps(EmploiDuTemps emploi);
    Optional<EmploiDuTemps> getEmploiDuTempsById(Long id);
    List<EmploiDuTemps> getAllEmploiDuTemps();
    void deleteEmploiDuTemps(Long id);
    List<EmploiDuTemps> getEmploiDuTempsByFiliere(Long filiereId);
    List<EmploiDuTemps> getEmploiDuTempsByFiliereAndAnnee(Long filiereId, Integer annee);
    List<EmploiDuTemps> getEmploiDuTempsByMatiere(Long matiereId);
    List<EmploiDuTemps> getEmploiDuTempsByProfesseur(Long professeurId);
    List<EmploiDuTemps> getEmploiDuTempsBySalle(Long salleId);
    List<EmploiDuTemps> getEmploiDuTempsByFiliereAnneeGroupe(Long filiereId, Integer annee, String groupe);
    boolean verifierConflitSalle(Long salleId, EmploiDuTemps.JourSemaine jour, 
                                 java.time.LocalTime heureDebut, java.time.LocalTime heureFin, Long excludeId);
    boolean verifierConflitProfesseur(Long professeurId, EmploiDuTemps.JourSemaine jour, 
                                      java.time.LocalTime heureDebut, java.time.LocalTime heureFin, Long excludeId);
    List<EmploiDuTemps> genererSeances(Long matiereId, Long salleId, EmploiDuTemps.JourSemaine jour, 
                                     java.time.LocalTime heureDebut, java.time.LocalTime heureFin, 
                                     Integer nombreSeances, String groupe);
}

