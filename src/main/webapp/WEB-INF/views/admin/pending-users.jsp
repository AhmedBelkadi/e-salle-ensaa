<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Administration - Utilisateurs en Attente"/>
</jsp:include>

<div class="flex-1 p-6">
    <div class="space-y-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Administration</h1>
                <p class="text-gray-600 mt-2">Approuver les inscriptions en attente</p>
                <p class="text-sm text-gray-500 mt-1">
                    <i class="fas fa-info-circle"></i> 
                    Vous pouvez accepter les <strong>Professeurs</strong> et <strong>Coordinateurs</strong> en leur assignant une filière, 
                    ou les <strong>Membres de Club</strong> en spécifiant le nom du club.
                </p>
            </div>
            <a href="${pageContext.request.contextPath}/admin/users/all" 
               class="px-4 py-2 rounded-md border border-gray-300 text-gray-700 hover:bg-gray-50">
                Tous les utilisateurs
            </a>
        </div>

        <!-- Success Message -->
        <c:if test="${param.success == 'approved'}">
            <div class="bg-green-50 border border-green-200 rounded-lg p-4">
                <p class="text-green-800">
                    <i class="fas fa-check-circle mr-2"></i>
                    Utilisateur approuvé avec succès
                </p>
            </div>
        </c:if>
        <c:if test="${param.success == 'refused'}">
            <div class="bg-red-50 border border-red-200 rounded-lg p-4">
                <p class="text-red-800">
                    <i class="fas fa-times-circle mr-2"></i>
                    Utilisateur refusé
                </p>
            </div>
        </c:if>
        
        <!-- Error Messages -->
        <c:if test="${not empty param.error}">
            <div class="bg-red-50 border border-red-200 rounded-lg p-4">
                <p class="text-red-800">
                    <i class="fas fa-exclamation-triangle mr-2"></i>
                    <c:choose>
                        <c:when test="${param.error == 'missing_filiere'}">
                            <strong>Erreur :</strong> La filière est obligatoire pour approuver un Professeur ou Coordinateur.
                        </c:when>
                        <c:when test="${param.error == 'missing_club_name'}">
                            <strong>Erreur :</strong> Le nom du club est obligatoire pour approuver un Membre de Club.
                        </c:when>
                        <c:when test="${param.error == 'missing_user_id'}">
                            <strong>Erreur :</strong> L'ID de l'utilisateur est manquant.
                        </c:when>
                        <c:when test="${param.error == 'user_not_found'}">
                            <strong>Erreur :</strong> Utilisateur introuvable.
                        </c:when>
                        <c:otherwise>
                            <strong>Erreur :</strong> ${param.error}
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
        </c:if>

        <!-- Pending Users List -->
        <c:choose>
            <c:when test="${empty pendingUsers}">
                <div class="bg-white rounded-xl border border-gray-200 py-12 text-center">
                    <p class="text-gray-500">Aucun utilisateur en attente d'approbation</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="space-y-4">
                    <c:forEach var="user" items="${pendingUsers}">
                        <div class="bg-white rounded-xl border border-gray-200 p-6 shadow-sm">
                            <div class="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-6">
                                <div class="flex-1 space-y-2">
                                    <div class="flex items-center gap-3">
                                        <div class="w-12 h-12 bg-blue-600 rounded-full flex items-center justify-center flex-shrink-0">
                                            <span class="text-white font-semibold">${user.nom.substring(0, 1)}${user.prenom.substring(0, 1)}</span>
                                        </div>
                                        <div>
                                            <h3 class="text-lg font-semibold text-gray-900">${user.nom} ${user.prenom}</h3>
                                            <p class="text-sm text-gray-600">${user.email}</p>
                                            <p class="text-xs text-gray-500 capitalize">${user.role}</p>
                                        </div>
                                    </div>
                                    
                                    <div class="ml-0 lg:ml-16 space-y-1 mt-3">
                                        <p class="text-sm text-gray-600">
                                            <span class="font-medium">Inscription:</span> 
                                            ${user.dateInscription}
                                        </p>
                                        <c:if test="${user.role.name() == 'PROFESSEUR' || user.role.name() == 'COORDINATEUR'}">
                                            <c:if test="${not empty user.filiereId}">
                                                <p class="text-sm text-gray-600">
                                                    <span class="font-medium">Filière demandée:</span> 
                                                    ${user.filiereId}
                                                </p>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${user.role.name() == 'MEMBRE_CLUB'}">
                                            <c:if test="${not empty user.nomClub}">
                                                <p class="text-sm text-gray-600">
                                                    <span class="font-medium">Club:</span> ${user.nomClub}
                                                </p>
                                            </c:if>
                                        </c:if>
                                    </div>
                                </div>
                                
                                <!-- Actions d'approbation - TOUJOURS VISIBLE -->
                                <div class="flex flex-col gap-3 w-full lg:w-auto lg:min-w-[280px] border-t-2 border-gray-300 lg:border-t-0 pt-4 lg:pt-0 bg-gray-50 lg:bg-transparent p-4 lg:p-0 rounded-lg">
                                    <h4 class="text-sm font-semibold text-gray-700 mb-2 lg:hidden">Actions</h4>
                                    <!-- Formulaire d'acceptation -->
                                    <form method="POST" action="${pageContext.request.contextPath}/admin/users/approve" class="w-full">
                                        <input type="hidden" name="userId" value="${user.id}">
                                        
                                        <!-- Champ filière pour COORDINATEUR uniquement -->
                                        <c:if test="${user.role.name() == 'COORDINATEUR'}">
                                            <div class="mb-3">
                                                <label class="block text-xs font-medium text-gray-700 mb-1">
                                                    <i class="fas fa-graduation-cap mr-1"></i>
                                                    Filière <span class="text-red-500">*</span>
                                                    <span class="text-gray-500 text-xs ml-1">
                                                        (sera assigné à toutes les années: Prépa 1, Prépa 2, DLA1, DLA2, DLA3)
                                                    </span>
                                                </label>
                                                <select name="filiereId" required 
                                                        class="w-full text-sm rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
                                                    <option value="">-- Sélectionner une filière --</option>
                                                    <%-- Pour les coordinateurs, afficher les noms uniques de filières --%>
                                                    <c:forEach var="entry" items="${nomsFilieresUniques}">
                                                        <option value="${entry.value}">${entry.key} (toutes les années)</option>
                                                    </c:forEach>
                                                </select>
                                                <c:if test="${empty filieres}">
                                                    <p class="text-xs text-red-600 mt-1">
                                                        <i class="fas fa-exclamation-triangle"></i> 
                                                        Aucune filière disponible. Veuillez créer une filière d'abord.
                                                    </p>
                                                </c:if>
                                            </div>
                                        </c:if>
                                        
                                        <!-- Message informatif pour PROFESSEUR -->
                                        <c:if test="${user.role.name() == 'PROFESSEUR'}">
                                            <div class="mb-3 p-3 bg-blue-50 border border-blue-200 rounded-md">
                                                <p class="text-xs text-blue-800">
                                                    <i class="fas fa-info-circle mr-1"></i>
                                                    Les professeurs peuvent enseigner dans toutes les filières. Aucune filière spécifique n'est requise.
                                                </p>
                                            </div>
                                        </c:if>
                                        
                                        <!-- Champ nom du club pour MEMBRE_CLUB -->
                                        <c:if test="${user.role.name() == 'MEMBRE_CLUB'}">
                                            <div class="mb-3">
                                                <label class="block text-xs font-medium text-gray-700 mb-1">
                                                    <i class="fas fa-users mr-1"></i>
                                                    Nom du club <span class="text-red-500">*</span>
                                                </label>
                                                <input type="text" name="nomClub" placeholder="Ex: Club Robotique, Club Informatique..." 
                                                       value="${user.nomClub}" 
                                                       required
                                                       class="w-full text-sm rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
                                            </div>
                                        </c:if>
                                        
                                        <!-- Bouton Accepter -->
                                        <button type="submit" 
                                                class="w-full inline-flex items-center justify-center gap-2 px-4 py-2.5 rounded-md bg-green-600 text-white hover:bg-green-700 font-medium text-sm transition-colors shadow-sm"
                                                <c:if test="${user.role.name() == 'COORDINATEUR' && empty filieres}">disabled title="Aucune filière disponible"</c:if>>
                                            <i class="fas fa-check-circle"></i>
                                            <c:choose>
                                                <c:when test="${user.role.name() == 'PROFESSEUR'}">
                                                    Accepter Professeur
                                                </c:when>
                                                <c:when test="${user.role.name() == 'COORDINATEUR'}">
                                                    Accepter Coordinateur
                                                </c:when>
                                                <c:otherwise>
                                                    Accepter
                                                </c:otherwise>
                                            </c:choose>
                                        </button>
                                    </form>
                                    
                                    <!-- Formulaire de refus -->
                                    <form method="POST" action="${pageContext.request.contextPath}/admin/users/refuse" class="w-full">
                                        <input type="hidden" name="userId" value="${user.id}">
                                        <button type="submit" 
                                                onclick="return confirm('Êtes-vous sûr de vouloir refuser cet utilisateur ? Cette action est irréversible.')"
                                                class="w-full inline-flex items-center justify-center gap-2 px-4 py-2.5 rounded-md bg-red-600 text-white hover:bg-red-700 font-medium text-sm transition-colors shadow-sm">
                                            <i class="fas fa-times-circle"></i>
                                            Refuser
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>

