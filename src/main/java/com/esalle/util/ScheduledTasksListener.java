package com.esalle.util;

import com.esalle.service.AutoReleaseService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Listener pour exécuter les tâches planifiées selon le workflow
 * Libère automatiquement les réservations terminées et supprime les non confirmées
 */
@WebListener
public class ScheduledTasksListener implements ServletContextListener {
    
    private static final Logger logger = Logger.getLogger(ScheduledTasksListener.class.getName());
    private Timer timer;
    private AutoReleaseService autoReleaseService;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initialisation des tâches planifiées...");
        
        autoReleaseService = new AutoReleaseService();
        timer = new Timer("AutoReleaseScheduler", true);
        
        // Exécuter toutes les heures : libération automatique
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    logger.info("Exécution de la libération automatique des réservations...");
                    autoReleaseService.libererReservationsTerminees();
                    autoReleaseService.supprimerReservationsNonConfirmees();
                } catch (Exception e) {
                    logger.severe("Erreur lors de l'exécution des tâches planifiées: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, 60000, 3600000); // Démarrer après 1 minute, puis toutes les heures
        
        logger.info("Tâches planifiées initialisées avec succès");
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Arrêt des tâches planifiées...");
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        logger.info("Tâches planifiées arrêtées");
    }
}

