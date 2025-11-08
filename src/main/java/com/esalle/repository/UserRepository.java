package com.esalle.repository;

import com.esalle.entity.User;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends BaseRepository<User, Long> {
    
    /**
     * Trouver un utilisateur par email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Trouver tous les utilisateurs par statut
     */
    List<User> findByStatut(User.UserStatus statut);
    
    /**
     * Trouver tous les utilisateurs par rôle
     */
    List<User> findByRole(User.UserRole role);
    
    /**
     * Vérifier si un email existe déjà
     */
    boolean existsByEmail(String email);
}

