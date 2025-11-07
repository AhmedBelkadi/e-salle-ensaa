package com.esalle.filter;

import com.esalle.entity.User;
import com.esalle.entity.User.UserRole;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * Filter de contrôle d'accès basé sur les rôles
 * S'exécute après AuthFilter pour vérifier les permissions
 */
public class RoleFilter implements Filter {

    // Définition des règles d'accès par URL et rôle
    private static final Map<String, List<UserRole>> ACCESS_RULES = new HashMap<>();
    
    static {
        // Module Administration - ADMIN uniquement
        ACCESS_RULES.put("/admin", Arrays.asList(UserRole.ADMIN));
        ACCESS_RULES.put("/users/list", Arrays.asList(UserRole.ADMIN));
        ACCESS_RULES.put("/users/approve", Arrays.asList(UserRole.ADMIN));
        ACCESS_RULES.put("/users/reject", Arrays.asList(UserRole.ADMIN));
        ACCESS_RULES.put("/users/delete", Arrays.asList(UserRole.ADMIN));
        
        // Module Filière - ADMIN et COORDINATEUR
        ACCESS_RULES.put("/filieres/new", Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR));
        ACCESS_RULES.put("/filieres/save", Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR));
        ACCESS_RULES.put("/filieres/edit", Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR));
        ACCESS_RULES.put("/filieres/delete", Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR));
        ACCESS_RULES.put("/filieres/assign-coordinateur", Arrays.asList(UserRole.ADMIN));
        ACCESS_RULES.put("/filieres/remove-coordinateur", Arrays.asList(UserRole.ADMIN));
        // Liste et vue accessibles à tous les utilisateurs authentifiés
        
        // Module Réclamation - Création interdite aux ADMIN
        ACCESS_RULES.put("/reclamations/new", Arrays.asList(UserRole.COORDINATEUR, UserRole.PROFESSEUR, UserRole.MEMBRE_CLUB));
        ACCESS_RULES.put("/reclamations/create", Arrays.asList(UserRole.COORDINATEUR, UserRole.PROFESSEUR, UserRole.MEMBRE_CLUB));
        
        // Traitement des réclamations - ADMIN uniquement
        ACCESS_RULES.put("/reclamations/traiter", Arrays.asList(UserRole.ADMIN));
        ACCESS_RULES.put("/reclamations/delete", Arrays.asList(UserRole.ADMIN));
        
        // Module Matière (Dev2) - ADMIN et COORDINATEUR
        ACCESS_RULES.put("/matieres/new", Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR));
        ACCESS_RULES.put("/matieres/save", Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR));
        ACCESS_RULES.put("/matieres/edit", Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR));
        ACCESS_RULES.put("/matieres/delete", Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR));
        
        // Module Emploi du Temps (Dev2) - Consultation pour tous, modification ADMIN/COORDINATEUR
        ACCESS_RULES.put("/emploi-temps/new", Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR));
        ACCESS_RULES.put("/emploi-temps/save", Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR));
        ACCESS_RULES.put("/emploi-temps/edit", Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR));
        ACCESS_RULES.put("/emploi-temps/delete", Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR));
        
        // Module Réservation (Dev2) - Création pour tous sauf restriction selon besoins métier
        ACCESS_RULES.put("/reservations/approve", Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR));
        ACCESS_RULES.put("/reservations/reject", Arrays.asList(UserRole.ADMIN, UserRole.COORDINATEUR));
        // Tous les utilisateurs peuvent annuler leurs propres réservations (vérifié dans le service)
        // ACCESS_RULES.put("/reservations/cancel", ...) - Pas de restriction, accessible à tous les utilisateurs authentifiés
        
        // Module Salle - Modification ADMIN uniquement, consultation pour tous
        ACCESS_RULES.put("/salle/new", Arrays.asList(UserRole.ADMIN));
        ACCESS_RULES.put("/salle/save", Arrays.asList(UserRole.ADMIN));
        ACCESS_RULES.put("/salle/edit", Arrays.asList(UserRole.ADMIN));
        ACCESS_RULES.put("/salle/delete", Arrays.asList(UserRole.ADMIN));
        ACCESS_RULES.put("/salle/toggle-disponibilite", Arrays.asList(UserRole.ADMIN));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation si nécessaire
    }

    // URLs publiques qui ne nécessitent pas de vérification de rôle
    private static final List<String> PUBLIC_URLS = Arrays.asList(
        "/auth/login",
        "/auth/register",
        "/auth/logout",
        "/error",
        "/css/",
        "/js/",
        "/images/",
        "/assets/",
        "/favicon.ico"
    );
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        // Ignorer les URLs publiques - pas de vérification de rôle nécessaire
        if (path.equals("/") || path.equals("/index.jsp") || isPublicUrl(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        HttpSession session = httpRequest.getSession(false);
        
        // Si pas de session ou pas d'utilisateur, AuthFilter s'en occupera
        if (session == null) {
            chain.doFilter(request, response);
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            chain.doFilter(request, response);
            return;
        }
        
        // Vérifier si l'URL nécessite des permissions spécifiques
        if (!hasPermission(path, user.getRole())) {
            session.setAttribute("error", "Vous n'avez pas les permissions pour accéder à cette page");
            httpResponse.sendRedirect(contextPath + "/error?type=business&message=" + 
                java.net.URLEncoder.encode("Vous n'avez pas les permissions pour accéder à cette page", "UTF-8"));
            return;
        }
        
        // Permission accordée - continuer
        chain.doFilter(request, response);
    }
    
    private boolean isPublicUrl(String path) {
        for (String publicUrl : PUBLIC_URLS) {
            if (path.startsWith(publicUrl)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        // Nettoyage si nécessaire
    }

    /**
     * Vérifie si l'utilisateur a la permission d'accéder à l'URL
     */
    private boolean hasPermission(String path, UserRole userRole) {
        // Chercher une règle correspondante
        for (Map.Entry<String, List<UserRole>> entry : ACCESS_RULES.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                // Règle trouvée - vérifier si le rôle est autorisé
                return entry.getValue().contains(userRole);
            }
        }
        
        // Pas de règle spécifique - accès autorisé par défaut pour les utilisateurs authentifiés
        return true;
    }
}

