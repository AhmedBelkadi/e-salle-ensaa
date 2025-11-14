package com.esalle.service;

import com.esalle.entity.User;
import com.esalle.repository.UserRepository;
import com.esalle.entity.Filiere;
import com.esalle.entity.Filiere.Cycle;
import com.esalle.entity.EmploiDuTemps;
import com.esalle.repository.FiliereRepository;
import com.esalle.repository.FiliereRepositoryImpl;
import com.esalle.repository.UserRepositoryImpl;
import com.esalle.repository.EmploiDuTempsRepository;
import com.esalle.repository.EmploiDuTempsRepositoryImpl;
import com.esalle.exception.BusinessException;
import com.esalle.exception.NotFoundException;

import java.util.List;

/**
 * Implémentation du service métier pour les Filières
 */
public class FiliereServiceImpl implements FiliereService {

    private final FiliereRepository filiereRepository;
    private final UserRepository userRepository;
    private final EmploiDuTempsRepository emploiDuTempsRepository;

    public FiliereServiceImpl() {
        this.filiereRepository = new FiliereRepositoryImpl();
        this.userRepository = new UserRepositoryImpl();
        this.emploiDuTempsRepository = new EmploiDuTempsRepositoryImpl();
    }

    public FiliereServiceImpl(FiliereRepository filiereRepository, UserRepository userRepository) {
        this.filiereRepository = filiereRepository;
        this.userRepository = userRepository;
        this.emploiDuTempsRepository = new EmploiDuTempsRepositoryImpl();
    }

    @Override
    public List<Filiere> createFilieres(String nom, Cycle cycle, Integer effectif, String description) {
        // Validation du nom
        if (nom == null || nom.trim().isEmpty()) {
            throw new BusinessException("Le nom de la filière est obligatoire");
        }

        // Validation du cycle
        if (cycle == null) {
            throw new BusinessException("Le cycle est obligatoire");
        }

        // Validation de l'effectif
        if (effectif == null || effectif <= 0) {
            throw new BusinessException("L'effectif doit être supérieur à 0");
        }

        // Vérifier si une filière avec ce nom et ce cycle existe déjà
        List<Filiere> existingFilieres = filiereRepository.findByCycle(cycle);
        for (Filiere existing : existingFilieres) {
            if (existing.getNom().equalsIgnoreCase(nom.trim())) {
                throw new BusinessException("Une filière avec ce nom et cycle existe déjà");
            }
        }

        List<Filiere> createdFilieres = new java.util.ArrayList<>();
        int nombreAnnees = (cycle == Cycle.PREPARATOIRE) ? 2 : 3;

        // Créer les filières pour chaque année
        for (int annee = 1; annee <= nombreAnnees; annee++) {
            Filiere filiere = new Filiere();
            filiere.setNom(nom.trim());
            filiere.setCycle(cycle);
            filiere.setAnnee(annee);
            filiere.setEffectif(effectif);
            
            // Description personnalisée selon l'année
            if (description != null && !description.trim().isEmpty()) {
                String anneeLibelle = cycle == Cycle.PREPARATOIRE ? 
                    (annee == 1 ? "1ère année" : "2ème année") : 
                    "DLA" + annee;
                filiere.setDescription(description + " - " + anneeLibelle);
            }

            Filiere saved = filiereRepository.save(filiere);
            createdFilieres.add(saved);
        }

        return createdFilieres;
    }

    @Override
    public Filiere updateFiliere(Filiere filiere) {
        // Validation du nom
        if (filiere.getNom() == null || filiere.getNom().trim().isEmpty()) {
            throw new BusinessException("Le nom de la filière est obligatoire");
        }

        // Validation du cycle
        if (filiere.getCycle() == null) {
            throw new BusinessException("Le cycle est obligatoire");
        }

        // Validation de l'année
        if (filiere.getAnnee() == null) {
            throw new BusinessException("L'année est obligatoire");
        }

        if (!filiere.isAnneeValide()) {
            if (filiere.getCycle() == Cycle.PREPARATOIRE) {
                throw new BusinessException("Le cycle Préparatoire n'a que 2 années (1 ou 2)");
            } else {
                throw new BusinessException("Le cycle Ingénieur n'a que 3 années (1, 2 ou 3)");
            }
        }

        // Validation de l'effectif
        if (filiere.getEffectif() == null || filiere.getEffectif() <= 0) {
            throw new BusinessException("L'effectif doit être supérieur à 0");
        }

        // Validation de l'unicité (nom + cycle + année)
        boolean exists;
        if (filiere.getId() == null) {
            exists = filiereRepository.existsByNomAndCycleAndAnnee(
                filiere.getNom(), filiere.getCycle(), filiere.getAnnee()
            );
        } else {
            exists = filiereRepository.existsByNomAndCycleAndAnneeAndIdNot(
                filiere.getNom(), filiere.getCycle(), filiere.getAnnee(), filiere.getId()
            );
        }

        if (exists) {
            throw new BusinessException("Une filière avec ce nom, cycle et année existe déjà");
        }

        // Validation du coordinateur si présent
        if (filiere.getCoordinateurId() != null) {
            User coordinateur = userRepository.findById(filiere.getCoordinateurId())
                .orElseThrow(() -> new NotFoundException("Coordinateur non trouvé"));

            if (coordinateur.getRole() != User.UserRole.COORDINATEUR) {
                throw new BusinessException("L'utilisateur sélectionné n'a pas le rôle COORDINATEUR");
            }

            // Mettre à jour le nom du coordinateur pour affichage
            filiere.setCoordinateurNom(coordinateur.getNom() + " " + coordinateur.getPrenom());
        }

        // Sauvegarder l'ancien nom si c'est une modification
        String oldNom = null;
        if (filiere.getId() != null) {
            Filiere existing = filiereRepository.findById(filiere.getId())
                .orElse(null);
            if (existing != null) {
                oldNom = existing.getNom();
            }
        }

        Filiere savedFiliere = filiereRepository.save(filiere);
        
        // Si le nom a changé, mettre à jour tous les EmploiDuTemps associés
        if (oldNom != null && !oldNom.equals(savedFiliere.getNom())) {
            updateEmploiDuTempsFiliereNom(savedFiliere.getId(), savedFiliere.getNom());
        }
        
        return savedFiliere;
    }

    @Override
    public Filiere getFiliereById(Long id) {
        return filiereRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Filière non trouvée avec l'ID: " + id));
    }

    @Override
    public List<Filiere> getAllFilieres() {
        return filiereRepository.findAll();
    }

    @Override
    public List<Filiere> filterFilieres(Cycle cycle, Integer annee, String keyword) {
        // Si recherche par mot-clé
        if (keyword != null && !keyword.trim().isEmpty()) {
            return filiereRepository.searchByNom(keyword.trim());
        }

        // Si filtre par cycle ET année
        if (cycle != null && annee != null) {
            return filiereRepository.findByCycleAndAnnee(cycle, annee);
        }

        // Si filtre par cycle uniquement
        if (cycle != null) {
            return filiereRepository.findByCycle(cycle);
        }

        // Sinon, retourner toutes les filières
        return filiereRepository.findAll();
    }

    @Override
    public List<Filiere> getFilieresByCoordinateur(Long coordinateurId) {
        if (coordinateurId == null) {
            throw new BusinessException("L'ID du coordinateur est obligatoire");
        }
        return filiereRepository.findByCoordinateurId(coordinateurId);
    }

    @Override
    public Filiere assignCoordinateur(Long filiereId, Long coordinateurId) {
        Filiere filiere = getFiliereById(filiereId);

        User coordinateur = userRepository.findById(coordinateurId)
            .orElseThrow(() -> new NotFoundException("Coordinateur non trouvé avec l'ID: " + coordinateurId));

        if (coordinateur.getRole() != User.UserRole.COORDINATEUR) {
            throw new BusinessException("L'utilisateur sélectionné n'a pas le rôle COORDINATEUR");
        }

        filiere.setCoordinateurId(coordinateurId);
        filiere.setCoordinateurNom(coordinateur.getNom() + " " + coordinateur.getPrenom());

        return filiereRepository.save(filiere);
    }

    @Override
    public Filiere removeCoordinateur(Long filiereId) {
        Filiere filiere = getFiliereById(filiereId);
        filiere.setCoordinateurId(null);
        filiere.setCoordinateurNom(null);
        return filiereRepository.save(filiere);
    }

    @Override
    public void deleteFiliere(Long id) {
        if (id == null) {
            throw new BusinessException("L'ID de la filière est obligatoire");
        }
        Filiere filiere = getFiliereById(id);
        if (filiere != null && filiere.hasCoordinateur()) {
            removeCoordinateur(filiere.getId());
        }
        if (filiere != null) {
            filiereRepository.deleteById(id);
        }
    }

    @Override
    public void deleteFilieresByCycleAndNom(String nom, Cycle cycle) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new BusinessException("Le nom est obligatoire");
        }
        if (cycle == null) {
            throw new BusinessException("Le cycle est obligatoire");
        }

        List<Filiere> filieres = filiereRepository.findByCycle(cycle);
        for (Filiere filiere : filieres) {
            if (filiere.getNom().equalsIgnoreCase(nom.trim())) {
                filiereRepository.deleteById(filiere.getId());
            }
        }
    }

    @Override
    public long countByCycle(Cycle cycle) {
        if (cycle == null) {
            throw new BusinessException("Le cycle est obligatoire");
        }
        return filiereRepository.countByCycle(cycle);
    }

    @Override
    public List<Filiere> getFilieresByNomExact(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new BusinessException("Le nom est obligatoire");
        }
        return filiereRepository.findByNomExact(nom.trim());
    }
    
    /**
     * Met à jour le nom de la filière dans tous les EmploiDuTemps associés
     */
    private void updateEmploiDuTempsFiliereNom(Long filiereId, String newFiliereNom) {
        List<EmploiDuTemps> emplois = emploiDuTempsRepository.findByFiliereId(filiereId);
        for (EmploiDuTemps emploi : emplois) {
            emploi.setFiliereNom(newFiliereNom);
            emploiDuTempsRepository.save(emploi);
        }
    }
}

