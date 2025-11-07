package com.esalle.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface for common CRUD operations
 * @param <T> Entity type
 * @param <ID> Entity ID type
 */
public interface BaseRepository<T, ID> {
    
    /**
     * Save or update an entity
     * @param entity Entity to save or update
     * @return Saved entity
     */
    T save(T entity);
    
    /**
     * Find entity by ID
     * @param id Entity ID
     * @return Optional containing entity if found
     */
    Optional<T> findById(ID id);
    
    /**
     * Find all entities
     * @return List of all entities
     */
    List<T> findAll();
    
    /**
     * Delete entity by ID
     * @param id Entity ID
     * @return true if deleted, false if not found
     */
    boolean deleteById(ID id);
    
    /**
     * Delete entity
     * @param entity Entity to delete
     * @return true if deleted, false if not found
     */
    boolean delete(T entity);
    
    /**
     * Check if entity exists by ID
     * @param id Entity ID
     * @return true if exists, false otherwise
     */
    boolean existsById(ID id);
    
    /**
     * Count all entities
     * @return Number of entities
     */
    long count();
}

