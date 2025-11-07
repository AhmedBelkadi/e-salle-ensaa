<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Réservations - E-Salle ENSAA"/>
</jsp:include>

<div class="flex-1 p-6">
    <div class="space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Réservations</h1>
                <p class="text-gray-600 mt-2">Gérer les réservations de salles</p>
            </div>
            <c:if test="${sessionScope.user.role.name() == 'PROFESSEUR' || sessionScope.user.role.name() == 'MEMBRE_CLUB' || sessionScope.user.role.name() == 'COORDINATEUR'}">
                <a href="${pageContext.request.contextPath}/reservations/new" 
                   class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-all bg-blue-600 hover:bg-blue-700 text-white h-9 px-4 py-2">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                    </svg>
                    Nouvelle Réservation
                </a>
            </c:if>
        </div>

        <!-- Stats Cards -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            <!-- Total Reservations -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Total Réservations</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${stats != null ? stats.total : 0}</p>
                        </div>
                        <div class="bg-blue-500 p-3 rounded-lg">
                            <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                            </svg>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Approved -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Approuvées</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${stats != null ? stats.approved : 0}</p>
                        </div>
                        <div class="bg-green-500 p-3 rounded-lg">
                            <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                            </svg>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Pending -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">En Attente</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${stats != null ? stats.pending : 0}</p>
                        </div>
                        <div class="bg-yellow-500 p-3 rounded-lg">
                            <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                            </svg>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Rejected -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Refusées</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${stats != null ? stats.refused : 0}</p>
                        </div>
                        <div class="bg-red-500 p-3 rounded-lg">
                            <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                            </svg>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Success Messages -->
        <c:if test="${not empty sessionScope.success}">
            <div class="bg-green-50 border border-green-200 rounded-lg p-4">
                <p class="text-green-800">${sessionScope.success}</p>
            </div>
            <c:remove var="success" scope="session"/>
        </c:if>
        <c:if test="${param.success == 'created'}">
            <div class="bg-green-50 border border-green-200 rounded-lg p-4">
                <p class="text-green-800">Réservation créée avec succès et en attente d'approbation</p>
            </div>
        </c:if>
        <c:if test="${param.success == 'approved'}">
            <div class="bg-green-50 border border-green-200 rounded-lg p-4">
                <p class="text-green-800">Réservation approuvée avec succès</p>
            </div>
        </c:if>
        <c:if test="${param.success == 'refused'}">
            <div class="bg-red-50 border border-red-200 rounded-lg p-4">
                <p class="text-red-800">Réservation refusée</p>
            </div>
        </c:if>
        <c:if test="${param.success == 'cancelled'}">
            <div class="bg-blue-50 border border-blue-200 rounded-lg p-4">
                <p class="text-blue-800">Réservation annulée avec succès</p>
            </div>
        </c:if>

        <!-- Error Messages -->
        <c:if test="${not empty sessionScope.error}">
            <div class="bg-red-50 border border-red-200 rounded-lg p-4">
                <p class="text-red-800">${sessionScope.error}</p>
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <!-- Filters -->
        <c:if test="${isAdmin}">
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex gap-2 flex-wrap">
                        <a href="${pageContext.request.contextPath}/reservations/list?filter=all" 
                           class="filter-btn ${filter == 'all' ? 'active' : ''} inline-flex items-center justify-center rounded-md text-sm font-medium h-9 px-4 py-2 ${filter == 'all' ? 'bg-blue-600 text-white' : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-100'}">
                            Toutes
                        </a>
                        <a href="${pageContext.request.contextPath}/reservations/list?filter=pending" 
                           class="filter-btn ${filter == 'pending' ? 'active' : ''} inline-flex items-center justify-center rounded-md text-sm font-medium h-9 px-4 py-2 ${filter == 'pending' ? 'bg-blue-600 text-white' : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-100'}">
                            En Attente
                        </a>
                        <a href="${pageContext.request.contextPath}/reservations/list?filter=approved" 
                           class="filter-btn ${filter == 'approved' ? 'active' : ''} inline-flex items-center justify-center rounded-md text-sm font-medium h-9 px-4 py-2 ${filter == 'approved' ? 'bg-blue-600 text-white' : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-100'}">
                            Approuvées
                        </a>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Reservations List -->
        <div class="space-y-3">
            <c:choose>
                <c:when test="${empty reservations}">
                    <div class="bg-white rounded-xl border border-gray-200 py-12 text-center">
                        <p class="text-gray-500">Aucune réservation trouvée</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="reservation" items="${reservations}">
                        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm hover:border-gray-300 transition">
                            <div class="px-6 pt-0">
                                <div class="flex items-center justify-between gap-4">
                                    <div class="flex-1 space-y-2">
                                        <h3 class="font-semibold text-gray-900">${reservation.motif}</h3>
                                        <div class="flex flex-wrap gap-4 text-sm text-gray-600">
                                            <div class="flex items-center gap-1">
                                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                                                </svg>
                                                ${reservation.dateReservation}
                                            </div>
                                            <div class="flex items-center gap-1">
                                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                                                </svg>
                                                ${reservation.heureDebut} - ${reservation.heureFin}
                                            </div>
                                            <div class="flex items-center gap-1">
                                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path>
                                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path>
                                                </svg>
                                                <c:choose>
                                                    <c:when test="${not empty reservation.salleNom}">
                                                        ${reservation.salleNom}
                                                    </c:when>
                                                    <c:otherwise>
                                                        Salle #${reservation.salleId}
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <c:if test="${not empty reservation.userNom}">
                                                <div class="flex items-center gap-1">
                                                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                                                    </svg>
                                                    ${reservation.userNom}
                                                </div>
                                            </c:if>
                                            <div class="flex items-center gap-1">
                                                <span class="text-xs">Priorité: ${reservation.priorite}</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="flex items-center gap-3">
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
                                            <c:when test="${reservation.statut.name() == 'TERMINEE'}">
                                                <span class="text-xs bg-gray-100 text-gray-800 px-2 py-1 rounded border border-gray-200">Terminée</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-xs bg-gray-100 text-gray-800 px-2 py-1 rounded border border-gray-200">${reservation.statut}</span>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.request.contextPath}/reservations/view?id=${reservation.id}" 
                                           class="text-blue-600 hover:text-blue-800 text-sm font-medium">
                                            Voir détails
                                        </a>
                                        <!-- Bouton d'annulation pour le propriétaire de la réservation -->
                                        <c:if test="${reservation.userId == userId && reservation.statut.name() != 'ANNULEE' && reservation.statut.name() != 'TERMINEE' && reservation.statut.name() != 'REFUSEE'}">
                                            <form method="POST" action="${pageContext.request.contextPath}/reservations/cancel" style="display: inline;">
                                                <input type="hidden" name="reservationId" value="${reservation.id}">
                                                <button type="submit" 
                                                        onclick="return confirm('Êtes-vous sûr de vouloir annuler cette réservation ?')"
                                                        class="text-red-600 hover:text-red-800 text-sm font-medium">
                                                    Annuler
                                                </button>
                                            </form>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>

