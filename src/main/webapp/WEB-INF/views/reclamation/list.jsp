<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Réclamations - E-Salle ENSAA"/>
</jsp:include>

<div class="container mx-auto px-4 py-8">
    <div class="flex justify-between items-center mb-6">
        <h2 class="text-3xl font-bold text-gray-800">
            ${isUserView ? 'Mes Réclamations' : 'Gestion des Réclamations'}
        </h2>
        <!-- Le bouton n'est pas affiché si l'utilisateur est un ADMIN -->
        <c:if test="${sessionScope.user.role != 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/reclamations/new" 
               class="bg-indigo-600 text-white px-6 py-3 rounded-lg hover:bg-indigo-700 transition">
                ➕ Nouvelle Réclamation
            </a>
        </c:if>
    </div>

    <!-- Messages -->
    <c:if test="${not empty sessionScope.success}">
        <div class="mb-4 p-4 bg-green-50 border-l-4 border-green-500 text-green-700 rounded">
            <p class="font-medium">✅ ${sessionScope.success}</p>
        </div>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="mb-4 p-4 bg-red-50 border-l-4 border-red-500 text-red-700 rounded">
            <p class="font-medium">❌ ${sessionScope.error}</p>
        </div>
    </c:if>
    <c:remove var="success" scope="session"/>
    <c:remove var="error" scope="session"/>

    <!-- Statistiques -->
    <c:if test="${not isUserView}">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
            <div class="bg-white rounded-lg shadow p-6">
                <p class="text-sm text-gray-500">En attente</p>
                <p class="text-2xl font-bold text-orange-600">${countEnAttente}</p>
            </div>
            <div class="bg-white rounded-lg shadow p-6">
                <p class="text-sm text-gray-500">Traitées</p>
                <p class="text-2xl font-bold text-green-600">${countTraitees}</p>
            </div>
            <div class="bg-white rounded-lg shadow p-6">
                <p class="text-sm text-gray-500">Urgentes</p>
                <p class="text-2xl font-bold text-red-600">${countUrgentes}</p>
            </div>
        </div>
    </c:if>

    <!-- Liste -->
    <div class="bg-white rounded-lg shadow overflow-hidden">
        <c:choose>
            <c:when test="${empty reclamations}">
                <div class="p-8 text-center">
                    <p class="text-gray-500">Aucune réclamation trouvée</p>
                </div>
            </c:when>
            <c:otherwise>
                <table class="w-full">
                    <thead class="bg-gray-50">
                        <tr>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Salle</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Urgence</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Statut</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y">
                        <c:forEach var="recla" items="${reclamations}">
                            <tr>
                                <td class="px-6 py-4">${recla.salleNom}</td>
                                <td class="px-6 py-4">
                                    <span class="px-2 py-1 text-xs rounded-full ${recla.urgenceCssClass}">
                                        ${recla.urgenceLibelle}
                                    </span>
                                </td>
                                <td class="px-6 py-4">
                                    <span class="px-2 py-1 text-xs rounded-full ${recla.statutCssClass}">
                                        ${recla.statutLibelle}
                                    </span>
                                </td>
                                <td class="px-6 py-4 text-sm">${recla.dateCreationFormatee}</td>
                                <td class="px-6 py-4">
                                    <a href="${pageContext.request.contextPath}/reclamations/view?id=${recla.id}" 
                                       class="text-indigo-600 hover:text-indigo-900">Voir</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>

