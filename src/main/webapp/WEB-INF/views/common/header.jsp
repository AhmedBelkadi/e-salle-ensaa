<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title != null ? param.title : 'E-Salle ENSAA'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/output.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-gray-50 min-h-screen flex">
    
    <!-- Sidebar -->
    <aside id="sidebar" class="w-64 bg-white border-r border-gray-200 flex flex-col transition-all duration-300 shadow-sm fixed h-screen overflow-y-auto">
        <!-- Header -->
        <div class="p-4 border-b border-gray-200 flex items-center justify-between">
            <div id="sidebarHeader">
                <h1 class="text-xl font-bold text-gray-900">E-Salle ENSAA</h1>
                <p class="text-xs text-gray-600 mt-1">Gestion des Salles</p>
            </div>
            <button id="toggleSidebar" class="text-gray-500 hover:bg-gray-100 p-2 rounded">
                <i class="fas fa-chevron-left w-4 h-4"></i>
            </button>
        </div>

        <!-- Navigation -->
        <nav class="flex-1 p-4 space-y-2">
            <c:if test="${not empty sessionScope.user}">

            <!-- Consultation - Tous les rôles -->
                <c:if test="${not empty sessionScope.user}">
                    <c:choose>
                        <c:when test="${sessionScope.user.role.name() == 'ADMIN'}">
                            <a href="${pageContext.request.contextPath}/consultation/admin" class="block">
                                <button class="text-gray-700 hover:bg-gray-100 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                                    <i class="fas fa-tachometer-alt w-4 h-4"></i>
                                    <span class="nav-text">Dashboard</span>
                                </button>
                            </a>
                        </c:when>
                        <c:when test="${sessionScope.user.role.name() == 'COORDINATEUR'}">
                            <a href="${pageContext.request.contextPath}/consultation/coordinateur" class="block">
                                <button class="text-gray-700 hover:bg-gray-100 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                                    <i class="fas fa-tachometer-alt w-4 h-4"></i>
                                    <span class="nav-text">Dashboard</span>
                                </button>
                            </a>
                        </c:when>
                        <c:when test="${sessionScope.user.role.name() == 'PROFESSEUR'}">
                            <a href="${pageContext.request.contextPath}/consultation/professeur" class="block">
                                <button class="text-gray-700 hover:bg-gray-100 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                                    <i class="fas fa-tachometer-alt w-4 h-4"></i>
                                    <span class="nav-text">Mon Dashboard</span>
                                </button>
                            </a>
                        </c:when>
                        <c:when test="${sessionScope.user.role.name() == 'MEMBRE_CLUB'}">
                            <a href="${pageContext.request.contextPath}/consultation/club" class="block">
                                <button class="text-gray-700 hover:bg-gray-100 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                                    <i class="fas fa-tachometer-alt w-4 h-4"></i>
                                    <span class="nav-text">Mon Dashboard</span>
                                </button>
                            </a>
                        </c:when>
                    </c:choose>
                </c:if>

                <!-- Salles -->
                <a href="${pageContext.request.contextPath}/salles/list" class="block">
                    <button class="text-gray-700 hover:bg-gray-100 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                        <i class="fas fa-door-open w-4 h-4"></i>
                        <span class="nav-text">Salles</span>
                    </button>
                </a>

                <!-- Filières - ADMIN et COORDINATEUR -->
                <c:if test="${sessionScope.user.role.name() == 'ADMIN' || sessionScope.user.role.name() == 'COORDINATEUR'}">
                    <a href="${pageContext.request.contextPath}/filieres/list" class="block">
                        <button class="text-gray-700 hover:bg-gray-100 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                            <i class="fas fa-graduation-cap w-4 h-4"></i>
                            <span class="nav-text">Filières</span>
                        </button>
                    </a>
                </c:if>

                <!-- Réclamations -->
                <c:choose>
                    <c:when test="${sessionScope.user.role.name() == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/reclamations/list" class="block">
                            <button class="text-gray-700 hover:bg-gray-100 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                                <i class="fas fa-exclamation-circle w-4 h-4"></i>
                                <span class="nav-text">Réclamations</span>
                            </button>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/reclamations/mes-reclamations" class="block">
                            <button class="text-gray-700 hover:bg-gray-100 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                                <i class="fas fa-exclamation-circle w-4 h-4"></i>
                                <span class="nav-text">Mes Réclamations</span>
                            </button>
                        </a>
                    </c:otherwise>
                </c:choose>

                <!-- Administration - ADMIN uniquement -->
                <c:if test="${sessionScope.user.role.name() == 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/admin/users/pending" class="block">
                        <button class="text-gray-700 hover:bg-gray-100 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                            <i class="fas fa-user-check w-4 h-4"></i>
                            <span class="nav-text">Administration</span>
                        </button>
                    </a>
                </c:if>

                <!-- Matières - COORDINATEUR, PROFESSEUR (ADMIN removed) -->
                <c:if test="${sessionScope.user.role.name() == 'COORDINATEUR' || sessionScope.user.role.name() == 'PROFESSEUR'}">
                    <a href="${pageContext.request.contextPath}/matieres/list" class="block">
                        <button class="text-gray-700 hover:bg-gray-100 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                            <i class="fas fa-book w-4 h-4"></i>
                            <span class="nav-text">Matières</span>
                        </button>
                    </a>
                </c:if>

                <!-- Emploi du Temps - ADMIN, COORDINATEUR, PROFESSEUR -->
                <c:if test="${sessionScope.user.role.name() == 'ADMIN' || sessionScope.user.role.name() == 'COORDINATEUR' || sessionScope.user.role.name() == 'PROFESSEUR'}">
                    <a href="${pageContext.request.contextPath}/emploi/list" class="block">
                        <button class="text-gray-700 hover:bg-gray-100 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                            <i class="fas fa-calendar-alt w-4 h-4"></i>
                            <span class="nav-text">Emploi du Temps</span>
                        </button>
                    </a>
                </c:if>

                <!-- Réservations -->
                <c:choose>
                    <c:when test="${sessionScope.user.role.name() == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/reservations/list?filter=all" class="block">
                            <button class="text-gray-700 hover:bg-gray-100 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                                <i class="fas fa-calendar-check w-4 h-4"></i>
                                <span class="nav-text">Réservations</span>
                            </button>
                        </a>
                    </c:when>
                    <c:when test="${sessionScope.user.role.name() == 'PROFESSEUR' || sessionScope.user.role.name() == 'MEMBRE_CLUB' || sessionScope.user.role.name() == 'COORDINATEUR'}">
                        <a href="${pageContext.request.contextPath}/reservations/list" class="block">
                            <button class="text-gray-700 hover:bg-gray-100 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                                <i class="fas fa-calendar-check w-4 h-4"></i>
                                <span class="nav-text">Mes Réservations</span>
                            </button>
                        </a>
                    </c:when>
                </c:choose>

                
            </c:if>
        </nav>

        <!-- Footer -->
        <div class="p-4 border-t border-gray-200 space-y-2">
            <c:if test="${not empty sessionScope.user}">
                <!-- User Info -->
                <div class="px-3 py-2 bg-gray-50 rounded-md mb-2">
                    <div class="flex items-center gap-3">
                        <div class="w-8 h-8 bg-blue-600 rounded-full flex items-center justify-center flex-shrink-0">
                            <span class="text-white text-xs font-semibold">${sessionScope.user.nom.substring(0, 1)}${sessionScope.user.prenom.substring(0, 1)}</span>
                        </div>
                        <div class="flex-1 min-w-0 user-info">
                            <p class="text-sm font-medium text-gray-900 truncate">${sessionScope.user.nom} ${sessionScope.user.prenom}</p>
                            <p class="text-xs text-gray-600 capitalize">
                                <c:choose>
                                    <c:when test="${sessionScope.user.role.name() == 'ADMIN'}">👑 Admin</c:when>
                                    <c:when test="${sessionScope.user.role.name() == 'COORDINATEUR'}">📚 Coordinateur</c:when>
                                    <c:when test="${sessionScope.user.role.name() == 'PROFESSEUR'}">👨‍🏫 Professeur</c:when>
                                    <c:when test="${sessionScope.user.role.name() == 'MEMBRE_CLUB'}">🎭 Membre Club</c:when>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                </div>

                <!-- Logout -->
                <a href="${pageContext.request.contextPath}/auth/logout" class="block">
                    <button class="text-red-600 hover:bg-red-50 hover:text-red-700 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                        <i class="fas fa-sign-out-alt w-4 h-4"></i>
                        <span class="nav-text">Déconnexion</span>
                    </button>
                </a>
            </c:if>
            
            <c:if test="${empty sessionScope.user}">
                <!-- Login -->
                <a href="${pageContext.request.contextPath}/auth/login" class="block">
                    <button class="text-blue-600 hover:bg-blue-50 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                        <i class="fas fa-sign-in-alt w-4 h-4"></i>
                        <span class="nav-text">Connexion</span>
                    </button>
                </a>
                <!-- Register -->
                <a href="${pageContext.request.contextPath}/auth/register" class="block">
                    <button class="text-gray-700 hover:bg-gray-100 w-full justify-start gap-3 px-3 py-2 rounded-md flex items-center">
                        <i class="fas fa-user-plus w-4 h-4"></i>
                        <span class="nav-text">Inscription</span>
                    </button>
                </a>
            </c:if>
        </div>
    </aside>

    <!-- Main Content Wrapper -->
    <div class="flex-1 flex flex-col min-h-screen ml-64">
        <!-- Top Bar -->
        <header class="bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between shadow-sm">
            <div>
                <h2 class="text-lg font-semibold text-gray-900">
                    <c:if test="${not empty sessionScope.user}">
                        Bienvenue, ${sessionScope.user.prenom} ${sessionScope.user.nom}
                    </c:if>
                    <c:if test="${empty sessionScope.user}">
                        E-Salle ENSAA
                    </c:if>
                </h2>
                <p class="text-sm text-gray-600 capitalize">
                    <c:if test="${not empty sessionScope.user}">
                        <c:choose>
                            <c:when test="${sessionScope.user.role.name() == 'ADMIN'}">Administrateur</c:when>
                            <c:when test="${sessionScope.user.role.name() == 'COORDINATEUR'}">Coordinateur</c:when>
                            <c:when test="${sessionScope.user.role.name() == 'PROFESSEUR'}">Professeur</c:when>
                            <c:when test="${sessionScope.user.role.name() == 'MEMBRE_CLUB'}">Membre Club</c:when>
                        </c:choose>
                    </c:if>
                    <c:if test="${empty sessionScope.user}">
                        Système de gestion des salles
                    </c:if>
                </p>
            </div>

            <div class="flex items-center gap-4">
                <c:if test="${not empty sessionScope.user}">
                    <!-- Notification Bell (placeholder) -->
                    <button class="relative p-2 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-lg transition">
                        <i class="fas fa-bell w-5 h-5"></i>
                        <span class="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full"></span>
                    </button>

                    <!-- User Avatar -->
                    <div class="w-10 h-10 bg-blue-600 border border-gray-300 rounded-full flex items-center justify-center">
                        <span class="text-white font-semibold">${sessionScope.user.nom.substring(0, 1)}${sessionScope.user.prenom.substring(0, 1)}</span>
                    </div>
                </c:if>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-1 overflow-auto">
