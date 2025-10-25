package com.esalle.features.auth.controller;

import com.esalle.features.auth.domain.User;
import com.esalle.features.auth.service.UserService;
import com.esalle.features.auth.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "AuthServlet", urlPatterns = {"/auth/*"})
public class AuthServlet extends HttpServlet {
    
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        userService = new UserServiceImpl();
        // Créer admin au démarrage de l'application
        try {
            User admin = userService.createAdmin();
            log("Admin account created/verified: " + admin.getEmail());
        } catch (Exception e) {
            log("Error creating admin account", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
    
    private void showLoginPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }
    
    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
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
            
            if (!password.equals(passwordConfirm)) {
                throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
            }
            
            // Créer l'utilisateur
            User user = new User();
            user.setNom(nom.trim());
            user.setPrenom(prenom.trim());
            user.setEmail(email.trim().toLowerCase());
            user.setPassword(password);
            user.setRole(User.UserRole.valueOf(roleStr));
            
            // Enregistrer
            User savedUser = userService.register(user);
            
            log("New user registered: " + savedUser.getEmail() + " (Role: " + savedUser.getRole() + ")");
            
            // TODO: Envoyer notification à l'admin
            
            // Rediriger vers login avec message de succès
            request.setAttribute("success", 
                "Inscription réussie ! Votre compte est en attente d'approbation par l'administrateur.");
            showLoginPage(request, response);
            
        } catch (Exception e) {
            log("Registration error", e);
            request.setAttribute("error", e.getMessage());
            
            // Garder les valeurs saisies
            request.setAttribute("nom", request.getParameter("nom"));
            request.setAttribute("prenom", request.getParameter("prenom"));
            request.setAttribute("email", request.getParameter("email"));
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
        switch (role) {
            case ADMIN:
                return "/admin/dashboard";
            case COORDINATEUR:
                return "/coordinateur/dashboard";
            case PROFESSEUR:
                return "/professeur/dashboard";
            case MEMBRE_CLUB:
                return "/club/dashboard";
            default:
                return "/";
        }
    }
}

