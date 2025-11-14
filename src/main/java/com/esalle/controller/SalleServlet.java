package com.esalle.controller;

import com.esalle.entity.Salle;
import com.esalle.service.SalleService;
import com.esalle.service.SalleServiceImpl;
import com.esalle.util.PaginationHelper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "SalleServlet", urlPatterns = {"/salles/*"})
public class SalleServlet extends HttpServlet {
    
    private SalleService salleService;
    
    @Override
    public void init() throws ServletException {
        salleService = new SalleServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = getAction(request);
        
        try {
            switch (action) {
                case "list":
                    listSalles(request, response);
                    break;
                case "view":
                    viewSalle(request, response);
                    break;
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteSalle(request, response);
                    break;
                case "search":
                    searchSalles(request, response);
                    break;
                default:
                    listSalles(request, response);
            }
        } catch (Exception e) {
            log("Error in SalleServlet GET", e);
            request.setAttribute("error", e.getMessage());
            listSalles(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = getAction(request);
        
        try {
            switch (action) {
                case "create":
                    createSalle(request, response);
                    break;
                case "update":
                    updateSalle(request, response);
                    break;
                case "toggle-disponibilite":
                    toggleDisponibilite(request, response);
                    break;
                default:
                    listSalles(request, response);
            }
        } catch (Exception e) {
            log("Error in SalleServlet POST", e);
            request.setAttribute("error", e.getMessage());
            
            // Garder les valeurs saisies
            keepFormValues(request);
            
            if (action.equals("create")) {
                showNewForm(request, response);
            } else if (action.equals("update")) {
                showEditForm(request, response);
            } else {
                listSalles(request, response);
            }
        }
    }
    
    private void listSalles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Récupérer les filtres
        String typeFilter = request.getParameter("type");
        String disponibleFilter = request.getParameter("disponible");
        String capaciteMinStr = request.getParameter("capaciteMin");
        String search = request.getParameter("search");
        
        // Pagination parameters
        int page = 0;
        int pageSize = 12; // Default page size
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
                if (page < 0) page = 0;
            }
            String sizeParam = request.getParameter("size");
            if (sizeParam != null && !sizeParam.isEmpty()) {
                pageSize = Integer.parseInt(sizeParam);
                if (pageSize < 1) pageSize = 12;
            }
        } catch (NumberFormatException e) {
            log("Invalid pagination parameters");
        }
        
        // Convertir les filtres
        Salle.TypeSalle type = null;
        if (typeFilter != null && !typeFilter.isEmpty()) {
            type = Salle.TypeSalle.valueOf(typeFilter);
        }
        
        Integer capaciteMin = null;
        if (capaciteMinStr != null && !capaciteMinStr.isEmpty()) {
            try {
                capaciteMin = Integer.parseInt(capaciteMinStr);
            } catch (NumberFormatException e) {
                log("Invalid capaciteMin: " + capaciteMinStr);
            }
        }
        
        Boolean disponible = null;
        if (disponibleFilter != null && !disponibleFilter.isEmpty()) {
            disponible = Boolean.valueOf(disponibleFilter);
        }
        
        // Utiliser la méthode filterSalles du service
        List<Salle> allSalles = salleService.filterSalles(type, capaciteMin, disponible, search);
        
        // Paginate results
        List<Salle> salles = PaginationHelper.paginate(allSalles, page, pageSize);
        int totalPages = PaginationHelper.getTotalPages(allSalles.size(), pageSize);
        int startIndex = PaginationHelper.getStartIndex(page, pageSize);
        int endIndex = PaginationHelper.getEndIndex(page, pageSize, allSalles.size());
        
        // Set attributes
        request.setAttribute("salles", salles);
        request.setAttribute("typeFilter", typeFilter);
        request.setAttribute("disponibleFilter", disponibleFilter);
        request.setAttribute("capaciteMin", capaciteMinStr);
        request.setAttribute("search", search);
        
        // Pagination attributes
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalItems", allSalles.size());
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startIndex", startIndex);
        request.setAttribute("endIndex", endIndex);
        request.setAttribute("hasNextPage", PaginationHelper.hasNextPage(page, totalPages));
        request.setAttribute("hasPreviousPage", PaginationHelper.hasPreviousPage(page));
        
        request.getRequestDispatcher("/WEB-INF/views/salle/list.jsp").forward(request, response);
    }
    
    private void searchSalles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        
        // Utiliser filterSalles avec searchNom
        List<Salle> salles = salleService.filterSalles(null, null, null, keyword);
        
        request.setAttribute("salles", salles);
        request.setAttribute("keyword", keyword);
        
        request.getRequestDispatcher("/WEB-INF/views/salle/list.jsp").forward(request, response);
    }
    
    private void viewSalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        
        Optional<Salle> salleOpt = salleService.getSalleById(id);
        if (salleOpt.isPresent()) {
            request.setAttribute("salle", salleOpt.get());
            request.getRequestDispatcher("/WEB-INF/views/salle/view.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("error", "Salle introuvable");
            response.sendRedirect(request.getContextPath() + "/salles/list");
        }
    }
    
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/salle/form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        
        Optional<Salle> salleOpt = salleService.getSalleById(id);
        if (salleOpt.isPresent()) {
            request.setAttribute("salle", salleOpt.get());
            request.getRequestDispatcher("/WEB-INF/views/salle/form.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("error", "Salle introuvable");
            response.sendRedirect(request.getContextPath() + "/salles/list");
        }
    }
    
    private void createSalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Récupérer les paramètres
        String nom = request.getParameter("nom");
        String typeStr = request.getParameter("type");
        String capaciteStr = request.getParameter("capacite");
        String equipements = request.getParameter("equipements");
        
        // Validation
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (typeStr == null || typeStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Le type est obligatoire");
        }
        if (capaciteStr == null || capaciteStr.trim().isEmpty()) {
            throw new IllegalArgumentException("La capacité est obligatoire");
        }
        
        // Créer la salle
        Salle salle = new Salle();
        salle.setNom(nom.trim());
        salle.setType(Salle.TypeSalle.valueOf(typeStr));
        salle.setCapacite(Integer.parseInt(capaciteStr));
        salle.setEquipements(equipements != null ? equipements.trim() : null);
        
        // Enregistrer
        Salle savedSalle = salleService.saveSalle(salle);
        
        log("Salle created: " + savedSalle.getNom() + " (ID: " + savedSalle.getId() + ")");
        
        // Message de succès dans la session
        request.getSession().setAttribute("success", "Salle créée avec succès");
        
        // Rediriger vers la liste
        response.sendRedirect(request.getContextPath() + "/salles/list");
    }
    
    private void updateSalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Récupérer l'ID
        Long id = Long.parseLong(request.getParameter("id"));
        
        // Récupérer la salle existante
        Optional<Salle> salleOpt = salleService.getSalleById(id);
        if (!salleOpt.isPresent()) {
            request.getSession().setAttribute("error", "Salle introuvable");
            response.sendRedirect(request.getContextPath() + "/salles/list");
            return;
        }
        
        Salle salle = salleOpt.get();
        
        // Récupérer les paramètres
        String nom = request.getParameter("nom");
        String typeStr = request.getParameter("type");
        String capaciteStr = request.getParameter("capacite");
        String equipements = request.getParameter("equipements");
        String disponibleStr = request.getParameter("disponible");
        
        // Validation
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (typeStr == null || typeStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Le type est obligatoire");
        }
        if (capaciteStr == null || capaciteStr.trim().isEmpty()) {
            throw new IllegalArgumentException("La capacité est obligatoire");
        }
        
        // Mettre à jour les valeurs
        salle.setNom(nom.trim());
        salle.setType(Salle.TypeSalle.valueOf(typeStr));
        salle.setCapacite(Integer.parseInt(capaciteStr));
        salle.setEquipements(equipements != null ? equipements.trim() : null);
        salle.setDisponible(disponibleStr != null && disponibleStr.equals("true"));
        
        // Enregistrer
        Salle updatedSalle = salleService.saveSalle(salle);
        
        log("Salle updated: " + updatedSalle.getNom() + " (ID: " + updatedSalle.getId() + ")");
        
        // Message de succès dans la session
        request.getSession().setAttribute("success", "Salle modifiée avec succès");
        
        // Rediriger vers la liste
        response.sendRedirect(request.getContextPath() + "/salles/list");
    }
    
    private void deleteSalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        
        try {
            salleService.deleteSalle(id);
            log("Salle deleted: ID " + id);
            request.getSession().setAttribute("success", "Salle supprimée avec succès");
        } catch (Exception e) {
            log("Error deleting salle: " + e.getMessage());
            request.getSession().setAttribute("error", "Erreur lors de la suppression");
        }
        
        response.sendRedirect(request.getContextPath() + "/salles/list");
    }
    
    private void toggleDisponibilite(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        
        try {
            Salle salle = salleService.toggleDisponibilite(id);
            log("Salle disponibilite changed: " + salle.getNom() + " -> " + salle.isDisponible());
            request.getSession().setAttribute("success", "Disponibilité modifiée");
        } catch (Exception e) {
            log("Error toggling disponibilite: " + e.getMessage());
            request.getSession().setAttribute("error", "Erreur lors de la modification");
        }
        
        response.sendRedirect(request.getContextPath() + "/salles/list");
    }
    
    private String getAction(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return "list";
        }
        return pathInfo.substring(1);
    }
    
    private void keepFormValues(HttpServletRequest request) {
        request.setAttribute("nom", request.getParameter("nom"));
        request.setAttribute("type", request.getParameter("type"));
        request.setAttribute("capacite", request.getParameter("capacite"));
        request.setAttribute("equipements", request.getParameter("equipements"));
        request.setAttribute("disponible", request.getParameter("disponible"));
    }
}

