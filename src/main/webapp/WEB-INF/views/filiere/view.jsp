<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${filiere.nom} - E-Salle ENSAA"/>
</jsp:include>

<div class="p-6">
    <div class="max-w-5xl mx-auto space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">${filiere.nomComplet}</h1>
                <p class="text-gray-600 mt-2">Détails de la filière</p>
            </div>
            <div class="flex gap-2">
                <a href="${pageContext.request.contextPath}/filieres/list" 
                   class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-4 py-2 border border-gray-300 bg-white text-gray-700 hover:bg-gray-50">
                    <i class="fas fa-arrow-left w-3 h-3"></i>
                    Retour
                </a>
                <c:if test="${sessionScope.user.role.name() == 'ADMIN' || sessionScope.user.role.name() == 'COORDINATEUR'}">
                    <a href="${pageContext.request.contextPath}/filieres/edit?id=${filiere.id}" 
                       class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white">
                        <i class="fas fa-edit w-3 h-3"></i>
                        Modifier
                    </a>
                    <a href="${pageContext.request.contextPath}/filieres/delete?id=${filiere.id}" 
                       onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette filière ?')"
                       class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-4 py-2 bg-red-600 hover:bg-red-700 text-white">
                        <i class="fas fa-trash w-3 h-3"></i>
                        Supprimer
                    </a>
                </c:if>
            </div>
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

        <!-- Main Info Card -->
        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
            <div class="px-6">
                <h2 class="text-lg font-semibold text-gray-900 mb-4">Informations Générales</h2>
                
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <!-- Nom -->
                    <div class="space-y-2">
                        <label class="text-sm text-gray-600 font-medium">Nom</label>
                        <p class="text-lg font-semibold text-gray-900">${filiere.nom}</p>
                    </div>

                    <!-- Cycle -->
                    <div class="space-y-2">
                        <label class="text-sm text-gray-600 font-medium">Cycle</label>
                        <div>
                            <span class="inline-flex items-center gap-2 px-3 py-1 text-sm font-medium rounded-md
                                ${filiere.cycle == 'PREPARATOIRE' ? 'bg-blue-100 text-blue-800' : 'bg-purple-100 text-purple-800'}">
                                <i class="fas ${filiere.cycle == 'PREPARATOIRE' ? 'fa-book' : 'fa-graduation-cap'} w-4 h-4"></i>
                                ${filiere.cycle == 'PREPARATOIRE' ? 'Préparatoire' : 'Ingénieur'}
                            </span>
                        </div>
                    </div>

                    <!-- Année -->
                    <div class="space-y-2">
                        <label class="text-sm text-gray-600 font-medium">Année</label>
                        <p class="text-lg font-semibold text-gray-900">${filiere.anneeLibelle}</p>
                    </div>

                    <!-- Effectif -->
                    <div class="space-y-2">
                        <label class="text-sm text-gray-600 font-medium">Effectif</label>
                        <p class="text-2xl font-bold text-gray-900">${filiere.effectif} <span class="text-base font-normal text-gray-600">étudiants</span></p>
                    </div>
                </div>

                <!-- Description -->
                <c:if test="${not empty filiere.description}">
                    <div class="mt-6 pt-6 border-t border-gray-200 space-y-2">
                        <label class="text-sm text-gray-600 font-medium">Description</label>
                        <p class="text-gray-700 leading-relaxed">${filiere.description}</p>
                    </div>
                </c:if>
            </div>
        </div>

        <!-- Coordinateur Card -->
        <c:if test="${filiere.hasCoordinateur()}">
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6">
                    <h2 class="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
                        <i class="fas fa-user-tie w-5 h-5 text-gray-600"></i>
                        Coordinateur
                    </h2>
                    
                    <div class="flex items-center gap-4">
                        <div class="bg-indigo-100 p-4 rounded-full">
                            <i class="fas fa-user-tie w-6 h-6 text-indigo-600"></i>
                        </div>
                        <div>
                            <p class="text-lg font-semibold text-gray-900">${filiere.coordinateurNom}</p>
                            <p class="text-sm text-gray-600">Coordinateur de la filière</p>
                        </div>
                    </div>
                    
                    <div class="mt-4 p-3 bg-blue-50 rounded-md">
                        <p class="text-sm text-blue-700 flex items-center gap-2">
                            <i class="fas fa-info-circle w-4 h-4"></i>
                            Le coordinateur a été assigné lors de son inscription
                        </p>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Métadonnées Card -->
        <div class="bg-gray-50 flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
            <div class="px-6">
                <h2 class="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
                    <i class="fas fa-clock w-5 h-5 text-gray-600"></i>
                    Métadonnées
                </h2>
                
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                    <div class="space-y-1">
                        <label class="text-gray-600">Date de création</label>
                        <p class="text-gray-900">${filiere.createdAt}</p>
                    </div>

                    <div class="space-y-1">
                        <label class="text-gray-600">Dernière modification</label>
                        <p class="text-gray-900">${filiere.updatedAt}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
