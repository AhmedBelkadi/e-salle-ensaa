<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Mon Dashboard - E-Salle ENSAA"/>
</jsp:include>

<div class="flex-1 p-6">
    <div class="space-y-6">
        <!-- Header -->
        <div>
            <h1 class="text-3xl font-bold text-gray-900">Mon Dashboard</h1>
            <p class="text-gray-600 mt-2">Mes réservations et activités</p>
        </div>

        <!-- Stats Card -->
        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
            <div class="px-6 pt-0">
                <div class="flex items-center justify-between">
                    <div>
                        <p class="text-gray-600 text-sm">Mes Réservations</p>
                        <p class="text-3xl font-bold text-gray-900 mt-2">${mesReservations.size()}</p>
                    </div>
                    <div class="bg-blue-500 p-3 rounded-lg">
                        <i class="fas fa-calendar-check text-white text-xl"></i>
                    </div>
                </div>
            </div>
        </div>

        <!-- Mes Réservations -->
        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
            <div class="px-6 pt-0">
                <div class="flex items-center justify-between mb-4">
                    <div>
                        <h2 class="text-lg font-semibold text-gray-900">Mes Réservations</h2>
                        <p class="text-sm text-gray-600">Historique de mes demandes</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/reservations/new" 
                       class="inline-flex items-center gap-2 px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700 text-sm">
                        <i class="fas fa-plus"></i> Nouvelle Réservation
                    </a>
                </div>
                <div class="space-y-3">
                    <c:choose>
                        <c:when test="${empty mesReservations}">
                            <div class="text-center py-12">
                                <p class="text-gray-500 mb-4">Aucune réservation</p>
                                <a href="${pageContext.request.contextPath}/reservations/new" 
                                   class="inline-block px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700">
                                    Créer ma première réservation
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="reservation" items="${mesReservations}">
                                <div class="p-4 bg-gray-50 rounded-lg border border-gray-200">
                                    <div class="flex items-start justify-between">
                                        <div class="flex-1">
                                            <h3 class="font-semibold text-gray-900">${reservation.motif}</h3>
                                            <div class="mt-2 space-y-1 text-sm text-gray-600">
                                                <p>
                                                    <i class="fas fa-calendar mr-2"></i>
                                                    ${reservation.dateReservation}
                                                </p>
                                                <p>
                                                    <i class="fas fa-clock mr-2"></i>
                                                    ${reservation.heureDebut} - ${reservation.heureFin}
                                                </p>
                                                <p>
                                                    <i class="fas fa-door-open mr-2"></i>
                                                    ${reservation.salleNom}
                                                </p>
                                            </div>
                                        </div>
                                        <div class="flex flex-col items-end gap-2">
                                            <c:choose>
                                                <c:when test="${reservation.statut.name() == 'APPROUVEE'}">
                                                    <span class="text-xs bg-green-100 text-green-800 px-2 py-1 rounded border border-green-200">Approuvée</span>
                                                </c:when>
                                                <c:when test="${reservation.statut.name() == 'EN_ATTENTE'}">
                                                    <span class="text-xs bg-yellow-100 text-yellow-800 px-2 py-1 rounded border border-yellow-200">En Attente</span>
                                                </c:when>
                                                <c:when test="${reservation.statut.name() == 'REFUSEE'}">
                                                    <span class="text-xs bg-red-100 text-red-800 px-2 py-1 rounded border border-red-200">Refusée</span>
                                                </c:when>
                                                <c:when test="${reservation.statut.name() == 'ANNULEE'}">
                                                    <span class="text-xs bg-gray-100 text-gray-800 px-2 py-1 rounded border border-gray-200">Annulée</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-xs bg-gray-100 text-gray-800 px-2 py-1 rounded">${reservation.statut}</span>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:if test="${reservation.statut.name() == 'EN_ATTENTE' || reservation.statut.name() == 'APPROUVEE'}">
                                                <form method="POST" action="${pageContext.request.contextPath}/reservations/cancel" class="inline">
                                                    <input type="hidden" name="reservationId" value="${reservation.id}">
                                                    <button type="submit" 
                                                            onclick="return confirm('Êtes-vous sûr de vouloir annuler cette réservation ?')"
                                                            class="text-xs text-red-600 hover:text-red-800">
                                                        Annuler
                                                    </button>
                                                </form>
                                            </c:if>
                                        </div>
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

