package com.esalle.service;

import com.esalle.entity.User;
import com.esalle.repository.UserRepository;
import com.esalle.repository.UserRepositoryImpl;
import com.esalle.entity.Reclamation;
import com.esalle.entity.Reclamation.Statut;
import com.esalle.entity.Reclamation.Urgence;
import com.esalle.repository.ReclamationRepository;
import com.esalle.repository.ReclamationRepositoryImpl;
import com.esalle.entity.Salle;
import com.esalle.repository.SalleRepository;
import com.esalle.repository.SalleRepositoryImpl;
import com.esalle.exception.BusinessException;
import com.esalle.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implémentation du service métier pour les Réclamations
 */
public class ReclamationServiceImpl implements ReclamationService {

    private final ReclamationRepository reclamationRepository;
    private final UserRepository userRepository;
    private final SalleRepository salleRepository;

    public ReclamationServiceImpl() {
        this.reclamationRepository = new ReclamationRepositoryImpl();
        this.userRepository = new UserRepositoryImpl();
        this.salleRepository = new SalleRepositoryImpl();
    }

    public ReclamationServiceImpl(ReclamationRepository reclamationRepository,
                                  UserRepository userRepository,
                                  SalleRepository salleRepository) {
        this.reclamationRepository = reclamationRepository;
        this.userRepository = userRepository;
        this.salleRepository = salleRepository;
    }

    @Override
    public Reclamation createReclamation(Long userId, Long salleId, String description, Urgence urgence) {
        // Validation userId
        if (userId == null) {
            throw new BusinessException("L'ID de l'utilisateur est obligatoire");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec l'ID: " + userId));

        // Validation salleId
        if (salleId == null) {
            throw new BusinessException("L'ID de la salle est obligatoire");
        }

        Salle salle = salleRepository.findById(salleId)
            .orElseThrow(() -> new NotFoundException("Salle non trouvée avec l'ID: " + salleId));

        // Validation description
        if (description == null || description.trim().isEmpty()) {
            throw new BusinessException("La description de la réclamation est obligatoire");
        }

        if (description.trim().length() < 10) {
            throw new BusinessException("La description doit contenir au moins 10 caractères");
        }

        // Validation urgence
        if (urgence == null) {
            throw new BusinessException("Le niveau d'urgence est obligatoire");
        }

        // Création de la réclamation
        Reclamation reclamation = new Reclamation();
        reclamation.setUserId(userId);
        reclamation.setUserNom(user.getNom() + " " + user.getPrenom());
        reclamation.setSalleId(salleId);
        reclamation.setSalleNom(salle.getNom());
        reclamation.setDescription(description.trim());
        reclamation.setUrgence(urgence);
        reclamation.setStatut(Statut.EN_ATTENTE);

        Reclamation saved = reclamationRepository.save(reclamation);

        return saved;
    }

    @Override
    public Reclamation traiterReclamation(Long reclamationId, Long adminId, String commentaire) {
        // Validation reclamationId
        if (reclamationId == null) {
            throw new BusinessException("L'ID de la réclamation est obligatoire");
        }

        Reclamation reclamation = reclamationRepository.findById(reclamationId)
            .orElseThrow(() -> new NotFoundException("Réclamation non trouvée avec l'ID: " + reclamationId));

        // Vérifier que la réclamation n'est pas déjà traitée
        if (reclamation.getStatut() == Statut.TRAITEE) {
            throw new BusinessException("Cette réclamation a déjà été traitée");
        }

        // Validation adminId
        if (adminId == null) {
            throw new BusinessException("L'ID de l'administrateur est obligatoire");
        }

        User admin = userRepository.findById(adminId)
            .orElseThrow(() -> new NotFoundException("Administrateur non trouvé avec l'ID: " + adminId));

        // Vérifier que l'utilisateur est bien un admin
        if (admin.getRole() != User.UserRole.ADMIN) {
            throw new BusinessException("Seul un administrateur peut traiter une réclamation");
        }

        // Mise à jour de la réclamation
        reclamation.setStatut(Statut.TRAITEE);
        reclamation.setDateTraitement(LocalDateTime.now());
        reclamation.setTraiteParId(adminId);
        reclamation.setTraiteParNom(admin.getNom() + " " + admin.getPrenom());
        reclamation.setCommentaireTraitement(commentaire);

        Reclamation updated = reclamationRepository.save(reclamation);

        return updated;
    }

    @Override
    public Reclamation getReclamationById(Long id) {
        return reclamationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Réclamation non trouvée avec l'ID: " + id));
    }

    @Override
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    @Override
    public List<Reclamation> getReclamationsByUser(Long userId) {
        if (userId == null) {
            throw new BusinessException("L'ID de l'utilisateur est obligatoire");
        }
        return reclamationRepository.findByUserId(userId);
    }

    @Override
    public List<Reclamation> getReclamationsBySalle(Long salleId) {
        if (salleId == null) {
            throw new BusinessException("L'ID de la salle est obligatoire");
        }
        return reclamationRepository.findBySalleId(salleId);
    }

    @Override
    public List<Reclamation> filterReclamations(Statut statut, Urgence urgence, Long userId, Long salleId) {
        return reclamationRepository.filterReclamations(statut, urgence, userId, salleId);
    }

    @Override
    public List<Reclamation> getReclamationsUrgentesEnAttente() {
        return reclamationRepository.findUrgentesEnAttente();
    }

    @Override
    public void deleteReclamation(Long id) {
        Reclamation reclamation = getReclamationById(id);
        reclamationRepository.deleteById(id);
    }

    @Override
    public long countByStatut(Statut statut) {
        if (statut == null) {
            throw new BusinessException("Le statut est obligatoire");
        }
        return reclamationRepository.countByStatut(statut);
    }

    @Override
    public long countUrgentesEnAttente() {
        return reclamationRepository.countUrgentesEnAttente();
    }


    @Override
    public List<Salle> getAllSallesForForm() {
        return salleRepository.findAll();
    }
}

