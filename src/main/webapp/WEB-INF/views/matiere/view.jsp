<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${matiere.nom} - E-Salle ENSAA"/>
</jsp:include>

<div class="flex-1 p-6">
    <div class="space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">${matiere.nom}</h1>
                <p class="text-gray-600 mt-2">Détails de la matière</p>
            </div>
            <div class="flex gap-2">
                <a href="${pageContext.request.contextPath}/matieres/list"
                   class="px-4 py-2 rounded-md border border-gray-300 text-gray-700 hover:bg-gray-50">
                    Retour
                </a>
                <c:if test="${sessionScope.user.role.name() == 'COORDINATEUR'}">
                    <a href="${pageContext.request.contextPath}/matieres/edit?id=${matiere.id}"
                       class="px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700">
                        Modifier
                    </a>
                </c:if>
            </div>
        </div>

        <!-- Details Card -->
        <div class="bg-white rounded-xl border border-gray-200 p-6 shadow-sm space-y-6">
            <!-- Informations générales -->
            <div>
                <h2 class="text-lg font-semibold text-gray-900 mb-4">Informations générales</h2>
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <p class="text-sm text-gray-600">Filière</p>
                        <p class="text-base font-medium text-gray-900">${matiere.filiereNom}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Professeur</p>
                        <p class="text-base font-medium text-gray-900">${matiere.professeurNom}</p>
                    </div>
                </div>
            </div>

            <!-- Charges horaires -->
            <div>
                <h2 class="text-lg font-semibold text-gray-900 mb-4">Charges horaires</h2>
                <div class="grid grid-cols-4 gap-4">
                    <div class="text-center p-4 bg-blue-50 rounded-lg">
                        <p class="text-sm text-gray-600">Cours</p>
                        <p class="text-2xl font-bold text-blue-600">${matiere.heuresCours}h</p>
                    </div>
                    <div class="text-center p-4 bg-green-50 rounded-lg">
                        <p class="text-sm text-gray-600">TD</p>
                        <p class="text-2xl font-bold text-green-600">${matiere.heuresTD}h</p>
                    </div>
                    <div class="text-center p-4 bg-purple-50 rounded-lg">
                        <p class="text-sm text-gray-600">TP</p>
                        <p class="text-2xl font-bold text-purple-600">${matiere.heuresTP}h</p>
                    </div>
                    <div class="text-center p-4 bg-gray-50 rounded-lg">
                        <p class="text-sm text-gray-600">Total</p>
                        <p class="text-2xl font-bold text-gray-900">${matiere.totalHeures}h</p>
                    </div>
                </div>
            </div>

            <!-- Description -->
            <c:if test="${not empty matiere.description}">
                <div>
                    <h2 class="text-lg font-semibold text-gray-900 mb-2">Description</h2>
                    <p class="text-gray-700">${matiere.description}</p>
                </div>
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>

