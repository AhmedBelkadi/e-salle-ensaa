<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Dashboard Professeur - E-Salle ENSAA"/>
</jsp:include>

<div class="flex-1 p-6">
    <div class="space-y-6">
        <!-- Header -->
        <div>
            <h1 class="text-3xl font-bold text-gray-900">Mon Dashboard</h1>
            <p class="text-gray-600 mt-2">Vue d'ensemble de vos activités</p>
        </div>

        <!-- Stats Cards -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <!-- Mes Classes -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Mes Matières</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${mesMatieres.size()}</p>
                        </div>
                        <div class="bg-blue-500 p-3 rounded-lg">
                            <i class="fas fa-book text-white text-xl"></i>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Mes Réservations -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Mes Réservations</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${mesReservations.size()}</p>
                        </div>
                        <div class="bg-green-500 p-3 rounded-lg">
                            <i class="fas fa-calendar-check text-white text-xl"></i>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Salles Disponibles -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Salles Disponibles</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${sallesDisponibles.size()}</p>
                        </div>
                        <div class="bg-purple-500 p-3 rounded-lg">
                            <i class="fas fa-door-open text-white text-xl"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Mon Emploi du Temps -->
        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
            <div class="px-6 pt-0">
                <div class="flex items-center justify-between mb-4">
                    <div>
                        <h2 class="text-lg font-semibold text-gray-900">Mon Emploi du Temps</h2>
                        <p class="text-sm text-gray-600">Séances de cette semaine</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/emploi/list" 
                       class="text-sm text-blue-600 hover:text-blue-800">
                        Voir tout
                    </a>
                </div>
                <div class="space-y-3">
                    <c:choose>
                        <c:when test="${empty monEmploi}">
                            <p class="text-gray-500 text-center py-4">Aucune séance planifiée</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="emploi" items="${monEmploi}" begin="0" end="4">
                                <div class="p-4 bg-gray-50 rounded-lg border border-gray-200">
                                    <div class="flex items-start justify-between">
                                        <div>
                                            <h3 class="font-semibold text-gray-900">${emploi.matiereNom}</h3>
                                            <p class="text-sm text-gray-600 mt-1">${emploi.heureDebut} - ${emploi.heureFin}</p>
                                            <p class="text-sm text-gray-600">Salle: ${emploi.salleNom}</p>
                                        </div>
                                        <span class="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded border border-blue-200">
                                            ${emploi.jourSemaine}
                                        </span>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Mes Réservations -->
        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
            <div class="px-6 pt-0">
                <div class="flex items-center justify-between mb-4">
                    <div>
                        <h2 class="text-lg font-semibold text-gray-900">Mes Réservations</h2>
                        <p class="text-sm text-gray-600">Réservations et demandes</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/reservations/list" 
                       class="text-sm text-blue-600 hover:text-blue-800">
                        Voir tout
                    </a>
                </div>
                <div class="space-y-3">
                    <c:choose>
                        <c:when test="${empty mesReservations}">
                            <p class="text-gray-500 text-center py-4">Aucune réservation</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="reservation" items="${mesReservations}" begin="0" end="4">
                                <div class="p-4 bg-gray-50 rounded-lg border border-gray-200">
                                    <div class="flex items-start justify-between">
                                        <div>
                                            <h3 class="font-semibold text-gray-900">${reservation.motif}</h3>
                                            <p class="text-sm text-gray-600 mt-1">
                                                ${reservation.dateReservation}
                                            </p>
                                            <p class="text-sm text-gray-600">${reservation.heureDebut} - ${reservation.heureFin}</p>
                                            <p class="text-sm text-gray-600">Salle: ${reservation.salleNom}</p>
                                        </div>
                                        <c:choose>
                                            <c:when test="${reservation.statut.name() == 'APPROUVEE'}">
                                                <span class="text-xs bg-green-100 text-green-800 px-2 py-1 rounded border border-green-200">Approuvée</span>
                                            </c:when>
                                            <c:when test="${reservation.statut.name() == 'EN_ATTENTE'}">
                                                <span class="text-xs bg-yellow-100 text-yellow-800 px-2 py-1 rounded border border-yellow-200">En Attente</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-xs bg-red-100 text-red-800 px-2 py-1 rounded border border-red-200">${reservation.statut}</span>
                                            </c:otherwise>
                                        </c:choose>
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

