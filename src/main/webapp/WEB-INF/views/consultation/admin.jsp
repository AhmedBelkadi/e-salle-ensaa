<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Dashboard Admin - E-Salle ENSAA"/>
</jsp:include>

<div class="flex-1 p-6">
    <div class="space-y-6">
        <!-- Header -->
        <div>
            <h1 class="text-3xl font-bold text-gray-900">Dashboard Administrateur</h1>
            <p class="text-gray-600 mt-2">Vue d'ensemble globale du système</p>
        </div>

        <!-- Stats Cards -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            <!-- Total Reservations -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Total Réservations</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${totalReservations}</p>
                        </div>
                        <div class="bg-blue-500 p-3 rounded-lg">
                            <i class="fas fa-calendar-check text-white text-xl"></i>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Pending Reservations -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">En Attente</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${pendingReservations}</p>
                        </div>
                        <div class="bg-yellow-500 p-3 rounded-lg">
                            <i class="fas fa-clock text-white text-xl"></i>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Total Salles -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Total Salles</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${totalSalles}</p>
                        </div>
                        <div class="bg-green-500 p-3 rounded-lg">
                            <i class="fas fa-door-open text-white text-xl"></i>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Open Issues -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Problèmes Ouverts</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${openIssues}</p>
                        </div>
                        <div class="bg-red-500 p-3 rounded-lg">
                            <i class="fas fa-exclamation-triangle text-white text-xl"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Recent Reservations -->
        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
            <div class="px-6 pt-0">
                <div class="flex items-center justify-between mb-4">
                    <div>
                        <h2 class="text-lg font-semibold text-gray-900">Réservations Récentes</h2>
                        <p class="text-sm text-gray-600">Dernières réservations en attente</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/reservations/list?filter=pending" 
                       class="text-sm text-blue-600 hover:text-blue-800">
                        Voir tout
                    </a>
                </div>
                <div class="space-y-3">
                    <c:choose>
                        <c:when test="${empty recentReservations}">
                            <p class="text-gray-500 text-center py-4">Aucune réservation récente</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="reservation" items="${recentReservations}">
                                <div class="p-4 bg-gray-50 rounded-lg border border-gray-200">
                                    <div class="flex items-start justify-between">
                                        <div>
                                            <h3 class="font-semibold text-gray-900">${reservation.motif}</h3>
                                            <p class="text-sm text-gray-600 mt-1">
                                                ${reservation.dateReservation}
                                            </p>
                                            <p class="text-sm text-gray-600">${reservation.userNom} - ${reservation.salleNom}</p>
                                        </div>
                                        <span class="text-xs bg-yellow-100 text-yellow-800 px-2 py-1 rounded border border-yellow-200">
                                            Priorité: ${reservation.priorite}
                                        </span>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Recent Reclamations -->
        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
            <div class="px-6 pt-0">
                <div class="flex items-center justify-between mb-4">
                    <div>
                        <h2 class="text-lg font-semibold text-gray-900">Réclamations Récentes</h2>
                        <p class="text-sm text-gray-600">Problèmes signalés</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/reclamations/list" 
                       class="text-sm text-blue-600 hover:text-blue-800">
                        Voir tout
                    </a>
                </div>
                <div class="space-y-3">
                    <c:choose>
                        <c:when test="${empty recentReclamations}">
                            <p class="text-gray-500 text-center py-4">Aucune réclamation récente</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="reclamation" items="${recentReclamations}">
                                <div class="p-4 bg-gray-50 rounded-lg border border-gray-200">
                                    <div class="flex items-start justify-between">
                                        <div>
                                            <h3 class="font-semibold text-gray-900">
                                                <c:out value="${reclamation.salleNom}" default="N/A"/>
                                            </h3>
                                            <p class="text-sm text-gray-600 mt-1">
                                                <c:choose>
                                                    <c:when test="${not empty reclamation.description}">
                                                        <c:choose>
                                                            <c:when test="${fn:length(reclamation.description) > 50}">
                                                                ${fn:substring(reclamation.description, 0, 50)}...
                                                            </c:when>
                                                            <c:otherwise>
                                                                ${reclamation.description}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        Aucune description
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                            <p class="text-xs text-gray-500 mt-1">
                                                Par: <c:out value="${reclamation.userNom}" default="Inconnu"/>
                                            </p>
                                        </div>
                                        <span class="text-xs bg-red-100 text-red-800 px-2 py-1 rounded border border-red-200">
                                            <c:out value="${reclamation.urgence}" default="NORMALE"/>
                                        </span>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>