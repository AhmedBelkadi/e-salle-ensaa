<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Nouvelle Réclamation - E-Salle ENSAA"/>
</jsp:include>

<div class="p-6">
    <div class="max-w-3xl mx-auto space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Nouvelle Réclamation</h1>
                <p class="text-gray-600 mt-2">Signaler un problème d'équipement</p>
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
            <form method="post" action="${pageContext.request.contextPath}/reclamations/create" class="p-6 space-y-4">
                
                <!-- Salle -->
                <div class="space-y-2">
                    <label for="salleId" class="text-gray-700 font-medium text-sm">
                        Salle concernée <span class="text-red-600">*</span>
                    </label>
                    <select 
                        id="salleId"
                        name="salleId" 
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 text-sm outline-none"
                    >
                        <option value="">Sélectionner une salle</option>
                        <c:forEach var="salle" items="${salles}">
                            <option value="${salle.id}" ${salleId == salle.id ? 'selected' : ''}>
                                ${salle.nom} (${salle.type} - ${salle.capacite} places)
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Urgence -->
                <div class="space-y-2">
                    <label for="urgence" class="text-gray-700 font-medium text-sm">
                        Niveau d'urgence <span class="text-red-600">*</span>
                    </label>
                    <select 
                        id="urgence"
                        name="urgence" 
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 text-sm outline-none"
                    >
                        <option value="">Sélectionner le niveau</option>
                        <option value="FAIBLE" ${urgence == 'FAIBLE' ? 'selected' : ''}>🟢 Faible</option>
                        <option value="MOYEN" ${urgence == 'MOYEN' ? 'selected' : ''}>🟡 Moyen</option>
                        <option value="URGENT" ${urgence == 'URGENT' ? 'selected' : ''}>🔴 Urgent</option>
                    </select>
                </div>

                <!-- Description -->
                <div class="space-y-2">
                    <label for="description" class="text-gray-700 font-medium text-sm">
                        Description du problème <span class="text-red-600">*</span>
                    </label>
                    <textarea 
                        id="description"
                        name="description" 
                        rows="6" 
                        required
                        placeholder="Décrivez le problème en détail (minimum 10 caractères)..."
                        class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 placeholder:text-gray-500 text-sm outline-none resize-none"
                    >${description}</textarea>
                    <p class="text-xs text-gray-500">Soyez précis pour faciliter la résolution du problème</p>
                </div>

                <!-- Info Box -->
                <div class="bg-blue-50 border border-blue-200 rounded-md p-3">
                    <p class="text-sm text-blue-700 flex items-center gap-2">
                        <i class="fas fa-info-circle w-4 h-4"></i>
                        Vous serez notifié par email et WhatsApp lorsque votre réclamation sera traitée
                    </p>
                </div>

                <!-- Buttons -->
                <div class="flex gap-3 pt-4">
                    <button 
                        type="submit"
                        class="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-md py-2 transition"
                    >
                        Créer la réclamation
                    </button>
                    <a 
                        href="${pageContext.request.contextPath}/reclamations/list"
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
