<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${matiere.id != null ? 'Modifier Matière' : 'Nouvelle Matière'} - E-Salle ENSAA"/>
</jsp:include>

<div class="flex-1 p-6">
        <div class="space-y-6">
            <!-- Header -->
            <div>
                <h1 class="text-3xl font-bold text-gray-900">${matiere.id != null ? 'Modifier' : 'Nouvelle'} Matière</h1>
                <p class="text-gray-600 mt-2">Gérer les informations de la matière</p>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="bg-red-50 border border-red-200 rounded-lg p-4">
                    <p class="text-red-800">${error}</p>
                </div>
            </c:if>

            <!-- Form -->
            <form method="POST" action="${pageContext.request.contextPath}/matieres/save" 
                  class="bg-white rounded-xl border border-gray-200 p-6 shadow-sm space-y-6">
                
                <c:if test="${matiere.id != null}">
                    <input type="hidden" name="id" value="${matiere.id}">
                </c:if>

                <!-- Nom -->
                <div>
                    <label for="nom" class="block text-sm font-medium text-gray-700 mb-2">
                        Nom de la matière <span class="text-red-500">*</span>
                    </label>
                    <input type="text" name="nom" id="nom" required value="${matiere.nom}"
                           class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                </div>

                <!-- Filière -->
                <div>
                    <label for="filiereId" class="block text-sm font-medium text-gray-700 mb-2">
                        Filière <span class="text-red-500">*</span>
                    </label>
                    <select name="filiereId" id="filiereId" required
                            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">Sélectionner une filière</option>
                        <c:forEach var="filiere" items="${filieres}">
                            <option value="${filiere.id}" ${matiere.filiereId == filiere.id ? 'selected' : ''}>
                                ${filiere.nom} - ${filiere.cycle} ${filiere.annee}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Professeur -->
                <div>
                    <label for="professeurId" class="block text-sm font-medium text-gray-700 mb-2">
                        Professeur <span class="text-red-500">*</span>
                    </label>
                    <select name="professeurId" id="professeurId" required
                            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">Sélectionner un professeur</option>
                        <c:forEach var="prof" items="${professeurs}">
                            <option value="${prof.id}" ${matiere.professeurId == prof.id ? 'selected' : ''}>
                                ${prof.nom} ${prof.prenom}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Charges horaires -->
                <div class="grid grid-cols-3 gap-4">
                    <div>
                        <label for="heuresCours" class="block text-sm font-medium text-gray-700 mb-2">
                            Heures Cours
                        </label>
                        <input type="number" name="heuresCours" id="heuresCours" min="0" 
                               value="${matiere.heuresCours != null ? matiere.heuresCours : 0}"
                               class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                    <div>
                        <label for="heuresTD" class="block text-sm font-medium text-gray-700 mb-2">
                            Heures TD
                        </label>
                        <input type="number" name="heuresTD" id="heuresTD" min="0" 
                               value="${matiere.heuresTD != null ? matiere.heuresTD : 0}"
                               class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                    <div>
                        <label for="heuresTP" class="block text-sm font-medium text-gray-700 mb-2">
                            Heures TP
                        </label>
                        <input type="number" name="heuresTP" id="heuresTP" min="0" 
                               value="${matiere.heuresTP != null ? matiere.heuresTP : 0}"
                               class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                </div>

                <!-- Description -->
                <div>
                    <label for="description" class="block text-sm font-medium text-gray-700 mb-2">
                        Description
                    </label>
                    <textarea name="description" id="description" rows="4"
                              class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">${matiere.description}</textarea>
                </div>

                <!-- Buttons -->
                <div class="flex gap-4 justify-end">
                    <a href="${pageContext.request.contextPath}/matieres/list"
                       class="px-4 py-2 rounded-md border border-gray-300 text-gray-700 hover:bg-gray-50">
                        Annuler
                    </a>
                    <button type="submit"
                            class="px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700">
                        ${matiere.id != null ? 'Modifier' : 'Créer'}
                    </button>
                </div>
            </form>
        </div>
</div>

<jsp:include page="../common/footer.jsp"/>

