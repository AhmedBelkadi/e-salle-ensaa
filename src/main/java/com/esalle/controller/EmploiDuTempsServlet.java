package com.esalle.controller;

import com.esalle.entity.EmploiDuTemps;
import com.esalle.entity.User;
import com.esalle.entity.Matiere;
import com.esalle.entity.Filiere;
import com.esalle.entity.Salle;
import com.esalle.service.EmploiDuTempsService;
import com.esalle.service.EmploiDuTempsServiceImpl;
import com.esalle.service.MatiereService;
import com.esalle.service.MatiereServiceImpl;
import com.esalle.service.FiliereService;
import com.esalle.service.FiliereServiceImpl;
import com.esalle.service.SalleService;
import com.esalle.service.SalleServiceImpl;
import com.esalle.exception.BusinessException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

/**
 * Servlet pour gérer l'emploi du temps
 * 
 * Routes:
 * GET  /emploi/list        - Liste/C calendrier emploi du temps
 * GET  /emploi/new         - Formulaire nouvelle séance
 * POST /emploi/save        - Créer/modifier une séance
 * POST /emploi/generate    - Générer 7 séances pour une matière
 * GET  /emploi/delete      - Supprimer une séance
 */
@WebServlet(name = "EmploiDuTempsServlet", urlPatterns = {
    "/emploi/list",
    "/emploi/new",
    "/emploi/save",
    "/emploi/generate",
    "/emploi/delete"
})
public class EmploiDuTempsServlet extends HttpServlet {

    private EmploiDuTempsService emploiService;
    private MatiereService matiereService;
    private FiliereService filiereService;
    private SalleService salleService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.emploiService = new EmploiDuTempsServiceImpl();
        this.matiereService = new MatiereServiceImpl();
        this.filiereService = new FiliereServiceImpl(
            new com.esalle.repository.FiliereRepositoryImpl(),
            new com.esalle.repository.UserRepositoryImpl());
        this.salleService = new SalleServiceImpl();
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
        String path = request.getServletPath();
        
        try {
            switch (path) {
                case "/emploi/list":
                    handleList(request, response, currentUser);
                    break;
                case "/emploi/new":
                    handleNew(request, response);
                    break;
                case "/emploi/delete":
                    handleDelete(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/emploi/list");
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

        String path = request.getServletPath();
        
        try {
            switch (path) {
                case "/emploi/save":
                    handleSave(request, response);
                    break;
                case "/emploi/generate":
                    handleGenerate(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/emploi/list");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
        }
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        
        try {
            String filiereIdStr = request.getParameter("filiereId");
            String anneeStr = request.getParameter("annee");
            String groupe = request.getParameter("groupe");
            String cycleStr = request.getParameter("cycle"); // "PREPARATOIRE" ou "INGENIEUR"
            
            List<EmploiDuTemps> emploiList;
            List<Filiere> filieres;
            
            // Si c'est un professeur, afficher seulement ses cours
            if (currentUser.getRole() == User.UserRole.PROFESSEUR) {
                emploiList = emploiService.getEmploiDuTempsByProfesseur(currentUser.getId());
                filieres = filiereService.getAllFilieres(); // Pour le formulaire
            } 
            // Si c'est un coordinateur, filtrer par ses filières uniquement
            else if (currentUser.getRole() == User.UserRole.COORDINATEUR) {
                // Récupérer les filières assignées à ce coordinateur
                List<Filiere> coordinateurFilieres = filiereService.getFilieresByCoordinateur(currentUser.getId());
                
                if (coordinateurFilieres == null || coordinateurFilieres.isEmpty()) {
                    // Si le coordinateur n'a pas de filières assignées, utiliser filiereId du User
                    if (currentUser.getFiliereId() != null) {
                        coordinateurFilieres = new java.util.ArrayList<>();
                        try {
                            Filiere filiere = filiereService.getFiliereById(currentUser.getFiliereId());
                            coordinateurFilieres.add(filiere);
                        } catch (Exception e) {
                            log("Coordinateur " + currentUser.getId() + " has invalid filiereId: " + currentUser.getFiliereId());
                        }
                    }
                }
                
                filieres = coordinateurFilieres;
                
                // Si aucune filière assignée, liste vide
                if (coordinateurFilieres == null || coordinateurFilieres.isEmpty()) {
                    emploiList = new java.util.ArrayList<>();
                    log("Coordinateur " + currentUser.getId() + " has no assigned filieres");
                } else {
                    // Récupérer tous les emplois du temps des filières du coordinateur
                    emploiList = new java.util.ArrayList<>();
                    for (Filiere filiere : coordinateurFilieres) {
                        List<EmploiDuTemps> emploisFiliere = emploiService.getEmploiDuTempsByFiliere(filiere.getId());
                        if (emploisFiliere != null) {
                            emploiList.addAll(emploisFiliere);
                        }
                    }
                    
                    // Si un filtre par filière spécifique est demandé, filtrer davantage
                    if (filiereIdStr != null && !filiereIdStr.isEmpty()) {
                        Long filiereId = Long.parseLong(filiereIdStr);
                        // Vérifier que cette filière appartient au coordinateur
                        boolean belongsToCoordinateur = coordinateurFilieres.stream()
                            .anyMatch(f -> f.getId().equals(filiereId));
                        
                        if (belongsToCoordinateur) {
                            Integer annee = (anneeStr != null && !anneeStr.isEmpty()) ? Integer.parseInt(anneeStr) : null;
                            
                            if (annee != null && groupe != null && !groupe.isEmpty()) {
                                emploiList = emploiService.getEmploiDuTempsByFiliereAnneeGroupe(filiereId, annee, groupe);
                            } else if (annee != null) {
                                emploiList = emploiService.getEmploiDuTempsByFiliereAndAnnee(filiereId, annee);
                            } else {
                                emploiList = emploiService.getEmploiDuTempsByFiliere(filiereId);
                            }
                        } else {
                            // Filière non autorisée pour ce coordinateur
                            emploiList = new java.util.ArrayList<>();
                            log("Coordinateur " + currentUser.getId() + " tried to access unauthorized filiere: " + filiereId);
                        }
                    }
                }
            } 
            // Admin peut voir toutes les filières
            else if (currentUser.getRole() == User.UserRole.ADMIN) {
                filieres = filiereService.getAllFilieres();
                
                if (filiereIdStr != null && !filiereIdStr.isEmpty()) {
                    Long filiereId = Long.parseLong(filiereIdStr);
                    Integer annee = (anneeStr != null && !anneeStr.isEmpty()) ? Integer.parseInt(anneeStr) : null;
                    
                    if (annee != null && groupe != null && !groupe.isEmpty()) {
                        emploiList = emploiService.getEmploiDuTempsByFiliereAnneeGroupe(filiereId, annee, groupe);
                    } else if (annee != null) {
                        emploiList = emploiService.getEmploiDuTempsByFiliereAndAnnee(filiereId, annee);
                    } else {
                        emploiList = emploiService.getEmploiDuTempsByFiliere(filiereId);
                    }
                } else {
                    emploiList = emploiService.getAllEmploiDuTemps();
                }
            } 
            // Autres rôles (MEMBRE_CLUB) - pas d'accès
            else {
                emploiList = new java.util.ArrayList<>();
                filieres = new java.util.ArrayList<>();
            }
            
            // Organiser les emplois par cycle et année pour l'affichage par onglets
            java.util.Map<String, java.util.Map<Integer, List<EmploiDuTemps>>> emploisByCycleAndAnnee = 
                organizeByCycleAndAnnee(emploiList, filieres);
            
            // Préparer les données pour le calendrier (pour l'année sélectionnée)
            java.util.Map<String, List<EmploiDuTemps>> calendarData = new java.util.HashMap<>();
            List<EmploiDuTemps> filteredListForCalendar = new java.util.ArrayList<>();
            
            // Si un cycle et une année sont sélectionnés, filtrer les emplois
            if (cycleStr != null && !cycleStr.isEmpty() && anneeStr != null && !anneeStr.isEmpty()) {
                Integer annee = Integer.parseInt(anneeStr);
                Filiere.Cycle cycle = Filiere.Cycle.valueOf(cycleStr);
                
                // Créer une map pour accéder rapidement aux filières par ID
                java.util.Map<Long, Filiere> filiereMap = new java.util.HashMap<>();
                for (Filiere f : filieres) {
                    filiereMap.put(f.getId(), f);
                }
                
                // Filtrer par cycle et année pour toutes les filières du coordinateur/admin
                for (EmploiDuTemps emploi : emploiList) {
                    Filiere filiere = filiereMap.get(emploi.getFiliereId());
                    if (filiere != null && filiere.getCycle() == cycle && emploi.getAnnee().equals(annee)) {
                        // Si un filtre par filière spécifique est demandé, vérifier
                        if (filiereIdStr != null && !filiereIdStr.isEmpty()) {
                            Long filiereId = Long.parseLong(filiereIdStr);
                            if (emploi.getFiliereId().equals(filiereId)) {
                                filteredListForCalendar.add(emploi);
                            }
                        } else {
                            // Afficher tous les emplois de toutes les filières avec ce cycle et cette année
                            filteredListForCalendar.add(emploi);
                        }
                    }
                }
            } else if (filiereIdStr != null && !filiereIdStr.isEmpty() && anneeStr != null && !anneeStr.isEmpty()) {
                // Filtre par filière et année uniquement
                Integer annee = Integer.parseInt(anneeStr);
                Long filiereId = Long.parseLong(filiereIdStr);
                filteredListForCalendar = emploiService.getEmploiDuTempsByFiliereAndAnnee(filiereId, annee);
            } else if (!emploiList.isEmpty()) {
                // Par défaut, afficher tous les emplois (ou le premier cycle/année disponible)
                filteredListForCalendar = emploiList;
            }
            
            calendarData = organizeByDayAndTime(filteredListForCalendar);
            
            // Charger les matières et salles pour le formulaire
            List<Matiere> matieres = matiereService.getAllMatieres();
            List<Salle> salles = salleService.getAllSalles();
            
            request.setAttribute("emploiList", emploiList);
            request.setAttribute("calendarData", calendarData);
            request.setAttribute("emploisByCycleAndAnnee", emploisByCycleAndAnnee);
            request.setAttribute("filieres", filieres);
            request.setAttribute("matieres", matieres);
            request.setAttribute("salles", salles);
            request.setAttribute("selectedFiliereId", filiereIdStr);
            request.setAttribute("selectedAnnee", anneeStr);
            request.setAttribute("selectedCycle", cycleStr);
            request.setAttribute("selectedGroupe", groupe);
            // Seuls les coordinateurs peuvent modifier (pas les admins)
            request.setAttribute("canEdit", currentUser.getRole() == User.UserRole.COORDINATEUR);
            request.setAttribute("isProfessor", currentUser.getRole() == User.UserRole.PROFESSEUR);
            request.setAttribute("isCoordinateur", currentUser.getRole() == User.UserRole.COORDINATEUR);
            request.setAttribute("isAdmin", currentUser.getRole() == User.UserRole.ADMIN);
            
            request.getRequestDispatcher("/WEB-INF/views/emploi/list.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in handleList: " + e.getMessage(), e);
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors du chargement de l'emploi du temps: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
        }
    }
    
    /**
     * Organise les emplois du temps par cycle (PREPARATOIRE/INGENIEUR) et par année
     */
    private java.util.Map<String, java.util.Map<Integer, List<EmploiDuTemps>>> organizeByCycleAndAnnee(
            List<EmploiDuTemps> emploiList, List<Filiere> filieres) {
        
        java.util.Map<String, java.util.Map<Integer, List<EmploiDuTemps>>> result = new java.util.HashMap<>();
        
        // Créer une map pour accéder rapidement aux filières par ID
        java.util.Map<Long, Filiere> filiereMap = new java.util.HashMap<>();
        for (Filiere f : filieres) {
            filiereMap.put(f.getId(), f);
        }
        
        // Organiser par cycle puis par année
        java.util.Map<Integer, List<EmploiDuTemps>> preparatoire = new java.util.HashMap<>();
        java.util.Map<Integer, List<EmploiDuTemps>> ingenieur = new java.util.HashMap<>();
        
        for (EmploiDuTemps emploi : emploiList) {
            Filiere filiere = filiereMap.get(emploi.getFiliereId());
            if (filiere == null) {
                continue; // Ignorer si la filière n'est pas trouvée
            }
            
            Filiere.Cycle cycle = filiere.getCycle();
            Integer annee = emploi.getAnnee();
            
            java.util.Map<Integer, List<EmploiDuTemps>> targetMap;
            if (cycle == Filiere.Cycle.PREPARATOIRE) {
                targetMap = preparatoire;
            } else {
                targetMap = ingenieur;
            }
            
            targetMap.computeIfAbsent(annee, k -> new java.util.ArrayList<>()).add(emploi);
        }
        
        result.put("PREPARATOIRE", preparatoire);
        result.put("INGENIEUR", ingenieur);
        
        return result;
    }
    
    /**
     * Organise les séances par jour et créneau horaire pour le calendrier
     */
    private java.util.Map<String, List<EmploiDuTemps>> organizeByDayAndTime(List<EmploiDuTemps> emploiList) {
        java.util.Map<String, List<EmploiDuTemps>> calendarData = new java.util.HashMap<>();
        
        // Créneaux horaires standard
        String[] timeSlots = {
            "08:30-10:30",
            "10:30-12:30",
            "14:30-16:30",
            "16:30-18:30",
            "18:30-20:30"
        };
        
        // Jours de la semaine
        EmploiDuTemps.JourSemaine[] jours = {
            EmploiDuTemps.JourSemaine.LUNDI,
            EmploiDuTemps.JourSemaine.MARDI,
            EmploiDuTemps.JourSemaine.MERCREDI,
            EmploiDuTemps.JourSemaine.JEUDI,
            EmploiDuTemps.JourSemaine.VENDREDI,
            EmploiDuTemps.JourSemaine.SAMEDI
        };
        
        for (String timeSlot : timeSlots) {
            String[] times = timeSlot.split("-");
            LocalTime start = LocalTime.parse(times[0]);
            LocalTime end = LocalTime.parse(times[1]);
            
            for (EmploiDuTemps.JourSemaine jour : jours) {
                String key = jour.name() + "_" + timeSlot;
                List<EmploiDuTemps> seances = new java.util.ArrayList<>();
                
                for (EmploiDuTemps emploi : emploiList) {
                    if (emploi.getJourSemaine() == jour &&
                        emploi.getHeureDebut() != null && emploi.getHeureFin() != null &&
                        emploi.getHeureDebut().equals(start) && emploi.getHeureFin().equals(end)) {
                        seances.add(emploi);
                    }
                }
                
                calendarData.put(key, seances);
            }
        }
        
        return calendarData;
    }

    private void handleNew(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Filiere> filieres = filiereService.getAllFilieres();
        List<Salle> salles = salleService.getAllSalles();
        List<Matiere> matieres = matiereService.getAllMatieres();
        
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            // Édition d'une séance existante
            EmploiDuTemps emploi = emploiService.getEmploiDuTempsById(Long.parseLong(idStr))
                    .orElse(null);
            if (emploi != null) {
                request.setAttribute("emploi", emploi);
                // Filtrer les matières par filière de la séance
                matieres = matiereService.getMatieresByFiliere(emploi.getFiliereId());
            }
        }
        
        request.setAttribute("filieres", filieres);
        request.setAttribute("salles", salles);
        request.setAttribute("matieres", matieres);
        request.getRequestDispatcher("/WEB-INF/views/emploi/form.jsp").forward(request, response);
    }

    private void handleSave(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            EmploiDuTemps emploi = new EmploiDuTemps();
            
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                emploi.setId(Long.parseLong(idStr));
            }
            
            emploi.setFiliereId(Long.parseLong(request.getParameter("filiereId")));
            emploi.setAnnee(Integer.parseInt(request.getParameter("annee")));
            emploi.setMatiereId(Long.parseLong(request.getParameter("matiereId")));
            emploi.setProfesseurId(Long.parseLong(request.getParameter("professeurId")));
            emploi.setSalleId(Long.parseLong(request.getParameter("salleId")));
            emploi.setJourSemaine(EmploiDuTemps.JourSemaine.valueOf(request.getParameter("jourSemaine")));
            emploi.setHeureDebut(LocalTime.parse(request.getParameter("heureDebut")));
            emploi.setHeureFin(LocalTime.parse(request.getParameter("heureFin")));
            emploi.setTypeSeance(EmploiDuTemps.TypeSeance.valueOf(request.getParameter("typeSeance")));
            
            String groupe = request.getParameter("groupe");
            if (groupe != null && !groupe.trim().isEmpty()) {
                emploi.setGroupe(groupe);
            }
            
            emploiService.saveEmploiDuTemps(emploi);
            
            response.sendRedirect(request.getContextPath() + "/emploi/list?success=saved");
        } catch (BusinessException e) {
            request.setAttribute("error", e.getMessage());
            handleNew(request, response);
        }
    }

    private void handleGenerate(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            Long matiereId = Long.parseLong(request.getParameter("matiereId"));
            Long salleId = Long.parseLong(request.getParameter("salleId"));
            EmploiDuTemps.JourSemaine jour = EmploiDuTemps.JourSemaine.valueOf(request.getParameter("jourSemaine"));
            LocalTime heureDebut = LocalTime.parse(request.getParameter("heureDebut"));
            LocalTime heureFin = LocalTime.parse(request.getParameter("heureFin"));
            Integer nombreSeances = Integer.parseInt(request.getParameter("nombreSeances"));
            String groupe = request.getParameter("groupe");
            
            List<EmploiDuTemps> seances = emploiService.genererSeances(
                matiereId, salleId, jour, heureDebut, heureFin, nombreSeances, groupe);
            
            response.sendRedirect(request.getContextPath() + "/emploi/list?success=generated&count=" + seances.size());
        } catch (BusinessException e) {
            request.setAttribute("error", e.getMessage());
            handleNew(request, response);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Long id = Long.parseLong(request.getParameter("id"));
        emploiService.deleteEmploiDuTemps(id);
        
        response.sendRedirect(request.getContextPath() + "/emploi/list?success=deleted");
    }
}

