<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Matières - E-Salle ENSAA"/>
</jsp:include>

<div class="flex-1 p-6">
    <div class="space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Matières</h1>
                <p class="text-gray-600 mt-2">Gérer les matières académiques</p>
            </div>
            <c:if test="${sessionScope.user.role.name() == 'COORDINATEUR'}">
                <a href="${pageContext.request.contextPath}/matieres/new" 
                   class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-9 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white">
                    <i class="fas fa-plus w-4 h-4"></i>
                    Nouvelle Matière
                </a>
            </c:if>
        </div>

        <!-- Filters -->
        <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
            <div class="px-6 pt-0">
                <form method="get" action="${pageContext.request.contextPath}/matieres/list" class="flex gap-4 flex-col md:flex-row">
                    <div class="flex-1">
                        <input type="text" name="search" placeholder="Rechercher une matière..." 
                               value="${search}" 
                               class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                    <div>
                        <select name="filiereId" class="rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                            <option value="">Toutes les filières</option>
                            <c:forEach var="filiere" items="${filieres}">
                                <option value="${filiere.id}" ${selectedFiliereId == filiere.id ? 'selected' : ''}>
                                    ${filiere.nom} - ${filiere.cycle} ${filiere.annee}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <button type="submit" class="px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700">
                        <i class="fas fa-search"></i> Rechercher
                    </button>
                </form>
            </div>
        </div>

        <!-- Matières List -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <c:choose>
                <c:when test="${empty matieres}">
                    <div class="col-span-full bg-white rounded-xl border border-gray-200 py-12 text-center">
                        <p class="text-gray-500">Aucune matière trouvée</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="matiere" items="${matieres}">
                        <div class="bg-white rounded-xl border border-gray-200 p-6 shadow-sm hover:shadow-md transition">
                            <div class="space-y-4">
                                <div>
                                    <h3 class="text-lg font-semibold text-gray-900">${matiere.nom}</h3>
                                    <p class="text-sm text-gray-600">${matiere.filiereNom}</p>
                                    <p class="text-xs text-gray-500 mt-1">Prof. ${matiere.professeurNom}</p>
                                </div>
                                
                                <div class="grid grid-cols-3 gap-2 pt-4 border-t border-gray-200">
                                    <div class="text-center">
                                        <p class="text-xs text-gray-600">Cours</p>
                                        <p class="text-lg font-bold text-blue-600">${matiere.heuresCours}h</p>
                                    </div>
                                    <div class="text-center">
                                        <p class="text-xs text-gray-600">TD</p>
                                        <p class="text-lg font-bold text-green-600">${matiere.heuresTD}h</p>
                                    </div>
                                    <div class="text-center">
                                        <p class="text-xs text-gray-600">TP</p>
                                        <p class="text-lg font-bold text-purple-600">${matiere.heuresTP}h</p>
                                    </div>
                                </div>
                                
                                <div class="pt-2">
                                    <p class="text-sm text-gray-600">Total: <span class="font-semibold">${matiere.totalHeures}h</span></p>
                                </div>
                                
                                <div class="flex gap-2 pt-2">
                                    <a href="${pageContext.request.contextPath}/matieres/view?id=${matiere.id}" 
                                       class="flex-1 text-center px-3 py-2 text-sm bg-blue-50 text-blue-600 rounded-md hover:bg-blue-100">
                                        Voir
                                    </a>
                                    <c:if test="${sessionScope.user.role.name() == 'COORDINATEUR'}">
                                        <a href="${pageContext.request.contextPath}/matieres/edit?id=${matiere.id}" 
                                           class="flex-1 text-center px-3 py-2 text-sm bg-gray-50 text-gray-600 rounded-md hover:bg-gray-100">
                                            Modifier
                                        </a>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>

