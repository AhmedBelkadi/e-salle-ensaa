<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${salle.nom} - E-Salle ENSAA"/>
</jsp:include>

<div class="p-6">
    <div class="space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">${salle.nom}</h1>
                <p class="text-gray-600 mt-2">Détails de la salle</p>
            </div>
            <div class="flex gap-2">
                <a href="${pageContext.request.contextPath}/salles/list" 
                   class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-4 py-2 border border-gray-300 bg-white text-gray-700 hover:bg-gray-50">
                    <i class="fas fa-arrow-left w-3 h-3"></i>
                    Retour
                </a>
                <c:if test="${sessionScope.user.role.name() == 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/salles/edit?id=${salle.id}" 
                       class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white">
                        <i class="fas fa-edit w-3 h-3"></i>
                        Modifier
                    </a>
                    <a href="${pageContext.request.contextPath}/salles/delete?id=${salle.id}" 
                       onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette salle ?')"
                       class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-4 py-2 bg-red-600 hover:bg-red-700 text-white">
                        <i class="fas fa-trash w-3 h-3"></i>
                        Supprimer
                    </a>
                </c:if>
            </div>
        </div>

        <!-- Main Info Card -->
        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
            <div class="px-6">
                <h2 class="text-lg font-semibold text-gray-900 mb-4">Informations Générales</h2>
                
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <!-- Nom -->
                    <div class="space-y-2">
                        <label class="text-sm text-gray-600 font-medium">Nom</label>
                        <p class="text-lg font-semibold text-gray-900">${salle.nom}</p>
                    </div>

                    <!-- Type -->
                    <div class="space-y-2">
                        <label class="text-sm text-gray-600 font-medium">Type</label>
                        <div>
                            <span class="inline-flex items-center gap-2 px-3 py-1 text-sm font-medium rounded-md
                                ${salle.type == 'COURS' ? 'bg-blue-100 text-blue-800' : 
                                  salle.type == 'TP' ? 'bg-purple-100 text-purple-800' : 
                                  'bg-green-100 text-green-800'}">
                                <i class="fas ${salle.type == 'COURS' ? 'fa-chalkboard-teacher' : 
                                              salle.type == 'TP' ? 'fa-flask' : 
                                              'fa-pencil-alt'} w-4 h-4"></i>
                                ${salle.type == 'COURS' ? 'Cours magistral' : 
                                  salle.type == 'TP' ? 'Travaux pratiques' : 
                                  'Travaux dirigés'}
                            </span>
                        </div>
                    </div>

                    <!-- Capacité -->
                    <div class="space-y-2">
                        <label class="text-sm text-gray-600 font-medium">Capacité</label>
                        <p class="text-2xl font-bold text-gray-900">${salle.capacite} <span class="text-base font-normal text-gray-600">places</span></p>
                    </div>

                    <!-- Disponibilité -->
                    <div class="space-y-2">
                        <label class="text-sm text-gray-600 font-medium">Disponibilité</label>
                        <div>
                            <c:choose>
                                <c:when test="${salle.disponible}">
                                    <span class="inline-flex items-center gap-2 px-3 py-1 text-sm font-medium rounded-md bg-green-100 text-green-800">
                                        <i class="fas fa-check-circle w-4 h-4"></i>
                                        Disponible
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="inline-flex items-center gap-2 px-3 py-1 text-sm font-medium rounded-md bg-gray-100 text-gray-800">
                                        <i class="fas fa-times-circle w-4 h-4"></i>
                                        Non disponible
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Date de création -->
                    <div class="space-y-2">
                        <label class="text-sm text-gray-600 font-medium">Date de création</label>
                        <p class="text-gray-900 flex items-center gap-2">
                            <i class="fas fa-calendar w-4 h-4 text-gray-500"></i>
                            ${salle.dateCreation}
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Équipements Card -->
        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
            <div class="px-6">
                <h2 class="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
                    <i class="fas fa-tools w-5 h-5 text-gray-600"></i>
                    Équipements
                </h2>
                
                <c:choose>
                    <c:when test="${not empty salle.equipements}">
                        <p class="text-gray-700 whitespace-pre-line leading-relaxed">${salle.equipements}</p>
                    </c:when>
                    <c:otherwise>
                        <p class="text-gray-400 italic">Aucun équipement renseigné</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Métadonnées Card -->
        <div class="bg-gray-50 flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
            <div class="px-6">
                <h2 class="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
                    <i class="fas fa-info-circle w-5 h-5 text-gray-600"></i>
                    Métadonnées
                </h2>
                
                <div class="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                    <div class="space-y-1">
                        <label class="text-gray-600">ID</label>
                        <p class="text-gray-900 font-mono">#${salle.id}</p>
                    </div>

                    <div class="space-y-1">
                        <label class="text-gray-600">Date de création</label>
                        <p class="text-gray-900">${salle.dateCreation}</p>
                    </div>

                    <c:if test="${not empty salle.dateModification}">
                        <div class="space-y-1">
                            <label class="text-gray-600">Dernière modification</label>
                            <p class="text-gray-900">${salle.dateModification}</p>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
