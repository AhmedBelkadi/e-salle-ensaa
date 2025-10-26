package com.esalle.features.reclamation.service;

import com.esalle.features.auth.domain.User;
import com.esalle.features.auth.repository.UserRepository;
import com.esalle.features.reclamation.domain.Reclamation;
import com.esalle.features.reclamation.domain.Reclamation.Statut;
import com.esalle.features.reclamation.domain.Reclamation.Urgence;
import com.esalle.features.reclamation.repository.ReclamationRepository;
import com.esalle.features.salle.domain.Salle;
import com.esalle.features.salle.repository.SalleRepository;
import com.esalle.shared.exception.BusinessException;
import com.esalle.shared.exception.NotFoundException;
import com.esalle.shared.service.NotificationService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implémentation du service métier pour les Réclamations
 */
public class ReclamationServiceImpl implements ReclamationService {

    private final ReclamationRepository reclamationRepository;
    private final UserRepository userRepository;
    private final SalleRepository salleRepository;
    private final NotificationService notificationService;

    public ReclamationServiceImpl(ReclamationRepository reclamationRepository,
                                  UserRepository userRepository,
                                  SalleRepository salleRepository,
                                  NotificationService notificationService) {
        this.reclamationRepository = reclamationRepository;
        this.userRepository = userRepository;
        this.salleRepository = salleRepository;
        this.notificationService = notificationService;
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

        // Notification à l'admin (email + WhatsApp)
        notifyAdminNewReclamation(saved, user, salle);

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

        // Notification à l'utilisateur (email + WhatsApp)
        notifyUserReclamationTraitee(updated, admin);

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

    // Private notification methods

    /**
     * Notifie l'admin de la création d'une nouvelle réclamation
     */
    private void notifyAdminNewReclamation(Reclamation reclamation, User user, Salle salle) {
        String urgenceText = reclamation.getUrgenceLibelle();
        String subject = String.format("🔔 Nouvelle réclamation %s - Salle %s", 
            urgenceText.toUpperCase(), salle.getNom());
        
        String message = String.format(
            "Une nouvelle réclamation a été créée:\n\n" +
            "👤 Utilisateur: %s\n" +
            "🏫 Salle: %s\n" +
            "⚠️ Urgence: %s\n" +
            "📝 Description: %s\n\n" +
            "Veuillez la traiter dès que possible.",
            reclamation.getUserNom(),
            salle.getNom(),
            urgenceText,
            reclamation.getDescription()
        );

        // Email + WhatsApp à l'admin
        String adminEmail = "admin@ensaa.ma";
        String adminPhone = "+212600000000"; // À configurer

        notificationService.sendEmail(adminEmail, subject, message);
        notificationService.sendWhatsApp(adminPhone, message);
    }

    /**
     * Notifie l'utilisateur que sa réclamation a été traitée
     */
    private void notifyUserReclamationTraitee(Reclamation reclamation, User admin) {
        // Récupérer les infos de l'utilisateur
        User user = userRepository.findById(reclamation.getUserId())
            .orElse(null);

        if (user == null) {
            return; // Pas de notification si user introuvable
        }

        String subject = String.format("✅ Réclamation traitée - Salle %s", reclamation.getSalleNom());
        
        String message = String.format(
            "Bonjour %s,\n\n" +
            "Votre réclamation concernant la salle %s a été traitée par %s.\n\n" +
            "📝 Votre réclamation: %s\n\n" +
            "💬 Commentaire: %s\n\n" +
            "Merci de votre signalement.",
            user.getPrenom(),
            reclamation.getSalleNom(),
            reclamation.getTraiteParNom(),
            reclamation.getDescription(),
            reclamation.getCommentaireTraitement() != null ? 
                reclamation.getCommentaireTraitement() : "Aucun commentaire"
        );

        // Email + WhatsApp à l'utilisateur
        if (user.getEmail() != null) {
            notificationService.sendEmail(user.getEmail(), subject, message);
        }
        if (user.getTelephone() != null) {
            notificationService.sendWhatsApp(user.getTelephone(), message);
        }
    }

    @Override
    public List<Salle> getAllSallesForForm() {
        return salleRepository.findAll();
    }
}

