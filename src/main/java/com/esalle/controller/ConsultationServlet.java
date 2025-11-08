package com.esalle.controller;

import com.esalle.entity.User;
import com.esalle.entity.Reservation;
import com.esalle.entity.EmploiDuTemps;
import com.esalle.entity.Matiere;
import com.esalle.entity.Filiere;
import com.esalle.service.ReservationService;
import com.esalle.service.ReservationServiceImpl;
import com.esalle.service.EmploiDuTempsService;
import com.esalle.service.EmploiDuTempsServiceImpl;
import com.esalle.service.MatiereService;
import com.esalle.service.MatiereServiceImpl;
import com.esalle.service.FiliereService;
import com.esalle.service.FiliereServiceImpl;
import com.esalle.service.SalleService;
import com.esalle.service.SalleServiceImpl;
import com.esalle.service.ReclamationService;
import com.esalle.service.ReclamationServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet pour les vues consultation selon le rôle
 * 
 * Routes:
 * GET /consultation/admin       - Vue admin (dashboard global)
 * GET /consultation/coordinateur - Vue coordinateur
 * GET /consultation/professeur  - Vue professeur
 * GET /consultation/club        - Vue membre club
 */
@WebServlet(name = "ConsultationServlet", urlPatterns = {
    "/consultation/admin",
    "/consultation/coordinateur",
    "/consultation/professeur",
    "/consultation/club"
})
public class ConsultationServlet extends HttpServlet {

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
                case "/consultation/admin":
                    if (currentUser.getRole() != User.UserRole.ADMIN) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    handleAdminView(request, response, currentUser);
                    break;
                case "/consultation/coordinateur":
                    if (currentUser.getRole() != User.UserRole.COORDINATEUR) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    handleCoordinateurView(request, response, currentUser);
                    break;
                case "/consultation/professeur":
                    if (currentUser.getRole() != User.UserRole.PROFESSEUR) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    handleProfesseurView(request, response, currentUser);
                    break;
                case "/consultation/club":
                    if (currentUser.getRole() != User.UserRole.MEMBRE_CLUB) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    handleClubView(request, response, currentUser);
                    break;
                default:
                    // Rediriger selon le rôle
                    redirectByRole(response, currentUser);
            }
        } catch (Exception e) {
            log("Error in ConsultationServlet.doGet: " + e.getMessage(), e);
            e.printStackTrace();
            request.setAttribute("error", "Erreur: " + e.getMessage());
            request.setAttribute("errorMessage", "Erreur: " + e.getMessage());
            try {
                request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
            } catch (Exception ex) {
                log("Error forwarding to error page: " + ex.getMessage(), ex);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur serveur: " + e.getMessage());
            }
        }
    }

    private void handleAdminView(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        try {
            log("Loading admin dashboard for user: " + currentUser.getEmail());
            
            ReservationService reservationService = new ReservationServiceImpl();
            EmploiDuTempsService emploiService = new EmploiDuTempsServiceImpl();
            SalleService salleService = new SalleServiceImpl();
            ReclamationService reclamationService = new ReclamationServiceImpl();
            
            // Stats globales - initialiser les listes si null
            List<Reservation> allReservations = reservationService.getAllReservations();
            if (allReservations == null) {
                allReservations = new java.util.ArrayList<>();
            }
            
            List<EmploiDuTemps> allEmploi = emploiService.getAllEmploiDuTemps();
            if (allEmploi == null) {
                allEmploi = new java.util.ArrayList<>();
            }
            
            List<com.esalle.entity.Salle> allSalles = salleService.getAllSalles();
            if (allSalles == null) {
                allSalles = new java.util.ArrayList<>();
            }
            
            List<com.esalle.entity.Reclamation> allReclamations = reclamationService.getAllReclamations();
            if (allReclamations == null) {
                allReclamations = new java.util.ArrayList<>();
            }
            
            long totalReservations = allReservations.size();
            long pendingReservations = allReservations.stream()
                .filter(r -> r.getStatut() == Reservation.StatutReservation.EN_ATTENTE).count();
            long approvedReservations = allReservations.stream()
                .filter(r -> r.getStatut() == Reservation.StatutReservation.APPROUVEE).count();
            
            long openIssues = allReclamations.stream()
                .filter(r -> r.getStatut() == com.esalle.entity.Reclamation.Statut.EN_ATTENTE).count();
            
            request.setAttribute("totalReservations", totalReservations);
            request.setAttribute("pendingReservations", pendingReservations);
            request.setAttribute("approvedReservations", approvedReservations);
            request.setAttribute("totalSalles", allSalles.size());
            request.setAttribute("openIssues", openIssues);
            
            // Éviter IndexOutOfBoundsException avec subList
            if (!allReservations.isEmpty()) {
                request.setAttribute("recentReservations", 
                    allReservations.subList(0, Math.min(5, allReservations.size())));
            } else {
                request.setAttribute("recentReservations", new java.util.ArrayList<>());
            }
            
            if (!allReclamations.isEmpty()) {
                request.setAttribute("recentReclamations", 
                    allReclamations.subList(0, Math.min(5, allReclamations.size())));
            } else {
                request.setAttribute("recentReclamations", new java.util.ArrayList<>());
            }
            
            log("Forwarding to admin dashboard JSP");
            request.getRequestDispatcher("/WEB-INF/views/consultation/admin.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in handleAdminView: " + e.getMessage(), e);
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors du chargement du dashboard: " + e.getMessage());
            request.setAttribute("errorMessage", "Erreur lors du chargement du dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
        }
    }

    private void handleCoordinateurView(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        try {
            log("Loading coordinateur dashboard for user: " + currentUser.getEmail());
            
            FiliereService filiereService = new FiliereServiceImpl(
                new com.esalle.repository.FiliereRepositoryImpl(),
                new com.esalle.repository.UserRepositoryImpl());
            EmploiDuTempsService emploiService = new EmploiDuTempsServiceImpl();
            ReservationService reservationService = new ReservationServiceImpl();
            
            // Trouver la filière du coordinateur
            List<Filiere> filieres = filiereService.getAllFilieres();
            if (filieres == null) {
                filieres = new java.util.ArrayList<>();
            }
            
            Filiere filiereCoordinateur = filieres.stream()
                .filter(f -> f.getCoordinateurId() != null && f.getCoordinateurId().equals(currentUser.getId()))
                .findFirst()
                .orElse(null);
            
            if (filiereCoordinateur != null) {
                List<EmploiDuTemps> emploiFiliere = emploiService.getEmploiDuTempsByFiliere(filiereCoordinateur.getId());
                List<Reservation> reservations = reservationService.getAllReservations();
                
                if (emploiFiliere == null) {
                    emploiFiliere = new java.util.ArrayList<>();
                }
                if (reservations == null) {
                    reservations = new java.util.ArrayList<>();
                }
                
                request.setAttribute("filiere", filiereCoordinateur);
                request.setAttribute("emploiFiliere", emploiFiliere);
                request.setAttribute("reservations", reservations);
            } else {
                request.setAttribute("filiere", null);
                request.setAttribute("emploiFiliere", new java.util.ArrayList<>());
                request.setAttribute("reservations", new java.util.ArrayList<>());
            }
            
            request.setAttribute("totalUsers", 0);
            request.setAttribute("totalRooms", 0);
            request.setAttribute("utilizationRate", 78);
            
            log("Forwarding to coordinateur dashboard JSP");
            request.getRequestDispatcher("/WEB-INF/views/consultation/coordinateur.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in handleCoordinateurView: " + e.getMessage(), e);
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors du chargement du dashboard: " + e.getMessage());
            request.setAttribute("errorMessage", "Erreur lors du chargement du dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
        }
    }

    private void handleProfesseurView(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        try {
            log("Loading professeur dashboard for user: " + currentUser.getEmail());
            
            MatiereService matiereService = new MatiereServiceImpl();
            EmploiDuTempsService emploiService = new EmploiDuTempsServiceImpl();
            ReservationService reservationService = new ReservationServiceImpl();
            SalleService salleService = new SalleServiceImpl();
            
            // Matières du professeur - initialiser si null
            List<Matiere> mesMatieres = matiereService.getMatieresByProfesseur(currentUser.getId());
            if (mesMatieres == null) {
                mesMatieres = new java.util.ArrayList<>();
            }
            
            List<EmploiDuTemps> monEmploi = emploiService.getEmploiDuTempsByProfesseur(currentUser.getId());
            if (monEmploi == null) {
                monEmploi = new java.util.ArrayList<>();
            }
            
            List<Reservation> mesReservations = reservationService.getReservationsByUser(currentUser.getId());
            if (mesReservations == null) {
                mesReservations = new java.util.ArrayList<>();
            }
            
            List<com.esalle.entity.Salle> sallesDisponibles = salleService.getAllSalles();
            if (sallesDisponibles == null) {
                sallesDisponibles = new java.util.ArrayList<>();
            }
            
            request.setAttribute("mesMatieres", mesMatieres);
            request.setAttribute("monEmploi", monEmploi);
            request.setAttribute("mesReservations", mesReservations);
            request.setAttribute("sallesDisponibles", sallesDisponibles);
            
            log("Forwarding to professeur dashboard JSP");
            request.getRequestDispatcher("/WEB-INF/views/consultation/professeur.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in handleProfesseurView: " + e.getMessage(), e);
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors du chargement du dashboard: " + e.getMessage());
            request.setAttribute("errorMessage", "Erreur lors du chargement du dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
        }
    }

    private void handleClubView(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        try {
            log("Loading club dashboard for user: " + currentUser.getEmail());
            
            ReservationService reservationService = new ReservationServiceImpl();
            List<Reservation> mesReservations = reservationService.getReservationsByUser(currentUser.getId());
            
            if (mesReservations == null) {
                mesReservations = new java.util.ArrayList<>();
            }
            
            request.setAttribute("mesReservations", mesReservations);
            
            log("Forwarding to club dashboard JSP");
            request.getRequestDispatcher("/WEB-INF/views/consultation/club.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in handleClubView: " + e.getMessage(), e);
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors du chargement du dashboard: " + e.getMessage());
            request.setAttribute("errorMessage", "Erreur lors du chargement du dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
        }
    }

    private void redirectByRole(HttpServletResponse response, User user) throws IOException {
        switch (user.getRole()) {
            case ADMIN:
                response.sendRedirect(response.encodeRedirectURL("/consultation/admin"));
                break;
            case COORDINATEUR:
                response.sendRedirect(response.encodeRedirectURL("/consultation/coordinateur"));
                break;
            case PROFESSEUR:
                response.sendRedirect(response.encodeRedirectURL("/consultation/professeur"));
                break;
            case MEMBRE_CLUB:
                response.sendRedirect(response.encodeRedirectURL("/consultation/club"));
                break;
            default:
                response.sendRedirect(response.encodeRedirectURL("/auth/login"));
        }
    }
}

