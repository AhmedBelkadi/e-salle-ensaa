<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title != null ? param.title : 'E-Salle ENSAA'}</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    
    <!-- Navigation -->
    <nav class="bg-indigo-600 text-white shadow-lg">
        <div class="container mx-auto px-4 py-4">
            <div class="flex justify-between items-center">
                <a href="${pageContext.request.contextPath}/" class="text-2xl font-bold hover:text-indigo-200 transition">
                    🏫 E-Salle ENSAA
                </a>
                
                <div class="hidden md:flex space-x-6">
                    <a href="${pageContext.request.contextPath}/" class="hover:text-indigo-200 transition">
                        🏠 Accueil
                    </a>
                    <a href="${pageContext.request.contextPath}/salles/list" class="hover:text-indigo-200 transition">
                        🚪 Salles
                    </a>
                    <c:if test="${not empty sessionScope.user}">
                        <a href="${pageContext.request.contextPath}/auth/logout" class="hover:text-indigo-200 transition">
                            🚪 Déconnexion
                        </a>
                    </c:if>
                </div>
                
                <!-- User info -->
                <c:if test="${not empty sessionScope.userName}">
                    <div class="hidden md:block text-sm">
                        <span class="bg-indigo-700 px-3 py-1 rounded-full">
                            👤 ${sessionScope.userName}
                        </span>
                    </div>
                </c:if>
            </div>
        </div>
    </nav>
    
    <!-- Main Content -->
    <main>
