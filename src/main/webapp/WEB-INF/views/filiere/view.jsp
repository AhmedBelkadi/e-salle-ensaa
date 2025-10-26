<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${filiere.nom} - E-Salle ENSAA"/>
</jsp:include>

    <div class="container mx-auto px-4 py-8 max-w-4xl">
        
        <!-- Titre et actions -->
        <div class="flex justify-between items-center mb-6">
            <h2 class="text-3xl font-bold text-gray-800">${filiere.nomComplet}</h2>
            <div class="flex gap-2">
                <a href="${pageContext.request.contextPath}/filieres/edit?id=${filiere.id}" 
                   class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition">
                    ✏️ Modifier
                </a>
                <a href="${pageContext.request.contextPath}/filieres/delete?id=${filiere.id}" 
                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette filière ?')"
                   class="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition">
                    🗑️ Supprimer
                </a>
            </div>
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

        <!-- Informations principales -->
        <div class="bg-white rounded-lg shadow-md p-6 mb-6">
            <h3 class="text-xl font-semibold text-gray-800 mb-4">📋 Informations</h3>
            
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <!-- Nom -->
                <div>
                    <label class="block text-sm font-medium text-gray-500 mb-1">Nom</label>
                    <p class="text-lg text-gray-900 font-semibold">${filiere.nom}</p>
                </div>

                <!-- Cycle -->
                <div>
                    <label class="block text-sm font-medium text-gray-500 mb-1">Cycle</label>
                    <span class="inline-flex px-3 py-1 text-sm font-semibold rounded-full 
                        ${filiere.cycle == 'PREPARATOIRE' ? 'bg-blue-100 text-blue-800' : 'bg-purple-100 text-purple-800'}">
                        ${filiere.cycle == 'PREPARATOIRE' ? '📚 Préparatoire' : '🏅 Ingénieur'}
                    </span>
                </div>

                <!-- Année -->
                <div>
                    <label class="block text-sm font-medium text-gray-500 mb-1">Année</label>
                    <p class="text-lg text-gray-900 font-semibold">${filiere.anneeLibelle}</p>
                </div>

                <!-- Effectif -->
                <div>
                    <label class="block text-sm font-medium text-gray-500 mb-1">Effectif</label>
                    <p class="text-lg text-gray-900 font-semibold">
                        <span class="text-2xl">${filiere.effectif}</span> étudiants
                    </p>
                </div>
            </div>

            <!-- Description -->
            <c:if test="${not empty filiere.description}">
                <div class="mt-6 pt-6 border-t border-gray-200">
                    <label class="block text-sm font-medium text-gray-500 mb-2">Description</label>
                    <p class="text-gray-700">${filiere.description}</p>
                </div>
            </c:if>
        </div>

        <!-- Coordinateur - Affiché uniquement si un coordinateur est assigné -->
        <c:if test="${filiere.hasCoordinateur()}">
            <div class="bg-white rounded-lg shadow-md p-6 mb-6">
                <h3 class="text-xl font-semibold text-gray-800 mb-4">👤 Coordinateur</h3>
                
                <div class="flex items-center">
                    <div class="p-3 bg-indigo-100 rounded-full mr-4">
                        <span class="text-2xl">👨‍🏫</span>
                    </div>
                    <div>
                        <p class="text-lg text-gray-900 font-semibold">${filiere.coordinateurNom}</p>
                        <p class="text-sm text-gray-500">Coordinateur de la filière</p>
                    </div>
                </div>
                
                <div class="mt-4 p-3 bg-blue-50 rounded-lg">
                    <p class="text-sm text-blue-700">
                        ℹ️ Le coordinateur a été assigné lors de son inscription
                    </p>
                </div>
            </div>
        </c:if>

        <!-- Métadonnées -->
        <div class="bg-white rounded-lg shadow-md p-6 mb-6">
            <h3 class="text-xl font-semibold text-gray-800 mb-4">🕒 Métadonnées</h3>
            
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                    <label class="block text-sm font-medium text-gray-500 mb-1">Date de création</label>
                    <p class="text-gray-700">${filiere.createdAt}</p>
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-500 mb-1">Dernière modification</label>
                    <p class="text-gray-700">${filiere.updatedAt}</p>
                </div>
            </div>
        </div>

        <!-- Bouton retour -->
        <div class="flex justify-between items-center">
            <a href="${pageContext.request.contextPath}/filieres/list" 
               class="px-6 py-2 bg-gray-300 text-gray-700 rounded-lg hover:bg-gray-400 transition">
                ← Retour à la liste
            </a>
        </div>
    </div>

<jsp:include page="../common/footer.jsp"/>

