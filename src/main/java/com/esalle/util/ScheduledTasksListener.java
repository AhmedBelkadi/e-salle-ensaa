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

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("🕐 Initialisation des tâches planifiées...");

        try {
            timer = new Timer("AutoReleaseScheduler", true);

            // Exécuter toutes les heures : libération automatique
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        logger.info("⏰ Exécution de la libération automatique des réservations...");

                        // Créer le service à chaque exécution pour éviter les problèmes de SessionFactory
                        AutoReleaseService autoReleaseService = new AutoReleaseService();
                        autoReleaseService.libererReservationsTerminees();
                        autoReleaseService.supprimerReservationsNonConfirmees();

                        logger.info("✅ Tâches planifiées exécutées avec succès");
                    } catch (Exception e) {
                        logger.severe("❌ Erreur lors de l'exécution des tâches planifiées: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }, 60000, 3600000); // Démarrer après 1 minute, puis toutes les heures

            logger.info("✅ Tâches planifiées initialisées avec succès");
        } catch (Exception e) {
            logger.severe("❌ ERREUR lors de l'initialisation des tâches planifiées: " + e.getMessage());
            e.printStackTrace();
            // Ne pas propager l'exception pour permettre à l'application de démarrer
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("🛑 Arrêt des tâches planifiées...");
        try {
            if (timer != null) {
                timer.cancel();
                timer.purge();
            }
            logger.info("✅ Tâches planifiées arrêtées avec succès");
        } catch (Exception e) {
            logger.warning("⚠️ Erreur lors de l'arrêt des tâches planifiées: " + e.getMessage());
        }
    }
}