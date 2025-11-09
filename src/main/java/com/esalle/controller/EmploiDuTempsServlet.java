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
            String viewMode = request.getParameter("viewMode"); // "manage" or "teach" for coordinateur
            // Par défaut, mode gestion pour coordinateur
            if (viewMode == null && currentUser.getRole() == User.UserRole.COORDINATEUR) {
                viewMode = "manage";
            }

            List<EmploiDuTemps> emploiList;
            List<Filiere> filieres;
            boolean isTeachingView = false;

            // Si c'est un professeur, afficher seulement ses cours
            if (currentUser.getRole() == User.UserRole.PROFESSEUR) {
                emploiList = emploiService.getEmploiDuTempsByProfesseur(currentUser.getId());
                filieres = filiereService.getAllFilieres(); // Pour le formulaire
                isTeachingView = true;
            }
            // Si c'est un coordinateur
            else if (currentUser.getRole() == User.UserRole.COORDINATEUR) {
                // Vérifier si le coordinateur veut voir son emploi d'enseignement (comme professeur)
                if ("teach".equals(viewMode)) {
                    // Mode enseignement: voir seulement les cours où il est professeur
                    emploiList = emploiService.getEmploiDuTempsByProfesseur(currentUser.getId());
                    filieres = filiereService.getAllFilieres();
                    isTeachingView = true;
                } else {
                    // Mode gestion: filtrer par ses filières (comportement normal)
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
                        // Par défaut, utiliser la première filière si aucune n'est sélectionnée
                        Long targetFiliereId;
                        if (filiereIdStr != null && !filiereIdStr.isEmpty()) {
                            final Long parsedTargetFiliereId = Long.parseLong(filiereIdStr);
                            // Vérifier que cette filière appartient au coordinateur
                            boolean belongsToCoordinateur = coordinateurFilieres.stream()
                                    .anyMatch(f -> f.getId().equals(parsedTargetFiliereId));
                            if (!belongsToCoordinateur) {
                                // Filière non autorisée, utiliser la première filière par défaut
                                targetFiliereId = coordinateurFilieres.get(0).getId();
                                filiereIdStr = targetFiliereId.toString();
                            } else {
                                targetFiliereId = parsedTargetFiliereId;
                                filiereIdStr = parsedTargetFiliereId.toString();
                            }
                        } else {
                            // Aucune filière sélectionnée, utiliser la première par défaut
                            targetFiliereId = coordinateurFilieres.get(0).getId();
                            filiereIdStr = targetFiliereId.toString();
                        }
                        
                        // Par défaut, utiliser annee = 1 si non spécifié
                        Integer annee = (anneeStr != null && !anneeStr.isEmpty()) ? Integer.parseInt(anneeStr) : 1;

                        if (groupe != null && !groupe.isEmpty()) {
                            emploiList = emploiService.getEmploiDuTempsByFiliereAnneeGroupe(targetFiliereId, annee, groupe);
                        } else {
                            emploiList = emploiService.getEmploiDuTempsByFiliereAndAnnee(targetFiliereId, annee);
                        }
                    }
                }
            // Admin peut voir toutes les filières
            } else if (currentUser.getRole() == User.UserRole.ADMIN) {
                    filieres = filiereService.getAllFilieres();

                    if (filiereIdStr != null && !filiereIdStr.isEmpty()) {
                        Long filiereId = Long.parseLong(filiereIdStr);
                        // Par défaut, utiliser annee = 1 si non spécifié
                        Integer annee = (anneeStr != null && !anneeStr.isEmpty()) ? Integer.parseInt(anneeStr) : 1;

                        if (groupe != null && !groupe.isEmpty()) {
                            emploiList = emploiService.getEmploiDuTempsByFiliereAnneeGroupe(filiereId, annee, groupe);
                        } else {
                            emploiList = emploiService.getEmploiDuTempsByFiliereAndAnnee(filiereId, annee);
                        }
                    } else {
                        // Par défaut, filtrer par annee = 1 pour éviter le mélange
                        // Si aucune filière sélectionnée, on ne peut pas filtrer par année facilement
                        // Donc on récupère tous les emplois mais on filtrera par annee = 1 dans le calendrier
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

                // Déterminer l'année à utiliser (par défaut = 1 si non spécifié)
                Integer targetAnnee = null;
                if (anneeStr != null && !anneeStr.isEmpty()) {
                    targetAnnee = Integer.parseInt(anneeStr);
                } else {
                    // Par défaut, filtrer par annee = 1 pour éviter le mélange de séances
                    targetAnnee = 1;
                    anneeStr = "1"; // Mettre à jour pour l'affichage
                }

                // Si c'est la vue enseignement (professeur ou coordinateur en mode teach), ne pas filtrer par année
                if (isTeachingView) {
                    // Pour la vue enseignement, afficher tous les cours du professeur
                    filteredListForCalendar = emploiList;
                }
                // Si un cycle et une année sont sélectionnés, filtrer les emplois
                else if (cycleStr != null && !cycleStr.isEmpty()) {
                    Filiere.Cycle cycle = Filiere.Cycle.valueOf(cycleStr);

                    // Créer une map pour accéder rapidement aux filières par ID
                    java.util.Map<Long, Filiere> filiereMap = new java.util.HashMap<>();
                    for (Filiere f : filieres) {
                        filiereMap.put(f.getId(), f);
                    }

                    // Filtrer par cycle et année pour toutes les filières du coordinateur/admin
                    for (EmploiDuTemps emploi : emploiList) {
                        Filiere filiere = filiereMap.get(emploi.getFiliereId());
                        if (filiere != null && filiere.getCycle() == cycle && emploi.getAnnee().equals(targetAnnee)) {
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
                } else if (filiereIdStr != null && !filiereIdStr.isEmpty()) {
                    // Filtre par filière et année
                    Long filiereId = Long.parseLong(filiereIdStr);
                    if (groupe != null && !groupe.isEmpty()) {
                        filteredListForCalendar = emploiService.getEmploiDuTempsByFiliereAnneeGroupe(filiereId, targetAnnee, groupe);
                    } else {
                        filteredListForCalendar = emploiService.getEmploiDuTempsByFiliereAndAnnee(filiereId, targetAnnee);
                    }
                } else if (!emploiList.isEmpty()) {
                    // Par défaut, filtrer par annee = 1 pour éviter le mélange
                    // Filtrer les emplois par année = 1
                    for (EmploiDuTemps emploi : emploiList) {
                        if (emploi.getAnnee() != null && emploi.getAnnee().equals(targetAnnee)) {
                            filteredListForCalendar.add(emploi);
                        }
                    }
                }

                calendarData = organizeByDayAndTime(filteredListForCalendar);

                // Charger les matières et salles pour le formulaire
                // Filtrer les matières selon le rôle
                List<Matiere> matieres;
                if (currentUser.getRole() == User.UserRole.COORDINATEUR) {
                    // COORDINATEUR: Afficher seulement les matières de ses filières
                    List<Filiere> coordinateurFilieres = filiereService.getFilieresByCoordinateur(currentUser.getId());
                    if (coordinateurFilieres != null && !coordinateurFilieres.isEmpty()) {
                        // Si une filière spécifique est sélectionnée, filtrer par cette filière
                        if (filiereIdStr != null && !filiereIdStr.isEmpty()) {
                            Long filiereId = Long.parseLong(filiereIdStr);
                            // Vérifier que cette filière appartient au coordinateur
                            boolean belongsToCoordinateur = coordinateurFilieres.stream()
                                    .anyMatch(f -> f.getId().equals(filiereId));
                            if (belongsToCoordinateur) {
                                matieres = matiereService.getMatieresByFiliere(filiereId);
                            } else {
                                matieres = java.util.Collections.emptyList();
                            }
                        } else {
                            // Sinon, afficher toutes les matières de ses filières
                            List<Long> filiereIds = coordinateurFilieres.stream()
                                    .map(Filiere::getId)
                                    .collect(java.util.stream.Collectors.toList());
                            matieres = matiereService.getMatieresByFilieres(filiereIds);
                        }
                    } else {
                        matieres = java.util.Collections.emptyList();
                    }
                } else {
                    // ADMIN et autres: Afficher toutes les matières
                    matieres = matiereService.getAllMatieres();
                }
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
                request.setAttribute("viewMode", viewMode);
                request.setAttribute("isTeachingView", isTeachingView);
                // Seuls les coordinateurs peuvent modifier (pas les admins) et seulement en mode gestion
                request.setAttribute("canEdit", currentUser.getRole() == User.UserRole.COORDINATEUR && !isTeachingView);
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

            HttpSession session = request.getSession(false);
            User currentUser = (User) session.getAttribute("user");

            List<Filiere> filieres;
            List<Salle> salles = salleService.getAllSalles();
            List<Matiere> matieres;

            // Si c'est un coordinateur, utiliser seulement ses filières
            if (currentUser != null && currentUser.getRole() == User.UserRole.COORDINATEUR) {
                filieres = filiereService.getFilieresByCoordinateur(currentUser.getId());
                // Charger toutes les matières de ses filières
                if (filieres != null && !filieres.isEmpty()) {
                    List<Long> filiereIds = filieres.stream()
                            .map(Filiere::getId)
                            .collect(java.util.stream.Collectors.toList());
                    matieres = matiereService.getMatieresByFilieres(filiereIds);
                } else {
                    matieres = java.util.Collections.emptyList();
                }
            } else {
                filieres = filiereService.getAllFilieres();
                matieres = matiereService.getAllMatieres();
            }

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

            // Charger les professeurs pour afficher les noms
            List<User> professeurs = new java.util.ArrayList<>();
            if (!matieres.isEmpty()) {
                java.util.Set<Long> professeurIds = matieres.stream()
                        .map(Matiere::getProfesseurId)
                        .filter(java.util.Objects::nonNull)
                        .collect(java.util.stream.Collectors.toSet());

                com.esalle.service.UserService userService = new com.esalle.service.UserServiceImpl();
                for (Long profId : professeurIds) {
                    userService.findById(profId).ifPresent(professeurs::add);
                }
            }

            request.setAttribute("filieres", filieres);
            request.setAttribute("salles", salles);
            request.setAttribute("matieres", matieres);
            request.setAttribute("professeurs", professeurs);
            request.setAttribute("isCoordinateur", currentUser != null && currentUser.getRole() == User.UserRole.COORDINATEUR);
            request.getRequestDispatcher("/WEB-INF/views/emploi/form.jsp").forward(request, response);
        }

        private void handleSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            HttpSession session = request.getSession(false);
            User currentUser = (User) session.getAttribute("user");

            try {
                EmploiDuTemps emploi = new EmploiDuTemps();

                String idStr = request.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) {
                    emploi.setId(Long.parseLong(idStr));
                }

                Long filiereId;
                Integer annee;
                List<Filiere> coordinateurFilieres = null;

                // Si c'est un coordinateur, déterminer automatiquement la filière et l'année
                if (currentUser != null && currentUser.getRole() == User.UserRole.COORDINATEUR) {
                    coordinateurFilieres = filiereService.getFilieresByCoordinateur(currentUser.getId());
                    if (coordinateurFilieres == null || coordinateurFilieres.isEmpty()) {
                        session.setAttribute("error", "Aucune filière assignée. Veuillez contacter l'administrateur.");
                        response.sendRedirect(request.getContextPath() + "/emploi/list");
                        return;
                    }
                    // Utiliser la première filière du coordinateur
                    filiereId = coordinateurFilieres.get(0).getId();
                    // Pour l'année, on peut la déterminer à partir de la filière ou utiliser la matière
                    // Si en mode édition, utiliser l'année de l'emploi existant
                    if (idStr != null && !idStr.isEmpty()) {
                        EmploiDuTemps existingEmploi = emploiService.getEmploiDuTempsById(Long.parseLong(idStr)).orElse(null);
                        if (existingEmploi != null) {
                            annee = existingEmploi.getAnnee();
                            filiereId = existingEmploi.getFiliereId();
                        } else {
                            // Sinon utiliser l'année de la filière
                            annee = coordinateurFilieres.get(0).getAnnee();
                        }
                    } else {
                        // Mode création: utiliser l'année de la filière
                        annee = coordinateurFilieres.get(0).getAnnee();
                    }
                } else {
                    // Admin: utiliser les paramètres du formulaire
                    filiereId = Long.parseLong(request.getParameter("filiereId"));
                    annee = Integer.parseInt(request.getParameter("annee"));
                }

                // Créer des copies finales pour les lambdas
                final Long finalFiliereId = filiereId;

                // Vérification de sécurité: si c'est un coordinateur, vérifier qu'il peut créer pour cette filière
                if (currentUser != null && currentUser.getRole() == User.UserRole.COORDINATEUR && coordinateurFilieres != null) {
                    boolean canAccess = coordinateurFilieres.stream()
                            .anyMatch(f -> f.getId().equals(finalFiliereId));

                    if (!canAccess) {
                        session.setAttribute("error", "Vous n'avez pas la permission de créer un emploi du temps pour cette filière.");
                        response.sendRedirect(request.getContextPath() + "/emploi/list");
                        return;
                    }
                }

                emploi.setFiliereId(filiereId);
                emploi.setAnnee(annee);
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

                session.setAttribute("success", "Séance créée avec succès");
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