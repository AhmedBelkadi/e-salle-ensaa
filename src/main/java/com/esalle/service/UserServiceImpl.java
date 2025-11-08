package com.esalle.service;

import com.esalle.entity.User;
import com.esalle.repository.UserRepository;
import com.esalle.repository.UserRepositoryImpl;
import com.esalle.exception.BusinessException;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final NotificationServiceImpl notificationService;
    
    public UserServiceImpl() {
        this.userRepository = new UserRepositoryImpl();
        this.notificationService = new NotificationServiceImpl();
    }
    
    @Override
    public User register(User user) {
        // Vérifier si email existe déjà
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException("Cet email est déjà utilisé");
        }
        
        // Hash password avec BCrypt
        user.setPassword(hashPassword(user.getPassword()));
        user.setDateInscription(LocalDateTime.now());
        
        // Auto-validation pour MEMBRE_CLUB selon le workflow
        if (user.getRole() == User.UserRole.MEMBRE_CLUB) {
            user.setStatut(User.UserStatus.ACTIF);
            user.setDateApprobation(LocalDateTime.now());
            // Le nomClub devrait être fourni à l'inscription
            if (user.getNomClub() == null || user.getNomClub().trim().isEmpty()) {
                throw new BusinessException("Le nom du club est obligatoire pour les membres de club");
            }
        } else {
            // Pour les autres rôles, nécessite approbation
            user.setStatut(User.UserStatus.EN_ATTENTE);
        }
        
        User savedUser = userRepository.save(user);
        
        // Envoyer notification à l'admin (sauf pour MEMBRE_CLUB auto-validé)
        if (savedUser.getStatut() == User.UserStatus.EN_ATTENTE) {
            String userName = savedUser.getPrenom() + " " + savedUser.getNom();
            String userRole = savedUser.getRole().toString();
            notificationService.notifyAdminNewRegistration(
                "admin@ensaa.ma", 
                "+212600000000", // Numéro WhatsApp admin (à configurer)
                userName, 
                userRole
            );
        } else if (savedUser.getRole() == User.UserRole.MEMBRE_CLUB) {
            // Notifier le membre de club de son auto-validation
            String userName = savedUser.getPrenom() + " " + savedUser.getNom();
            notificationService.notifyUserAccountApproved(
                savedUser.getEmail(),
                savedUser.getTelephone(),
                userName
            );
        }
        
        return savedUser;
    }
    
    @Override
    public Optional<User> login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Vérifier password avec BCrypt
            if (checkPassword(password, user.getPassword())) {
                // Vérifier statut
                if (user.getStatut() == User.UserStatus.ACTIF) {
                    return Optional.of(user);
                } else if (user.getStatut() == User.UserStatus.EN_ATTENTE) {
                    throw new BusinessException("Votre compte est en attente d'approbation");
                } else {
                    throw new BusinessException("Votre compte a été refusé");
                }
            }
        }
        
        return Optional.empty();
    }
    
    @Override
    public User createAdmin() {
        // Vérifier si admin existe déjà
        List<User> admins = userRepository.findByRole(User.UserRole.ADMIN);
        if (!admins.isEmpty()) {
            return admins.get(0);
        }
        
        // Créer admin
        User admin = new User();
        admin.setNom("Admin");
        admin.setPrenom("System");
        admin.setEmail("admin@ensaa.ma");
        admin.setPassword(hashPassword("admin123")); // Changer ce mot de passe en production
        admin.setRole(User.UserRole.ADMIN);
        admin.setStatut(User.UserStatus.ACTIF);
        admin.setDateInscription(LocalDateTime.now());
        admin.setDateApprobation(LocalDateTime.now());
        
        return userRepository.save(admin);
    }
    
    @Override
    public List<User> findPendingUsers() {
        return userRepository.findByStatut(User.UserStatus.EN_ATTENTE);
    }
    
    @Override
    public User approveUser(Long userId, Long filiereId, String nomClub, Long approvedBy) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("Utilisateur introuvable"));
        
        // Vérifier selon le rôle
        // Les coordinateurs nécessitent une filière, les professeurs peuvent enseigner dans toutes les filières
        if (user.getRole() == User.UserRole.COORDINATEUR) {
            if (filiereId == null) {
                throw new BusinessException("La filière est obligatoire pour un coordinateur");
            }
            user.setFiliereId(filiereId);
        } else if (user.getRole() == User.UserRole.PROFESSEUR) {
            // Les professeurs n'ont pas besoin de filière spécifique (peuvent enseigner partout)
            // filiereId peut être null pour les professeurs
            if (filiereId != null) {
                user.setFiliereId(filiereId);
            }
        } else if (user.getRole() == User.UserRole.MEMBRE_CLUB) {
            if (nomClub == null || nomClub.trim().isEmpty()) {
                throw new BusinessException("Le nom du club est obligatoire");
            }
            user.setNomClub(nomClub);
        }
        
        user.setStatut(User.UserStatus.ACTIF);
        user.setDateApprobation(LocalDateTime.now());
        user.setApprovedBy(approvedBy);
        
        User updatedUser = userRepository.save(user);
        
        // Envoyer notification au user
        String userName = updatedUser.getPrenom() + " " + updatedUser.getNom();
        notificationService.notifyUserAccountApproved(
            updatedUser.getEmail(),
            updatedUser.getTelephone(),
            userName
        );
        
        return updatedUser;
    }
    
    @Override
    public User refuseUser(Long userId, Long refusedBy) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("Utilisateur introuvable"));
        
        user.setStatut(User.UserStatus.REFUSE);
        user.setDateApprobation(LocalDateTime.now());
        user.setApprovedBy(refusedBy);
        
        User updatedUser = userRepository.save(user);
        
        // Envoyer notification au user
        String userName = updatedUser.getPrenom() + " " + updatedUser.getNom();
        notificationService.notifyUserAccountRefused(
            updatedUser.getEmail(),
            updatedUser.getTelephone(),
            userName
        );
        
        return updatedUser;
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public User update(User user) {
        return userRepository.save(user);
    }
    
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    // Méthodes utilitaires - BCrypt
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
    
    private boolean checkPassword(String raw, String hashed) {
        return BCrypt.checkpw(raw, hashed);
    }
}

