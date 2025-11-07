package com.esalle.controller;

import com.esalle.entity.Reservation;
import com.esalle.entity.User;
import com.esalle.entity.Salle;
import com.esalle.service.ReservationService;
import com.esalle.service.ReservationServiceImpl;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servlet pour gérer les réservations
 * 
 * Routes:
 * GET  /reservations/list    - Liste toutes les réservations
 * GET  /reservations/new     - Formulaire nouvelle réservation
 * POST /reservations/save    - Créer une réservation
 * POST /reservations/approve - Approuver une réservation (admin)
 * POST /reservations/refuse  - Refuser une réservation (admin)
 * POST /reservations/cancel  - Annuler une réservation (user)
 * GET  /reservations/view    - Voir détails réservation
 */
@WebServlet(name = "ReservationServlet", urlPatterns = {
    "/reservations/list",
    "/reservations/new",
    "/reservations/save",
    "/reservations/approve",
    "/reservations/refuse",
    "/reservations/cancel",
    "/reservations/view"
})
public class ReservationServlet extends HttpServlet {

    private ReservationService reservationService;
    private SalleService salleService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.reservationService = new ReservationServiceImpl();
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
                case "/reservations/list":
                    handleList(request, response, currentUser);
                    break;
                case "/reservations/new":
                    handleNew(request, response);
                    break;
                case "/reservations/view":
                    handleView(request, response, currentUser);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/reservations/list");
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
        String path = request.getServletPath();
        
        try {
            switch (path) {
                case "/reservations/save":
                    handleSave(request, response, currentUser);
                    break;
                case "/reservations/approve":
                    handleApprove(request, response, currentUser);
                    break;
                case "/reservations/refuse":
                    handleRefuse(request, response, currentUser);
                    break;
                case "/reservations/cancel":
                    handleCancel(request, response, currentUser);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/reservations/list");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
        }
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        
        try {
            log("Loading reservations list for user: " + currentUser.getEmail() + " (Role: " + currentUser.getRole() + ")");
            String filter = request.getParameter("filter"); // "all", "pending", "approved", "my-reservations"
            log("Filter parameter: " + filter);
            
            List<Reservation> reservations;
            
            if ("my-reservations".equals(filter) || 
                (currentUser.getRole() != User.UserRole.ADMIN && filter == null)) {
                // Pour les non-admins, afficher seulement leurs réservations
                log("Loading user's own reservations");
                reservations = reservationService.getReservationsByUser(currentUser.getId());
            } else if ("pending".equals(filter) && currentUser.getRole() == User.UserRole.ADMIN) {
                // Admin peut voir toutes les réservations en attente (triées par priorité)
                log("Loading pending reservations for admin");
                reservations = reservationService.getReservationsEnAttente();
            } else if ("all".equals(filter) && currentUser.getRole() == User.UserRole.ADMIN) {
                // Admin voit toutes les réservations quand filter=all
                log("Loading ALL reservations for admin");
                reservations = reservationService.getAllReservations();
            } else if (currentUser.getRole() == User.UserRole.ADMIN && (filter == null || filter.isEmpty())) {
                // Admin sans filtre spécifique voit toutes les réservations
                log("Loading ALL reservations for admin (no filter)");
                reservations = reservationService.getAllReservations();
            } else {
                // Pour les autres rôles (PROFESSEUR, MEMBRE_CLUB, COORDINATEUR), afficher leurs réservations
                log("Loading user's own reservations (fallback)");
                reservations = reservationService.getReservationsByUser(currentUser.getId());
            }
            
            // Calculer les stats (s'assurer que la liste n'est pas null)
            if (reservations == null) {
                log("WARNING: reservations list is null, initializing empty list");
                reservations = new java.util.ArrayList<>();
            }
            
            log("Loaded " + reservations.size() + " reservations");
            
            // Pour les stats, toujours calculer sur TOUTES les réservations pour l'admin
            List<Reservation> allReservationsForStats = reservations;
            if (currentUser.getRole() == User.UserRole.ADMIN) {
                allReservationsForStats = reservationService.getAllReservations();
                if (allReservationsForStats == null) {
                    allReservationsForStats = new java.util.ArrayList<>();
                }
            }
            
            long total = allReservationsForStats.size();
            long approved = allReservationsForStats.stream()
                .filter(r -> r.getStatut() == Reservation.StatutReservation.APPROUVEE).count();
            long pending = allReservationsForStats.stream()
                .filter(r -> r.getStatut() == Reservation.StatutReservation.EN_ATTENTE).count();
            long refused = allReservationsForStats.stream()
                .filter(r -> r.getStatut() == Reservation.StatutReservation.REFUSEE).count();
            
            log("Stats - Total: " + total + ", Approved: " + approved + ", Pending: " + pending + ", Refused: " + refused);
            
            request.setAttribute("reservations", reservations);
            request.setAttribute("filter", filter != null ? filter : (currentUser.getRole() == User.UserRole.ADMIN ? "all" : "my-reservations"));
            request.setAttribute("stats", new Stats(total, approved, pending, refused));
            request.setAttribute("isAdmin", currentUser.getRole() == User.UserRole.ADMIN);
            request.setAttribute("userId", currentUser.getId()); // Pour permettre aux utilisateurs d'annuler leurs propres réservations
            
            log("Forwarding to reservation list JSP");
            request.getRequestDispatcher("/WEB-INF/views/reservation/list.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in handleList: " + e.getMessage(), e);
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors du chargement des réservations: " + e.getMessage());
            request.setAttribute("errorMessage", "Erreur lors du chargement des réservations: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
        }
    }

    private void handleNew(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Salle> salles = salleService.getAllSalles();
        request.setAttribute("salles", salles);
        request.getRequestDispatcher("/WEB-INF/views/reservation/form.jsp").forward(request, response);
    }

    private void handleSave(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        
        try {
            Reservation reservation = new Reservation();
            reservation.setUserId(currentUser.getId());
            reservation.setSalleId(Long.parseLong(request.getParameter("salleId")));
            reservation.setDateReservation(LocalDate.parse(request.getParameter("dateReservation")));
            reservation.setHeureDebut(LocalTime.parse(request.getParameter("heureDebut")));
            reservation.setHeureFin(LocalTime.parse(request.getParameter("heureFin")));
            reservation.setMotif(request.getParameter("motif"));
            
            String liberationExceptionnelle = request.getParameter("estLiberationExceptionnelle");
            if ("true".equals(liberationExceptionnelle)) {
                reservation.setEstLiberationExceptionnelle(true);
            }
            
            reservationService.createReservation(reservation);
            
            response.sendRedirect(request.getContextPath() + "/reservations/list?success=created");
        } catch (BusinessException e) {
            request.setAttribute("error", e.getMessage());
            List<Salle> salles = salleService.getAllSalles();
            request.setAttribute("salles", salles);
            request.getRequestDispatcher("/WEB-INF/views/reservation/form.jsp").forward(request, response);
        }
    }

    private void handleApprove(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        
        if (currentUser.getRole() != User.UserRole.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        Long reservationId = Long.parseLong(request.getParameter("reservationId"));
        String commentaire = request.getParameter("commentaire");
        
        reservationService.approveReservation(reservationId, currentUser.getId(), commentaire);
        
        response.sendRedirect(request.getContextPath() + "/reservations/list?filter=pending&success=approved");
    }

    private void handleRefuse(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        
        if (currentUser.getRole() != User.UserRole.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        Long reservationId = Long.parseLong(request.getParameter("reservationId"));
        String commentaire = request.getParameter("commentaire");
        
        reservationService.refuseReservation(reservationId, currentUser.getId(), commentaire);
        
        response.sendRedirect(request.getContextPath() + "/reservations/list?filter=pending&success=refused");
    }

    private void handleCancel(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        
        try {
            String reservationIdParam = request.getParameter("reservationId");
            if (reservationIdParam == null || reservationIdParam.trim().isEmpty()) {
                request.getSession().setAttribute("error", "L'ID de la réservation est requis.");
                response.sendRedirect(request.getContextPath() + "/reservations/list");
                return;
            }
            
            Long reservationId;
            try {
                reservationId = Long.parseLong(reservationIdParam);
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "ID de réservation invalide.");
                response.sendRedirect(request.getContextPath() + "/reservations/list");
                return;
            }
            
            reservationService.cancelReservation(reservationId, currentUser.getId());
            request.getSession().setAttribute("success", "Réservation annulée avec succès.");
            response.sendRedirect(request.getContextPath() + "/reservations/list?success=cancelled");
        } catch (BusinessException e) {
            log("Error cancelling reservation: " + e.getMessage());
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/reservations/list");
        } catch (Exception e) {
            log("Unexpected error cancelling reservation: " + e.getMessage(), e);
            request.getSession().setAttribute("error", "Une erreur inattendue s'est produite lors de l'annulation.");
            response.sendRedirect(request.getContextPath() + "/reservations/list");
        }
    }

    private void handleView(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                request.setAttribute("error", "L'ID de la réservation est requis.");
                request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
                return;
            }
            
            Long reservationId;
            try {
                reservationId = Long.parseLong(idParam);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID de réservation invalide: " + idParam);
                request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
                return;
            }
            
            Reservation reservation = reservationService.getReservationById(reservationId)
                    .orElse(null);
            
            if (reservation == null) {
                request.setAttribute("error", "Réservation introuvable avec l'ID: " + reservationId);
                request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
                return;
            }
            
            // Enrichir la réservation avec les noms si manquants
            if (reservation.getSalleNom() == null || reservation.getSalleNom().trim().isEmpty() ||
                reservation.getUserNom() == null || reservation.getUserNom().trim().isEmpty()) {
                // Le service devrait déjà enrichir, mais on vérifie quand même
                log("Reservation " + reservationId + " has missing salleNom or userNom");
            }
            
            // Vérifier que l'utilisateur peut voir cette réservation
            if (currentUser.getRole() != User.UserRole.ADMIN && 
                !reservation.getUserId().equals(currentUser.getId())) {
                request.setAttribute("error", "Vous n'avez pas l'autorisation de voir cette réservation.");
                request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
                return;
            }
            
            request.setAttribute("reservation", reservation);
            request.setAttribute("isAdmin", currentUser.getRole() == User.UserRole.ADMIN);
            request.setAttribute("userId", currentUser.getId()); // Pour permettre aux utilisateurs d'annuler leurs propres réservations
            request.getRequestDispatcher("/WEB-INF/views/reservation/view.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in handleView: " + e.getMessage(), e);
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors du chargement de la réservation: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/generic-error.jsp").forward(request, response);
        }
    }

    // Classe interne pour les stats
    public static class Stats {
        private final long total;
        private final long approved;
        private final long pending;
        private final long refused;
        
        public Stats(long total, long approved, long pending, long refused) {
            this.total = total;
            this.approved = approved;
            this.pending = pending;
            this.refused = refused;
        }
        
        public long getTotal() {
            return total;
        }
        
        public long getApproved() {
            return approved;
        }
        
        public long getPending() {
            return pending;
        }
        
        public long getRefused() {
            return refused;
        }
    }
}

