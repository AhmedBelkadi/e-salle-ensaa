<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Tous les Utilisateurs - E-Salle ENSAA"/>
</jsp:include>

<div class="flex-1 p-6">
    <div class="space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Tous les Utilisateurs</h1>
                <p class="text-gray-600 mt-2">Gestion complète des utilisateurs</p>
            </div>
            <a href="${pageContext.request.contextPath}/admin/users/pending" 
               class="px-4 py-2 rounded-md border border-gray-300 text-gray-700 hover:bg-gray-50">
                En attente
            </a>
        </div>

        <!-- Users Table -->
        <div class="bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden">
            <div class="overflow-x-auto">
                <table class="min-w-full divide-y divide-gray-200">
                    <thead class="bg-gray-50">
                        <tr>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Nom</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Email</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Rôle</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Statut</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Inscription</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                        </tr>
                    </thead>
                    <tbody class="bg-white divide-y divide-gray-200">
                        <c:choose>
                            <c:when test="${empty users}">
                                <tr>
                                    <td colspan="6" class="px-6 py-12 text-center text-gray-500">
                                        Aucun utilisateur trouvé
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="user" items="${users}">
                                    <tr class="hover:bg-gray-50">
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <div class="flex items-center">
                                                <div class="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center mr-3">
                                                    <span class="text-white text-xs font-semibold">${user.nom.substring(0, 1)}${user.prenom.substring(0, 1)}</span>
                                                </div>
                                                <div>
                                                    <div class="text-sm font-medium text-gray-900">${user.nom} ${user.prenom}</div>
                                                </div>
                                            </div>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">${user.email}</td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <span class="px-2 py-1 text-xs rounded-full bg-blue-100 text-blue-800">
                                                ${user.role}
                                            </span>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <c:choose>
                                                <c:when test="${user.statut.name() == 'ACTIF'}">
                                                    <span class="px-2 py-1 text-xs rounded-full bg-green-100 text-green-800">Actif</span>
                                                </c:when>
                                                <c:when test="${user.statut.name() == 'EN_ATTENTE'}">
                                                    <span class="px-2 py-1 text-xs rounded-full bg-yellow-100 text-yellow-800">En Attente</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="px-2 py-1 text-xs rounded-full bg-red-100 text-red-800">Refusé</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                                            ${user.dateInscription}
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm">
                                            <a href="${pageContext.request.contextPath}/admin/users/view?id=${user.id}" 
                                               class="text-blue-600 hover:text-blue-800">Voir</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>

