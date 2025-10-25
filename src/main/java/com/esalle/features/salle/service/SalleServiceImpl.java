package com.esalle.features.salle.service;

import com.esalle.features.salle.domain.Salle;
import com.esalle.features.salle.repository.SalleRepository;
import com.esalle.features.salle.repository.SalleRepositoryImpl;
import com.esalle.shared.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class SalleServiceImpl implements SalleService {
    
    private final SalleRepository salleRepository;
    
    public SalleServiceImpl() {
        this.salleRepository = new SalleRepositoryImpl();
    }
    
    @Override
    public Salle create(Salle salle) {
        // Vérifier si le nom existe déjà
        if (salleRepository.existsByNom(salle.getNom())) {
            throw new BusinessException("Une salle avec ce nom existe déjà");
        }
        
        // Valider la capacité
        if (salle.getCapacite() == null || salle.getCapacite() <= 0) {
            throw new BusinessException("La capacité doit être supérieure à 0");
        }
        
        // Valider le type
        if (salle.getType() == null) {
            throw new BusinessException("Le type de salle est obligatoire");
        }
        
        // Définir les valeurs par défaut
        salle.setDisponible(true);
        salle.setDateCreation(LocalDateTime.now());
        
        return salleRepository.save(salle);
    }
    
    @Override
    public Salle update(Long id, Salle salle) {
        // Vérifier que la salle existe
        Salle existingSalle = salleRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Salle introuvable"));
        
        // Vérifier si le nouveau nom existe déjà (sauf si c'est le même)
        if (!existingSalle.getNom().equals(salle.getNom()) && 
            salleRepository.existsByNom(salle.getNom())) {
            throw new BusinessException("Une salle avec ce nom existe déjà");
        }
        
        // Valider la capacité
        if (salle.getCapacite() == null || salle.getCapacite() <= 0) {
            throw new BusinessException("La capacité doit être supérieure à 0");
        }
        
        // Valider le type
        if (salle.getType() == null) {
            throw new BusinessException("Le type de salle est obligatoire");
        }
        
        // Mettre à jour les champs
        existingSalle.setNom(salle.getNom());
        existingSalle.setType(salle.getType());
        existingSalle.setCapacite(salle.getCapacite());
        existingSalle.setEquipements(salle.getEquipements());
        existingSalle.setDisponible(salle.getDisponible());
        existingSalle.setDateModification(LocalDateTime.now());
        
        return salleRepository.save(existingSalle);
    }
    
    @Override
    public boolean delete(Long id) {
        // Vérifier que la salle existe
        if (!salleRepository.existsById(id)) {
            throw new BusinessException("Salle introuvable");
        }
        
        // TODO: Vérifier qu'il n'y a pas de réservations actives avant de supprimer
        
        return salleRepository.deleteById(id);
    }
    
    @Override
    public Optional<Salle> findById(Long id) {
        return salleRepository.findById(id);
    }
    
    @Override
    public List<Salle> findAll() {
        return salleRepository.findAll();
    }
    
    @Override
    public Optional<Salle> findByNom(String nom) {
        return salleRepository.findByNom(nom);
    }
    
    @Override
    public List<Salle> findByType(Salle.TypeSalle type) {
        return salleRepository.findByType(type);
    }
    
    @Override
    public List<Salle> findDisponibles() {
        return salleRepository.findByDisponible(true);
    }
    
    @Override
    public List<Salle> findByTypeAndDisponible(Salle.TypeSalle type, Boolean disponible) {
        return salleRepository.findByTypeAndDisponible(type, disponible);
    }
    
    @Override
    public List<Salle> findByCapaciteMin(Integer capaciteMin) {
        if (capaciteMin == null || capaciteMin <= 0) {
            throw new BusinessException("La capacité minimale doit être supérieure à 0");
        }
        return salleRepository.findByCapaciteGreaterThanEqual(capaciteMin);
    }
    
    @Override
    public List<Salle> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        return salleRepository.searchByNom(keyword.trim());
    }
    
    @Override
    public Salle changeDisponibilite(Long id, Boolean disponible) {
        Salle salle = salleRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Salle introuvable"));
        
        salle.setDisponible(disponible);
        salle.setDateModification(LocalDateTime.now());
        
        return salleRepository.save(salle);
    }
}

