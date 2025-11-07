package com.esalle.controller;

import com.esalle.entity.Matiere;
import com.esalle.entity.User;
import com.esalle.entity.Filiere;
import com.esalle.service.MatiereService;
import com.esalle.service.MatiereServiceImpl;
import com.esalle.service.FiliereService;
import com.esalle.service.FiliereServiceImpl;
import com.esalle.service.UserService;
import com.esalle.service.UserServiceImpl;
import com.esalle.exception.BusinessException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servlet pour gérer les matières
 * 
 * Routes:
 * GET  /matieres/list    - Liste toutes les matières
 * GET  /matieres/new     - Formulaire nouvelle matière
 * POST /matieres/save    - Créer/Modifier une matière
 * GET  /matieres/edit    - Formulaire édition matière
 * GET  /matieres/view    - Afficher détails matière
 * GET  /matieres/delete  - Supprimer une matière
 */
@WebServlet(name = "MatiereServlet", urlPatterns = {
    "/matieres/list",
    "/matieres/new",
    "/matieres/save",
    "/matieres/edit",
    "/matieres/view",
    "/matieres/delete"
})
public class MatiereServlet extends HttpServlet {

    private MatiereService matiereService;
    private FiliereService filiereService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.matiereService = new MatiereServiceImpl();
        this.filiereService = new FiliereServiceImpl(
            new com.esalle.repository.FiliereRepositoryImpl(),
            new com.esalle.repository.UserRepositoryImpl());
        this.userService = new UserServiceImpl();
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
        
        // Vérifier les permissions pour création/modification/suppression
        String path = request.getServletPath();
        if (("/matieres/new".equals(path) || "/matieres/edit".equals(path) || "/matieres/delete".equals(path) || "/matieres/save".equals(path)) &&
            currentUser.getRole() != User.UserRole.COORDINATEUR) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Seuls les coordinateurs peuvent créer, modifier ou supprimer des matières.");
            return;
        }
        
        try {
            switch (path) {
                case "/matieres/list":
                    handleList(request, response);
                    break;
                case "/matieres/new":
                    handleNew(request, response);
                    break;
                case "/matieres/edit":
                    handleEdit(request, response);
                    break;
                case "/matieres/view":
                    handleView(request, response);
                    break;
                case "/matieres/delete":
                    handleDelete(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/matieres/list");
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
        
        // Vérifier les permissions pour création/modification
        String path = request.getServletPath();
        if ("/matieres/save".equals(path) && currentUser.getRole() != User.UserRole.COORDINATEUR) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Seuls les coordinateurs peuvent créer ou modifier des matières.");
            return;
        }
        
        try {
            if ("/matieres/save".equals(path)) {
                handleSave(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/matieres/list");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
        }
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String filiereIdStr = request.getParameter("filiereId");
        String search = request.getParameter("search");
        
        List<Matiere> matieres;
        
        if (filiereIdStr != null && !filiereIdStr.isEmpty()) {
            Long filiereId = Long.parseLong(filiereIdStr);
            matieres = matiereService.getMatieresByFiliere(filiereId);
        } else if (search != null && !search.trim().isEmpty()) {
            matieres = matiereService.searchMatieres(search);
        } else {
            matieres = matiereService.getAllMatieres();
        }
        
        List<Filiere> filieres = filiereService.getAllFilieres();
        
        request.setAttribute("matieres", matieres);
        request.setAttribute("filieres", filieres);
        request.setAttribute("selectedFiliereId", filiereIdStr);
        request.setAttribute("search", search);
        
        request.getRequestDispatcher("/WEB-INF/views/matiere/list.jsp").forward(request, response);
    }

    private void handleNew(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Filiere> filieres = filiereService.getAllFilieres();
        List<User> professeurs = userService.findAll().stream()
            .filter(u -> u.getRole() == User.UserRole.PROFESSEUR && u.getStatut() == User.UserStatus.ACTIF)
            .collect(Collectors.toList());
        
        request.setAttribute("filieres", filieres);
        request.setAttribute("professeurs", professeurs);
        request.setAttribute("matiere", new Matiere());
        request.getRequestDispatcher("/WEB-INF/views/matiere/form.jsp").forward(request, response);
    }

    private void handleEdit(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Long id = Long.parseLong(request.getParameter("id"));
        Matiere matiere = matiereService.getMatiereById(id)
                .orElseThrow(() -> new BusinessException("Matière introuvable."));
        
        List<Filiere> filieres = filiereService.getAllFilieres();
        List<User> professeurs = userService.findAll().stream()
            .filter(u -> u.getRole() == User.UserRole.PROFESSEUR && u.getStatut() == User.UserStatus.ACTIF)
            .collect(Collectors.toList());
        
        request.setAttribute("matiere", matiere);
        request.setAttribute("filieres", filieres);
        request.setAttribute("professeurs", professeurs);
        request.getRequestDispatcher("/WEB-INF/views/matiere/form.jsp").forward(request, response);
    }

    private void handleView(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Long id = Long.parseLong(request.getParameter("id"));
        Matiere matiere = matiereService.getMatiereById(id)
                .orElseThrow(() -> new BusinessException("Matière introuvable."));
        
        request.setAttribute("matiere", matiere);
        request.getRequestDispatcher("/WEB-INF/views/matiere/view.jsp").forward(request, response);
    }

    private void handleSave(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            Matiere matiere = new Matiere();
            
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                matiere = matiereService.getMatiereById(Long.parseLong(idStr))
                        .orElse(new Matiere());
            }
            
            matiere.setNom(request.getParameter("nom"));
            matiere.setFiliereId(Long.parseLong(request.getParameter("filiereId")));
            matiere.setProfesseurId(Long.parseLong(request.getParameter("professeurId")));
            matiere.setHeuresCours(Integer.parseInt(request.getParameter("heuresCours")));
            matiere.setHeuresTD(Integer.parseInt(request.getParameter("heuresTD")));
            matiere.setHeuresTP(Integer.parseInt(request.getParameter("heuresTP")));
            matiere.setDescription(request.getParameter("description"));
            
            matiereService.saveMatiere(matiere);
            
            response.sendRedirect(request.getContextPath() + "/matieres/list?success=saved");
        } catch (BusinessException e) {
            request.setAttribute("error", e.getMessage());
            handleNew(request, response);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Long id = Long.parseLong(request.getParameter("id"));
        matiereService.deleteMatiere(id);
        
        response.sendRedirect(request.getContextPath() + "/matieres/list?success=deleted");
    }
}

