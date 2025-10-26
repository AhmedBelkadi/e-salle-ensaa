<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Réclamation #${reclamation.id} - E-Salle ENSAA"/>
</jsp:include>

<div class="container mx-auto px-4 py-8 max-w-4xl">
    <h2 class="text-3xl font-bold text-gray-800 mb-6">Réclamation #${reclamation.id}</h2>

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

    <!-- Détails -->
    <div class="bg-white rounded-lg shadow p-6 mb-6">
        <div class="grid grid-cols-2 gap-4">
            <div>
                <p class="text-sm text-gray-500">Salle</p>
                <p class="text-lg font-semibold">${reclamation.salleNom}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500">Utilisateur</p>
                <p class="text-lg font-semibold">${reclamation.userNom}</p>
            </div>
            <div>
                <p class="text-sm text-gray-500">Urgence</p>
                <span class="px-3 py-1 text-sm rounded-full ${reclamation.urgenceCssClass}">
                    ${reclamation.urgenceLibelle}
                </span>
            </div>
            <div>
                <p class="text-sm text-gray-500">Statut</p>
                <span class="px-3 py-1 text-sm rounded-full ${reclamation.statutCssClass}">
                    ${reclamation.statutLibelle}
                </span>
            </div>
        </div>

        <div class="mt-4 pt-4 border-t">
            <p class="text-sm text-gray-500">Description</p>
            <p class="mt-2">${reclamation.description}</p>
        </div>

        <div class="mt-4 pt-4 border-t">
            <p class="text-sm text-gray-500">Date de création</p>
            <p class="font-medium">${reclamation.dateCreationFormatee}</p>
        </div>

        <c:if test="${reclamation.traitee}">
            <div class="mt-4 pt-4 border-t">
                <p class="text-sm text-gray-500">Traité par</p>
                <p class="font-semibold">${reclamation.traiteParNom}</p>
                <p class="text-sm text-gray-500 mt-2">Date de traitement</p>
                <p class="font-medium">${reclamation.dateTraitementFormatee}</p>
                <p class="text-sm text-gray-500 mt-2">Commentaire</p>
                <p>${reclamation.commentaireTraitement}</p>
            </div>
        </c:if>
    </div>

    <!-- Formulaire traitement (Admin uniquement) -->
    <c:if test="${sessionScope.user.role.name() == 'ADMIN' && reclamation.enAttente}">
        <div class="bg-white rounded-lg shadow p-6">
            <h3 class="text-lg font-semibold mb-4">Traiter la réclamation</h3>
            <form method="post" action="${pageContext.request.contextPath}/reclamations/traiter">
                <input type="hidden" name="reclamationId" value="${reclamation.id}">
                <textarea name="commentaire" rows="3" 
                          class="w-full px-4 py-2 border rounded-lg mb-4" 
                          placeholder="Commentaire de traitement..." required></textarea>
                <button type="submit" 
                        class="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700">
                    Marquer comme traitée
                </button>
            </form>
        </div>
    </c:if>

    <div class="mt-4">
        <a href="${pageContext.request.contextPath}/reclamations/list" 
           class="text-indigo-600 hover:text-indigo-800">← Retour à la liste</a>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>

