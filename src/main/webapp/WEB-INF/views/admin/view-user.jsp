<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Détails Utilisateur - E-Salle ENSAA"/>
</jsp:include>

<div class="flex-1 p-6">
    <div class="max-w-4xl mx-auto space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">${user.nom} ${user.prenom}</h1>
                <p class="text-gray-600 mt-2">Détails de l'utilisateur</p>
            </div>
            <a href="${pageContext.request.contextPath}/admin/users/all"
               class="px-4 py-2 rounded-md border border-gray-300 text-gray-700 hover:bg-gray-50">
                Retour
            </a>
        </div>

        <!-- User Details -->
        <div class="bg-white rounded-xl border border-gray-200 p-6 shadow-sm space-y-6">
            <div class="grid grid-cols-2 gap-6">
                <div>
                    <p class="text-sm text-gray-600">Email</p>
                    <p class="text-base font-medium text-gray-900">${user.email}</p>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Téléphone</p>
                    <p class="text-base font-medium text-gray-900">${user.telephone != null ? user.telephone : 'Non renseigné'}</p>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Rôle</p>
                    <p class="text-base font-medium text-gray-900 capitalize">${user.role}</p>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Statut</p>
                    <c:choose>
                        <c:when test="${user.statut.name() == 'ACTIF'}">
                            <span class="inline-block px-3 py-1 text-sm bg-green-100 text-green-800 rounded">Actif</span>
                        </c:when>
                        <c:when test="${user.statut.name() == 'EN_ATTENTE'}">
                            <span class="inline-block px-3 py-1 text-sm bg-yellow-100 text-yellow-800 rounded">En Attente</span>
                        </c:when>
                        <c:otherwise>
                            <span class="inline-block px-3 py-1 text-sm bg-red-100 text-red-800 rounded">Refusé</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Date d'inscription</p>
                    <p class="text-base font-medium text-gray-900">
                        <fmt:formatDate value="${user.dateInscription}" pattern="dd/MM/yyyy HH:mm"/>
                    </p>
                </div>
                <c:if test="${user.dateApprobation != null}">
                    <div>
                        <p class="text-sm text-gray-600">Date d'approbation</p>
                        <p class="text-base font-medium text-gray-900">
                            <fmt:formatDate value="${user.dateApprobation}" pattern="dd/MM/yyyy HH:mm"/>
                        </p>
                    </div>
                </c:if>
            </div>

            <!-- Actions pour utilisateur en attente -->
            <c:if test="${user.statut.name() == 'EN_ATTENTE'}">
                <div class="border-t border-gray-200 pt-6">
                    <h2 class="text-lg font-semibold text-gray-900 mb-4">Actions</h2>
                    <div class="flex gap-4">
                        <form method="POST" action="${pageContext.request.contextPath}/admin/users/approve" class="flex-1">
                            <input type="hidden" name="userId" value="${user.id}">
                            <c:if test="${user.role.name() == 'PROFESSEUR' || user.role.name() == 'MEMBRE_CLUB'}">
                                <div class="mb-2">
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Filière</label>
                                    <select name="filiereId" class="w-full rounded-md border border-gray-300 px-3 py-2">
                                        <option value="">Sélectionner filière</option>
                                        <c:forEach var="filiere" items="${filieres}">
                                            <option value="${filiere.id}">${filiere.nom}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </c:if>
                            <c:if test="${user.role.name() == 'MEMBRE_CLUB'}">
                                <div class="mb-2">
                                    <label class="block text-sm font-medium text-gray-700 mb-1">Nom du Club</label>
                                    <input type="text" name="nomClub" value="${user.nomClub}" 
                                           class="w-full rounded-md border border-gray-300 px-3 py-2">
                                </div>
                            </c:if>
                            <button type="submit"
                                    class="w-full px-4 py-2 rounded-md bg-green-600 text-white hover:bg-green-700">
                                Approuver
                            </button>
                        </form>
                        <form method="POST" action="${pageContext.request.contextPath}/admin/users/refuse" class="flex-1">
                            <input type="hidden" name="userId" value="${user.id}">
                            <button type="submit"
                                    onclick="return confirm('Êtes-vous sûr de vouloir refuser cet utilisateur ?')"
                                    class="w-full px-4 py-2 rounded-md bg-red-600 text-white hover:bg-red-700">
                                Refuser
                            </button>
                        </form>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>

