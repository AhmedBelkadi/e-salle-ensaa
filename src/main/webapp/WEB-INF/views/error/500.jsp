<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row justify-content-center">
    <div class="col-md-6 text-center">
        <div class="error-template">
            <h1 class="display-1 text-muted">500</h1>
            <h2 class="mb-4">
                <i class="bi bi-exclamation-octagon text-danger"></i>
                Erreur interne du serveur
            </h2>
            <div class="error-details mb-4">
                <p class="lead">Une erreur inattendue s'est produite.</p>
                <p class="text-muted">Notre équipe technique a été notifiée et travaille à résoudre le problème.</p>
            </div>
            <div class="error-actions">
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary me-2">
                    <i class="bi bi-house"></i> Retour à l'accueil
                </a>
                <button onclick="history.back()" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left"></i> Page précédente
                </button>
            </div>
        </div>
    </div>
</div>
