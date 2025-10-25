<c:set var="title" value="Gestion des Salles - E-Salle ENSAA" scope="request"/>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Gestion des Salles - E-Salle ENSAA"/>
</jsp:include>

    <div class="container mx-auto px-4 py-8">
        
        <!-- Titre et bouton ajouter -->
        <div class="flex justify-between items-center mb-6">
            <h2 class="text-3xl font-bold text-gray-800">Gestion des Salles</h2>
            <a href="${pageContext.request.contextPath}/salles/new" 
               class="bg-indigo-600 text-white px-6 py-3 rounded-lg hover:bg-indigo-700 transition shadow-lg">
                ➕ Nouvelle Salle
            </a>
        </div>

        <!-- Messages -->
        <c:if test="${not empty param.success}">
            <div class="mb-4 p-4 bg-green-50 border-l-4 border-green-500 text-green-700 rounded">
                <p class="font-medium">✅ ${param.success}</p>
            </div>
        </c:if>

        <c:if test="${not empty param.error}">
            <div class="mb-4 p-4 bg-red-50 border-l-4 border-red-500 text-red-700 rounded">
                <p class="font-medium">❌ ${param.error}</p>
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="mb-4 p-4 bg-red-50 border-l-4 border-red-500 text-red-700 rounded">
                <p class="font-medium">❌ ${error}</p>
            </div>
        </c:if>

        <!-- Filtres et recherche -->
        <div class="bg-white rounded-lg shadow-md p-6 mb-6">
            <h3 class="text-lg font-semibold text-gray-800 mb-4">🔍 Filtres et Recherche</h3>
            
            <form method="get" action="${pageContext.request.contextPath}/salles/list" class="grid grid-cols-1 md:grid-cols-4 gap-4">
                
                <!-- Type -->
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Type</label>
                    <select name="type" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
                        <option value="">Tous les types</option>
                        <option value="COURS" ${typeFilter == 'COURS' ? 'selected' : ''}>Cours</option>
                        <option value="TP" ${typeFilter == 'TP' ? 'selected' : ''}>TP</option>
                        <option value="TD" ${typeFilter == 'TD' ? 'selected' : ''}>TD</option>
                    </select>
                </div>

                <!-- Disponibilité -->
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Disponibilité</label>
                    <select name="disponible" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
                        <option value="">Toutes</option>
                        <option value="true" ${disponibleFilter == 'true' ? 'selected' : ''}>Disponibles</option>
                        <option value="false" ${disponibleFilter == 'false' ? 'selected' : ''}>Non disponibles</option>
                    </select>
                </div>

                <!-- Capacité minimale -->
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Capacité min.</label>
                    <input type="number" name="capaciteMin" value="${capaciteMin}" 
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500"
                           placeholder="Ex: 30">
                </div>

                <!-- Bouton filtrer -->
                <div class="flex items-end">
                    <button type="submit" class="w-full bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition">
                        Filtrer
                    </button>
                </div>
            </form>

            <!-- Recherche par nom -->
            <form method="get" action="${pageContext.request.contextPath}/salles/search" class="mt-4">
                <div class="flex gap-2">
                    <input type="text" name="keyword" value="${keyword}" 
                           class="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500"
                           placeholder="Rechercher par nom...">
                    <button type="submit" class="bg-gray-600 text-white px-6 py-2 rounded-lg hover:bg-gray-700 transition">
                        Rechercher
                    </button>
                    <a href="${pageContext.request.contextPath}/salles/list" 
                       class="bg-gray-300 text-gray-700 px-6 py-2 rounded-lg hover:bg-gray-400 transition">
                        Réinitialiser
                    </a>
                </div>
            </form>
        </div>

        <!-- Liste des salles -->
        <div class="bg-white rounded-lg shadow-md overflow-hidden">
            <c:choose>
                <c:when test="${empty salles}">
                    <div class="p-8 text-center text-gray-500">
                        <p class="text-lg">Aucune salle trouvée</p>
                        <a href="${pageContext.request.contextPath}/salles/new" 
                           class="text-indigo-600 hover:text-indigo-800 font-semibold mt-2 inline-block">
                            Créer la première salle
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="w-full">
                        <thead class="bg-gray-50 border-b border-gray-200">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nom</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Type</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Capacité</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Équipements</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Disponibilité</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach var="salle" items="${salles}">
                                <tr class="hover:bg-gray-50">
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="text-sm font-medium text-gray-900">${salle.nom}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full 
                                            ${salle.type == 'COURS' ? 'bg-blue-100 text-blue-800' : 
                                              salle.type == 'TP' ? 'bg-purple-100 text-purple-800' : 
                                              'bg-green-100 text-green-800'}">
                                            ${salle.type}
                                        </span>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                        ${salle.capacite} places
                                    </td>
                                    <td class="px-6 py-4 text-sm text-gray-500">
                                        <c:choose>
                                            <c:when test="${not empty salle.equipements}">
                                                ${salle.equipements.length() > 50 ? salle.equipements.substring(0, 50).concat('...') : salle.equipements}
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-gray-400 italic">Aucun</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <c:choose>
                                            <c:when test="${salle.disponible}">
                                                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                                                    ✓ Disponible
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">
                                                    ✗ Non disponible
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                        <a href="${pageContext.request.contextPath}/salles/view?id=${salle.id}" 
                                           class="text-indigo-600 hover:text-indigo-900">👁️ Voir</a>
                                        <a href="${pageContext.request.contextPath}/salles/edit?id=${salle.id}" 
                                           class="text-blue-600 hover:text-blue-900">✏️ Modifier</a>
                                        <a href="${pageContext.request.contextPath}/salles/delete?id=${salle.id}" 
                                           onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette salle ?')"
                                           class="text-red-600 hover:text-red-900">🗑️ Supprimer</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    
                    <!-- Statistiques -->
                    <div class="bg-gray-50 px-6 py-4 border-t border-gray-200">
                        <p class="text-sm text-gray-600">
                            <strong>${salles.size()}</strong> salle(s) trouvée(s)
                        </p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

<jsp:include page="../common/footer.jsp"/>

