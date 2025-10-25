<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="row">
    <div class="col-12">
        <!-- Hero Section -->
        <div class="jumbotron bg-primary text-white rounded p-5 mb-5">
            <div class="container-fluid">
                <div class="row align-items-center">
                    <div class="col-lg-8">
                        <h1 class="display-4 fw-bold mb-3">
                            <i class="bi bi-building"></i> E-Salle ENSAA
                        </h1>
                        <p class="lead mb-4">
                            Système de gestion des salles de l'École Nationale Supérieure d'Architecture et d'Art
                        </p>
                        <div class="alert alert-info">
                            <i class="bi bi-info-circle"></i>
                            Bienvenue sur E-Salle ENSAA. Utilisez le guide de développement pour créer vos modules.
                        </div>
                    </div>
                    <div class="col-lg-4 text-center">
                        <i class="bi bi-building display-1 opacity-75"></i>
                    </div>
                </div>
            </div>
        </div>

        <!-- Features Section -->
        <div class="row mb-5">
            <div class="col-md-4 mb-4">
                <div class="card h-100 text-center">
                    <div class="card-body">
                        <i class="bi bi-search display-4 text-primary mb-3"></i>
                        <h5 class="card-title">Recherche Avancée</h5>
                        <p class="card-text">
                            Trouvez rapidement les salles selon vos critères : capacité, bâtiment, étage, équipements.
                        </p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-4">
                <div class="card h-100 text-center">
                    <div class="card-body">
                        <i class="bi bi-gear display-4 text-success mb-3"></i>
                        <h5 class="card-title">Gestion Complète</h5>
                        <p class="card-text">
                            Gérez facilement toutes les informations des salles : création, modification, suppression.
                        </p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-4">
                <div class="card h-100 text-center">
                    <div class="card-body">
                        <i class="bi bi-graph-up display-4 text-info mb-3"></i>
                        <h5 class="card-title">Statistiques</h5>
                        <p class="card-text">
                            Consultez les statistiques d'utilisation et la disponibilité des salles en temps réel.
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Getting Started -->
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header bg-success text-white">
                        <h5 class="card-title mb-0">
                            <i class="bi bi-rocket"></i> Démarrage Rapide
                        </h5>
                    </div>
                    <div class="card-body">
                        <h6 class="mb-3">Pour commencer le développement :</h6>
                        <ol class="mb-0">
                            <li class="mb-2">Consultez le fichier <code>GUIDE_CREATION_MODULE_CRUD.tex</code> pour apprendre à créer des modules CRUD</li>
                            <li class="mb-2">Suivez le <code>PROJECT_MANAGEMENT_PLAN.tex</code> pour la répartition des tâches</li>
                            <li class="mb-2">Référez-vous à <code>STRATEGIE_GIT_GITHUB.tex</code> pour le workflow Git</li>
                            <li class="mb-2">Créez vos modules : Salle, Filière, Matière, Réclamation, Réservation, Emploi du Temps</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


