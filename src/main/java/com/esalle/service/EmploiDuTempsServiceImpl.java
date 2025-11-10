package com.esalle.service;

import com.esalle.entity.EmploiDuTemps;
import com.esalle.entity.Matiere;
import com.esalle.entity.Salle;
import com.esalle.entity.Filiere;
import com.esalle.entity.User;
import com.esalle.repository.EmploiDuTempsRepository;
import com.esalle.repository.EmploiDuTempsRepositoryImpl;
import com.esalle.repository.MatiereRepository;
import com.esalle.repository.MatiereRepositoryImpl;
import com.esalle.repository.SalleRepository;
import com.esalle.repository.SalleRepositoryImpl;
import com.esalle.repository.FiliereRepository;
import com.esalle.repository.FiliereRepositoryImpl;
import com.esalle.repository.UserRepository;
import com.esalle.repository.UserRepositoryImpl;
import com.esalle.exception.BusinessException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmploiDuTempsServiceImpl implements EmploiDuTempsService {

    private final EmploiDuTempsRepository emploiRepository;
    private final MatiereRepository matiereRepository;
    private final SalleRepository salleRepository;
    private final FiliereRepository filiereRepository;
    private final UserRepository userRepository;

    public EmploiDuTempsServiceImpl() {
        this.emploiRepository = new EmploiDuTempsRepositoryImpl();
        this.matiereRepository = new MatiereRepositoryImpl();
        this.salleRepository = new SalleRepositoryImpl();
        this.filiereRepository = new FiliereRepositoryImpl();
        this.userRepository = new UserRepositoryImpl();
    }

    @Override
    public EmploiDuTemps saveEmploiDuTemps(EmploiDuTemps emploi) {
        // Validation
        if (emploi.getMatiereId() == null) {
            throw new BusinessException("La matière est obligatoire.");
        }
        if (emploi.getSalleId() == null) {
            throw new BusinessException("La salle est obligatoire.");
        }
        if (emploi.getJourSemaine() == null) {
            throw new BusinessException("Le jour de la semaine est obligatoire.");
        }
        if (emploi.getHeureDebut() == null || emploi.getHeureFin() == null) {
            throw new BusinessException("Les heures de début et de fin sont obligatoires.");
        }
        if (emploi.getHeureDebut().isAfter(emploi.getHeureFin()) || emploi.getHeureDebut().equals(emploi.getHeureFin())) {
            throw new BusinessException("L'heure de début doit être antérieure à l'heure de fin.");
        }

        // Vérifier que la matière existe
        Matiere matiere = matiereRepository.findById(emploi.getMatiereId())
                .orElseThrow(() -> new BusinessException("La matière spécifiée n'existe pas."));
        emploi.setMatiereNom(matiere.getNom());

        // Vérifier que la salle existe
        Salle salle = salleRepository.findById(emploi.getSalleId())
                .orElseThrow(() -> new BusinessException("La salle spécifiée n'existe pas."));
        emploi.setSalleNom(salle.getNom());

        // Vérifier que la filière existe
        Filiere filiere = filiereRepository.findById(emploi.getFiliereId())
                .orElseThrow(() -> new BusinessException("La filière spécifiée n'existe pas."));
        emploi.setFiliereNom(filiere.getNom());

        // Vérifier que le professeur existe
        User professeur = userRepository.findById(emploi.getProfesseurId())
                .orElseThrow(() -> new BusinessException("Le professeur spécifié n'existe pas."));
        emploi.setProfesseurNom(professeur.getNom() + " " + professeur.getPrenom());

        // Vérifier les conflits de salle (sauf pour l'ID actuel si modification)
        if (verifierConflitSalle(emploi.getSalleId(), emploi.getJourSemaine(), 
                                emploi.getHeureDebut(), emploi.getHeureFin(), emploi.getId())) {
            throw new BusinessException("Conflit d'horaire : la salle est déjà réservée à cet horaire.");
        }

        // Vérifier les conflits de professeur (sauf pour l'ID actuel si modification)
        if (verifierConflitProfesseur(emploi.getProfesseurId(), emploi.getJourSemaine(), 
                                     emploi.getHeureDebut(), emploi.getHeureFin(), emploi.getId())) {
            throw new BusinessException("Conflit d'horaire : le professeur n'est pas disponible à cet horaire. Il a déjà un cours programmé à ce créneau.");
        }

        // Vérifier la charge horaire selon le workflow
        verifierChargeHoraire(emploi, matiere);

        return emploiRepository.save(emploi);
    }

    @Override
    public Optional<EmploiDuTemps> getEmploiDuTempsById(Long id) {
        return emploiRepository.findById(id);
    }

    @Override
    public List<EmploiDuTemps> getAllEmploiDuTemps() {
        return emploiRepository.findAll();
    }

    @Override
    public void deleteEmploiDuTemps(Long id) {
        if (!emploiRepository.existsById(id)) {
            throw new BusinessException("Emploi du temps introuvable pour la suppression.");
        }
        emploiRepository.deleteById(id);
    }

    @Override
    public List<EmploiDuTemps> getEmploiDuTempsByFiliere(Long filiereId) {
        return emploiRepository.findByFiliereId(filiereId);
    }

    @Override
    public List<EmploiDuTemps> getEmploiDuTempsByFiliereAndAnnee(Long filiereId, Integer annee) {
        return emploiRepository.findByFiliereIdAndAnnee(filiereId, annee);
    }

    @Override
    public List<EmploiDuTemps> getEmploiDuTempsByMatiere(Long matiereId) {
        return emploiRepository.findByMatiereId(matiereId);
    }

    @Override
    public List<EmploiDuTemps> getEmploiDuTempsByProfesseur(Long professeurId) {
        return emploiRepository.findByProfesseurId(professeurId);
    }

    @Override
    public List<EmploiDuTemps> getEmploiDuTempsBySalle(Long salleId) {
        return emploiRepository.findBySalleId(salleId);
    }

    @Override
    public List<EmploiDuTemps> getEmploiDuTempsByFiliereAnneeGroupe(Long filiereId, Integer annee, String groupe) {
        if (groupe == null || groupe.trim().isEmpty()) {
            return getEmploiDuTempsByFiliereAndAnnee(filiereId, annee);
        }
        return emploiRepository.findByFiliereIdAndAnneeAndGroupe(filiereId, annee, groupe);
    }

    @Override
    public boolean verifierConflitSalle(Long salleId, EmploiDuTemps.JourSemaine jour, 
                                       LocalTime heureDebut, LocalTime heureFin, Long excludeId) {
        List<EmploiDuTemps> conflits = emploiRepository.findConflitsSalle(salleId, jour, heureDebut, heureFin);
        if (excludeId != null) {
            conflits.removeIf(e -> e.getId().equals(excludeId));
        }
        return !conflits.isEmpty();
    }

    @Override
    public boolean verifierConflitProfesseur(Long professeurId, EmploiDuTemps.JourSemaine jour, 
                                             LocalTime heureDebut, LocalTime heureFin, Long excludeId) {
        List<EmploiDuTemps> conflits = emploiRepository.findConflitsProfesseur(professeurId, jour, heureDebut, heureFin);
        if (excludeId != null) {
            conflits.removeIf(e -> e.getId().equals(excludeId));
        }
        return !conflits.isEmpty();
    }

    @Override
    public List<EmploiDuTemps> genererSeances(Long matiereId, Long salleId, EmploiDuTemps.JourSemaine jour, 
                                             LocalTime heureDebut, LocalTime heureFin, 
                                             Integer nombreSeances, String groupe) {
        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new BusinessException("La matière spécifiée n'existe pas."));
        
        Filiere filiere = filiereRepository.findById(matiere.getFiliereId())
                .orElseThrow(() -> new BusinessException("La filière spécifiée n'existe pas."));
        
        User professeur = userRepository.findById(matiere.getProfesseurId())
                .orElseThrow(() -> new BusinessException("Le professeur spécifié n'existe pas."));
        
        Salle salle = salleRepository.findById(salleId)
                .orElseThrow(() -> new BusinessException("La salle spécifiée n'existe pas."));

        List<EmploiDuTemps> seances = new ArrayList<>();
        EmploiDuTemps.TypeSeance typeSeance = determineTypeSeance(salle.getType(), matiere);

        for (int i = 1; i <= nombreSeances; i++) {
            EmploiDuTemps seance = new EmploiDuTemps();
            seance.setFiliereId(matiere.getFiliereId());
            seance.setFiliereNom(filiere.getNom());
            seance.setAnnee(filiere.getAnnee());
            seance.setMatiereId(matiereId);
            seance.setMatiereNom(matiere.getNom());
            seance.setProfesseurId(matiere.getProfesseurId());
            seance.setProfesseurNom(professeur.getNom() + " " + professeur.getPrenom());
            seance.setSalleId(salleId);
            seance.setSalleNom(salle.getNom());
            seance.setJourSemaine(jour);
            seance.setHeureDebut(heureDebut);
            seance.setHeureFin(heureFin);
            seance.setTypeSeance(typeSeance);
            seance.setNumeroSeance(i);
            seance.setGroupe(groupe);

            seances.add(emploiRepository.save(seance));
        }

        return seances;
    }

    private EmploiDuTemps.TypeSeance determineTypeSeance(Salle.TypeSalle typeSalle, Matiere matiere) {
        if (typeSalle == Salle.TypeSalle.TP) {
            return EmploiDuTemps.TypeSeance.TP;
        } else if (typeSalle == Salle.TypeSalle.TD) {
            return EmploiDuTemps.TypeSeance.TD;
        } else {
            return EmploiDuTemps.TypeSeance.COURS;
        }
    }

    /**
     * Vérifie que la charge horaire de la matière n'est pas dépassée selon le workflow
     */
    private void verifierChargeHoraire(EmploiDuTemps emploi, Matiere matiere) {
        // Calculer la durée de la séance en heures
        long minutes = java.time.Duration.between(emploi.getHeureDebut(), emploi.getHeureFin()).toMinutes();
        double dureeSeance = minutes / 60.0; // en heures

        // Récupérer toutes les séances existantes pour cette matière (sauf celle en cours de modification)
        List<EmploiDuTemps> seancesExistantes = emploiRepository.findByMatiereId(matiere.getId());
        
        // Calculer les heures déjà utilisées par type de séance
        double heuresCoursUtilisees = 0;
        double heuresTDUtilisees = 0;
        double heuresTPUtilisees = 0;

        for (EmploiDuTemps seance : seancesExistantes) {
            if (seance.getId() != null && emploi.getId() != null && seance.getId().equals(emploi.getId())) {
                continue; // Ignorer la séance en cours de modification
            }
            
            long seanceMinutes = java.time.Duration.between(seance.getHeureDebut(), seance.getHeureFin()).toMinutes();
            double seanceHeures = seanceMinutes / 60.0;

            if (seance.getTypeSeance() == EmploiDuTemps.TypeSeance.COURS) {
                heuresCoursUtilisees += seanceHeures;
            } else if (seance.getTypeSeance() == EmploiDuTemps.TypeSeance.TD) {
                heuresTDUtilisees += seanceHeures;
            } else if (seance.getTypeSeance() == EmploiDuTemps.TypeSeance.TP) {
                heuresTPUtilisees += seanceHeures;
            }
        }

        // Ajouter la durée de la séance actuelle
        if (emploi.getTypeSeance() == EmploiDuTemps.TypeSeance.COURS) {
            heuresCoursUtilisees += dureeSeance;
        } else if (emploi.getTypeSeance() == EmploiDuTemps.TypeSeance.TD) {
            heuresTDUtilisees += dureeSeance;
        } else if (emploi.getTypeSeance() == EmploiDuTemps.TypeSeance.TP) {
            heuresTPUtilisees += dureeSeance;
        }

        // Vérifier les limites
        if (matiere.getHeuresCours() != null && heuresCoursUtilisees > matiere.getHeuresCours()) {
            throw new BusinessException(String.format(
                "La charge horaire de cours est dépassée. Limite: %d h, Utilisé: %.2f h",
                matiere.getHeuresCours(), heuresCoursUtilisees
            ));
        }
        if (matiere.getHeuresTD() != null && heuresTDUtilisees > matiere.getHeuresTD()) {
            throw new BusinessException(String.format(
                "La charge horaire de TD est dépassée. Limite: %d h, Utilisé: %.2f h",
                matiere.getHeuresTD(), heuresTDUtilisees
            ));
        }
        if (matiere.getHeuresTP() != null && heuresTPUtilisees > matiere.getHeuresTP()) {
            throw new BusinessException(String.format(
                "La charge horaire de TP est dépassée. Limite: %d h, Utilisé: %.2f h",
                matiere.getHeuresTP(), heuresTPUtilisees
            ));
        }
    }
}

