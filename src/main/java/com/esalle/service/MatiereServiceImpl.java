package com.esalle.service;

import com.esalle.entity.Matiere;
import com.esalle.entity.User;
import com.esalle.entity.Filiere;
import com.esalle.repository.MatiereRepository;
import com.esalle.repository.MatiereRepositoryImpl;
import com.esalle.repository.UserRepository;
import com.esalle.repository.UserRepositoryImpl;
import com.esalle.repository.FiliereRepository;
import com.esalle.repository.FiliereRepositoryImpl;
import com.esalle.exception.BusinessException;
import com.esalle.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

public class MatiereServiceImpl implements MatiereService {

    private final MatiereRepository matiereRepository;
    private final UserRepository userRepository;
    private final FiliereRepository filiereRepository;

    public MatiereServiceImpl() {
        this.matiereRepository = new MatiereRepositoryImpl();
        this.userRepository = new UserRepositoryImpl();
        this.filiereRepository = new FiliereRepositoryImpl();
    }

    @Override
    public Matiere saveMatiere(Matiere matiere) {
        // Validation
        if (matiere.getNom() == null || matiere.getNom().trim().isEmpty()) {
            throw new BusinessException("Le nom de la matière est obligatoire.");
        }

        // Vérifier que la filière existe
        Filiere filiere = filiereRepository.findById(matiere.getFiliereId())
                .orElseThrow(() -> new BusinessException("La filière spécifiée n'existe pas."));
        matiere.setFiliereNom(filiere.getNom());

        // Vérifier que le professeur existe et est bien un PROFESSEUR
        User professeur = userRepository.findById(matiere.getProfesseurId())
                .orElseThrow(() -> new BusinessException("Le professeur spécifié n'existe pas."));
        
        if (professeur.getRole() != User.UserRole.PROFESSEUR) {
            throw new BusinessException("L'utilisateur sélectionné n'est pas un professeur.");
        }
        matiere.setProfesseurNom(professeur.getNom() + " " + professeur.getPrenom());

        // Vérifier unicité (nom + filière)
        if (matiere.getId() == null) {
            // Création
            if (matiereRepository.existsByNomAndFiliereId(matiere.getNom(), matiere.getFiliereId())) {
                throw new BusinessException("Une matière avec ce nom existe déjà pour cette filière.");
            }
        } else {
            // Modification
            Matiere existing = matiereRepository.findById(matiere.getId())
                    .orElseThrow(() -> new BusinessException("Matière introuvable pour la modification."));
            
            if (!existing.getNom().equals(matiere.getNom()) && 
                matiereRepository.existsByNomAndFiliereId(matiere.getNom(), matiere.getFiliereId())) {
                throw new BusinessException("Une autre matière avec ce nom existe déjà pour cette filière.");
            }
        }

        return matiereRepository.save(matiere);
    }

    @Override
    public Optional<Matiere> getMatiereById(Long id) {
        return matiereRepository.findById(id);
    }

    @Override
    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }

    @Override
    public void deleteMatiere(Long id) {
        if (!matiereRepository.existsById(id)) {
            throw new BusinessException("Matière introuvable pour la suppression.");
        }
        matiereRepository.deleteById(id);
    }

    @Override
    public List<Matiere> getMatieresByFiliere(Long filiereId) {
        return matiereRepository.findByFiliereId(filiereId);
    }

    @Override
    public List<Matiere> getMatieresByProfesseur(Long professeurId) {
        return matiereRepository.findByProfesseurId(professeurId);
    }

    @Override
    public List<Matiere> searchMatieres(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllMatieres();
        }
        return matiereRepository.searchByNom(keyword);
    }
}

