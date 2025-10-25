<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur - ${pageContext.servletContext.getInitParameter('appName')}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card border-danger">
                    <div class="card-header bg-danger text-white">
                        <h4 class="mb-0">
                            <i class="bi bi-exclamation-triangle"></i>
                            Erreur Système
                        </h4>
                    </div>
                    <div class="card-body">
                        <div class="alert alert-danger">
                            <h5 class="alert-heading">
                                <i class="bi bi-x-circle"></i>
                                Une erreur s'est produite
                            </h5>
                            <p class="mb-0">${errorMessage}</p>
                        </div>
                        
                        <div class="mt-4">
                            <h6>Que pouvez-vous faire ?</h6>
                            <ul>
                                <li>Vérifiez que l'URL est correcte</li>
                                <li>Réessayez dans quelques instants</li>
                                <li>Contactez l'administrateur si le problème persiste</li>
                            </ul>
                        </div>
                        
                        <div class="mt-4">
                            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                                <i class="bi bi-house"></i> Retour à l'accueil
                            </a>
                            <button onclick="history.back()" class="btn btn-secondary">
                                <i class="bi bi-arrow-left"></i> Page précédente
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
