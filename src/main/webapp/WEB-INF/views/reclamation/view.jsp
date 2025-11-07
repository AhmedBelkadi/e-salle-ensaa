<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Réclamation #${reclamation.id} - E-Salle ENSAA"/>
</jsp:include>

<div class="p-6">
    <div class="max-w-5xl mx-auto space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Réclamation #${reclamation.id}</h1>
                <p class="text-gray-600 mt-2">Détails de la réclamation</p>
            </div>
            <a href="${pageContext.request.contextPath}/reclamations/list" 
               class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-4 py-2 border border-gray-300 bg-white text-gray-700 hover:bg-gray-50">
                <i class="fas fa-arrow-left w-3 h-3"></i>
                Retour
            </a>
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
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <!-- Salle -->
                    <div class="space-y-2">
                        <label class="text-sm text-gray-600 font-medium">Salle</label>
                        <p class="text-lg font-semibold text-gray-900 flex items-center gap-2">
                            <i class="fas fa-door-open w-5 h-5 text-gray-500"></i>
                            ${reclamation.salleNom}
                        </p>
                    </div>

                    <!-- Demandeur -->
                    <div class="space-y-2">
                        <label class="text-sm text-gray-600 font-medium">Demandeur</label>
                        <p class="text-lg font-semibold text-gray-900 flex items-center gap-2">
                            <i class="fas fa-user w-5 h-5 text-gray-500"></i>
                            ${reclamation.userNom}
                        </p>
                    </div>

                    <!-- Urgence -->
                    <div class="space-y-2">
                        <label class="text-sm text-gray-600 font-medium">Urgence</label>
                        <div>
                            <span class="inline-flex items-center gap-2 px-3 py-1 text-sm font-medium rounded-md
                                ${reclamation.urgence.name() == 'URGENT' ? 'bg-red-100 text-red-800' : 
                                  reclamation.urgence.name() == 'MOYEN' ? 'bg-yellow-100 text-yellow-800' : 
                                  'bg-blue-100 text-blue-800'}">
                                <i class="fas fa-exclamation-triangle w-4 h-4"></i>
                                ${reclamation.urgence.name() == 'URGENT' ? 'Urgent' : 
                                  reclamation.urgence.name() == 'MOYEN' ? 'Moyen' : 'Faible'}
                            </span>
                        </div>
                    </div>

                    <!-- Statut -->
                    <div class="space-y-2">
                        <label class="text-sm text-gray-600 font-medium">Statut</label>
                        <div>
                            <span class="inline-flex items-center gap-2 px-3 py-1 text-sm font-medium rounded-md
                                ${reclamation.statut.name() == 'TRAITEE' ? 'bg-green-100 text-green-800' : 'bg-orange-100 text-orange-800'}">
                                <i class="fas ${reclamation.statut.name() == 'TRAITEE' ? 'fa-check-circle' : 'fa-clock'} w-4 h-4"></i>
                                ${reclamation.statut.name() == 'TRAITEE' ? 'Traitée' : 'En attente'}
                            </span>
                        </div>
                    </div>
                </div>

                <!-- Description -->
                <div class="mt-6 pt-6 border-t border-gray-200 space-y-2">
                    <label class="text-sm text-gray-600 font-medium">Description du problème</label>
                    <p class="text-gray-700 leading-relaxed">${reclamation.description}</p>
                </div>

                <!-- Dates -->
                <div class="mt-6 pt-6 border-t border-gray-200">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                        <div class="space-y-1">
                            <label class="text-gray-600">Date de création</label>
                            <p class="text-gray-900 font-medium">${reclamation.dateCreationFormatee}</p>
                        </div>

                        <c:if test="${reclamation.statut.name() == 'TRAITEE'}">
                            <div class="space-y-1">
                                <label class="text-gray-600">Date de traitement</label>
                                <p class="text-gray-900 font-medium">${reclamation.dateTraitementFormatee}</p>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <!-- Traitement Card (if treated) -->
        <c:if test="${reclamation.statut.name() == 'TRAITEE'}">
            <div class="bg-green-50 flex flex-col gap-6 rounded-xl border border-green-200 py-6 shadow-sm">
                <div class="px-6">
                    <h2 class="text-lg font-semibold text-green-900 mb-4 flex items-center gap-2">
                        <i class="fas fa-check-circle w-5 h-5"></i>
                        Traitement
                    </h2>
                    
                    <div class="space-y-4">
                        <div class="space-y-1">
                            <label class="text-sm text-green-700 font-medium">Traité par</label>
                            <p class="text-green-900 font-semibold">${reclamation.traiteParNom}</p>
                        </div>

                        <div class="space-y-1">
                            <label class="text-sm text-green-700 font-medium">Commentaire</label>
                            <p class="text-green-900">${reclamation.commentaireTraitement}</p>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Traitement Form (Admin only, if pending) -->
        <c:if test="${sessionScope.user.role.name() == 'ADMIN' && reclamation.statut.name() == 'EN_ATTENTE'}">
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6">
                    <h2 class="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
                        <i class="fas fa-wrench w-5 h-5 text-gray-600"></i>
                        Traiter la réclamation
                    </h2>
                    
                    <form method="post" action="${pageContext.request.contextPath}/reclamations/traiter" class="space-y-4">
                        <input type="hidden" name="reclamationId" value="${reclamation.id}">
                        
                        <div class="space-y-2">
                            <label for="commentaire" class="text-gray-700 font-medium text-sm">
                                Commentaire de traitement <span class="text-red-600">*</span>
                            </label>
                            <textarea 
                                id="commentaire"
                                name="commentaire" 
                                rows="4" 
                                required
                                placeholder="Expliquez comment le problème a été résolu..."
                                class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 placeholder:text-gray-500 text-sm outline-none resize-none"
                            ></textarea>
                        </div>
                        
                        <button 
                            type="submit" 
                            class="w-full bg-green-600 hover:bg-green-700 text-white font-medium rounded-md py-2 transition"
                        >
                            Marquer comme traitée
                        </button>
                    </form>
                </div>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
