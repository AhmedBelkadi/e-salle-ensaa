<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty salle ? 'Nouvelle' : 'Modifier'} Salle - E-Salle ENSAA</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    
    <!-- Header -->
    <nav class="bg-indigo-600 text-white shadow-lg">
        <div class="container mx-auto px-4 py-4">
            <div class="flex justify-between items-center">
                <h1 class="text-2xl font-bold">E-Salle ENSAA</h1>
                <div class="space-x-4">
                    <a href="${pageContext.request.contextPath}/salles/list" class="hover:text-indigo-200">← Retour à la liste</a>
                </div>
            </div>
        </div>
    </nav>

    <div class="container mx-auto px-4 py-8 max-w-2xl">
        
        <!-- Titre -->
        <h2 class="text-3xl font-bold text-gray-800 mb-6">
            ${empty salle ? '➕ Nouvelle Salle' : '✏️ Modifier la Salle'}
        </h2>

        <!-- Messages d'erreur -->
        <c:if test="${not empty error}">
            <div class="mb-4 p-4 bg-red-50 border-l-4 border-red-500 text-red-700 rounded">
                <p class="font-medium">❌ Erreur</p>
                <p class="text-sm">${error}</p>
            </div>
        </c:if>

        <!-- Formulaire -->
        <div class="bg-white rounded-lg shadow-md p-8">
            <form method="post" 
                  action="${pageContext.request.contextPath}/salles/${empty salle ? 'create' : 'update'}" 
                  class="space-y-6">
                
                <!-- ID caché pour l'édition -->
                <c:if test="${not empty salle}">
                    <input type="hidden" name="id" value="${salle.id}">
                </c:if>

                <!-- Nom -->
                <div>
                    <label for="nom" class="block text-sm font-medium text-gray-700 mb-2">
                        Nom de la salle <span class="text-red-500">*</span>
                    </label>
                    <input 
                        type="text" 
                        id="nom" 
                        name="nom" 
                        required
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                        placeholder="Ex: Amphithéâtre A, Salle TP1, TD-B3"
                        value="${not empty salle ? salle.nom : nom}"
                    >
                    <p class="mt-1 text-xs text-gray-500">Le nom doit être unique</p>
                </div>

                <!-- Type -->
                <div>
                    <label for="type" class="block text-sm font-medium text-gray-700 mb-2">
                        Type de salle <span class="text-red-500">*</span>
                    </label>
                    <select 
                        id="type" 
                        name="type" 
                        required
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                    >
                        <option value="">-- Sélectionnez un type --</option>
                        <option value="COURS" ${(not empty salle && salle.type == 'COURS') || type == 'COURS' ? 'selected' : ''}>
                            📚 Cours magistral
                        </option>
                        <option value="TP" ${(not empty salle && salle.type == 'TP') || type == 'TP' ? 'selected' : ''}>
                            🔬 Travaux pratiques (TP)
                        </option>
                        <option value="TD" ${(not empty salle && salle.type == 'TD') || type == 'TD' ? 'selected' : ''}>
                            ✍️ Travaux dirigés (TD)
                        </option>
                    </select>
                </div>

                <!-- Capacité -->
                <div>
                    <label for="capacite" class="block text-sm font-medium text-gray-700 mb-2">
                        Capacité (nombre de places) <span class="text-red-500">*</span>
                    </label>
                    <input 
                        type="number" 
                        id="capacite" 
                        name="capacite" 
                        required
                        min="1"
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                        placeholder="Ex: 50"
                        value="${not empty salle ? salle.capacite : capacite}"
                    >
                    <p class="mt-1 text-xs text-gray-500">La capacité doit être supérieure à 0</p>
                </div>

                <!-- Équipements -->
                <div>
                    <label for="equipements" class="block text-sm font-medium text-gray-700 mb-2">
                        Équipements (optionnel)
                    </label>
                    <textarea 
                        id="equipements" 
                        name="equipements" 
                        rows="4"
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                        placeholder="Ex: Projecteur, Tableau blanc, 20 ordinateurs, Climatisation..."
                    >${not empty salle ? salle.equipements : equipements}</textarea>
                    <p class="mt-1 text-xs text-gray-500">Listez les équipements disponibles dans cette salle</p>
                </div>

                <!-- Disponibilité (seulement en édition) -->
                <c:if test="${not empty salle}">
                    <div>
                        <label class="flex items-center space-x-3">
                            <input 
                                type="checkbox" 
                                name="disponible" 
                                value="true"
                                ${salle.disponible ? 'checked' : ''}
                                class="w-5 h-5 text-indigo-600 border-gray-300 rounded focus:ring-indigo-500"
                            >
                            <span class="text-sm font-medium text-gray-700">
                                Salle disponible pour réservation
                            </span>
                        </label>
                    </div>
                </c:if>

                <!-- Boutons -->
                <div class="flex gap-4 pt-4">
                    <button 
                        type="submit"
                        class="flex-1 bg-indigo-600 text-white py-3 rounded-lg font-semibold hover:bg-indigo-700 transition shadow-lg"
                    >
                        ${empty salle ? '➕ Créer la salle' : '💾 Enregistrer les modifications'}
                    </button>
                    <a 
                        href="${pageContext.request.contextPath}/salles/list"
                        class="flex-1 bg-gray-300 text-gray-700 py-3 rounded-lg font-semibold hover:bg-gray-400 transition text-center"
                    >
                        ❌ Annuler
                    </a>
                </div>
            </form>
        </div>

        <!-- Informations supplémentaires (en édition) -->
        <c:if test="${not empty salle}">
            <div class="mt-6 bg-blue-50 border border-blue-200 rounded-lg p-4">
                <h3 class="text-sm font-semibold text-blue-800 mb-2">ℹ️ Informations</h3>
                <div class="text-xs text-blue-700 space-y-1">
                    <p><strong>ID:</strong> ${salle.id}</p>
                    <p><strong>Créée le:</strong> ${salle.dateCreation}</p>
                    <c:if test="${not empty salle.dateModification}">
                        <p><strong>Dernière modification:</strong> ${salle.dateModification}</p>
                    </c:if>
                </div>
            </div>
        </c:if>
    </div>

</body>
</html>

