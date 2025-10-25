<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row justify-content-center">
    <div class="col-md-6 text-center">
        <div class="error-template">
            <h1 class="display-1 text-muted">404</h1>
            <h2 class="mb-4">
                <i class="bi bi-exclamation-triangle text-warning"></i>
                Page non trouvée
            </h2>
            <div class="error-details mb-4">
                <p class="lead">Désolé, la page que vous recherchez n'existe pas.</p>
                <p class="text-muted">Il est possible que l'URL soit incorrecte ou que la page ait été déplacée.</p>
            </div>
            <div class="error-actions">
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary me-2">
                    <i class="bi bi-house"></i> Retour à l'accueil
                </a>
                <a href="${pageContext.request.contextPath}/salles" class="btn btn-outline-primary">
                    <i class="bi bi-list-ul"></i> Voir les salles
                </a>
            </div>
        </div>
    </div>
</div>
