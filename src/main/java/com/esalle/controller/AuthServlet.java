package com.esalle.controller;

import com.esalle.entity.User;
import com.esalle.service.UserService;
import com.esalle.service.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "AuthServlet", urlPatterns = {"/auth/*"})
public class AuthServlet extends HttpServlet {
    
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        userService = new UserServiceImpl();    
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ensureUserServiceInitialized();
        
        String action = getAction(request);
        
        switch (action) {
            case "login":
                showLoginPage(request, response);
                break;
            case "register":
                showRegisterPage(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/auth/login");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ensureUserServiceInitialized();
        
        String action = getAction(request);
        
        switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "register":
                handleRegister(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/auth/login");
        }
    }
    
    /**
     * Ensure UserService is initialized (lazy initialization)
     */
    private void ensureUserServiceInitialized() throws ServletException {
        if (userService == null) {
            try {
                userService = new UserServiceImpl();
                log("UserService initialized successfully on first request");
            } catch (Exception e) {
                log("Error initializing UserService: " + e.getMessage(), e);
                throw new ServletException("Database connection unavailable. Please check your database configuration.", e);
            }
        }
    }
    
    private void showLoginPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Vérifier si l'utilisateur est déjà connecté
            HttpSession session = request.getSession(false);
            if (session != null) {
                User user = (User) session.getAttribute("user");
                if (user != null && user.getStatut() == User.UserStatus.ACTIF) {
                    // Utilisateur déjà connecté - rediriger vers son dashboard
                    String redirectUrl = getRedirectUrlByRole(user.getRole());
                    response.sendRedirect(request.getContextPath() + redirectUrl);
                    return;
                }
            }
            
            // Empêcher la mise en cache de la page de login
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            
            // Log before forwarding
            log("Forwarding to login page: /WEB-INF/views/auth/login.jsp");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        } catch (ServletException e) {
            log("=== JSP Compilation Error ===");
            log("Error forwarding to login page: " + e.getMessage(), e);
            if (e.getRootCause() != null) {
                log("Root cause: " + e.getRootCause().getClass().getName() + ": " + e.getRootCause().getMessage());
                log("Root cause stack trace:", e.getRootCause());
            }
            log("=== End JSP Error ===");
            
            // Show detailed error page
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>JSP Compilation Error</title></head><body>");
            out.println("<h1>Erreur lors du chargement de la page de connexion</h1>");
            out.println("<h2>Détails de l'erreur:</h2>");
            out.println("<p><strong>Message:</strong> " + escapeHtml(e.getMessage()) + "</p>");
            
            if (e.getRootCause() != null) {
                out.println("<p><strong>Cause:</strong> " + escapeHtml(e.getRootCause().getClass().getName()) + "</p>");
                out.println("<p><strong>Cause Message:</strong> " + escapeHtml(e.getRootCause().getMessage()) + "</p>");
                out.println("<h3>Stack Trace:</h3>");
                out.println("<pre style='background: #f5f5f5; padding: 10px; overflow: auto;'>");
                e.getRootCause().printStackTrace(out);
                out.println("</pre>");
            } else {
                out.println("<h3>Stack Trace:</h3>");
                out.println("<pre style='background: #f5f5f5; padding: 10px; overflow: auto;'>");
                e.printStackTrace(out);
                out.println("</pre>");
            }
            
            out.println("<p><a href='" + request.getContextPath() + "'>Retour à l'accueil</a></p>");
            out.println("</body></html>");
            out.flush();
        } catch (Exception e) {
            log("Unexpected error in showLoginPage: " + e.getMessage(), e);
            throw new ServletException("Unexpected error displaying login page", e);
        }
    }
    
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
    
    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        } catch (ServletException e) {
            log("Error forwarding to register page: " + e.getMessage(), e);
            throw e;
        }
    }
    
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        try {
            Optional<User> userOpt = userService.login(email, password);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // Créer session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userRole", user.getRole());
                session.setAttribute("userName", user.getPrenom() + " " + user.getNom());
                
                log("User logged in: " + user.getEmail() + " (Role: " + user.getRole() + ")");
                
                // Rediriger selon le rôle
                String redirectUrl = getRedirectUrlByRole(user.getRole());
                response.sendRedirect(request.getContextPath() + redirectUrl);
            } else {
                request.setAttribute("error", "Email ou mot de passe incorrect");
                showLoginPage(request, response);
            }
        } catch (Exception e) {
            log("Login error", e);
            request.setAttribute("error", e.getMessage());
            showLoginPage(request, response);
        }
    }
    
    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Récupérer les paramètres
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String email = request.getParameter("email");
            String telephone = request.getParameter("telephone");
            String password = request.getParameter("password");
            String passwordConfirm = request.getParameter("passwordConfirm");
            String roleStr = request.getParameter("role");
            
            // Validation
            if (nom == null || nom.trim().isEmpty() ||
                prenom == null || prenom.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                roleStr == null || roleStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Tous les champs sont obligatoires");
            }
            
            // Vérifier passwordConfirm (peut être null si le formulaire ne l'envoie pas)
            if (passwordConfirm == null || passwordConfirm.trim().isEmpty()) {
                throw new IllegalArgumentException("La confirmation du mot de passe est obligatoire");
            }
            
            if (!password.equals(passwordConfirm)) {
                throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
            }
            
            // Créer l'utilisateur
            User user = new User();
            user.setNom(nom.trim());
            user.setPrenom(prenom.trim());
            user.setEmail(email.trim().toLowerCase());
            if (telephone != null && !telephone.trim().isEmpty()) {
                user.setTelephone(telephone.trim());
            }
            user.setPassword(password);
            user.setRole(User.UserRole.valueOf(roleStr));
            
            // Pour MEMBRE_CLUB, récupérer le nom du club
            if (user.getRole() == User.UserRole.MEMBRE_CLUB) {
                String nomClub = request.getParameter("nomClub");
                if (nomClub == null || nomClub.trim().isEmpty()) {
                    throw new IllegalArgumentException("Le nom du club est obligatoire pour les membres de club");
                }
                user.setNomClub(nomClub.trim());
            }
            
            // Enregistrer
            User savedUser = userService.register(user);
            
            log("New user registered: " + savedUser.getEmail() + " (Role: " + savedUser.getRole() + ")");
            
            // Message de succès selon le statut
            String successMessage;
            if (savedUser.getStatut() == User.UserStatus.ACTIF) {
                successMessage = "Inscription réussie ! Votre compte a été automatiquement validé. Vous pouvez vous connecter.";
            } else {
                successMessage = "Inscription réussie ! Votre compte est en attente d'approbation par l'administrateur.";
            }
            
            // Rediriger vers login avec message de succès
            request.setAttribute("success", successMessage);
            showLoginPage(request, response);
            
        } catch (Exception e) {
            log("Registration error", e);
            request.setAttribute("error", e.getMessage());
            
            // Garder les valeurs saisies
            request.setAttribute("nom", request.getParameter("nom"));
            request.setAttribute("prenom", request.getParameter("prenom"));
            request.setAttribute("email", request.getParameter("email"));
            request.setAttribute("telephone", request.getParameter("telephone"));
            request.setAttribute("role", request.getParameter("role"));
            
            showRegisterPage(request, response);
        }
    }
    
    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String userEmail = session.getAttribute("user") != null ? 
                ((User) session.getAttribute("user")).getEmail() : "unknown";
            log("User logged out: " + userEmail);
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/auth/login");
    }
    
    private String getAction(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return "login";
        }
        return pathInfo.substring(1);
    }
    
    private String getRedirectUrlByRole(User.UserRole role) {
        // Rediriger vers le dashboard selon le rôle
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

