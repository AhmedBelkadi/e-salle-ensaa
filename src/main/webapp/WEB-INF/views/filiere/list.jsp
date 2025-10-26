<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Gestion des Filières - E-Salle ENSAA"/>
</jsp:include>

<div class="p-6">
    <div class="space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Filières</h1>
                <p class="text-gray-600 mt-2">Gérer les filières et programmes académiques</p>
            </div>
            <c:if test="${sessionScope.user.role.name() == 'ADMIN' || sessionScope.user.role.name() == 'COORDINATEUR'}">
                <a href="${pageContext.request.contextPath}/filieres/new" 
                   class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white">
                    <i class="fas fa-plus w-4 h-4"></i>
                    Nouvelle Filière
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

        <!-- Stats Cards -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <!-- Total -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Total Filières</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${filieres.size()}</p>
                        </div>
                        <div class="bg-indigo-500 p-3 rounded-lg">
                            <i class="fas fa-graduation-cap w-6 h-6 text-white"></i>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Préparatoire -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Préparatoire</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${countPrepa}</p>
                        </div>
                        <div class="bg-blue-500 p-3 rounded-lg">
                            <i class="fas fa-book w-6 h-6 text-white"></i>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Ingénieur -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Ingénieur</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${countIngenieur}</p>
                        </div>
                        <div class="bg-purple-500 p-3 rounded-lg">
                            <i class="fas fa-award w-6 h-6 text-white"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Filters Card -->
        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
            <div class="px-6 pt-0">
                <h3 class="text-gray-900 font-semibold mb-4">Filtres</h3>
                
                <form method="get" action="${pageContext.request.contextPath}/filieres/list">
                    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <!-- Cycle -->
                        <div class="space-y-2">
                            <label class="text-gray-700 font-medium text-sm">Cycle</label>
                            <select name="cycle" class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500 text-gray-900 text-sm">
                                <option value="">Tous les cycles</option>
                                <option value="PREPARATOIRE" ${cycleFilter == 'PREPARATOIRE' ? 'selected' : ''}>Préparatoire</option>
                                <option value="INGENIEUR" ${cycleFilter == 'INGENIEUR' ? 'selected' : ''}>Ingénieur</option>
                            </select>
                        </div>

                        <!-- Année -->
                        <div class="space-y-2">
                            <label class="text-gray-700 font-medium text-sm">Année</label>
                            <select name="annee" class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500 text-gray-900 text-sm">
                                <option value="">Toutes les années</option>
                                <option value="1" ${anneeFilter == '1' ? 'selected' : ''}>1ère année</option>
                                <option value="2" ${anneeFilter == '2' ? 'selected' : ''}>2ème année</option>
                                <option value="3" ${anneeFilter == '3' ? 'selected' : ''}>3ème année</option>
                            </select>
                        </div>

                        <!-- Buttons -->
                        <div class="flex items-end gap-2">
                            <button type="submit" class="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-md py-2 text-sm">
                                Filtrer
                            </button>
                            <a href="${pageContext.request.contextPath}/filieres/list" 
                               class="flex-1 text-center border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 font-medium rounded-md py-2 text-sm">
                                Reset
                            </a>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <!-- Filières List -->
        <c:choose>
            <c:when test="${empty filieres}">
                <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-12 shadow-sm text-center">
                    <div class="px-6">
                        <p class="text-lg text-gray-500">Aucune filière trouvée</p>
                        <c:if test="${sessionScope.user.role.name() == 'ADMIN' || sessionScope.user.role.name() == 'COORDINATEUR'}">
                            <a href="${pageContext.request.contextPath}/filieres/new" 
                               class="text-blue-600 hover:underline font-medium mt-2 inline-block">
                                Créer la première filière
                            </a>
                        </c:if>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="space-y-3">
                    <c:forEach var="filiere" items="${filieres}">
                        <!-- Filiere Card -->
                        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm hover:border-gray-300 transition">
                            <div class="px-6 pt-0">
                                <div class="flex items-start justify-between gap-4">
                                    <div class="flex-1 space-y-2">
                                        <div class="flex items-center gap-3">
                                            <i class="fas fa-graduation-cap w-5 h-5 ${filiere.cycle == 'PREPARATOIRE' ? 'text-blue-600' : 'text-purple-600'}"></i>
                                            <h3 class="font-semibold text-gray-900 text-lg">${filiere.nom}</h3>
                                        </div>
                                        
                                        <div class="flex flex-wrap gap-4 text-sm text-gray-600">
                                            <span class="flex items-center gap-2">
                                                <i class="fas fa-layer-group w-4 h-4"></i>
                                                ${filiere.cycle} - Année ${filiere.annee}
                                            </span>
                                            <span class="flex items-center gap-2">
                                                <i class="fas fa-users w-4 h-4"></i>
                                                ${filiere.effectif} étudiants
                                            </span>
                                            <c:if test="${not empty filiere.coordinateurNom}">
                                                <span class="flex items-center gap-2">
                                                    <i class="fas fa-user-tie w-4 h-4"></i>
                                                    ${filiere.coordinateurNom}
                                                </span>
                                            </c:if>
                                        </div>

                                        <c:if test="${not empty filiere.description}">
                                            <p class="text-sm text-gray-600 mt-2">
                                                ${filiere.description.length() > 150 ? filiere.description.substring(0, 150).concat('...') : filiere.description}
                                            </p>
                                        </c:if>
                                    </div>

                                    <div class="flex items-center gap-3">
                                        <div class="flex items-center gap-1 px-3 py-1 rounded text-sm 
                                            ${filiere.cycle == 'PREPARATOIRE' ? 'bg-blue-100 text-blue-700 border border-blue-200' : 'bg-purple-100 text-purple-700 border border-purple-200'}">
                                            ${filiere.cycle == 'PREPARATOIRE' ? '📚 Prépa' : '🎓 Ingénieur'}
                                        </div>
                                        
                                        <a href="${pageContext.request.contextPath}/filieres/view?id=${filiere.id}"
                                           class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-4 py-2 border border-gray-300 bg-white text-gray-700 hover:bg-gray-50">
                                            Voir détails
                                        </a>
                                        
                                        <c:if test="${sessionScope.user.role.name() == 'ADMIN' || sessionScope.user.role.name() == 'COORDINATEUR'}">
                                            <a href="${pageContext.request.contextPath}/filieres/edit?id=${filiere.id}"
                                               class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-3 border border-gray-300 bg-white text-gray-700 hover:bg-gray-50">
                                                <i class="fas fa-edit w-3 h-3"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/filieres/delete?id=${filiere.id}"
                                               onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette filière ?')"
                                               class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-3 bg-red-600 hover:bg-red-700 text-white">
                                                <i class="fas fa-trash w-3 h-3"></i>
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
