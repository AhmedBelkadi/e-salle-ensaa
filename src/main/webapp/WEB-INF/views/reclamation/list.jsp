<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Réclamations - E-Salle ENSAA"/>
</jsp:include>

<div class="p-6">
    <div class="space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">
                    ${isUserView ? 'Mes Réclamations' : 'Réclamations'}
                </h1>
                <p class="text-gray-600 mt-2">Signaler et suivre les problèmes d'équipement</p>
            </div>
            <c:if test="${sessionScope.user.role.name() != 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/reclamations/new" 
                   class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white">
                    <i class="fas fa-plus w-4 h-4"></i>
                    Nouvelle Réclamation
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

        <!-- Stats Cards (Admin only) -->
        <c:if test="${not isUserView}">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <!-- En Attente -->
                <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                    <div class="px-6 pt-0">
                        <div class="flex items-center justify-between">
                            <div>
                                <p class="text-gray-600 text-sm">En Attente</p>
                                <p class="text-3xl font-bold text-gray-900 mt-2">${countEnAttente}</p>
                            </div>
                            <div class="bg-orange-500 p-3 rounded-lg">
                                <i class="fas fa-clock w-6 h-6 text-white"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Traitées -->
                <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                    <div class="px-6 pt-0">
                        <div class="flex items-center justify-between">
                            <div>
                                <p class="text-gray-600 text-sm">Traitées</p>
                                <p class="text-3xl font-bold text-gray-900 mt-2">${countTraitees}</p>
                            </div>
                            <div class="bg-green-500 p-3 rounded-lg">
                                <i class="fas fa-check-circle w-6 h-6 text-white"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Urgentes -->
                <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                    <div class="px-6 pt-0">
                        <div class="flex items-center justify-between">
                            <div>
                                <p class="text-gray-600 text-sm">Urgentes</p>
                                <p class="text-3xl font-bold text-gray-900 mt-2">${countUrgentes}</p>
                            </div>
                            <div class="bg-red-500 p-3 rounded-lg">
                                <i class="fas fa-exclamation-triangle w-6 h-6 text-white"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Réclamations List -->
        <c:choose>
            <c:when test="${empty reclamations}">
                <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-12 shadow-sm text-center">
                    <div class="px-6">
                        <p class="text-lg text-gray-500">Aucune réclamation trouvée</p>
                        <c:if test="${sessionScope.user.role.name() != 'ADMIN'}">
                            <a href="${pageContext.request.contextPath}/reclamations/new" 
                               class="text-blue-600 hover:underline font-medium mt-2 inline-block">
                                Signaler un problème
                            </a>
                        </c:if>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="space-y-3">
                    <c:forEach var="recla" items="${reclamations}">
                        <!-- Réclamation Card -->
                        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm hover:border-gray-300 transition">
                            <div class="px-6 pt-0">
                                <div class="flex items-start justify-between gap-4">
                                    <div class="flex-1 space-y-2">
                                        <div class="flex items-center gap-2">
                                            <i class="fas fa-door-open w-4 h-4 text-gray-600"></i>
                                            <h3 class="font-semibold text-gray-900">${recla.salleNom}</h3>
                                        </div>
                                        
                                        <p class="text-sm text-gray-700">
                                            ${recla.description.length() > 120 ? recla.description.substring(0, 120).concat('...') : recla.description}
                                        </p>
                                        
                                        <div class="flex flex-wrap gap-4 text-xs text-gray-600 mt-2">
                                            <span>Demandeur: ${recla.userNom}</span>
                                            <span>Créé: ${recla.dateCreationFormatee}</span>
                                            <c:if test="${recla.statut.name() == 'TRAITEE'}">
                                                <span>Traité: ${recla.dateTraitementFormatee}</span>
                                            </c:if>
                                        </div>
                                    </div>

                                    <div class="flex items-center gap-3">
                                        <!-- Urgence Badge -->
                                        <div class="flex items-center gap-1 px-3 py-1 rounded text-sm
                                            ${recla.urgence.name() == 'URGENT' ? 'bg-red-100 text-red-700 border border-red-200' : 
                                              recla.urgence.name() == 'MOYEN' ? 'bg-yellow-100 text-yellow-700 border border-yellow-200' : 
                                              'bg-blue-100 text-blue-700 border border-blue-200'}">
                                            <i class="fas fa-exclamation-triangle w-4 h-4"></i>
                                            ${recla.urgence.name() == 'URGENT' ? 'Urgent' : 
                                              recla.urgence.name() == 'MOYEN' ? 'Moyen' : 
                                              'Faible'}
                                        </div>
                                        
                                        <!-- Statut Badge -->
                                        <div class="flex items-center gap-1 px-3 py-1 rounded text-sm
                                            ${recla.statut.name() == 'TRAITEE' ? 'bg-green-100 text-green-700 border border-green-200' : 
                                              'bg-orange-100 text-orange-700 border border-orange-200'}">
                                            <i class="fas ${recla.statut.name() == 'TRAITEE' ? 'fa-check-circle' : 'fa-clock'} w-4 h-4"></i>
                                            ${recla.statut.name() == 'TRAITEE' ? 'Traitée' : 'En attente'}
                                        </div>
                                        
                                        <a href="${pageContext.request.contextPath}/reclamations/view?id=${recla.id}"
                                           class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-4 py-2 border border-gray-300 bg-white text-gray-700 hover:bg-gray-50">
                                            Voir détails
                                        </a>
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
