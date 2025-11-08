package com.esalle.service;

import com.esalle.entity.Matiere;
import java.util.List;
import java.util.Optional;

public interface MatiereService {
    Matiere saveMatiere(Matiere matiere);
    Optional<Matiere> getMatiereById(Long id);
    List<Matiere> getAllMatieres();
    void deleteMatiere(Long id);
    List<Matiere> getMatieresByFiliere(Long filiereId);
    List<Matiere> getMatieresByFilieres(List<Long> filiereIds);
    List<Matiere> getMatieresByProfesseur(Long professeurId);
    List<Matiere> searchMatieres(String keyword);
}

