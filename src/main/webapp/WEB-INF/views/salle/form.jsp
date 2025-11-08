<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${empty salle ? 'Nouvelle' : 'Modifier'} Salle - E-Salle ENSAA"/>
</jsp:include>

<div class="p-6">
    <div class="max-w-3xl mx-auto space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">
                    ${empty salle ? 'Nouvelle Salle' : 'Modifier la Salle'}
                </h1>
                <p class="text-gray-600 mt-2">
                    ${empty salle ? 'Créer une nouvelle salle' : 'Mettre à jour les informations'}
                </p>
            </div>
        </div>

        <!-- Messages -->
        <c:if test="${not empty error}">
            <div class="p-3 text-sm text-red-600 bg-red-50 border border-red-200 rounded-md">
                ${error}
            </div>
        </c:if>

        <!-- Form Card -->
        <div class="bg-white border border-gray-200 rounded-xl shadow-sm">
            <form method="post" 
                  action="${pageContext.request.contextPath}/salles/${empty salle ? 'create' : 'update'}" 
                  class="p-6 space-y-4">
                
                <c:if test="${not empty salle}">
                    <input type="hidden" name="id" value="${salle.id}">
                </c:if>

                <!-- Nom -->
                <div class="space-y-2">
                    <label for="nom" class="text-gray-700 font-medium text-sm">
                        Nom de la salle <span class="text-red-600">*</span>
                    </label>
                    <input 
                        type="text" 
                        id="nom" 
                        name="nom" 
                        required
                        placeholder="Ex: Amphithéâtre A, Salle TP1"
                        value="${not empty salle ? salle.nom : nom}"
                        class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 placeholder:text-gray-500 text-sm outline-none"
                    >
                    <p class="text-xs text-gray-500">Le nom doit être unique</p>
                </div>

                <!-- Type -->
                <div class="space-y-2">
                    <label for="type" class="text-gray-700 font-medium text-sm">
                        Type de salle <span class="text-red-600">*</span>
                    </label>
                    <select 
                        id="type" 
                        name="type" 
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 text-sm outline-none"
                    >
                        <option value="">Sélectionnez un type</option>
                        <option value="COURS" ${(not empty salle && salle.type == 'COURS') || type == 'COURS' ? 'selected' : ''}>
                            Cours magistral
                        </option>
                        <option value="TP" ${(not empty salle && salle.type == 'TP') || type == 'TP' ? 'selected' : ''}>
                            Travaux pratiques (TP)
                        </option>
                        <option value="TD" ${(not empty salle && salle.type == 'TD') || type == 'TD' ? 'selected' : ''}>
                            Travaux dirigés (TD)
                        </option>
                    </select>
                </div>

                <!-- Capacité -->
                <div class="space-y-2">
                    <label for="capacite" class="text-gray-700 font-medium text-sm">
                        Capacité <span class="text-red-600">*</span>
                    </label>
                    <input 
                        type="number" 
                        id="capacite" 
                        name="capacite" 
                        required
                        min="1"
                        placeholder="Ex: 50"
                        value="${not empty salle ? salle.capacite : capacite}"
                        class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 placeholder:text-gray-500 text-sm outline-none"
                    >
                    <p class="text-xs text-gray-500">Nombre de places disponibles</p>
                </div>

                <!-- Équipements -->
                <div class="space-y-2">
                    <label for="equipements" class="text-gray-700 font-medium text-sm">
                        Équipements
                    </label>
                    <textarea 
                        id="equipements" 
                        name="equipements" 
                        rows="4"
                        placeholder="Ex: Projecteur, Tableau blanc, 20 ordinateurs..."
                        class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 placeholder:text-gray-500 text-sm outline-none resize-none"
                    >${not empty salle ? salle.equipements : equipements}</textarea>
                    <p class="text-xs text-gray-500">Listez les équipements disponibles</p>
                </div>

                <!-- Disponibilité -->
                <c:if test="${not empty salle}">
                    <div class="flex items-center gap-3 p-3 bg-gray-50 rounded-md">
                        <input 
                            type="checkbox" 
                            id="disponible"
                            name="disponible" 
                            value="true"
                            ${salle.disponible ? 'checked' : ''}
                            class="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
                        >
                        <label for="disponible" class="text-sm text-gray-700 font-medium cursor-pointer">
                            Salle disponible pour réservation
                        </label>
                    </div>
                </c:if>

                <!-- Buttons -->
                <div class="flex gap-3 pt-4">
                    <button 
                        type="submit"
                        class="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-md py-2 transition"
                    >
                        ${empty salle ? 'Créer la salle' : 'Enregistrer les modifications'}
                    </button>
                    <a 
                        href="${pageContext.request.contextPath}/salles/list"
                        class="flex-1 text-center border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 font-medium rounded-md py-2 transition"
                    >
                        Annuler
                    </a>
                </div>
            </form>
        </div>

        <!-- Metadata (edit only) -->
        <c:if test="${not empty salle}">
            <div class="bg-gray-50 border border-gray-200 rounded-xl p-4">
                <h3 class="text-sm font-semibold text-gray-900 mb-2">Informations</h3>
                <div class="text-xs text-gray-600 space-y-1">
                    <p><strong>ID:</strong> #${salle.id}</p>
                    <p><strong>Créée le:</strong> ${salle.dateCreation}</p>
                    <c:if test="${not empty salle.dateModification}">
                        <p><strong>Dernière modification:</strong> ${salle.dateModification}</p>
                    </c:if>
                </div>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
