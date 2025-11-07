package com.esalle.service;

import com.esalle.entity.Salle;
import com.esalle.entity.Salle.TypeSalle;

import java.util.List;
import java.util.Optional;

public interface SalleService {
    Salle saveSalle(Salle salle);
    Optional<Salle> getSalleById(Long id);
    List<Salle> getAllSalles();
    void deleteSalle(Long id);
    List<Salle> filterSalles(TypeSalle type, Integer capaciteMin, Boolean disponible, String searchNom);
    Salle toggleDisponibilite(Long id);
}

