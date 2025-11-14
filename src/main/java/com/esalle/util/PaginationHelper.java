package com.esalle.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for pagination operations
 * Helps paginate lists of items for display in views
 */
public class PaginationHelper {
    
    /**
     * Paginate a list of items
     * 
     * @param <T> Type of items in the list
     * @param items Full list of items
     * @param page Page number (0-indexed)
     * @param size Number of items per page
     * @return Paginated sublist
     */
    public static <T> List<T> paginate(List<T> items, int page, int size) {
        if (items == null || items.isEmpty()) {
            return new ArrayList<>();
        }
        
        if (size <= 0) {
            size = 10; // Default page size
        }
        
        if (page < 0) {
            page = 0;
        }
        
        int start = page * size;
        int end = Math.min(start + size, items.size());
        
        if (start >= items.size()) {
            return new ArrayList<>();
        }
        
        return new ArrayList<>(items.subList(start, end));
    }
    
    /**
     * Calculate total number of pages
     * 
     * @param totalItems Total number of items
     * @param pageSize Number of items per page
     * @return Total number of pages
     */
    public static int getTotalPages(int totalItems, int pageSize) {
        if (pageSize <= 0) {
            pageSize = 10;
        }
        return (int) Math.ceil((double) totalItems / pageSize);
    }
    
    /**
     * Check if there is a next page
     * 
     * @param currentPage Current page number (0-indexed)
     * @param totalPages Total number of pages
     * @return true if there is a next page
     */
    public static boolean hasNextPage(int currentPage, int totalPages) {
        return currentPage < totalPages - 1;
    }
    
    /**
     * Check if there is a previous page
     * 
     * @param currentPage Current page number (0-indexed)
     * @return true if there is a previous page
     */
    public static boolean hasPreviousPage(int currentPage) {
        return currentPage > 0;
    }
    
    /**
     * Get start index for pagination (1-indexed for display)
     * 
     * @param page Page number (0-indexed)
     * @param size Page size
     * @return Start index (1-indexed)
     */
    public static int getStartIndex(int page, int size) {
        return (page * size) + 1;
    }
    
    /**
     * Get end index for pagination (1-indexed for display)
     * 
     * @param page Page number (0-indexed)
     * @param size Page size
     * @param totalItems Total number of items
     * @return End index (1-indexed)
     */
    public static int getEndIndex(int page, int size, int totalItems) {
        return Math.min((page + 1) * size, totalItems);
    }
}

