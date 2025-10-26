<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Gestion des Filières - E-Salle ENSAA"/>
</jsp:include>

    <div class="container mx-auto px-4 py-8">
        
        <!-- Titre et bouton ajouter -->
        <div class="flex justify-between items-center mb-6">
            <h2 class="text-3xl font-bold text-gray-800">Gestion des Filières</h2>
            <a href="${pageContext.request.contextPath}/filieres/new" 
               class="bg-indigo-600 text-white px-6 py-3 rounded-lg hover:bg-indigo-700 transition shadow-lg">
                ➕ Nouvelle Filière
            </a>
        </div>

        <!-- Messages de succès et d'erreur -->
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
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
            <div class="bg-white rounded-lg shadow p-6">
                <div class="flex items-center">
                    <div class="p-3 bg-indigo-100 rounded-full">
                        <span class="text-2xl">🎓</span>
                    </div>
                    <div class="ml-4">
                        <p class="text-sm text-gray-500">Total Filières</p>
                        <p class="text-2xl font-bold text-gray-800">${filieres.size()}</p>
                    </div>
                </div>
            </div>
            <div class="bg-white rounded-lg shadow p-6">
                <div class="flex items-center">
                    <div class="p-3 bg-blue-100 rounded-full">
                        <span class="text-2xl">📚</span>
                    </div>
                    <div class="ml-4">
                        <p class="text-sm text-gray-500">Préparatoire</p>
                        <p class="text-2xl font-bold text-gray-800">${countPrepa}</p>
                    </div>
                </div>
            </div>
            <div class="bg-white rounded-lg shadow p-6">
                <div class="flex items-center">
                    <div class="p-3 bg-purple-100 rounded-full">
                        <span class="text-2xl">🏅</span>
                    </div>
                    <div class="ml-4">
                        <p class="text-sm text-gray-500">Ingénieur</p>
                        <p class="text-2xl font-bold text-gray-800">${countIngenieur}</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Filtres et recherche -->
        <div class="bg-white rounded-lg shadow-md p-6 mb-6">
            <h3 class="text-lg font-semibold text-gray-800 mb-4">🔍 Filtres et Recherche</h3>
            
            <form method="get" action="${pageContext.request.contextPath}/filieres/list" class="grid grid-cols-1 md:grid-cols-3 gap-4">
                
                <!-- Cycle -->
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Cycle</label>
                    <select name="cycle" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
                        <option value="">Tous les cycles</option>
                        <option value="PREPARATOIRE" ${cycleFilter == 'PREPARATOIRE' ? 'selected' : ''}>Préparatoire</option>
                        <option value="INGENIEUR" ${cycleFilter == 'INGENIEUR' ? 'selected' : ''}>Ingénieur</option>
                    </select>
                </div>

                <!-- Année -->
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Année</label>
                    <select name="annee" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
                        <option value="">Toutes les années</option>
                        <option value="1" ${anneeFilter == '1' ? 'selected' : ''}>1ère année</option>
                        <option value="2" ${anneeFilter == '2' ? 'selected' : ''}>2ème année</option>
                        <option value="3" ${anneeFilter == '3' ? 'selected' : ''}>3ème année (Ingénieur)</option>
                    </select>
                </div>

                <!-- Bouton filtrer -->
                <div class="flex items-end">
                    <button type="submit" class="w-full bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition">
                        Filtrer
                    </button>
                </div>
            </form>

            <!-- Recherche par nom -->
            <form method="get" action="${pageContext.request.contextPath}/filieres/search" class="mt-4">
                <div class="flex gap-2">
                    <input type="text" name="keyword" value="${keyword}" 
                           class="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500"
                           placeholder="Rechercher par nom...">
                    <button type="submit" class="bg-gray-600 text-white px-6 py-2 rounded-lg hover:bg-gray-700 transition">
                        Rechercher
                    </button>
                    <a href="${pageContext.request.contextPath}/filieres/list" 
                       class="bg-gray-300 text-gray-700 px-6 py-2 rounded-lg hover:bg-gray-400 transition">
                        Réinitialiser
                    </a>
                </div>
            </form>
        </div>

        <!-- Liste des filières -->
        <div class="bg-white rounded-lg shadow-md overflow-hidden">
            <c:choose>
                <c:when test="${empty filieres}">
                    <div class="p-8 text-center text-gray-500">
                        <p class="text-lg">Aucune filière trouvée</p>
                        <a href="${pageContext.request.contextPath}/filieres/new" 
                           class="text-indigo-600 hover:text-indigo-800 font-semibold mt-2 inline-block">
                            Créer la première filière
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="w-full">
                        <thead class="bg-gray-50 border-b border-gray-200">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nom</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Cycle</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Année</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Effectif</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Coordinateur</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach var="filiere" items="${filieres}">
                                <tr class="hover:bg-gray-50">
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm font-medium text-gray-900">${filiere.nom}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full 
                                            ${filiere.cycle == 'PREPARATOIRE' ? 'bg-blue-100 text-blue-800' : 'bg-purple-100 text-purple-800'}">
                                            ${filiere.cycle == 'PREPARATOIRE' ? '📚 Préparatoire' : '🏅 Ingénieur'}
                                        </span>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                        ${filiere.anneeLibelle}
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                        ${filiere.effectif} étudiants
                                    </td>
                                    <td class="px-6 py-4 text-sm text-gray-500">
                                        <c:choose>
                                            <c:when test="${filiere.hasCoordinateur()}">
                                                <span class="text-green-600 font-medium">${filiere.coordinateurNom}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-gray-400 italic">Non assigné</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                        <a href="${pageContext.request.contextPath}/filieres/view?id=${filiere.id}" 
                                           class="text-indigo-600 hover:text-indigo-900">👁️ Voir</a>
                                        <a href="${pageContext.request.contextPath}/filieres/edit?id=${filiere.id}" 
                                           class="text-blue-600 hover:text-blue-900">✏️ Modifier</a>
                                        <a href="${pageContext.request.contextPath}/filieres/delete?id=${filiere.id}" 
                                           onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette filière ?')"
                                           class="text-red-600 hover:text-red-900">🗑️ Supprimer</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    
                    <!-- Statistiques -->
                    <div class="bg-gray-50 px-6 py-4 border-t border-gray-200">
                        <p class="text-sm text-gray-600">
                            <strong>${filieres.size()}</strong> filière(s) trouvée(s)
                        </p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

<jsp:include page="../common/footer.jsp"/>

