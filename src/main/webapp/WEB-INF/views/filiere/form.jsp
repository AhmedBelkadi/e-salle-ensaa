<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${empty filiere ? 'Nouvelle' : 'Modifier'} Filière - E-Salle ENSAA"/>
</jsp:include>

<div class="p-6">
    <div class="space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">
                    ${empty filiere ? 'Nouvelle Filière' : 'Modifier Filière'}
                </h1>
                <p class="text-gray-600 mt-2">
                    ${empty filiere ? 'Créer une nouvelle filière' : 'Mettre à jour les informations'}
                </p>
            </div>
        </div>

        <!-- Messages -->
        <c:if test="${not empty error}">
            <div class="p-3 text-sm text-red-600 bg-red-50 border border-red-200 rounded-md">
                ${error}
            </div>
        </c:if>

        <!-- Info Box -->
        <c:if test="${empty filiere}">
            <div class="bg-blue-50 border border-blue-200 rounded-xl p-4">
                <div class="flex gap-3">
                    <i class="fas fa-info-circle w-5 h-5 text-blue-600 mt-0.5"></i>
                    <div class="text-sm text-blue-700">
                        <p class="font-semibold mb-2">Création automatique des filières</p>
                        <ul class="space-y-1 list-disc list-inside">
                            <li><strong>Préparatoire :</strong> 2 filières créées (1ère et 2ème année)</li>
                            <li><strong>Ingénieur :</strong> 3 filières créées (1ère, 2ème et 3ème année)</li>
                        </ul>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Form Card -->
        <div class="bg-white border border-gray-200 rounded-xl shadow-sm">
            <form method="post" action="${pageContext.request.contextPath}/filieres/save" class="p-6 space-y-4">
                
                <c:if test="${not empty filiere}">
                    <input type="hidden" name="id" value="${filiere.id}">
                </c:if>

                <!-- Nom -->
                <div class="space-y-2">
                    <label for="nom" class="text-gray-700 font-medium text-sm">
                        Nom de la filière <span class="text-red-600">*</span>
                    </label>
                    <input 
                        type="text" 
                        id="nom" 
                        name="nom" 
                        required
                        placeholder="Ex: Informatique, Génie Civil..."
                        value="${not empty filiere ? filiere.nom : nom}"
                        class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 placeholder:text-gray-500 text-sm outline-none"
                    >
                    <p class="text-xs text-gray-500">Le nom de base (sans cycle ni année)</p>
                </div>

                <!-- Cycle -->
                <div class="space-y-2">
                    <label for="cycle" class="text-gray-700 font-medium text-sm">
                        Cycle <span class="text-red-600">*</span>
                    </label>
                    <select 
                        id="cycle" 
                        name="cycle" 
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 text-sm outline-none"
                    >
                        <option value="">Sélectionner un cycle</option>
                        <option value="PREPARATOIRE" 
                                ${(not empty filiere && filiere.cycle == 'PREPARATOIRE') || cycle == 'PREPARATOIRE' ? 'selected' : ''}>
                            Préparatoire (2 ans)
                        </option>
                        <option value="INGENIEUR" 
                                ${(not empty filiere && filiere.cycle == 'INGENIEUR') || cycle == 'INGENIEUR' ? 'selected' : ''}>
                            Ingénieur (3 ans)
                        </option>
                    </select>
                </div>

                <!-- Effectif -->
                <div class="space-y-2">
                    <label for="effectif" class="text-gray-700 font-medium text-sm">
                        Effectif moyen par année <span class="text-red-600">*</span>
                    </label>
                    <input 
                        type="number" 
                        id="effectif" 
                        name="effectif" 
                        required
                        min="1"
                        placeholder="Ex: 50"
                        value="${not empty filiere ? filiere.effectif : effectif}"
                        class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 placeholder:text-gray-500 text-sm outline-none"
                    >
                    <p class="text-xs text-gray-500">Nombre d'étudiants par année</p>
                </div>

                <!-- Description -->
                <div class="space-y-2">
                    <label for="description" class="text-gray-700 font-medium text-sm">
                        Description
                    </label>
                    <textarea 
                        id="description" 
                        name="description" 
                        rows="4"
                        placeholder="Description de la filière..."
                        class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 placeholder:text-gray-500 text-sm outline-none resize-none"
                    >${not empty filiere ? filiere.description : description}</textarea>
                </div>

                <!-- Buttons -->
                <div class="flex gap-3 pt-4">
                    <button 
                        type="submit"
                        class="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-md py-2 transition"
                    >
                        ${empty filiere ? 'Créer la filière' : 'Enregistrer les modifications'}
                    </button>
                    <a 
                        href="${pageContext.request.contextPath}/filieres/list"
                        class="flex-1 text-center border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 font-medium rounded-md py-2 transition"
                    >
                        Annuler
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
