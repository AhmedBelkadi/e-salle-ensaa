<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Détails Réservation - E-Salle ENSAA"/>
</jsp:include>

<div class="flex-1 p-6">
    <div class="max-w-4xl mx-auto space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Détails Réservation</h1>
                <p class="text-gray-600 mt-2">Informations complètes de la réservation</p>
            </div>
            <a href="${pageContext.request.contextPath}/reservations/list"
               class="px-4 py-2 rounded-md border border-gray-300 text-gray-700 hover:bg-gray-50">
                Retour
            </a>
        </div>

        <!-- Details Card -->
        <div class="bg-white rounded-xl border border-gray-200 p-6 shadow-sm space-y-6">
            <!-- Informations générales -->
            <div>
                <h2 class="text-lg font-semibold text-gray-900 mb-4">Informations</h2>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <p class="text-sm text-gray-600">Motif</p>
                        <p class="text-base font-medium text-gray-900">${reservation.motif}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Statut</p>
                        <c:choose>
                            <c:when test="${reservation.statut.name() == 'APPROUVEE'}">
                                <span class="inline-block px-3 py-1 text-sm bg-green-100 text-green-800 rounded border border-green-200">Approuvée</span>
                            </c:when>
                            <c:when test="${reservation.statut.name() == 'EN_ATTENTE'}">
                                <span class="inline-block px-3 py-1 text-sm bg-yellow-100 text-yellow-800 rounded border border-yellow-200">En Attente</span>
                            </c:when>
                            <c:when test="${reservation.statut.name() == 'REFUSEE'}">
                                <span class="inline-block px-3 py-1 text-sm bg-red-100 text-red-800 rounded border border-red-200">Refusée</span>
                            </c:when>
                            <c:when test="${reservation.statut.name() == 'ANNULEE'}">
                                <span class="inline-block px-3 py-1 text-sm bg-gray-100 text-gray-800 rounded border border-gray-200">Annulée</span>
                            </c:when>
                            <c:when test="${reservation.statut.name() == 'TERMINEE'}">
                                <span class="inline-block px-3 py-1 text-sm bg-gray-100 text-gray-800 rounded border border-gray-200">Terminée</span>
                            </c:when>
                            <c:otherwise>
                                <span class="inline-block px-3 py-1 text-sm bg-gray-100 text-gray-800 rounded">${reservation.statut}</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Date</p>
                        <p class="text-base font-medium text-gray-900">${reservation.dateReservation}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Horaire</p>
                        <p class="text-base font-medium text-gray-900">${reservation.heureDebut} - ${reservation.heureFin}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Salle</p>
                        <p class="text-base font-medium text-gray-900">${reservation.salleNom}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Réservateur</p>
                        <p class="text-base font-medium text-gray-900">${reservation.userNom}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Priorité</p>
                        <p class="text-base font-medium text-gray-900">${reservation.priorite}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Type</p>
                        <p class="text-base font-medium text-gray-900">${reservation.typeReservateur}</p>
                    </div>
                </div>
            </div>

            <!-- Commentaire -->
            <c:if test="${not empty reservation.commentaire}">
                <div>
                    <h2 class="text-lg font-semibold text-gray-900 mb-2">Commentaire</h2>
                    <p class="text-gray-700">${reservation.commentaire}</p>
                </div>
            </c:if>

            <!-- Actions Admin -->
            <c:if test="${isAdmin && reservation.statut.name() == 'EN_ATTENTE'}">
                <div class="border-t border-gray-200 pt-6">
                    <h2 class="text-lg font-semibold text-gray-900 mb-4">Actions Administrateur</h2>
                    <div class="flex gap-4">
                        <form method="POST" action="${pageContext.request.contextPath}/reservations/approve" class="flex-1">
                            <input type="hidden" name="reservationId" value="${reservation.id}">
                            <textarea name="commentaire" placeholder="Commentaire (optionnel)" rows="2"
                                      class="w-full rounded-md border border-gray-300 px-3 py-2 mb-2"></textarea>
                            <button type="submit"
                                    class="w-full px-4 py-2 rounded-md bg-green-600 text-white hover:bg-green-700">
                                Approuver
                            </button>
                        </form>
                        <form method="POST" action="${pageContext.request.contextPath}/reservations/refuse" class="flex-1">
                            <input type="hidden" name="reservationId" value="${reservation.id}">
                            <textarea name="commentaire" placeholder="Raison du refus" rows="2"
                                      class="w-full rounded-md border border-gray-300 px-3 py-2 mb-2"></textarea>
                            <button type="submit"
                                    class="w-full px-4 py-2 rounded-md bg-red-600 text-white hover:bg-red-700">
                                Refuser
                            </button>
                        </form>
                    </div>
                </div>
            </c:if>

            <!-- Actions Utilisateur : Annulation -->
            <c:if test="${reservation.userId == userId && reservation.statut.name() != 'ANNULEE' && reservation.statut.name() != 'TERMINEE' && reservation.statut.name() != 'REFUSEE'}">
                <div class="border-t border-gray-200 pt-6">
                    <h2 class="text-lg font-semibold text-gray-900 mb-4">Actions</h2>
                    <form method="POST" action="${pageContext.request.contextPath}/reservations/cancel">
                        <input type="hidden" name="reservationId" value="${reservation.id}">
                        <button type="submit" 
                                onclick="return confirm('Êtes-vous sûr de vouloir annuler cette réservation ? Cette action est irréversible.')"
                                class="w-full px-4 py-2 rounded-md bg-red-600 text-white hover:bg-red-700">
                            <i class="fas fa-times-circle mr-2"></i>
                            Annuler cette réservation
                        </button>
                    </form>
                </div>
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>

