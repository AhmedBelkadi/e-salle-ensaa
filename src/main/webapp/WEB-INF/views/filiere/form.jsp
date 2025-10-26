<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${empty filiere ? 'Nouvelle' : 'Modifier'} Filière - E-Salle ENSAA"/>
</jsp:include>

    <div class="container mx-auto px-4 py-8 max-w-2xl">
        
        <!-- Titre -->
        <h2 class="text-3xl font-bold text-gray-800 mb-6">
            ${empty filiere ? '➕ Nouvelle Filière' : '✏️ Modifier Filière'}
        </h2>

        <!-- Message d'erreur -->
        <c:if test="${not empty error}">
            <div class="mb-4 p-4 bg-red-50 border-l-4 border-red-500 text-red-700 rounded">
                <p class="font-medium">❌ ${error}</p>
            </div>
        </c:if>

        <!-- Formulaire -->
        <div class="bg-white rounded-lg shadow-md p-6">
            <form method="post" action="${pageContext.request.contextPath}/filieres/save">
                
                <!-- ID caché pour modification -->
                <c:if test="${not empty filiere}">
                    <input type="hidden" name="id" value="${filiere.id}">
                </c:if>

                <!-- Nom -->
                <div class="mb-4">
                    <label for="nom" class="block text-sm font-medium text-gray-700 mb-2">
                        Nom de la filière <span class="text-red-500">*</span>
                    </label>
                    <input type="text" id="nom" name="nom" 
                           value="${not empty filiere ? filiere.nom : nom}"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500"
                           placeholder="Ex: Informatique, Génie Civil..."
                           required>
                    <p class="mt-1 text-sm text-gray-500">Le nom de la filière (sans le cycle ni l'année)</p>
                </div>

                <!-- Cycle -->
                <div class="mb-4">
                    <label for="cycle" class="block text-sm font-medium text-gray-700 mb-2">
                        Cycle <span class="text-red-500">*</span>
                    </label>
                    <select id="cycle" name="cycle" 
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500"
                            required>
                        <option value="">-- Sélectionner un cycle --</option>
                        <option value="PREPARATOIRE" 
                                ${(not empty filiere && filiere.cycle == 'PREPARATOIRE') || cycle == 'PREPARATOIRE' ? 'selected' : ''}>
                            📚 Préparatoire (2 filières créées automatiquement)
                        </option>
                        <option value="INGENIEUR" 
                                ${(not empty filiere && filiere.cycle == 'INGENIEUR') || cycle == 'INGENIEUR' ? 'selected' : ''}>
                            🏅 Ingénieur (3 filières DLA1, DLA2, DLA3 créées automatiquement)
                        </option>
                    </select>
                    <p class="mt-1 text-sm text-gray-500">
                        💡 Les filières pour toutes les années seront créées automatiquement
                    </p>
                </div>

                <!-- Effectif -->
                <div class="mb-4">
                    <label for="effectif" class="block text-sm font-medium text-gray-700 mb-2">
                        Effectif moyen par année <span class="text-red-500">*</span>
                    </label>
                    <input type="number" id="effectif" name="effectif" 
                           value="${not empty filiere ? filiere.effectif : effectif}"
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500"
                           placeholder="Ex: 50"
                           min="1"
                           required>
                    <p class="mt-1 text-sm text-gray-500">Effectif qui sera attribué à chaque année créée</p>
                </div>

                <!-- Description (optionnel) -->
                <div class="mb-4">
                    <label for="description" class="block text-sm font-medium text-gray-700 mb-2">
                        Description
                    </label>
                    <textarea id="description" name="description" rows="3"
                              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500"
                              placeholder="Description de la filière...">${not empty filiere ? filiere.description : description}</textarea>
                </div>

                <!-- Note sur la création automatique -->
                <div class="mb-6 p-4 bg-blue-50 border-l-4 border-blue-500 text-blue-700 rounded">
                    <p class="text-sm">
                        <strong>ℹ️ Information:</strong> En sélectionnant un cycle, toutes les filières correspondantes seront créées automatiquement :
                        <br>• <strong>Préparatoire:</strong> 2 filières (1ère et 2ème année)
                        <br>• <strong>Ingénieur:</strong> 3 filières (DLA1, DLA2, DLA3)
                        <br><br>Le coordinateur sera assigné lors de son inscription en sélectionnant sa filière.
                    </p>
                </div>

                <!-- Boutons d'action -->
                <div class="flex justify-between items-center">
                    <a href="${pageContext.request.contextPath}/filieres/list" 
                       class="px-6 py-2 bg-gray-300 text-gray-700 rounded-lg hover:bg-gray-400 transition">
                        ← Retour
                    </a>
                    <button type="submit" 
                            class="px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition shadow-lg">
                        ${empty filiere ? '➕ Créer' : '💾 Enregistrer'}
                    </button>
                </div>
            </form>
        </div>
    </div>


<jsp:include page="../common/footer.jsp"/>

