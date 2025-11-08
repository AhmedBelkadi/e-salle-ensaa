package com.esalle.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implémentation du service de notifications
 * Pour l'instant : logs uniquement (TODO: intégrer vraies APIs)
 */
public class NotificationServiceImpl implements NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    
    @Override
    public boolean sendEmail(String to, String subject, String body) {
        try {
            // TODO: Intégrer un service d'email réel (JavaMail, SendGrid, etc.)
            logger.info("📧 EMAIL SENT:");
            logger.info("   To: {}", to);
            logger.info("   Subject: {}", subject);
            logger.info("   Body: {}", body);
            
            // Simulation d'envoi réussi
            return true;
        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'envoi d'email à {}: {}", to, e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean sendWhatsApp(String phoneNumber, String message) {
        try {
            // TODO: Intégrer WhatsApp Business API ou Twilio
            logger.info("📱 WHATSAPP SENT:");
            logger.info("   To: {}", phoneNumber);
            logger.info("   Message: {}", message);
            
            // Simulation d'envoi réussi
            return true;
        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'envoi WhatsApp à {}: {}", phoneNumber, e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean sendNotification(String email, String phoneNumber, String subject, String message) {
        boolean emailSent = false;
        boolean whatsappSent = false;
        
        // Envoyer email si fourni
        if (email != null && !email.trim().isEmpty()) {
            emailSent = sendEmail(email, subject, message);
        }
        
        // Envoyer WhatsApp si fourni
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            whatsappSent = sendWhatsApp(phoneNumber, message);
        }
        
        // Succès si au moins un canal a fonctionné
        return emailSent || whatsappSent;
    }
    
    // Méthodes utilitaires pour des notifications prédéfinies
    
    /**
     * Notification pour nouvelle inscription utilisateur (à l'admin)
     */
    public boolean notifyAdminNewRegistration(String adminEmail, String adminPhone, String userName, String userRole) {
        String subject = "Nouvelle inscription - E-Salle ENSAA";
        String message = String.format(
            "Nouvelle demande d'inscription :\n\n" +
            "Utilisateur : %s\n" +
            "Rôle : %s\n\n" +
            "Veuillez approuver ou refuser cette demande dans l'interface d'administration.",
            userName, userRole
        );
        
        return sendNotification(adminEmail, adminPhone, subject, message);
    }
    
    /**
     * Notification d'approbation de compte (à l'utilisateur)
     */
    public boolean notifyUserAccountApproved(String userEmail, String userPhone, String userName) {
        String subject = "Compte approuvé - E-Salle ENSAA";
        String message = String.format(
            "Bonjour %s,\n\n" +
            "Votre compte E-Salle ENSAA a été approuvé !\n" +
            "Vous pouvez maintenant vous connecter à l'application.\n\n" +
            "Cordialement,\n" +
            "L'équipe E-Salle ENSAA",
            userName
        );
        
        return sendNotification(userEmail, userPhone, subject, message);
    }
    
    /**
     * Notification de refus de compte (à l'utilisateur)
     */
    public boolean notifyUserAccountRefused(String userEmail, String userPhone, String userName) {
        String subject = "Compte refusé - E-Salle ENSAA";
        String message = String.format(
            "Bonjour %s,\n\n" +
            "Votre demande d'inscription à E-Salle ENSAA a été refusée.\n" +
            "Pour plus d'informations, veuillez contacter l'administration.\n\n" +
            "Cordialement,\n" +
            "L'équipe E-Salle ENSAA",
            userName
        );
        
        return sendNotification(userEmail, userPhone, subject, message);
    }
    
    /**
     * Notification de nouvelle réclamation (à l'admin)
     */
    public boolean notifyAdminNewReclamation(String adminEmail, String adminPhone, 
                                            String userName, String salleName, String urgence) {
        String subject = String.format("Nouvelle réclamation [%s] - E-Salle ENSAA", urgence);
        String message = String.format(
            "Nouvelle réclamation :\n\n" +
            "Utilisateur : %s\n" +
            "Salle : %s\n" +
            "Urgence : %s\n\n" +
            "Veuillez traiter cette réclamation dans l'interface d'administration.",
            userName, salleName, urgence
        );
        
        return sendNotification(adminEmail, adminPhone, subject, message);
    }
    
    /**
     * Notification de réclamation traitée (à l'utilisateur)
     */
    public boolean notifyUserReclamationTreated(String userEmail, String userPhone, 
                                               String userName, String salleName) {
        String subject = "Réclamation traitée - E-Salle ENSAA";
        String message = String.format(
            "Bonjour %s,\n\n" +
            "Votre réclamation concernant la salle '%s' a été traitée.\n\n" +
            "Cordialement,\n" +
            "L'équipe E-Salle ENSAA",
            userName, salleName
        );
        
        return sendNotification(userEmail, userPhone, subject, message);
    }
    
    /**
     * Notification de réservation confirmée
     */
    public boolean notifyReservationConfirmed(String userEmail, String userPhone, 
                                             String userName, String salleName, 
                                             String dateDebut, String dateFin) {
        String subject = "Réservation confirmée - E-Salle ENSAA";
        String message = String.format(
            "Bonjour %s,\n\n" +
            "Votre réservation a été confirmée :\n\n" +
            "Salle : %s\n" +
            "Début : %s\n" +
            "Fin : %s\n\n" +
            "Cordialement,\n" +
            "L'équipe E-Salle ENSAA",
            userName, salleName, dateDebut, dateFin
        );
        
        return sendNotification(userEmail, userPhone, subject, message);
    }
    
    /**
     * Notification de salle alternative proposée
     */
    public boolean notifyAlternativeRoomProposed(String userEmail, String userPhone, 
                                                String userName, String salleOriginale, 
                                                String salleAlternative) {
        String subject = "Salle alternative proposée - E-Salle ENSAA";
        String message = String.format(
            "Bonjour %s,\n\n" +
            "La salle '%s' n'est pas disponible.\n" +
            "Nous vous proposons la salle alternative : '%s'\n\n" +
            "Cordialement,\n" +
            "L'équipe E-Salle ENSAA",
            userName, salleOriginale, salleAlternative
        );
        
        return sendNotification(userEmail, userPhone, subject, message);
    }
}

