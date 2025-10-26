<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${salle.nom} - E-Salle ENSAA"/>
</jsp:include>

    <div class="container mx-auto px-4 py-8 max-w-4xl">
        
        <!-- Titre et actions -->
        <div class="flex justify-between items-center mb-6">
            <h2 class="text-3xl font-bold text-gray-800">${salle.nom}</h2>
            <!-- Boutons visibles uniquement pour ADMIN -->
            <c:if test="${sessionScope.user.role.name() == 'ADMIN'}">
                <div class="space-x-2">
                    <a href="${pageContext.request.contextPath}/salles/edit?id=${salle.id}" 
                       class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition">
                        ✏️ Modifier
                    </a>
                    <a href="${pageContext.request.contextPath}/salles/delete?id=${salle.id}" 
                       onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette salle ?')"
                       class="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition">
                        🗑️ Supprimer
                    </a>
                </div>
            </c:if>
        </div>

        <!-- Informations principales -->
        <div class="bg-white rounded-lg shadow-md p-8 mb-6">
            <h3 class="text-xl font-semibold text-gray-800 mb-6 border-b pb-2">📋 Informations Générales</h3>
            
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                
                <!-- Nom -->
                <div>
                    <label class="block text-sm font-medium text-gray-500 mb-1">Nom</label>
                    <p class="text-lg font-semibold text-gray-900">${salle.nom}</p>
                </div>

                <!-- Type -->
                <div>
                    <label class="block text-sm font-medium text-gray-500 mb-1">Type</label>
                    <span class="inline-flex px-3 py-1 text-sm font-semibold rounded-full 
                        ${salle.type == 'COURS' ? 'bg-blue-100 text-blue-800' : 
                          salle.type == 'TP' ? 'bg-purple-100 text-purple-800' : 
                          'bg-green-100 text-green-800'}">
                        ${salle.type == 'COURS' ? '📚 Cours magistral' : 
                          salle.type == 'TP' ? '🔬 Travaux pratiques' : 
                          '✍️ Travaux dirigés'}
                    </span>
                </div>

                <!-- Capacité -->
                <div>
                    <label class="block text-sm font-medium text-gray-500 mb-1">Capacité</label>
                    <p class="text-lg font-semibold text-gray-900">
                        <span class="text-2xl">${salle.capacite}</span> places
                    </p>
                </div>

                <!-- Disponibilité -->
                <div>
                    <label class="block text-sm font-medium text-gray-500 mb-1">Disponibilité</label>
                    <c:choose>
                        <c:when test="${salle.disponible}">
                            <span class="inline-flex px-3 py-1 text-sm font-semibold rounded-full bg-green-100 text-green-800">
                                ✓ Disponible
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="inline-flex px-3 py-1 text-sm font-semibold rounded-full bg-red-100 text-red-800">
                                ✗ Non disponible
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Équipements -->
        <div class="bg-white rounded-lg shadow-md p-8 mb-6">
            <h3 class="text-xl font-semibold text-gray-800 mb-4 border-b pb-2">🛠️ Équipements</h3>
            <c:choose>
                <c:when test="${not empty salle.equipements}">
                    <p class="text-gray-700 whitespace-pre-line">${salle.equipements}</p>
                </c:when>
                <c:otherwise>
                    <p class="text-gray-400 italic">Aucun équipement renseigné</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Métadonnées -->
        <div class="bg-gray-50 rounded-lg shadow-md p-8">
            <h3 class="text-xl font-semibold text-gray-800 mb-4 border-b pb-2">ℹ️ Métadonnées</h3>
            
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                <div>
                    <label class="block text-gray-500 mb-1">ID</label>
                    <p class="text-gray-900 font-mono">${salle.id}</p>
                </div>

                <div>
                    <label class="block text-gray-500 mb-1">Date de création</label>
                    <p class="text-gray-900">${salle.dateCreation}</p>
                </div>

                <c:if test="${not empty salle.dateModification}">
                    <div>
                        <label class="block text-gray-500 mb-1">Dernière modification</label>
                        <p class="text-gray-900">${salle.dateModification}</p>
                    </div>
                </c:if>
            </div>
        </div>

        <!-- Actions rapides -->
        <div class="mt-6 bg-blue-50 border border-blue-200 rounded-lg p-6">
            <h3 class="text-lg font-semibold text-blue-900 mb-4">⚡ Actions Rapides</h3>
            <div class="flex flex-wrap gap-3">
                <a href="${pageContext.request.contextPath}/salles/edit?id=${salle.id}" 
                   class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition">
                    ✏️ Modifier cette salle
                </a>
                
                <c:choose>
                    <c:when test="${salle.disponible}">
                        <form method="post" action="${pageContext.request.contextPath}/salles/toggle-disponibilite" class="inline">
                            <input type="hidden" name="id" value="${salle.id}">
                            <input type="hidden" name="disponible" value="false">
                            <button type="submit" 
                                    class="bg-orange-600 text-white px-4 py-2 rounded-lg hover:bg-orange-700 transition">
                                🚫 Marquer comme non disponible
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <form method="post" action="${pageContext.request.contextPath}/salles/toggle-disponibilite" class="inline">
                            <input type="hidden" name="id" value="${salle.id}">
                            <input type="hidden" name="disponible" value="true">
                            <button type="submit" 
                                    class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition">
                                ✓ Marquer comme disponible
                            </button>
                        </form>
                    </c:otherwise>
                </c:choose>

                <a href="${pageContext.request.contextPath}/salles/list" 
                   class="bg-gray-300 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-400 transition">
                    ← Retour à la liste
                </a>
            </div>
        </div>
    </div>

<jsp:include page="../common/footer.jsp"/>

