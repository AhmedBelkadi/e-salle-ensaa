package com.esalle.features.filiere.controller;

import com.esalle.features.auth.repository.UserRepository;
import com.esalle.features.auth.repository.UserRepositoryImpl;
import com.esalle.features.filiere.domain.Filiere;
import com.esalle.features.filiere.domain.Filiere.Cycle;
import com.esalle.features.filiere.repository.FiliereRepository;
import com.esalle.features.filiere.repository.FiliereRepositoryImpl;
import com.esalle.features.filiere.service.FiliereService;
import com.esalle.features.filiere.service.FiliereServiceImpl;
import com.esalle.shared.util.HibernateUtil;
import com.esalle.shared.exception.ApplicationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet pour gérer les opérations CRUD sur les Filières
 * 
 * Routes:
 * GET  /filieres/list    - Liste toutes les filières avec filtres
 * GET  /filieres/new     - Formulaire nouvelle filière
 * POST /filieres/save    - Créer/Modifier une filière
 * GET  /filieres/edit    - Formulaire édition filière
 * GET  /filieres/view    - Afficher détails filière
 * GET  /filieres/delete  - Supprimer une filière
 * POST /filieres/assign-coordinateur - Assigner un coordinateur
 * POST /filieres/remove-coordinateur - Retirer un coordinateur
 */
@WebServlet(name = "FiliereServlet", urlPatterns = {
    "/filieres/list",
    "/filieres/new",
    "/filieres/save",
    "/filieres/edit",
    "/filieres/view",
    "/filieres/delete",
    "/filieres/search",
    "/filieres/assign-coordinateur",
    "/filieres/remove-coordinateur"
})
public class FiliereServlet extends HttpServlet {

    private FiliereService filiereService;

    @Override
    public void init() throws ServletException {
        super.init();
        FiliereRepository filiereRepository = new FiliereRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl();
        this.filiereService = new FiliereServiceImpl(filiereRepository, userRepository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        try {
            switch (path) {
                case "/filieres/list":
                    handleList(request, response);
                    break;
                case "/filieres/new":
                    handleNew(request, response);
                    break;
                case "/filieres/edit":
                    handleEdit(request, response);
                    break;
                case "/filieres/view":
                    handleView(request, response);
                    break;
                case "/filieres/delete":
                    handleDelete(request, response);
                    break;
                case "/filieres/search":
                    handleSearch(request, response);
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
                case "/filieres/save":
                    handleSave(request, response);
                    break;
                case "/filieres/assign-coordinateur":
                    handleAssignCoordinateur(request, response);
                    break;
                case "/filieres/remove-coordinateur":
                    handleRemoveCoordinateur(request, response);
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
     * Affiche la liste des filières avec filtres
     */
    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Récupération des paramètres de filtre
        String cycleParam = request.getParameter("cycle");
        String anneeParam = request.getParameter("annee");
        String keyword = request.getParameter("keyword");

        // Conversion des paramètres
        Cycle cycle = (cycleParam != null && !cycleParam.isEmpty()) ? Cycle.valueOf(cycleParam) : null;
        Integer annee = (anneeParam != null && !anneeParam.isEmpty()) ? Integer.parseInt(anneeParam) : null;

        // Récupération des filières filtrées
        List<Filiere> filieres = filiereService.filterFilieres(cycle, annee, keyword);

        // Statistiques
        long countPrepa = filiereService.countByCycle(Cycle.PREPARATOIRE);
        long countIngenieur = filiereService.countByCycle(Cycle.INGENIEUR);

        // Passage des données à la JSP
        request.setAttribute("filieres", filieres);
        request.setAttribute("countPrepa", countPrepa);
        request.setAttribute("countIngenieur", countIngenieur);
        request.setAttribute("cycleFilter", cycleParam);
        request.setAttribute("anneeFilter", anneeParam);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/WEB-INF/views/filiere/list.jsp").forward(request, response);
    }

    /**
     * Affiche le formulaire de création
     */
    private void handleNew(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/WEB-INF/views/filiere/form.jsp").forward(request, response);
    }

    /**
     * Affiche le formulaire d'édition
     */
    private void handleEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Long id = Long.parseLong(request.getParameter("id"));
        Filiere filiere = filiereService.getFiliereById(id);
        
        request.setAttribute("filiere", filiere);
        request.getRequestDispatcher("/WEB-INF/views/filiere/form.jsp").forward(request, response);
    }

    /**
     * Affiche les détails d'une filière
     */
    private void handleView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Long id = Long.parseLong(request.getParameter("id"));
        Filiere filiere = filiereService.getFiliereById(id);
        
        request.setAttribute("filiere", filiere);
        request.getRequestDispatcher("/WEB-INF/views/filiere/view.jsp").forward(request, response);
    }

    /**
     * Sauvegarde (création automatique multiple ou modification simple)
     */
    private void handleSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        String nom = request.getParameter("nom");
        String cycleParam = request.getParameter("cycle");
        String effectifParam = request.getParameter("effectif");
        String description = request.getParameter("description");

        try {
            HttpSession session = request.getSession();
            
            // Si ID présent = modification d'une filière existante
            if (idParam != null && !idParam.isEmpty()) {
                Filiere filiere = filiereService.getFiliereById(Long.parseLong(idParam));
                filiere.setNom(nom);
                filiere.setCycle(Cycle.valueOf(cycleParam));
                filiere.setEffectif(Integer.parseInt(effectifParam));
                filiere.setDescription(description);
                
                filiereService.updateFiliere(filiere);
                session.setAttribute("success", "Filière modifiée avec succès");
            } 
            // Sinon = création automatique de 2 ou 3 filières
            else {
                Cycle cycle = Cycle.valueOf(cycleParam);
                Integer effectif = Integer.parseInt(effectifParam);
                
                List<Filiere> createdFilieres = filiereService.createFilieres(nom, cycle, effectif, description);
                
                int count = createdFilieres.size();
                String cycleText = cycle == Cycle.PREPARATOIRE ? "Préparatoire" : "Ingénieur";
                session.setAttribute("success", 
                    count + " filières " + cycleText + " créées avec succès pour " + nom);
            }

            response.sendRedirect(request.getContextPath() + "/filieres/list");

        } catch (ApplicationException e) {
            // En cas d'erreur, retourner au formulaire avec les données
            request.setAttribute("error", e.getMessage());
            request.setAttribute("nom", nom);
            request.setAttribute("cycle", cycleParam);
            request.setAttribute("effectif", effectifParam);
            request.setAttribute("description", description);
            
            if (idParam != null && !idParam.isEmpty()) {
                request.setAttribute("id", idParam);
            }
            
            request.getRequestDispatcher("/WEB-INF/views/filiere/form.jsp").forward(request, response);
        }
    }

    /**
     * Supprime une filière
     */
    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Long id = Long.parseLong(request.getParameter("id"));
        filiereService.deleteFiliere(id);
        
        HttpSession session = request.getSession();
        session.setAttribute("success", "Filière supprimée avec succès");
        
        response.sendRedirect(request.getContextPath() + "/filieres/list");
    }

    /**
     * Recherche de filières par mot-clé
     */
    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        List<Filiere> filieres = filiereService.filterFilieres(null, null, keyword);
        
        request.setAttribute("filieres", filieres);
        request.setAttribute("keyword", keyword);
        
        request.getRequestDispatcher("/WEB-INF/views/filiere/list.jsp").forward(request, response);
    }

    /**
     * Assigne un coordinateur à une filière
     */
    private void handleAssignCoordinateur(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Long filiereId = Long.parseLong(request.getParameter("filiereId"));
        Long coordinateurId = Long.parseLong(request.getParameter("coordinateurId"));
        
        filiereService.assignCoordinateur(filiereId, coordinateurId);
        
        HttpSession session = request.getSession();
        session.setAttribute("success", "Coordinateur assigné avec succès");
        
        response.sendRedirect(request.getContextPath() + "/filieres/view?id=" + filiereId);
    }

    /**
     * Retire le coordinateur d'une filière
     */
    private void handleRemoveCoordinateur(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Long filiereId = Long.parseLong(request.getParameter("filiereId"));
        
        filiereService.removeCoordinateur(filiereId);
        
        HttpSession session = request.getSession();
        session.setAttribute("success", "Coordinateur retiré avec succès");
        
        response.sendRedirect(request.getContextPath() + "/filieres/view?id=" + filiereId);
    }

    /**
     * Gestion des erreurs
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        session.setAttribute("error", errorMessage);
        
        response.sendRedirect(request.getContextPath() + "/filieres/list");
    }
}

