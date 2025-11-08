package com.esalle.service;

import com.esalle.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    
    /**
     * Inscrire un nouvel utilisateur (statut = EN_ATTENTE)
     */
    User register(User user);
    
    /**
     * Login d'un utilisateur
     */
    Optional<User> login(String email, String password);
    
    /**
     * Créer le compte admin au démarrage
     */
    User createAdmin();
    
    /**
     * Trouver tous les utilisateurs en attente d'approbation
     */
    List<User> findPendingUsers();
    
    /**
     * Approuver un utilisateur
     */
    User approveUser(Long userId, Long filiereId, String nomClub, Long approvedBy);
    
    /**
     * Refuser un utilisateur
     */
    User refuseUser(Long userId, Long refusedBy);
    
    /**
     * Trouver un utilisateur par ID
     */
    Optional<User> findById(Long id);
    
    /**
     * Trouver un utilisateur par email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Mettre à jour un utilisateur
     */
    User update(User user);
    
    /**
     * Trouver tous les utilisateurs
     */
    List<User> findAll();
}

