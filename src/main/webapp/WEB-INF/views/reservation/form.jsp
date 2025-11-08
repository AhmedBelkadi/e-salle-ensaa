<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Nouvelle Réservation - E-Salle ENSAA"/>
</jsp:include>

<div class="flex-1 p-6">
    <div class="max-w-2xl mx-auto">
        <div class="space-y-6">
            <!-- Header -->
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Nouvelle Réservation</h1>
                <p class="text-gray-600 mt-2">Réserver une salle pour votre activité</p>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="bg-red-50 border border-red-200 rounded-lg p-4">
                    <p class="text-red-800">${error}</p>
                </div>
            </c:if>

            <!-- Form -->
            <form method="POST" action="${pageContext.request.contextPath}/reservations/save" 
                  class="bg-white rounded-xl border border-gray-200 p-6 shadow-sm space-y-6">
                
                <!-- Salle -->
                <div>
                    <label for="salleId" class="block text-sm font-medium text-gray-700 mb-2">
                        Salle <span class="text-red-500">*</span>
                    </label>
                    <select name="salleId" id="salleId" required
                            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">Sélectionner une salle</option>
                        <c:forEach var="salle" items="${salles}">
                            <option value="${salle.id}">${salle.nom} (${salle.type}) - ${salle.capacite} places</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Date -->
                <div>
                    <label for="dateReservation" class="block text-sm font-medium text-gray-700 mb-2">
                        Date <span class="text-red-500">*</span>
                    </label>
                    <input type="date" name="dateReservation" id="dateReservation" required
                           min="<%= java.time.LocalDate.now() %>"
                           class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                </div>

                <!-- Heures -->
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="heureDebut" class="block text-sm font-medium text-gray-700 mb-2">
                            Heure de début <span class="text-red-500">*</span>
                        </label>
                        <input type="time" name="heureDebut" id="heureDebut" required
                               class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                    <div>
                        <label for="heureFin" class="block text-sm font-medium text-gray-700 mb-2">
                            Heure de fin <span class="text-red-500">*</span>
                        </label>
                        <input type="time" name="heureFin" id="heureFin" required
                               class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                </div>

                <!-- Motif -->
                <div>
                    <label for="motif" class="block text-sm font-medium text-gray-700 mb-2">
                        Motif <span class="text-red-500">*</span>
                    </label>
                    <textarea name="motif" id="motif" rows="4" required
                              placeholder="Décrivez le motif de votre réservation..."
                              class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"></textarea>
                </div>

                <!-- Libération exceptionnelle (pour clubs réservant TP) -->
                <c:if test="${sessionScope.user.role.name() == 'MEMBRE_CLUB'}">
                    <div class="flex items-center">
                        <input type="checkbox" name="estLiberationExceptionnelle" id="estLiberationExceptionnelle" value="true"
                               class="rounded border-gray-300 text-blue-600 focus:ring-blue-500">
                        <label for="estLiberationExceptionnelle" class="ml-2 text-sm text-gray-700">
                            Libération exceptionnelle (pour réservation de salle TP)
                        </label>
                    </div>
                </c:if>

                <!-- Buttons -->
                <div class="flex gap-4 justify-end">
                    <a href="${pageContext.request.contextPath}/reservations/list"
                       class="px-4 py-2 rounded-md border border-gray-300 text-gray-700 hover:bg-gray-50">
                        Annuler
                    </a>
                    <button type="submit"
                            class="px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700">
                        Créer la réservation
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>

