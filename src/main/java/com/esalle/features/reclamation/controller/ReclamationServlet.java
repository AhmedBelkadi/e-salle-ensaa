package com.esalle.features.reclamation.controller;

import com.esalle.features.auth.domain.User;
import com.esalle.features.auth.repository.UserRepository;
import com.esalle.features.auth.repository.UserRepositoryImpl;
import com.esalle.features.reclamation.domain.Reclamation;
import com.esalle.features.reclamation.domain.Reclamation.Statut;
import com.esalle.features.reclamation.domain.Reclamation.Urgence;
import com.esalle.features.reclamation.repository.ReclamationRepository;
import com.esalle.features.reclamation.repository.ReclamationRepositoryImpl;
import com.esalle.features.reclamation.service.ReclamationService;
import com.esalle.features.reclamation.service.ReclamationServiceImpl;
import com.esalle.features.salle.repository.SalleRepository;
import com.esalle.features.salle.repository.SalleRepositoryImpl;
import com.esalle.shared.exception.ApplicationException;
import com.esalle.shared.service.NotificationService;
import com.esalle.shared.service.NotificationServiceImpl;
import com.esalle.shared.util.HibernateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet pour gérer les opérations CRUD sur les Réclamations
 * 
 * Routes:
 * GET  /reclamations/list    - Liste toutes les réclamations
 * GET  /reclamations/new     - Formulaire nouvelle réclamation
 * POST /reclamations/create  - Créer une réclamation
 * GET  /reclamations/view    - Afficher détails réclamation
 * POST /reclamations/traiter - Traiter une réclamation (admin)
 * GET  /reclamations/delete  - Supprimer une réclamation
 */
@WebServlet(name = "ReclamationServlet", urlPatterns = {
    "/reclamations/list",
    "/reclamations/new",
    "/reclamations/create",
    "/reclamations/view",
    "/reclamations/traiter",
    "/reclamations/delete",
    "/reclamations/mes-reclamations"
})
public class ReclamationServlet extends HttpServlet {

    private ReclamationService reclamationService;

    @Override
    public void init() throws ServletException {
        super.init();
        ReclamationRepository reclamationRepository = new ReclamationRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl();
        SalleRepository salleRepository = new SalleRepositoryImpl();
        NotificationService notificationService = new NotificationServiceImpl();
        
        this.reclamationService = new ReclamationServiceImpl(
            reclamationRepository,
            userRepository,
            salleRepository,
            notificationService
        );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        try {
            switch (path) {
                case "/reclamations/list":
                    handleList(request, response);
                    break;
                case "/reclamations/mes-reclamations":
                    handleMesReclamations(request, response);
                    break;
                case "/reclamations/new":
                    handleNew(request, response);
                    break;
                case "/reclamations/view":
                    handleView(request, response);
                    break;
                case "/reclamations/delete":
                    handleDelete(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ApplicationException e) {
            handleError(request, response, e.getMessage());
        } catch (Exception e) {
            handleError(request, response, "Une erreur inattendue s'est produite: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        try {
            switch (path) {
                case "/reclamations/create":
                    handleCreate(request, response);
                    break;
                case "/reclamations/traiter":
                    handleTraiter(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ApplicationException e) {
            handleError(request, response, e.getMessage());
        } catch (Exception e) {
            handleError(request, response, "Une erreur inattendue s'est produite: " + e.getMessage());
        }
    }

    /**
     * Affiche la liste de toutes les réclamations (Admin)
     */
    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Récupération des paramètres de filtre
        String statutParam = request.getParameter("statut");
        String urgenceParam = request.getParameter("urgence");

        Statut statut = (statutParam != null && !statutParam.isEmpty()) ? Statut.valueOf(statutParam) : null;
        Urgence urgence = (urgenceParam != null && !urgenceParam.isEmpty()) ? Urgence.valueOf(urgenceParam) : null;

        // Récupération des réclamations filtrées
        List<Reclamation> reclamations = reclamationService.filterReclamations(statut, urgence, null, null);

        // Statistiques
        long countEnAttente = reclamationService.countByStatut(Statut.EN_ATTENTE);
        long countTraitees = reclamationService.countByStatut(Statut.TRAITEE);
        long countUrgentes = reclamationService.countUrgentesEnAttente();

        // Passage des données à la JSP
        request.setAttribute("reclamations", reclamations);
        request.setAttribute("countEnAttente", countEnAttente);
        request.setAttribute("countTraitees", countTraitees);
        request.setAttribute("countUrgentes", countUrgentes);
        request.setAttribute("statutFilter", statutParam);
        request.setAttribute("urgenceFilter", urgenceParam);

        request.getRequestDispatcher("/WEB-INF/views/reclamation/list.jsp").forward(request, response);
    }

    /**
     * Affiche les réclamations de l'utilisateur connecté
     */
    private void handleMesReclamations(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        List<Reclamation> reclamations = reclamationService.getReclamationsByUser(currentUser.getId());
        
        request.setAttribute("reclamations", reclamations);
        request.setAttribute("isUserView", true);
        
        request.getRequestDispatcher("/WEB-INF/views/reclamation/list.jsp").forward(request, response);
    }

    /**
     * Affiche le formulaire de création
     */
    private void handleNew(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/WEB-INF/views/reclamation/form.jsp").forward(request, response);
    }

    /**
     * Affiche les détails d'une réclamation
     */
    private void handleView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Long id = Long.parseLong(request.getParameter("id"));
        Reclamation reclamation = reclamationService.getReclamationById(id);
        
        request.setAttribute("reclamation", reclamation);
        request.getRequestDispatcher("/WEB-INF/views/reclamation/view.jsp").forward(request, response);
    }

    /**
     * Crée une nouvelle réclamation
     */
    private void handleCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        String salleIdParam = request.getParameter("salleId");
        String description = request.getParameter("description");
        String urgenceParam = request.getParameter("urgence");

        try {
            Long salleId = Long.parseLong(salleIdParam);
            Urgence urgence = Urgence.valueOf(urgenceParam);
            
            reclamationService.createReclamation(currentUser.getId(), salleId, description, urgence);
            
            session.setAttribute("success", "Réclamation créée avec succès. L'administrateur a été notifié.");
            response.sendRedirect(request.getContextPath() + "/reclamations/mes-reclamations");

        } catch (ApplicationException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("salleId", salleIdParam);
            request.setAttribute("description", description);
            request.setAttribute("urgence", urgenceParam);
            
            request.getRequestDispatcher("/WEB-INF/views/reclamation/form.jsp").forward(request, response);
        }
    }

    /**
     * Traite une réclamation (Admin uniquement)
     */
    private void handleTraiter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || currentUser.getRole() != User.UserRole.ADMIN) {
            session.setAttribute("error", "Seul un administrateur peut traiter une réclamation");
            response.sendRedirect(request.getContextPath() + "/reclamations/list");
            return;
        }

        String reclamationIdParam = request.getParameter("reclamationId");
        String commentaire = request.getParameter("commentaire");

        try {
            Long reclamationId = Long.parseLong(reclamationIdParam);
            
            reclamationService.traiterReclamation(reclamationId, currentUser.getId(), commentaire);
            
            session.setAttribute("success", "Réclamation traitée avec succès. L'utilisateur a été notifié.");
            response.sendRedirect(request.getContextPath() + "/reclamations/list");

        } catch (ApplicationException e) {
            session.setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/reclamations/view?id=" + reclamationIdParam);
        }
    }

    /**
     * Supprime une réclamation
     */
    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Long id = Long.parseLong(request.getParameter("id"));
        reclamationService.deleteReclamation(id);
        
        HttpSession session = request.getSession();
        session.setAttribute("success", "Réclamation supprimée avec succès");
        
        response.sendRedirect(request.getContextPath() + "/reclamations/list");
    }

    /**
     * Gestion des erreurs
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        session.setAttribute("error", errorMessage);
        
        response.sendRedirect(request.getContextPath() + "/reclamations/list");
    }
}

