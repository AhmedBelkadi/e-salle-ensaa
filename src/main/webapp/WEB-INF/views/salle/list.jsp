<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Gestion des Salles - E-Salle ENSAA"/>
</jsp:include>

<div class="p-6">
    <div class="space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Salles</h1>
                <p class="text-gray-600 mt-2">Gérer et consulter les salles disponibles</p>
            </div>
            <c:if test="${sessionScope.user.role.name() == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/salles/new" 
                   class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white">
                    <i class="fas fa-plus w-4 h-4"></i>
                    Nouvelle Salle
                </a>
            </c:if>
        </div>

        <!-- Messages -->
        <c:if test="${not empty sessionScope.success}">
            <div class="p-3 text-sm text-green-600 bg-green-50 border border-green-200 rounded-md">
                ${sessionScope.success}
            </div>
            <c:remove var="success" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="p-3 text-sm text-red-600 bg-red-50 border border-red-200 rounded-md">
                ${sessionScope.error}
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <div class="grid grid-cols-1 lg:grid-cols-4 gap-6">
            <!-- Filters Sidebar -->
            <div class="lg:col-span-1">
                <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                    <div class="px-6 pt-0 space-y-6">
                        <h3 class="text-gray-900 font-semibold">Filtres</h3>

                        <form method="get" action="${pageContext.request.contextPath}/salles/list" class="space-y-4">
                            <!-- Type Filter -->
                            <div class="space-y-2">
                                <label class="text-gray-700 font-medium text-sm">Type</label>
                                <select name="type" class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500 text-gray-900 text-sm">
                                    <option value="">Tous les types</option>
                                    <option value="COURS" ${typeFilter == 'COURS' ? 'selected' : ''}>Cours</option>
                                    <option value="TP" ${typeFilter == 'TP' ? 'selected' : ''}>TP</option>
                                    <option value="TD" ${typeFilter == 'TD' ? 'selected' : ''}>TD</option>
                                </select>
                            </div>

                            <!-- Disponibilité Filter -->
                            <div class="space-y-2">
                                <label class="text-gray-700 font-medium text-sm">Disponibilité</label>
                                <select name="disponible" class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500 text-gray-900 text-sm">
                                    <option value="">Toutes</option>
                                    <option value="true" ${disponibleFilter == 'true' ? 'selected' : ''}>Disponibles</option>
                                    <option value="false" ${disponibleFilter == 'false' ? 'selected' : ''}>Non disponibles</option>
                                </select>
                            </div>

                            <!-- Capacité Filter -->
                            <div class="space-y-2">
                                <label class="text-gray-700 font-medium text-sm">Capacité minimale</label>
                                <input type="number" name="capaciteMin" value="${capaciteMin}" 
                                       placeholder="Ex: 30"
                                       class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500 text-gray-900 text-sm">
                            </div>

                            <!-- Buttons -->
                            <button type="submit" class="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-md py-2 text-sm">
                                Appliquer les filtres
                            </button>
                            <a href="${pageContext.request.contextPath}/salles/list" 
                               class="block w-full text-center border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 font-medium rounded-md py-2 text-sm">
                                Réinitialiser
                            </a>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Rooms Grid -->
            <div class="lg:col-span-3 space-y-4">
                <div class="text-sm text-gray-600">
                    ${salles.size()} salle(s) trouvée(s)
                </div>

                <c:choose>
                    <c:when test="${empty salles}">
                        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-12 shadow-sm text-center">
                            <div class="px-6">
                                <p class="text-lg text-gray-500">Aucune salle trouvée</p>
                                <c:if test="${sessionScope.user.role.name() == 'ADMIN'}">
                                    <a href="${pageContext.request.contextPath}/salles/new" 
                                       class="text-blue-600 hover:underline font-medium mt-2 inline-block">
                                        Créer la première salle
                                    </a>
                                </c:if>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <c:forEach var="salle" items="${salles}">
                                <!-- Room Card -->
                                <div class="relative">
                                    <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm hover:border-gray-300 transition overflow-hidden">
                                        <!-- Room Header with Type Badge -->
                                        <div class="h-40 
                                            ${salle.type == 'COURS' ? 'bg-gradient-to-br from-blue-100 to-blue-200' : 
                                              salle.type == 'TP' ? 'bg-gradient-to-br from-purple-100 to-purple-200' : 
                                              'bg-gradient-to-br from-green-100 to-green-200'}
                                            flex items-center justify-center relative">
                                            <div class="text-center">
                                                <div class="text-4xl font-bold 
                                                    ${salle.type == 'COURS' ? 'text-blue-400' : 
                                                      salle.type == 'TP' ? 'text-purple-400' : 
                                                      'text-green-400'}">
                                                    ${salle.nom.substring(0, 1).toUpperCase()}
                                                </div>
                                                <p class="text-xs mt-2
                                                    ${salle.type == 'COURS' ? 'text-blue-600' : 
                                                      salle.type == 'TP' ? 'text-purple-600' : 
                                                      'text-green-600'}">
                                                    ${salle.nom}
                                                </p>
                                            </div>
                                            <div class="absolute top-3 right-3">
                                                <c:choose>
                                                    <c:when test="${salle.disponible}">
                                                        <span class="text-xs px-2 py-1 rounded font-medium border bg-green-100 text-green-800 border-green-200">
                                                            Disponible
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-xs px-2 py-1 rounded font-medium border bg-gray-100 text-gray-600 border-gray-200">
                                                            Non disponible
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>

                                        <div class="px-6 pt-0 space-y-3">
                                            <div class="space-y-2">
                                                <div class="flex items-center gap-2 text-gray-700">
                                                    <i class="fas fa-door-open w-4 h-4 text-gray-500"></i>
                                                    <span class="text-sm font-semibold">${salle.nom}</span>
                                                </div>
                                                <div class="flex items-center gap-2 text-gray-700">
                                                    <i class="fas fa-users w-4 h-4 text-gray-500"></i>
                                                    <span class="text-sm">Capacité: ${salle.capacite} personnes</span>
                                                </div>
                                                <div class="flex items-center gap-2 text-gray-700">
                                                    <i class="fas fa-tag w-4 h-4 text-gray-500"></i>
                                                    <span class="text-xs px-2 py-1 rounded font-medium
                                                        ${salle.type == 'COURS' ? 'bg-blue-100 text-blue-800' : 
                                                          salle.type == 'TP' ? 'bg-purple-100 text-purple-800' : 
                                                          'bg-green-100 text-green-800'}">
                                                        ${salle.type}
                                                    </span>
                                                </div>
                                            </div>

                                            <c:if test="${not empty salle.equipements}">
                                                <div class="space-y-2">
                                                    <div class="flex items-center gap-2 text-gray-700">
                                                        <i class="fas fa-bolt w-4 h-4 text-yellow-500"></i>
                                                        <span class="text-xs text-gray-500">Équipements</span>
                                                    </div>
                                                    <p class="text-xs text-gray-600">
                                                        ${salle.equipements.length() > 80 ? salle.equipements.substring(0, 80).concat('...') : salle.equipements}
                                                    </p>
                                                </div>
                                            </c:if>

                                            <a href="${pageContext.request.contextPath}/salles/view?id=${salle.id}"
                                               class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-4 py-2 w-full border border-gray-300 bg-white text-gray-700 hover:bg-gray-50">
                                                Voir détails
                                            </a>
                                        </div>
                                    </div>
                                    
                                    <!-- Admin Actions -->
                                    <c:if test="${sessionScope.user.role.name() == 'ADMIN'}">
                                        <div class="absolute top-3 left-3 flex gap-2">
                                            <a href="${pageContext.request.contextPath}/salles/edit?id=${salle.id}"
                                               class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-8 px-3 border border-gray-300 bg-white/90 text-gray-700 hover:bg-white gap-1">
                                                <i class="fas fa-edit w-3 h-3"></i>
                                                Modifier
                                            </a>
                                            <a href="${pageContext.request.contextPath}/salles/delete?id=${salle.id}"
                                               onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette salle ?')"
                                               class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-8 px-3 bg-red-600 hover:bg-red-700 text-white gap-1">
                                                <i class="fas fa-trash w-3 h-3"></i>
                                                Supprimer
                                            </a>
                                        </div>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
        <!-- Pagination -->
        <jsp:include page="../common/pagination.jsp"/>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
