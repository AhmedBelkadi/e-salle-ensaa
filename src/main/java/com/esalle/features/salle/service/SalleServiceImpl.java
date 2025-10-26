package com.esalle.features.salle.service;

import com.esalle.features.salle.domain.Salle;
import com.esalle.features.salle.domain.Salle.TypeSalle;
import com.esalle.features.salle.repository.SalleRepository;
import com.esalle.features.salle.repository.SalleRepositoryImpl;
import com.esalle.shared.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SalleServiceImpl implements SalleService {

    private final SalleRepository salleRepository;

    public SalleServiceImpl() {
        this.salleRepository = new SalleRepositoryImpl();
    }

    @Override
    public Salle saveSalle(Salle salle) {
        if (salle.getId() == null) {
            // Création
            if (salleRepository.existsByNom(salle.getNom())) {
                throw new BusinessException("Une salle avec ce nom existe déjà.");
            }
            salle.setDateCreation(LocalDateTime.now());
        } else {
            // Modification
            Salle existingSalle = salleRepository.findById(salle.getId())
                    .orElseThrow(() -> new BusinessException("Salle introuvable pour la modification."));

            if (!existingSalle.getNom().equals(salle.getNom()) && salleRepository.existsByNom(salle.getNom())) {
                throw new BusinessException("Une autre salle avec ce nom existe déjà.");
            }
            salle.setDateCreation(existingSalle.getDateCreation()); // Conserver la date de création
        }

        salle.setDateModification(LocalDateTime.now());
        return salleRepository.save(salle);
    }

    @Override
    public Optional<Salle> getSalleById(Long id) {
        return salleRepository.findById(id);
    }

    @Override
    public List<Salle> getAllSalles() {
        return salleRepository.findAll();
    }

    @Override
    public void deleteSalle(Long id) {
        if (!salleRepository.existsById(id)) {
            throw new BusinessException("Salle introuvable pour la suppression.");
        }
        salleRepository.deleteById(id);
    }

    @Override
    public List<Salle> filterSalles(TypeSalle type, Integer capaciteMin, Boolean disponible, String searchNom) {
        List<Salle> salles = salleRepository.findAll();

        return salles.stream()
                .filter(s -> type == null || s.getType() == type)
                .filter(s -> capaciteMin == null || s.getCapacite() >= capaciteMin)
                .filter(s -> disponible == null || s.isDisponible() == disponible)
                .filter(s -> searchNom == null || searchNom.trim().isEmpty() || s.getNom().toLowerCase().contains(searchNom.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Salle toggleDisponibilite(Long id) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Salle introuvable pour changer la disponibilité."));
        salle.setDisponible(!salle.isDisponible());
        salle.setDateModification(LocalDateTime.now());
        return salleRepository.save(salle);
    }
}
