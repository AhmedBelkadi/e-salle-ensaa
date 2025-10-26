<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Nouvelle Réclamation - E-Salle ENSAA"/>
</jsp:include>

<div class="container mx-auto px-4 py-8 max-w-2xl">
    <h2 class="text-3xl font-bold text-gray-800 mb-6">Nouvelle Réclamation</h2>

    <c:if test="${not empty error}">
        <div class="mb-4 p-4 bg-red-50 border-l-4 border-red-500 text-red-700 rounded">
            <p class="font-medium">❌ ${error}</p>
        </div>
    </c:if>

    <div class="bg-white rounded-lg shadow p-6">
        <form method="post" action="${pageContext.request.contextPath}/reclamations/create">
            
            <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">
                    Salle concernée <span class="text-red-500">*</span>
                </label>
                <select name="salleId" class="w-full px-4 py-2 border rounded-lg" required>
                    <option value="">-- Sélectionner une salle --</option>
                    <c:forEach var="salle" items="${salles}">
                        <option value="${salle.id}" ${salleId == salle.id ? 'selected' : ''}>
                            ${salle.nom} (${salle.type} - Capacité: ${salle.capacite})
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">
                    Urgence <span class="text-red-500">*</span>
                </label>
                <select name="urgence" class="w-full px-4 py-2 border rounded-lg" required>
                    <option value="">-- Sélectionner --</option>
                    <option value="FAIBLE" ${urgence == 'FAIBLE' ? 'selected' : ''}>Faible</option>
                    <option value="MOYENNE" ${urgence == 'MOYENNE' ? 'selected' : ''}>Moyenne</option>
                    <option value="ELEVEE" ${urgence == 'ELEVEE' ? 'selected' : ''}>Élevée</option>
                </select>
            </div>

            <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">
                    Description <span class="text-red-500">*</span>
                </label>
                <textarea name="description" rows="5" 
                          class="w-full px-4 py-2 border rounded-lg" 
                          placeholder="Décrivez le problème (min 10 caractères)" 
                          required>${description}</textarea>
            </div>

            <div class="flex justify-between">
                <a href="${pageContext.request.contextPath}/reclamations/mes-reclamations" 
                   class="px-6 py-2 bg-gray-300 text-gray-700 rounded-lg hover:bg-gray-400">Retour</a>
                <button type="submit" 
                        class="px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700">
                    Créer
                </button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>

