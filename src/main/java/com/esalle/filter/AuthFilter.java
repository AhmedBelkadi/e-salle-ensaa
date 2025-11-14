package com.esalle.filter;

import com.esalle.entity.User;
import com.esalle.entity.User.UserStatus;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filter d'authentification - Vérifie que l'utilisateur est connecté
 * S'applique à toutes les URLs sauf les pages publiques
 */
public class AuthFilter implements Filter {

    // URLs publiques (accessibles sans authentification)
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
        
        // Vérifier si la requête est pour une URL publique
        if (isPublicUrl(path)) {
            // Si c'est la page de login et que l'utilisateur est déjà connecté, rediriger vers son dashboard
            if (path.equals("/auth/login") || path.startsWith("/auth/login")) {
                HttpSession session = httpRequest.getSession(false);
                User user = (session != null) ? (User) session.getAttribute("user") : null;
                if (user != null && user.getStatut() == UserStatus.ACTIF) {
                    // Rediriger selon le rôle
                    String redirectUrl = getRedirectUrlByRole(user.getRole());
                    httpResponse.sendRedirect(contextPath + redirectUrl);
                    return;
                }
                // Empêcher la mise en cache de la page de login
                httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                httpResponse.setHeader("Pragma", "no-cache");
                httpResponse.setHeader("Expires", "0");
            }
            chain.doFilter(request, response);
            return;
        }
        
        // Vérifier si l'utilisateur est connecté
        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        
        if (user == null) {
            // Utilisateur non connecté - rediriger vers login
            session = httpRequest.getSession(true);
            session.setAttribute("error", "Vous devez être connecté pour accéder à cette page");
            session.setAttribute("redirectUrl", requestURI); // Sauvegarder l'URL demandée
            httpResponse.sendRedirect(contextPath + "/auth/login");
            return;
        }
        
        // Vérifier si le compte est approuvé
        if (user.getStatut() != UserStatus.ACTIF) {
            session.setAttribute("error", "Votre compte est en attente d'approbation");
            httpResponse.sendRedirect(contextPath + "/auth/login");
            return;
        }
        
        // Utilisateur authentifié et approuvé - continuer
        chain.doFilter(request, response);
    }



    /**
     * Vérifie si l'URL est publique (accessible sans authentification)
     */
    private boolean isPublicUrl(String path) {
        // Page d'accueil est publique
        if (path.equals("/") || path.equals("/index.jsp")) {
            return true;
        }
        
        // Vérifier les préfixes publics
        for (String publicUrl : PUBLIC_URLS) {
            if (path.startsWith(publicUrl)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Retourne l'URL de redirection selon le rôle de l'utilisateur
     */
    private String getRedirectUrlByRole(User.UserRole role) {
        switch (role) {
            case ADMIN:
                return "/consultation/admin";
            case COORDINATEUR:
                return "/consultation/coordinateur";
            case PROFESSEUR:
                return "/consultation/professeur";
            case MEMBRE_CLUB:
                return "/consultation/club";
            default:
                return "/";
        }
    }
}

