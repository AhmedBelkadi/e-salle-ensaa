package com.esalle.shared.service;

/**
 * Service de notifications (Email + WhatsApp)
 * Interface pour l'envoi de notifications
 */
public interface NotificationService {
    
    /**
     * Envoyer une notification par email
     * @param to Adresse email du destinataire
     * @param subject Sujet de l'email
     * @param body Corps du message
     * @return true si envoyé avec succès
     */
    boolean sendEmail(String to, String subject, String body);
    
    /**
     * Envoyer une notification par WhatsApp
     * @param phoneNumber Numéro de téléphone du destinataire
     * @param message Message à envoyer
     * @return true si envoyé avec succès
     */
    boolean sendWhatsApp(String phoneNumber, String message);
    
    /**
     * Envoyer une notification par email ET WhatsApp
     * @param email Adresse email
     * @param phoneNumber Numéro de téléphone
     * @param subject Sujet (pour email)
     * @param message Message
     * @return true si au moins un canal a réussi
     */
    boolean sendNotification(String email, String phoneNumber, String subject, String message);
}

