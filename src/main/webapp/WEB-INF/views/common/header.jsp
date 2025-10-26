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
                    
                    <c:if test="${not empty sessionScope.user}">
                        <!-- Salles - Tous les utilisateurs authentifiés -->
                        <a href="${pageContext.request.contextPath}/salle/list" class="hover:text-indigo-200 transition">
                            🚪 Salles
                        </a>
                        
                        <!-- Filières - ADMIN et COORDINATEUR -->
                        <c:if test="${sessionScope.user.role.name() == 'ADMIN' || sessionScope.user.role.name() == 'COORDINATEUR'}">
                            <a href="${pageContext.request.contextPath}/filieres/list" class="hover:text-indigo-200 transition">
                                🎓 Filières
                            </a>
                        </c:if>
                        
                        <!-- Réclamations - Tous sauf ADMIN pour créer, ADMIN pour traiter -->
                        <c:choose>
                            <c:when test="${sessionScope.user.role.name() == 'ADMIN'}">
                                <a href="${pageContext.request.contextPath}/reclamations/list" class="hover:text-indigo-200 transition">
                                    📋 Réclamations (Gérer)
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/reclamations/mes-reclamations" class="hover:text-indigo-200 transition">
                                    📋 Mes Réclamations
                                </a>
                            </c:otherwise>
                        </c:choose>
                        
                        <!-- Administration - ADMIN uniquement -->
                        <c:if test="${sessionScope.user.role.name() == 'ADMIN'}">
                            <a href="${pageContext.request.contextPath}/users/list" class="hover:text-indigo-200 transition">
                                👥 Utilisateurs
                            </a>
                        </c:if>
                        
                        <a href="${pageContext.request.contextPath}/auth/logout" class="hover:text-indigo-200 transition">
                            🚪 Déconnexion
                        </a>
                    </c:if>
                    
                    <!-- Login pour utilisateurs non connectés -->
                    <c:if test="${empty sessionScope.user}">
                        <a href="${pageContext.request.contextPath}/auth/login" class="hover:text-indigo-200 transition">
                            🔐 Connexion
                        </a>
                        <a href="${pageContext.request.contextPath}/auth/register" class="hover:text-indigo-200 transition">
                            📝 Inscription
                        </a>
                    </c:if>
                </div>
                
                <!-- User info -->
                <c:if test="${not empty sessionScope.user}">
                    <div class="hidden md:block text-sm">
                        <div class="flex items-center space-x-2">
                            <span class="bg-indigo-700 px-3 py-1 rounded-full">
                                👤 ${sessionScope.user.nom} ${sessionScope.user.prenom}
                            </span>
                            <c:choose>
                                <c:when test="${sessionScope.user.role.name() == 'ADMIN'}">
                                    <span class="bg-red-600 px-2 py-1 rounded text-xs font-bold">👑 ADMIN</span>
                                </c:when>
                                <c:when test="${sessionScope.user.role.name() == 'COORDINATEUR'}">
                                    <span class="bg-green-600 px-2 py-1 rounded text-xs font-bold">📚 COORDINATEUR</span>
                                </c:when>
                                <c:when test="${sessionScope.user.role.name() == 'PROFESSEUR'}">
                                    <span class="bg-blue-600 px-2 py-1 rounded text-xs font-bold">👨‍🏫 PROFESSEUR</span>
                                </c:when>
                                <c:when test="${sessionScope.user.role.name() == 'MEMBRE_CLUB'}">
                                    <span class="bg-purple-600 px-2 py-1 rounded text-xs font-bold">🎭 CLUB</span>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </nav>
    
    <!-- Main Content -->
    <main>
