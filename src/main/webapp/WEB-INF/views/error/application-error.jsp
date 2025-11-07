<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur application - E-Salle ENSAA</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-purple-50 to-gray-100 p-4">
        <div class="text-center max-w-md">
            <div class="mb-8">
                <div class="inline-flex items-center justify-center w-24 h-24 bg-purple-100 rounded-full">
                    <i class="fas fa-bug w-12 h-12 text-purple-600"></i>
                </div>
            </div>
            <h1 class="text-4xl font-bold text-gray-900 mb-4">Erreur application</h1>
            <p class="text-gray-600 mb-2">
                <c:choose>
                    <c:when test="${not empty errorMessage}">${errorMessage}</c:when>
                    <c:when test="${not empty param.message}">${param.message}</c:when>
                    <c:otherwise>Une erreur d'application s'est produite.</c:otherwise>
                </c:choose>
            </p>
            <div class="flex flex-col sm:flex-row gap-3 justify-center mt-8">
                <a href="${pageContext.request.contextPath}/auth/login" 
                   class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-10 px-6 bg-blue-600 hover:bg-blue-700 text-white transition">
                    <i class="fas fa-sign-in-alt w-4 h-4"></i>
                    Page de connexion
                </a>
                <button onclick="history.back()"
                    class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-10 px-6 border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 transition">
                    <i class="fas fa-arrow-left w-4 h-4"></i>
                    Page précédente
                </button>
            </div>
        </div>
    </div>
</body>
</html>

