package com.esalle.shared.filter;

import com.esalle.features.auth.domain.User;
import com.esalle.features.auth.domain.User.UserStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filter d'authentification - Vérifie que l'utilisateur est connecté
 * S'applique à toutes les URLs sauf les pages publiques
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    // URLs publiques (accessibles sans authentification)
    private static final List<String> PUBLIC_URLS = Arrays.asList(
        "/auth/login",
        "/auth/register",
        "/auth/logout",
        "/css/",
        "/js/",
        "/images/",
        "/favicon.ico"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation si nécessaire
    }

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

    @Override
    public void destroy() {
        // Nettoyage si nécessaire
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
}

