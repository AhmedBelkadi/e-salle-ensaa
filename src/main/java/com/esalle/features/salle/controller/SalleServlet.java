package com.esalle.features.salle.controller;

import com.esalle.features.salle.domain.Salle;
import com.esalle.features.salle.service.SalleService;
import com.esalle.features.salle.service.SalleServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        
        List<Salle> salles;
        
        // Appliquer les filtres
        if (typeFilter != null && !typeFilter.isEmpty()) {
            Salle.TypeSalle type = Salle.TypeSalle.valueOf(typeFilter);
            if (disponibleFilter != null && !disponibleFilter.isEmpty()) {
                Boolean disponible = Boolean.valueOf(disponibleFilter);
                salles = salleService.findByTypeAndDisponible(type, disponible);
            } else {
                salles = salleService.findByType(type);
            }
        } else if (disponibleFilter != null && !disponibleFilter.isEmpty()) {
            Boolean disponible = Boolean.valueOf(disponibleFilter);
            if (disponible) {
                salles = salleService.findDisponibles();
            } else {
                salles = salleService.findAll();
            }
        } else if (capaciteMinStr != null && !capaciteMinStr.isEmpty()) {
            Integer capaciteMin = Integer.parseInt(capaciteMinStr);
            salles = salleService.findByCapaciteMin(capaciteMin);
        } else {
            salles = salleService.findAll();
        }
        
        request.setAttribute("salles", salles);
        request.setAttribute("typeFilter", typeFilter);
        request.setAttribute("disponibleFilter", disponibleFilter);
        request.setAttribute("capaciteMin", capaciteMinStr);
        
        request.getRequestDispatcher("/WEB-INF/views/salle/list.jsp").forward(request, response);
    }
    
    private void searchSalles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        
        List<Salle> salles = salleService.search(keyword);
        
        request.setAttribute("salles", salles);
        request.setAttribute("keyword", keyword);
        
        request.getRequestDispatcher("/WEB-INF/views/salle/list.jsp").forward(request, response);
    }
    
    private void viewSalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        
        Optional<Salle> salleOpt = salleService.findById(id);
        if (salleOpt.isPresent()) {
            request.setAttribute("salle", salleOpt.get());
            request.getRequestDispatcher("/WEB-INF/views/salle/view.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Salle introuvable");
            listSalles(request, response);
        }
    }
    
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/salle/form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        
        Optional<Salle> salleOpt = salleService.findById(id);
        if (salleOpt.isPresent()) {
            request.setAttribute("salle", salleOpt.get());
            request.getRequestDispatcher("/WEB-INF/views/salle/form.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Salle introuvable");
            listSalles(request, response);
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
        Salle savedSalle = salleService.create(salle);
        
        log("Salle created: " + savedSalle.getNom() + " (ID: " + savedSalle.getId() + ")");
        
        // Rediriger vers la liste avec message de succès
        response.sendRedirect(request.getContextPath() + "/salles/list?success=Salle créée avec succès");
    }
    
    private void updateSalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Récupérer l'ID
        Long id = Long.parseLong(request.getParameter("id"));
        
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
        
        // Créer la salle avec les nouvelles valeurs
        Salle salle = new Salle();
        salle.setNom(nom.trim());
        salle.setType(Salle.TypeSalle.valueOf(typeStr));
        salle.setCapacite(Integer.parseInt(capaciteStr));
        salle.setEquipements(equipements != null ? equipements.trim() : null);
        salle.setDisponible(disponibleStr != null && disponibleStr.equals("true"));
        
        // Mettre à jour
        Salle updatedSalle = salleService.update(id, salle);
        
        log("Salle updated: " + updatedSalle.getNom() + " (ID: " + updatedSalle.getId() + ")");
        
        // Rediriger vers la liste avec message de succès
        response.sendRedirect(request.getContextPath() + "/salles/list?success=Salle modifiée avec succès");
    }
    
    private void deleteSalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        
        boolean deleted = salleService.delete(id);
        
        if (deleted) {
            log("Salle deleted: ID " + id);
            response.sendRedirect(request.getContextPath() + "/salles/list?success=Salle supprimée avec succès");
        } else {
            response.sendRedirect(request.getContextPath() + "/salles/list?error=Erreur lors de la suppression");
        }
    }
    
    private void toggleDisponibilite(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Boolean disponible = Boolean.parseBoolean(request.getParameter("disponible"));
        
        Salle salle = salleService.changeDisponibilite(id, disponible);
        
        log("Salle disponibilite changed: " + salle.getNom() + " -> " + disponible);
        
        response.sendRedirect(request.getContextPath() + "/salles/list?success=Disponibilité modifiée");
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

