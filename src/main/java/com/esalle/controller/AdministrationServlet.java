package com.esalle.controller;

import com.esalle.entity.User;
import com.esalle.entity.Filiere;
import com.esalle.service.UserService;
import com.esalle.service.UserServiceImpl;
import com.esalle.service.FiliereService;
import com.esalle.service.FiliereServiceImpl;
import com.esalle.repository.FiliereRepository;
import com.esalle.repository.FiliereRepositoryImpl;
import com.esalle.exception.BusinessException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet pour gérer l'administration (approbation inscriptions, gestion users)
 * 
 * Routes:
 * GET  /admin/users/pending    - Liste des utilisateurs en attente
 * GET  /admin/users/all        - Liste de tous les utilisateurs
 * POST /admin/users/approve    - Approuver un utilisateur
 * POST /admin/users/refuse     - Refuser un utilisateur
 * GET  /admin/users/view       - Voir détails d'un utilisateur
 */
@WebServlet(name = "AdministrationServlet", urlPatterns = {
    "/admin/users/pending",
    "/admin/users/all",
    "/admin/users/approve",
    "/admin/users/refuse",
    "/admin/users/view"
})
public class AdministrationServlet extends HttpServlet {

    private UserService userService;
    private FiliereService filiereService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserServiceImpl();
        FiliereRepository filiereRepository = new FiliereRepositoryImpl();
        this.filiereService = new FiliereServiceImpl(filiereRepository, 
            new com.esalle.repository.UserRepositoryImpl());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        if (currentUser.getRole() != User.UserRole.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux administrateurs.");
            return;
        }

        String path = request.getServletPath();
        
        try {
            switch (path) {
                case "/admin/users/pending":
                    handlePendingUsers(request, response);
                    break;
                case "/admin/users/all":
                    handleAllUsers(request, response);
                    break;
                case "/admin/users/view":
                    handleViewUser(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/users/pending");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        if (currentUser.getRole() != User.UserRole.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux administrateurs.");
            return;
        }

        String path = request.getServletPath();
        
        try {
            switch (path) {
                case "/admin/users/approve":
                    handleApproveUser(request, response);
                    break;
                case "/admin/users/refuse":
                    handleRefuseUser(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/users/pending");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/error.jsp").forward(request, response);
        }
    }

    private void handlePendingUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Initialiser les services si nécessaire
            if (userService == null) {
                userService = new UserServiceImpl();
            }
            if (filiereService == null) {
                FiliereRepository filiereRepository = new FiliereRepositoryImpl();
                filiereService = new FiliereServiceImpl(filiereRepository, 
                    new com.esalle.repository.UserRepositoryImpl());
            }
            
            List<User> pendingUsers = userService.findPendingUsers();
            List<Filiere> filieres = filiereService.getAllFilieres();
            
            // S'assurer que les listes ne sont pas null
            if (pendingUsers == null) {
                pendingUsers = new java.util.ArrayList<>();
            }
            if (filieres == null) {
                filieres = new java.util.ArrayList<>();
            }
            
            // Créer une map des noms de filières uniques pour les coordinateurs
            java.util.Map<String, Long> nomsFilieresUniques = new java.util.LinkedHashMap<>();
            for (Filiere filiere : filieres) {
                if (!nomsFilieresUniques.containsKey(filiere.getNom())) {
                    // Stocker l'ID de la première filière avec ce nom
                    nomsFilieresUniques.put(filiere.getNom(), filiere.getId());
                }
            }
            
            request.setAttribute("pendingUsers", pendingUsers);
            request.setAttribute("filieres", filieres);
            request.setAttribute("nomsFilieresUniques", nomsFilieresUniques);
            request.getRequestDispatcher("/WEB-INF/views/admin/pending-users.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in handlePendingUsers: " + e.getMessage(), e);
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors du chargement des utilisateurs en attente: " + e.getMessage());
            request.setAttribute("errorMessage", "Erreur lors du chargement des utilisateurs en attente: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
        }
    }

    private void handleAllUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<User> allUsers = userService.findAll();
            
            // S'assurer que la liste n'est pas null
            if (allUsers == null) {
                allUsers = new java.util.ArrayList<>();
            }
            
            log("Loading all users: " + allUsers.size() + " users found");
            
            request.setAttribute("users", allUsers);
            request.getRequestDispatcher("/WEB-INF/views/admin/all-users.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in handleAllUsers: " + e.getMessage(), e);
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors du chargement des utilisateurs: " + e.getMessage());
            request.setAttribute("errorMessage", "Erreur lors du chargement des utilisateurs: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
        }
    }

    private void handleViewUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String userIdStr = request.getParameter("id");
        if (userIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/admin/users/pending");
            return;
        }

        Long userId = Long.parseLong(userIdStr);
        User user = userService.findById(userId)
                .orElseThrow(() -> new BusinessException("Utilisateur introuvable."));
        
        List<Filiere> filieres = filiereService.getAllFilieres();
        
        request.setAttribute("user", user);
        request.setAttribute("filieres", filieres);
        request.getRequestDispatcher("/WEB-INF/views/admin/view-user.jsp").forward(request, response);
    }

    private void handleApproveUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String userIdStr = request.getParameter("userId");
            String filiereIdStr = request.getParameter("filiereId");
            String nomClub = request.getParameter("nomClub");
            
            HttpSession session = request.getSession();
            User admin = (User) session.getAttribute("user");

            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin/users/pending?error=missing_user_id");
                return;
            }

            Long userId;
            try {
                userId = Long.parseLong(userIdStr);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/users/pending?error=invalid_user_id");
                return;
            }

            // Récupérer l'utilisateur pour vérifier son rôle
            User userToApprove = userService.findById(userId)
                    .orElse(null);
            
            if (userToApprove == null) {
                response.sendRedirect(request.getContextPath() + "/admin/users/pending?error=user_not_found");
                return;
            }

            // Vérifier que les données requises sont fournies selon le rôle
            // Seuls les coordinateurs nécessitent une filière (les professeurs peuvent enseigner dans toutes les filières)
            if (userToApprove.getRole() == User.UserRole.COORDINATEUR &&
                (filiereIdStr == null || filiereIdStr.trim().isEmpty())) {
                response.sendRedirect(request.getContextPath() + "/admin/users/pending?error=missing_filiere&userId=" + userId);
                return;
            }

            if (userToApprove.getRole() == User.UserRole.MEMBRE_CLUB &&
                (nomClub == null || nomClub.trim().isEmpty())) {
                response.sendRedirect(request.getContextPath() + "/admin/users/pending?error=missing_club_name&userId=" + userId);
                return;
            }

            Long filiereId = (filiereIdStr != null && !filiereIdStr.trim().isEmpty()) 
                    ? Long.parseLong(filiereIdStr) : null;

            // Si c'est un coordinateur, assigner à toutes les années de la filière
            if (userToApprove.getRole() == User.UserRole.COORDINATEUR && filiereId != null) {
                // Récupérer la filière sélectionnée pour obtenir son nom
                Filiere filiereSelectionnee = filiereService.getFiliereById(filiereId);
                String nomFiliere = filiereSelectionnee.getNom();
                
                // Récupérer toutes les filières avec ce nom (toutes années et cycles)
                List<Filiere> toutesFilieres = filiereService.getFilieresByNomExact(nomFiliere);
                
                // Assigner le coordinateur à toutes ces filières
                for (Filiere filiere : toutesFilieres) {
                    filiereService.assignCoordinateur(filiere.getId(), userId);
                }
                
                // Approuver l'utilisateur avec la première filière (pour le filiereId du User)
                userService.approveUser(userId, filiereId, nomClub, admin.getId());
                
                log("Coordinateur " + userId + " assigné à toutes les années de la filière: " + nomFiliere + " (" + toutesFilieres.size() + " filières)");
            } else {
                // Pour les professeurs et autres rôles, comportement normal
                userService.approveUser(userId, filiereId, nomClub, admin.getId());
            }
            
            response.sendRedirect(request.getContextPath() + "/admin/users/pending?success=approved");
        } catch (BusinessException e) {
            log("Business error in handleApproveUser: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/users/pending?error=" + 
                java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            log("Error in handleApproveUser: " + e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + "/admin/users/pending?error=unexpected_error");
        }
    }

    private void handleRefuseUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String userIdStr = request.getParameter("userId");
        
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");

        if (userIdStr == null) {
            throw new BusinessException("ID utilisateur manquant.");
        }

        Long userId = Long.parseLong(userIdStr);
        userService.refuseUser(userId, admin.getId());
        
        response.sendRedirect(request.getContextPath() + "/admin/users/pending?success=refused");
    }
}

