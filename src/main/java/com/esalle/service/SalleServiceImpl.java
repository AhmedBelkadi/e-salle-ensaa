package com.esalle.service;

import com.esalle.entity.Salle;
import com.esalle.entity.Salle.TypeSalle;
import com.esalle.entity.EmploiDuTemps;
import com.esalle.repository.SalleRepository;
import com.esalle.repository.SalleRepositoryImpl;
import com.esalle.repository.EmploiDuTempsRepository;
import com.esalle.repository.EmploiDuTempsRepositoryImpl;
import com.esalle.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SalleServiceImpl implements SalleService {

    private final SalleRepository salleRepository;
    private final EmploiDuTempsRepository emploiDuTempsRepository;

    public SalleServiceImpl() {
        this.salleRepository = new SalleRepositoryImpl();
        this.emploiDuTempsRepository = new EmploiDuTempsRepositoryImpl();
    }

    @Override
    public Salle saveSalle(Salle salle) {
        String oldNom = null;
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

            oldNom = existingSalle.getNom(); // Sauvegarder l'ancien nom

            if (!existingSalle.getNom().equals(salle.getNom()) && salleRepository.existsByNom(salle.getNom())) {
                throw new BusinessException("Une autre salle avec ce nom existe déjà.");
            }
            salle.setDateCreation(existingSalle.getDateCreation()); // Conserver la date de création
        }

        salle.setDateModification(LocalDateTime.now());
        Salle savedSalle = salleRepository.save(salle);
        
        // Si le nom a changé, mettre à jour tous les EmploiDuTemps associés
        if (oldNom != null && !oldNom.equals(savedSalle.getNom())) {
            updateEmploiDuTempsSalleNom(savedSalle.getId(), savedSalle.getNom());
        }
        
        return savedSalle;
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
    
    /**
     * Met à jour le nom de la salle dans tous les EmploiDuTemps associés
     */
    private void updateEmploiDuTempsSalleNom(Long salleId, String newSalleNom) {
        List<EmploiDuTemps> emplois = emploiDuTempsRepository.findBySalleId(salleId);
        for (EmploiDuTemps emploi : emplois) {
            emploi.setSalleNom(newSalleNom);
            emploiDuTempsRepository.save(emploi);
        }
    }
}

