<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Emploi du Temps - E-Salle ENSAA"/>
</jsp:include>

<div class="flex-1 p-6">
    <div class="space-y-6 max-w-7xl mx-auto">
        <!-- Header Section -->
        <div class="flex justify-between items-center">
            <div>
                <h1 class="text-3xl font-bold text-gray-900">Emploi du Temps</h1>
                <p class="text-gray-600 mt-2">
                    <c:choose>
                        <c:when test="${isTeachingView}">
                            <c:choose>
                                <c:when test="${isProfessor}">
                                    Mes horaires de cours
                                </c:when>
                                <c:when test="${isCoordinateur}">
                                    Mes cours en tant que professeur
                                </c:when>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            Créer et gérer les horaires des classes par filière
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
            <div class="flex gap-2 items-center">
                <c:if test="${isCoordinateur}">
                    <c:choose>
                        <c:when test="${viewMode == 'teach'}">
                            <a href="${pageContext.request.contextPath}/emploi/list?viewMode=manage${not empty selectedFiliereId ? '&filiereId='.concat(selectedFiliereId) : ''}${not empty selectedAnnee ? '&annee='.concat(selectedAnnee) : '&annee=1'}" 
                               class="gap-2 bg-gray-600 hover:bg-gray-700 text-white px-4 py-2 rounded-md flex items-center">
                                <i class="fas fa-cog w-4 h-4"></i>
                                Mode Gestion
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/emploi/list?viewMode=teach" 
                               class="gap-2 bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-md flex items-center">
                                <i class="fas fa-chalkboard-teacher w-4 h-4"></i>
                                Mes Cours
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <c:if test="${canEdit}">
                    <button id="addScheduleBtn" class="gap-2 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md flex items-center">
                        <i class="fas fa-plus w-4 h-4"></i>
                        Ajouter Séance
                    </button>
                </c:if>
            </div>
        </div>

        <!-- Message si coordinateur sans filières (seulement en mode gestion) -->
        <c:if test="${isCoordinateur && !isTeachingView && (empty filieres || filieres.size() == 0)}">
            <div class="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                <div class="flex items-center gap-3">
                    <i class="fas fa-exclamation-triangle text-yellow-600"></i>
                    <div>
                        <h3 class="font-semibold text-yellow-900">Aucune filière assignée</h3>
                        <p class="text-sm text-yellow-700 mt-1">
                            Vous n'avez pas encore de filière assignée. Veuillez contacter l'administrateur pour obtenir une filière.
                        </p>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Filters (only for admin/coordinateur in manage mode, NOT for professeur or teaching view) -->
        <c:if test="${(canEdit || isAdmin) && !isProfessor && !isTeachingView}">
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <form method="get" action="${pageContext.request.contextPath}/emploi/list" class="flex gap-4 flex-col md:flex-row">
                        <input type="hidden" name="viewMode" value="manage">
                        <c:if test="${isAdmin}">
                            <div>
                                <label class="block text-sm font-medium text-gray-700 mb-1">Filière</label>
                                <select name="filiereId" class="rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                                    <option value="">Toutes les filières</option>
                                    <c:forEach var="filiere" items="${filieres}">
                                        <option value="${filiere.id}" ${selectedFiliereId == filiere.id ? 'selected' : ''}>
                                            ${filiere.nom} - ${filiere.cycle} ${filiere.annee}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </c:if>
                        <c:if test="${isCoordinateur && not empty filieres}">
                            <div>
                                <label class="block text-sm font-medium text-gray-700 mb-1">Filière</label>
                                <select name="filiereId" class="rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                                    <c:forEach var="filiere" items="${filieres}" varStatus="status">
                                        <option value="${filiere.id}" ${(selectedFiliereId == filiere.id || (empty selectedFiliereId && status.first)) ? 'selected' : ''}>
                                            ${filiere.nom} - ${filiere.cycle} ${filiere.annee}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </c:if>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Groupe</label>
                            <select name="groupe" class="rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                                <option value="">Tous les groupes</option>
                                <option value="1" ${selectedGroupe == '1' ? 'selected' : ''}>Groupe 1</option>
                                <option value="2" ${selectedGroupe == '2' ? 'selected' : ''}>Groupe 2</option>
                            </select>
                        </div>
                        <div class="flex items-end">
                            <button type="submit" class="px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700">
                                <i class="fas fa-filter"></i> Filtrer
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </c:if>

        <!-- Modal for Adding Schedule -->
        <c:if test="${canEdit}">
            <!-- Modal Overlay -->
            <div id="scheduleModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 hidden z-50 flex items-center justify-center">
                <div class="bg-white rounded-lg shadow-xl max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
                    <!-- Modal Header -->
                    <div class="p-6 border-b border-gray-200 flex items-center justify-between">
                        <div>
                            <h2 class="text-xl font-semibold text-gray-900">Ajouter une Nouvelle Séance</h2>
                            <p id="selectedInfo" class="text-gray-600 mt-1 text-sm"></p>
                        </div>
                        <button type="button" id="closeModalBtn" class="text-gray-400 hover:text-gray-600">
                            <i class="fas fa-times w-5 h-5"></i>
                        </button>
                    </div>
                    <!-- Modal Body -->
                    <div class="p-6">
                        <form method="POST" action="${pageContext.request.contextPath}/emploi/save" class="space-y-4">
                            <input type="hidden" name="filiereId" id="formFiliereId" required 
                                   value="${isCoordinateur && not empty filieres ? (not empty selectedFiliereId ? selectedFiliereId : filieres[0].id) : ''}">
                            <input type="hidden" name="annee" id="formAnnee" 
                                   value="${isCoordinateur && not empty filieres ? (not empty selectedFiliereId && not empty selectedAnnee ? selectedAnnee : filieres[0].annee) : '1'}">
                            <input type="hidden" name="jourSemaine" id="formJourSemaine" required>
                            <input type="hidden" name="heureDebut" id="formHeureDebut" required>
                            <input type="hidden" name="heureFin" id="formHeureFin" required>
                            
                            <!-- Filière selector (only for admin) -->
                            <c:if test="${isAdmin}">
                                <div>
                                    <label class="text-gray-700 font-medium block mb-2">Filière <span class="text-red-500">*</span></label>
                                    <select name="filiereId" id="branchSelect" required onchange="filterMatieresByFiliere()"
                                            class="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded text-gray-900 focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500">
                                        <option value="">Sélectionner une filière</option>
                                        <c:forEach var="filiere" items="${filieres}">
                                            <option value="${filiere.id}" data-annee="${filiere.annee}">${filiere.nom} - ${filiere.cycle} ${filiere.annee}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </c:if>
                            
                            <!-- Day and Time (hidden when clicking on slot) -->
                            <div id="dayTimeSelectors" class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div>
                                    <label class="text-gray-700 font-medium block mb-2">Jour <span class="text-red-500">*</span></label>
                                    <select id="daySelect" required
                                            class="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded text-gray-900 focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500">
                                        <option value="">Sélectionner un jour</option>
                                        <option value="LUNDI">Lundi</option>
                                        <option value="MARDI">Mardi</option>
                                        <option value="MERCREDI">Mercredi</option>
                                        <option value="JEUDI">Jeudi</option>
                                        <option value="VENDREDI">Vendredi</option>
                                        <option value="SAMEDI">Samedi</option>
                                    </select>
                                </div>
                                <div>
                                    <label class="text-gray-700 font-medium block mb-2">Créneau Horaire <span class="text-red-500">*</span></label>
                                    <select id="timeSlotSelect" required
                                            class="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded text-gray-900 focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500">
                                        <option value="">Sélectionner un créneau</option>
                                        <option value="08:30-10:30">08:30-10:30</option>
                                        <option value="10:30-12:30">10:30-12:30</option>
                                        <option value="14:30-16:30">14:30-16:30</option>
                                        <option value="16:30-18:30">16:30-18:30</option>
                                        <option value="18:30-20:30">18:30-20:30</option>
                                    </select>
                                </div>
                            </div>
                            
                            <div>
                                <label class="text-gray-700 font-medium block mb-2">Matière <span class="text-red-500">*</span></label>
                                <select name="matiereId" id="subjectSelect" required onchange="updateProfesseur()"
                                        class="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded text-gray-900 focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500">
                                    <option value="">Sélectionner une matière</option>
                                    <c:forEach var="matiere" items="${matieres}">
                                        <option value="${matiere.id}" 
                                                data-filiere="${matiere.filiereId}" 
                                                data-prof="${matiere.professeurId}"
                                                data-prof-nom="${matiere.professeurNom}">${matiere.nom}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <div>
                                <label class="text-gray-700 font-medium block mb-2">Professeur <span class="text-red-500">*</span></label>
                                <input type="hidden" name="professeurId" id="professorId" required>
                                <input type="text" id="professorNom" readonly
                                       class="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded text-gray-900"
                                       placeholder="Sélectionnez une matière pour voir le professeur">
                            </div>
                            
                            <div>
                                <label class="text-gray-700 font-medium block mb-2">Salle <span class="text-red-500">*</span></label>
                                <select name="salleId" id="salleSelect" required
                                        class="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded text-gray-900 focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500">
                                    <option value="">Sélectionner une salle</option>
                                    <c:forEach var="salle" items="${salles}">
                                        <option value="${salle.id}">${salle.nom} (${salle.type})</option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <div>
                                <label class="text-gray-700 font-medium block mb-2">Type de Séance <span class="text-red-500">*</span></label>
                                <select name="typeSeance" id="typeSeanceSelect" required
                                        class="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded text-gray-900 focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500">
                                    <option value="COURS">Cours</option>
                                    <option value="TD">TD</option>
                                    <option value="TP">TP</option>
                                </select>
                            </div>
                            
                            <div>
                                <label class="text-gray-700 font-medium block mb-2">Groupe (optionnel)</label>
                                <select name="groupe" id="groupeSelect"
                                        class="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded text-gray-900 focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500">
                                    <option value="">Tous les groupes</option>
                                    <option value="1">Groupe 1</option>
                                    <option value="2">Groupe 2</option>
                                </select>
                            </div>
                            
                            <div class="flex gap-2 justify-end pt-4 border-t border-gray-200">
                                <button type="button" id="cancelFormBtn" class="border border-gray-300 text-gray-700 hover:bg-gray-50 px-4 py-2 rounded-md">
                                    Annuler
                                </button>
                                <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md">
                                    Ajouter Séance
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Timetable Schedule Table -->
        <div class="bg-white border border-gray-200 rounded-lg shadow-sm">
            <div class="p-6 border-b border-gray-200">
                <h2 class="text-xl font-semibold text-gray-900">
                    <c:choose>
                        <c:when test="${isTeachingView}">
                            <c:choose>
                                <c:when test="${isProfessor}">
                                    Mon Emploi du Temps - Professeur
                                </c:when>
                                <c:when test="${isCoordinateur}">
                                    Mon Emploi du Temps - Enseignement
                                </c:when>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            Emploi du Temps - ${not empty selectedAnnee ? selectedAnnee.concat('ère année') : '1ère année'}
                            <c:if test="${not empty selectedFiliereId}">
                                <c:forEach var="filiere" items="${filieres}">
                                    <c:if test="${filiere.id == selectedFiliereId}">
                                        - ${filiere.nom}
                                    </c:if>
                                </c:forEach>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </h2>
                <p class="text-gray-600">
                    <c:choose>
                        <c:when test="${isTeachingView}">
                            Vos cours en tant que professeur
                        </c:when>
                        <c:when test="${canEdit}">
                            Cliquez sur une cellule vide pour ajouter une matière
                        </c:when>
                        <c:otherwise>
                            Emploi du temps filtré par année ${not empty selectedAnnee ? selectedAnnee : '1'}
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
            <div class="p-6">
                <div class="overflow-x-auto">
                    <table class="w-full border-collapse">
                        <thead>
                            <tr class="border-b border-gray-200">
                                <th class="text-gray-700 font-medium p-3 text-left w-20">Heure</th>
                                <th class="text-gray-700 font-medium p-3 text-center">Lundi</th>
                                <th class="text-gray-700 font-medium p-3 text-center">Mardi</th>
                                <th class="text-gray-700 font-medium p-3 text-center">Mercredi</th>
                                <th class="text-gray-700 font-medium p-3 text-center">Jeudi</th>
                                <th class="text-gray-700 font-medium p-3 text-center">Vendredi</th>
                                <th class="text-gray-700 font-medium p-3 text-center">Samedi</th>
                            </tr>
                        </thead>
                        <tbody id="timetableBody">
                            <!-- Time Slot 1: 08:30-10:30 -->
                            <tr class="border-b border-gray-200">
                                <td class="text-gray-700 font-medium p-3">08:30-10:30</td>
                                <c:forEach var="jour" items="${['LUNDI', 'MARDI', 'MERCREDI', 'JEUDI', 'VENDREDI', 'SAMEDI']}" varStatus="jourIdx">
                                    <c:set var="key" value="${jour}_08:30-10:30" />
                                    <c:set var="seances" value="${calendarData[key]}" />
                                    <td class="p-2 h-20 border border-gray-200 ${canEdit ? 'hover:bg-gray-50 cursor-pointer' : ''} transition-colors text-center timetable-cell" 
                                        data-day="${jour}" 
                                        data-time="08:30-10:30"
                                        data-heure-debut="08:30"
                                        data-heure-fin="10:30">
                                        <c:choose>
                                            <c:when test="${empty seances}">
                                                <c:if test="${canEdit}">
                                                    <div class="text-gray-400 text-sm h-full flex items-center justify-center hover:text-gray-600">
                                                        <i class="fas fa-plus w-4 h-4"></i>
                                                    </div>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="seance" items="${seances}">
                                                    <c:set var="colorClass" value="${jourIdx.index == 0 ? 'blue' : jourIdx.index == 1 ? 'green' : jourIdx.index == 2 ? 'purple' : jourIdx.index == 3 ? 'yellow' : jourIdx.index == 4 ? 'pink' : 'indigo'}" />
                                                    <div class="rounded p-2 h-full flex flex-col justify-between mb-1
                                                        ${colorClass == 'blue' ? 'bg-blue-50 border border-blue-200' : ''}
                                                        ${colorClass == 'green' ? 'bg-green-50 border border-green-200' : ''}
                                                        ${colorClass == 'purple' ? 'bg-purple-50 border border-purple-200' : ''}
                                                        ${colorClass == 'yellow' ? 'bg-yellow-50 border border-yellow-200' : ''}
                                                        ${colorClass == 'pink' ? 'bg-pink-50 border border-pink-200' : ''}
                                                        ${colorClass == 'indigo' ? 'bg-indigo-50 border border-indigo-200' : ''}">
                                                        <div class="font-medium text-sm
                                                            ${colorClass == 'blue' ? 'text-blue-900' : ''}
                                                            ${colorClass == 'green' ? 'text-green-900' : ''}
                                                            ${colorClass == 'purple' ? 'text-purple-900' : ''}
                                                            ${colorClass == 'yellow' ? 'text-yellow-900' : ''}
                                                            ${colorClass == 'pink' ? 'text-pink-900' : ''}
                                                            ${colorClass == 'indigo' ? 'text-indigo-900' : ''}">${seance.matiereNom}</div>
                                                        <div class="text-xs
                                                            ${colorClass == 'blue' ? 'text-blue-700' : ''}
                                                            ${colorClass == 'green' ? 'text-green-700' : ''}
                                                            ${colorClass == 'purple' ? 'text-purple-700' : ''}
                                                            ${colorClass == 'yellow' ? 'text-yellow-700' : ''}
                                                            ${colorClass == 'pink' ? 'text-pink-700' : ''}
                                                            ${colorClass == 'indigo' ? 'text-indigo-700' : ''}">${seance.professeurNom}</div>
                                                        <c:if test="${not empty seance.salleNom}">
                                                            <div class="text-xs
                                                                ${colorClass == 'blue' ? 'text-blue-700' : ''}
                                                                ${colorClass == 'green' ? 'text-green-700' : ''}
                                                                ${colorClass == 'purple' ? 'text-purple-700' : ''}
                                                                ${colorClass == 'yellow' ? 'text-yellow-700' : ''}
                                                                ${colorClass == 'pink' ? 'text-pink-700' : ''}
                                                                ${colorClass == 'indigo' ? 'text-indigo-700' : ''}">${seance.salleNom}</div>
                                                        </c:if>
                                                        <c:if test="${canEdit}">
                                                            <div class="flex gap-1 mt-1 justify-end">
                                                                <a href="${pageContext.request.contextPath}/emploi/new?id=${seance.id}" 
                                                                   class="h-6 w-6 p-0 rounded flex items-center justify-center
                                                                   ${colorClass == 'blue' ? 'text-blue-600 hover:text-blue-800 hover:bg-blue-100' : ''}
                                                                   ${colorClass == 'green' ? 'text-green-600 hover:text-green-800 hover:bg-green-100' : ''}
                                                                   ${colorClass == 'purple' ? 'text-purple-600 hover:text-purple-800 hover:bg-purple-100' : ''}
                                                                   ${colorClass == 'yellow' ? 'text-yellow-600 hover:text-yellow-800 hover:bg-yellow-100' : ''}
                                                                   ${colorClass == 'pink' ? 'text-pink-600 hover:text-pink-800 hover:bg-pink-100' : ''}
                                                                   ${colorClass == 'indigo' ? 'text-indigo-600 hover:text-indigo-800 hover:bg-indigo-100' : ''}">
                                                                    <i class="fas fa-edit w-3 h-3"></i>
                                                                </a>
                                                                <a href="${pageContext.request.contextPath}/emploi/delete?id=${seance.id}" 
                                                                   onclick="return confirm('Êtes-vous sûr ?')"
                                                                   class="h-6 w-6 p-0 text-red-600 hover:text-red-800 hover:bg-red-100 rounded flex items-center justify-center">
                                                                    <i class="fas fa-trash w-3 h-3"></i>
                                                                </a>
                                                            </div>
                                                        </c:if>
                                                    </div>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:forEach>
                            </tr>
                            
                            <!-- Time Slot 2: 10:30-12:30 -->
                            <tr class="border-b border-gray-200">
                                <td class="text-gray-700 font-medium p-3">10:30-12:30</td>
                                <c:forEach var="jour" items="${['LUNDI', 'MARDI', 'MERCREDI', 'JEUDI', 'VENDREDI', 'SAMEDI']}" varStatus="jourIdx">
                                    <c:set var="key" value="${jour}_10:30-12:30" />
                                    <c:set var="seances" value="${calendarData[key]}" />
                                    <td class="p-2 h-20 border border-gray-200 ${canEdit ? 'hover:bg-gray-50 cursor-pointer' : ''} transition-colors text-center timetable-cell" 
                                        data-day="${jour}" 
                                        data-time="10:30-12:30"
                                        data-heure-debut="10:30"
                                        data-heure-fin="12:30">
                                        <c:choose>
                                            <c:when test="${empty seances}">
                                                <c:if test="${canEdit}">
                                                    <div class="text-gray-400 text-sm h-full flex items-center justify-center hover:text-gray-600">
                                                        <i class="fas fa-plus w-4 h-4"></i>
                                                    </div>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="seance" items="${seances}">
                                                    <c:set var="colorClass" value="${jourIdx.index == 0 ? 'blue' : jourIdx.index == 1 ? 'green' : jourIdx.index == 2 ? 'purple' : jourIdx.index == 3 ? 'yellow' : jourIdx.index == 4 ? 'pink' : 'indigo'}" />
                                                    <div class="rounded p-2 h-full flex flex-col justify-between mb-1
                                                        ${colorClass == 'blue' ? 'bg-blue-50 border border-blue-200' : ''}
                                                        ${colorClass == 'green' ? 'bg-green-50 border border-green-200' : ''}
                                                        ${colorClass == 'purple' ? 'bg-purple-50 border border-purple-200' : ''}
                                                        ${colorClass == 'yellow' ? 'bg-yellow-50 border border-yellow-200' : ''}
                                                        ${colorClass == 'pink' ? 'bg-pink-50 border border-pink-200' : ''}
                                                        ${colorClass == 'indigo' ? 'bg-indigo-50 border border-indigo-200' : ''}">
                                                        <div class="font-medium text-sm
                                                            ${colorClass == 'blue' ? 'text-blue-900' : ''}
                                                            ${colorClass == 'green' ? 'text-green-900' : ''}
                                                            ${colorClass == 'purple' ? 'text-purple-900' : ''}
                                                            ${colorClass == 'yellow' ? 'text-yellow-900' : ''}
                                                            ${colorClass == 'pink' ? 'text-pink-900' : ''}
                                                            ${colorClass == 'indigo' ? 'text-indigo-900' : ''}">${seance.matiereNom}</div>
                                                        <div class="text-xs
                                                            ${colorClass == 'blue' ? 'text-blue-700' : ''}
                                                            ${colorClass == 'green' ? 'text-green-700' : ''}
                                                            ${colorClass == 'purple' ? 'text-purple-700' : ''}
                                                            ${colorClass == 'yellow' ? 'text-yellow-700' : ''}
                                                            ${colorClass == 'pink' ? 'text-pink-700' : ''}
                                                            ${colorClass == 'indigo' ? 'text-indigo-700' : ''}">${seance.professeurNom}</div>
                                                        <c:if test="${not empty seance.salleNom}">
                                                            <div class="text-xs
                                                                ${colorClass == 'blue' ? 'text-blue-700' : ''}
                                                                ${colorClass == 'green' ? 'text-green-700' : ''}
                                                                ${colorClass == 'purple' ? 'text-purple-700' : ''}
                                                                ${colorClass == 'yellow' ? 'text-yellow-700' : ''}
                                                                ${colorClass == 'pink' ? 'text-pink-700' : ''}
                                                                ${colorClass == 'indigo' ? 'text-indigo-700' : ''}">${seance.salleNom}</div>
                                                        </c:if>
                                                        <c:if test="${canEdit}">
                                                            <div class="flex gap-1 mt-1 justify-end">
                                                                <a href="${pageContext.request.contextPath}/emploi/new?id=${seance.id}" 
                                                                   class="h-6 w-6 p-0 rounded flex items-center justify-center
                                                                   ${colorClass == 'blue' ? 'text-blue-600 hover:text-blue-800 hover:bg-blue-100' : ''}
                                                                   ${colorClass == 'green' ? 'text-green-600 hover:text-green-800 hover:bg-green-100' : ''}
                                                                   ${colorClass == 'purple' ? 'text-purple-600 hover:text-purple-800 hover:bg-purple-100' : ''}
                                                                   ${colorClass == 'yellow' ? 'text-yellow-600 hover:text-yellow-800 hover:bg-yellow-100' : ''}
                                                                   ${colorClass == 'pink' ? 'text-pink-600 hover:text-pink-800 hover:bg-pink-100' : ''}
                                                                   ${colorClass == 'indigo' ? 'text-indigo-600 hover:text-indigo-800 hover:bg-indigo-100' : ''}">
                                                                    <i class="fas fa-edit w-3 h-3"></i>
                                                                </a>
                                                                <a href="${pageContext.request.contextPath}/emploi/delete?id=${seance.id}" 
                                                                   onclick="return confirm('Êtes-vous sûr ?')"
                                                                   class="h-6 w-6 p-0 text-red-600 hover:text-red-800 hover:bg-red-100 rounded flex items-center justify-center">
                                                                    <i class="fas fa-trash w-3 h-3"></i>
                                                                </a>
                                                            </div>
                                                        </c:if>
                                                    </div>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:forEach>
                            </tr>
                            
                            <!-- Time Slot 3: 14:30-16:30 -->
                            <tr class="border-b border-gray-200">
                                <td class="text-gray-700 font-medium p-3">14:30-16:30</td>
                                <c:forEach var="jour" items="${['LUNDI', 'MARDI', 'MERCREDI', 'JEUDI', 'VENDREDI', 'SAMEDI']}" varStatus="jourIdx">
                                    <c:set var="key" value="${jour}_14:30-16:30" />
                                    <c:set var="seances" value="${calendarData[key]}" />
                                    <td class="p-2 h-20 border border-gray-200 ${canEdit ? 'hover:bg-gray-50 cursor-pointer' : ''} transition-colors text-center timetable-cell" 
                                        data-day="${jour}" 
                                        data-time="14:30-16:30"
                                        data-heure-debut="14:30"
                                        data-heure-fin="16:30">
                                        <c:choose>
                                            <c:when test="${empty seances}">
                                                <c:if test="${canEdit}">
                                                    <div class="text-gray-400 text-sm h-full flex items-center justify-center hover:text-gray-600">
                                                        <i class="fas fa-plus w-4 h-4"></i>
                                                    </div>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="seance" items="${seances}">
                                                    <c:set var="colorClass" value="${jourIdx.index == 0 ? 'blue' : jourIdx.index == 1 ? 'green' : jourIdx.index == 2 ? 'purple' : jourIdx.index == 3 ? 'yellow' : jourIdx.index == 4 ? 'pink' : 'indigo'}" />
                                                    <div class="rounded p-2 h-full flex flex-col justify-between mb-1
                                                        ${colorClass == 'blue' ? 'bg-blue-50 border border-blue-200' : ''}
                                                        ${colorClass == 'green' ? 'bg-green-50 border border-green-200' : ''}
                                                        ${colorClass == 'purple' ? 'bg-purple-50 border border-purple-200' : ''}
                                                        ${colorClass == 'yellow' ? 'bg-yellow-50 border border-yellow-200' : ''}
                                                        ${colorClass == 'pink' ? 'bg-pink-50 border border-pink-200' : ''}
                                                        ${colorClass == 'indigo' ? 'bg-indigo-50 border border-indigo-200' : ''}">
                                                        <div class="font-medium text-sm
                                                            ${colorClass == 'blue' ? 'text-blue-900' : ''}
                                                            ${colorClass == 'green' ? 'text-green-900' : ''}
                                                            ${colorClass == 'purple' ? 'text-purple-900' : ''}
                                                            ${colorClass == 'yellow' ? 'text-yellow-900' : ''}
                                                            ${colorClass == 'pink' ? 'text-pink-900' : ''}
                                                            ${colorClass == 'indigo' ? 'text-indigo-900' : ''}">${seance.matiereNom}</div>
                                                        <div class="text-xs
                                                            ${colorClass == 'blue' ? 'text-blue-700' : ''}
                                                            ${colorClass == 'green' ? 'text-green-700' : ''}
                                                            ${colorClass == 'purple' ? 'text-purple-700' : ''}
                                                            ${colorClass == 'yellow' ? 'text-yellow-700' : ''}
                                                            ${colorClass == 'pink' ? 'text-pink-700' : ''}
                                                            ${colorClass == 'indigo' ? 'text-indigo-700' : ''}">${seance.professeurNom}</div>
                                                        <c:if test="${not empty seance.salleNom}">
                                                            <div class="text-xs
                                                                ${colorClass == 'blue' ? 'text-blue-700' : ''}
                                                                ${colorClass == 'green' ? 'text-green-700' : ''}
                                                                ${colorClass == 'purple' ? 'text-purple-700' : ''}
                                                                ${colorClass == 'yellow' ? 'text-yellow-700' : ''}
                                                                ${colorClass == 'pink' ? 'text-pink-700' : ''}
                                                                ${colorClass == 'indigo' ? 'text-indigo-700' : ''}">${seance.salleNom}</div>
                                                        </c:if>
                                                        <c:if test="${canEdit}">
                                                            <div class="flex gap-1 mt-1 justify-end">
                                                                <a href="${pageContext.request.contextPath}/emploi/new?id=${seance.id}" 
                                                                   class="h-6 w-6 p-0 rounded flex items-center justify-center
                                                                   ${colorClass == 'blue' ? 'text-blue-600 hover:text-blue-800 hover:bg-blue-100' : ''}
                                                                   ${colorClass == 'green' ? 'text-green-600 hover:text-green-800 hover:bg-green-100' : ''}
                                                                   ${colorClass == 'purple' ? 'text-purple-600 hover:text-purple-800 hover:bg-purple-100' : ''}
                                                                   ${colorClass == 'yellow' ? 'text-yellow-600 hover:text-yellow-800 hover:bg-yellow-100' : ''}
                                                                   ${colorClass == 'pink' ? 'text-pink-600 hover:text-pink-800 hover:bg-pink-100' : ''}
                                                                   ${colorClass == 'indigo' ? 'text-indigo-600 hover:text-indigo-800 hover:bg-indigo-100' : ''}">
                                                                    <i class="fas fa-edit w-3 h-3"></i>
                                                                </a>
                                                                <a href="${pageContext.request.contextPath}/emploi/delete?id=${seance.id}" 
                                                                   onclick="return confirm('Êtes-vous sûr ?')"
                                                                   class="h-6 w-6 p-0 text-red-600 hover:text-red-800 hover:bg-red-100 rounded flex items-center justify-center">
                                                                    <i class="fas fa-trash w-3 h-3"></i>
                                                                </a>
                                                            </div>
                                                        </c:if>
                                                    </div>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:forEach>
                            </tr>
                            
                            <!-- Time Slot 4: 16:30-18:30 -->
                            <tr class="border-b border-gray-200">
                                <td class="text-gray-700 font-medium p-3">16:30-18:30</td>
                                <c:forEach var="jour" items="${['LUNDI', 'MARDI', 'MERCREDI', 'JEUDI', 'VENDREDI', 'SAMEDI']}" varStatus="jourIdx">
                                    <c:set var="key" value="${jour}_16:30-18:30" />
                                    <c:set var="seances" value="${calendarData[key]}" />
                                    <td class="p-2 h-20 border border-gray-200 ${canEdit ? 'hover:bg-gray-50 cursor-pointer' : ''} transition-colors text-center timetable-cell" 
                                        data-day="${jour}" 
                                        data-time="16:30-18:30"
                                        data-heure-debut="16:30"
                                        data-heure-fin="18:30">
                                        <c:choose>
                                            <c:when test="${empty seances}">
                                                <c:if test="${canEdit}">
                                                    <div class="text-gray-400 text-sm h-full flex items-center justify-center hover:text-gray-600">
                                                        <i class="fas fa-plus w-4 h-4"></i>
                                                    </div>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="seance" items="${seances}">
                                                    <c:set var="colorClass" value="${jourIdx.index == 0 ? 'blue' : jourIdx.index == 1 ? 'green' : jourIdx.index == 2 ? 'purple' : jourIdx.index == 3 ? 'yellow' : jourIdx.index == 4 ? 'pink' : 'indigo'}" />
                                                    <div class="rounded p-2 h-full flex flex-col justify-between mb-1
                                                        ${colorClass == 'blue' ? 'bg-blue-50 border border-blue-200' : ''}
                                                        ${colorClass == 'green' ? 'bg-green-50 border border-green-200' : ''}
                                                        ${colorClass == 'purple' ? 'bg-purple-50 border border-purple-200' : ''}
                                                        ${colorClass == 'yellow' ? 'bg-yellow-50 border border-yellow-200' : ''}
                                                        ${colorClass == 'pink' ? 'bg-pink-50 border border-pink-200' : ''}
                                                        ${colorClass == 'indigo' ? 'bg-indigo-50 border border-indigo-200' : ''}">
                                                        <div class="font-medium text-sm
                                                            ${colorClass == 'blue' ? 'text-blue-900' : ''}
                                                            ${colorClass == 'green' ? 'text-green-900' : ''}
                                                            ${colorClass == 'purple' ? 'text-purple-900' : ''}
                                                            ${colorClass == 'yellow' ? 'text-yellow-900' : ''}
                                                            ${colorClass == 'pink' ? 'text-pink-900' : ''}
                                                            ${colorClass == 'indigo' ? 'text-indigo-900' : ''}">${seance.matiereNom}</div>
                                                        <div class="text-xs
                                                            ${colorClass == 'blue' ? 'text-blue-700' : ''}
                                                            ${colorClass == 'green' ? 'text-green-700' : ''}
                                                            ${colorClass == 'purple' ? 'text-purple-700' : ''}
                                                            ${colorClass == 'yellow' ? 'text-yellow-700' : ''}
                                                            ${colorClass == 'pink' ? 'text-pink-700' : ''}
                                                            ${colorClass == 'indigo' ? 'text-indigo-700' : ''}">${seance.professeurNom}</div>
                                                        <c:if test="${not empty seance.salleNom}">
                                                            <div class="text-xs
                                                                ${colorClass == 'blue' ? 'text-blue-700' : ''}
                                                                ${colorClass == 'green' ? 'text-green-700' : ''}
                                                                ${colorClass == 'purple' ? 'text-purple-700' : ''}
                                                                ${colorClass == 'yellow' ? 'text-yellow-700' : ''}
                                                                ${colorClass == 'pink' ? 'text-pink-700' : ''}
                                                                ${colorClass == 'indigo' ? 'text-indigo-700' : ''}">${seance.salleNom}</div>
                                                        </c:if>
                                                        <c:if test="${canEdit}">
                                                            <div class="flex gap-1 mt-1 justify-end">
                                                                <a href="${pageContext.request.contextPath}/emploi/new?id=${seance.id}" 
                                                                   class="h-6 w-6 p-0 rounded flex items-center justify-center
                                                                   ${colorClass == 'blue' ? 'text-blue-600 hover:text-blue-800 hover:bg-blue-100' : ''}
                                                                   ${colorClass == 'green' ? 'text-green-600 hover:text-green-800 hover:bg-green-100' : ''}
                                                                   ${colorClass == 'purple' ? 'text-purple-600 hover:text-purple-800 hover:bg-purple-100' : ''}
                                                                   ${colorClass == 'yellow' ? 'text-yellow-600 hover:text-yellow-800 hover:bg-yellow-100' : ''}
                                                                   ${colorClass == 'pink' ? 'text-pink-600 hover:text-pink-800 hover:bg-pink-100' : ''}
                                                                   ${colorClass == 'indigo' ? 'text-indigo-600 hover:text-indigo-800 hover:bg-indigo-100' : ''}">
                                                                    <i class="fas fa-edit w-3 h-3"></i>
                                                                </a>
                                                                <a href="${pageContext.request.contextPath}/emploi/delete?id=${seance.id}" 
                                                                   onclick="return confirm('Êtes-vous sûr ?')"
                                                                   class="h-6 w-6 p-0 text-red-600 hover:text-red-800 hover:bg-red-100 rounded flex items-center justify-center">
                                                                    <i class="fas fa-trash w-3 h-3"></i>
                                                                </a>
                                                            </div>
                                                        </c:if>
                                                    </div>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:forEach>
                            </tr>
                            
                            <!-- Time Slot 5: 18:30-20:30 -->
                            <tr>
                                <td class="text-gray-700 font-medium p-3">18:30-20:30</td>
                                <c:forEach var="jour" items="${['LUNDI', 'MARDI', 'MERCREDI', 'JEUDI', 'VENDREDI', 'SAMEDI']}" varStatus="jourIdx">
                                    <c:set var="key" value="${jour}_18:30-20:30" />
                                    <c:set var="seances" value="${calendarData[key]}" />
                                    <td class="p-2 h-20 border border-gray-200 ${canEdit ? 'hover:bg-gray-50 cursor-pointer' : ''} transition-colors text-center timetable-cell" 
                                        data-day="${jour}" 
                                        data-time="18:30-20:30"
                                        data-heure-debut="18:30"
                                        data-heure-fin="20:30">
                                        <c:choose>
                                            <c:when test="${empty seances}">
                                                <c:if test="${canEdit}">
                                                    <div class="text-gray-400 text-sm h-full flex items-center justify-center hover:text-gray-600">
                                                        <i class="fas fa-plus w-4 h-4"></i>
                                                    </div>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="seance" items="${seances}">
                                                    <c:set var="colorClass" value="${jourIdx.index == 0 ? 'blue' : jourIdx.index == 1 ? 'green' : jourIdx.index == 2 ? 'purple' : jourIdx.index == 3 ? 'yellow' : jourIdx.index == 4 ? 'pink' : 'indigo'}" />
                                                    <div class="rounded p-2 h-full flex flex-col justify-between mb-1
                                                        ${colorClass == 'blue' ? 'bg-blue-50 border border-blue-200' : ''}
                                                        ${colorClass == 'green' ? 'bg-green-50 border border-green-200' : ''}
                                                        ${colorClass == 'purple' ? 'bg-purple-50 border border-purple-200' : ''}
                                                        ${colorClass == 'yellow' ? 'bg-yellow-50 border border-yellow-200' : ''}
                                                        ${colorClass == 'pink' ? 'bg-pink-50 border border-pink-200' : ''}
                                                        ${colorClass == 'indigo' ? 'bg-indigo-50 border border-indigo-200' : ''}">
                                                        <div class="font-medium text-sm
                                                            ${colorClass == 'blue' ? 'text-blue-900' : ''}
                                                            ${colorClass == 'green' ? 'text-green-900' : ''}
                                                            ${colorClass == 'purple' ? 'text-purple-900' : ''}
                                                            ${colorClass == 'yellow' ? 'text-yellow-900' : ''}
                                                            ${colorClass == 'pink' ? 'text-pink-900' : ''}
                                                            ${colorClass == 'indigo' ? 'text-indigo-900' : ''}">${seance.matiereNom}</div>
                                                        <div class="text-xs
                                                            ${colorClass == 'blue' ? 'text-blue-700' : ''}
                                                            ${colorClass == 'green' ? 'text-green-700' : ''}
                                                            ${colorClass == 'purple' ? 'text-purple-700' : ''}
                                                            ${colorClass == 'yellow' ? 'text-yellow-700' : ''}
                                                            ${colorClass == 'pink' ? 'text-pink-700' : ''}
                                                            ${colorClass == 'indigo' ? 'text-indigo-700' : ''}">${seance.professeurNom}</div>
                                                        <c:if test="${not empty seance.salleNom}">
                                                            <div class="text-xs
                                                                ${colorClass == 'blue' ? 'text-blue-700' : ''}
                                                                ${colorClass == 'green' ? 'text-green-700' : ''}
                                                                ${colorClass == 'purple' ? 'text-purple-700' : ''}
                                                                ${colorClass == 'yellow' ? 'text-yellow-700' : ''}
                                                                ${colorClass == 'pink' ? 'text-pink-700' : ''}
                                                                ${colorClass == 'indigo' ? 'text-indigo-700' : ''}">${seance.salleNom}</div>
                                                        </c:if>
                                                        <c:if test="${canEdit}">
                                                            <div class="flex gap-1 mt-1 justify-end">
                                                                <a href="${pageContext.request.contextPath}/emploi/new?id=${seance.id}" 
                                                                   class="h-6 w-6 p-0 rounded flex items-center justify-center
                                                                   ${colorClass == 'blue' ? 'text-blue-600 hover:text-blue-800 hover:bg-blue-100' : ''}
                                                                   ${colorClass == 'green' ? 'text-green-600 hover:text-green-800 hover:bg-green-100' : ''}
                                                                   ${colorClass == 'purple' ? 'text-purple-600 hover:text-purple-800 hover:bg-purple-100' : ''}
                                                                   ${colorClass == 'yellow' ? 'text-yellow-600 hover:text-yellow-800 hover:bg-yellow-100' : ''}
                                                                   ${colorClass == 'pink' ? 'text-pink-600 hover:text-pink-800 hover:bg-pink-100' : ''}
                                                                   ${colorClass == 'indigo' ? 'text-indigo-600 hover:text-indigo-800 hover:bg-indigo-100' : ''}">
                                                                    <i class="fas fa-edit w-3 h-3"></i>
                                                                </a>
                                                                <a href="${pageContext.request.contextPath}/emploi/delete?id=${seance.id}" 
                                                                   onclick="return confirm('Êtes-vous sûr ?')"
                                                                   class="h-6 w-6 p-0 text-red-600 hover:text-red-800 hover:bg-red-100 rounded flex items-center justify-center">
                                                                    <i class="fas fa-trash w-3 h-3"></i>
                                                                </a>
                                                            </div>
                                                        </c:if>
                                                    </div>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:forEach>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- All Schedule Entries Table -->
        <div class="bg-white border border-gray-200 rounded-lg shadow-sm">
            <div class="p-6 border-b border-gray-200">
                <h2 class="text-xl font-semibold text-gray-900">Toutes les Séances</h2>
                <p class="text-gray-600">Total: ${emploiList.size()} séances</p>
            </div>
            <div class="p-6">
                <div class="overflow-x-auto">
                    <table class="w-full border-collapse">
                        <thead>
                            <tr class="border-b border-gray-200">
                                <th class="text-gray-700 font-medium p-3 text-left">Filière</th>
                                <th class="text-gray-700 font-medium p-3 text-left">Jour</th>
                                <th class="text-gray-700 font-medium p-3 text-left">Créneau</th>
                                <th class="text-gray-700 font-medium p-3 text-left">Matière</th>
                                <th class="text-gray-700 font-medium p-3 text-left">Professeur</th>
                                <th class="text-gray-700 font-medium p-3 text-left">Salle</th>
                                <th class="text-gray-700 font-medium p-3 text-left">Type</th>
                                <c:if test="${canEdit}">
                                    <th class="text-gray-700 font-medium p-3 text-left">Actions</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty emploiList}">
                                    <tr>
                                        <td colspan="${canEdit ? 8 : 7}" class="p-6 text-center text-gray-500">
                                            Aucune séance trouvée
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="emploi" items="${emploiList}">
                                        <tr class="border-b border-gray-200 hover:bg-gray-50">
                                            <td class="text-gray-900 font-medium p-3">${emploi.filiereNom}</td>
                                            <td class="text-gray-700 p-3">${emploi.jourSemaine}</td>
                                            <td class="text-gray-700 p-3">${emploi.heureDebut} - ${emploi.heureFin}</td>
                                            <td class="text-gray-700 p-3">${emploi.matiereNom}</td>
                                            <td class="text-gray-700 p-3">${emploi.professeurNom}</td>
                                            <td class="text-gray-700 p-3">${emploi.salleNom}</td>
                                            <td class="text-gray-700 p-3">
                                                <span class="px-2 py-1 text-xs rounded-full
                                                    ${emploi.typeSeance.name() == 'COURS' ? 'bg-blue-100 text-blue-800' : ''}
                                                    ${emploi.typeSeance.name() == 'TD' ? 'bg-green-100 text-green-800' : ''}
                                                    ${emploi.typeSeance.name() == 'TP' ? 'bg-purple-100 text-purple-800' : ''}">
                                                    ${emploi.typeSeance}
                                                </span>
                                            </td>
                                            <c:if test="${canEdit}">
                                                <td class="p-3 flex gap-2">
                                                    <a href="${pageContext.request.contextPath}/emploi/new?id=${emploi.id}" 
                                                       class="gap-1 border border-gray-300 text-gray-700 hover:bg-gray-50 px-3 py-1 rounded-md flex items-center text-sm">
                                                        <i class="fas fa-edit w-3 h-3"></i>
                                                        Modifier
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/emploi/delete?id=${emploi.id}" 
                                                       onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette séance ?')"
                                                       class="gap-1 bg-red-600 hover:bg-red-700 text-white px-3 py-1 rounded-md flex items-center text-sm">
                                                        <i class="fas fa-trash w-3 h-3"></i>
                                                        Supprimer
                                                    </a>
                                                </td>
                                            </c:if>
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
</div>

<c:if test="${canEdit}">
<script>
// Variables JSP pour JavaScript
var isCoordinateur = <c:choose><c:when test="${isCoordinateur}">true</c:when><c:otherwise>false</c:otherwise></c:choose>;
var isAdmin = <c:choose><c:when test="${isAdmin}">true</c:when><c:otherwise>false</c:otherwise></c:choose>;
var coordinateurFiliereId = <c:choose><c:when test="${isCoordinateur && not empty selectedFiliereId}">'${selectedFiliereId}'</c:when><c:when test="${isCoordinateur && not empty filieres}">'${filieres[0].id}'</c:when><c:otherwise>null</c:otherwise></c:choose>;
var coordinateurAnnee = <c:choose><c:when test="${isCoordinateur && not empty selectedAnnee}">'${selectedAnnee}'</c:when><c:when test="${isCoordinateur && not empty filieres}">'${filieres[0].annee}'</c:when><c:otherwise>null</c:otherwise></c:choose>;
var selectedFiliereId = <c:choose><c:when test="${not empty selectedFiliereId}">'${selectedFiliereId}'</c:when><c:otherwise>null</c:otherwise></c:choose>;

// Store all matieres for filtering
var allMatieres = [];
document.addEventListener('DOMContentLoaded', function() {
    // Store all matiere options for filtering
    const subjectSelect = document.getElementById('subjectSelect');
    if (subjectSelect) {
        Array.from(subjectSelect.options).forEach(option => {
            if (option.value) {
                allMatieres.push({
                    value: option.value,
                    text: option.text,
                    filiereId: option.getAttribute('data-filiere'),
                    profId: option.getAttribute('data-prof'),
                    profNom: option.getAttribute('data-prof-nom')
                });
            }
        });
    }
    
    const addScheduleBtn = document.getElementById('addScheduleBtn');
    const scheduleModal = document.getElementById('scheduleModal');
    const closeModalBtn = document.getElementById('closeModalBtn');
    const cancelFormBtn = document.getElementById('cancelFormBtn');
    const tableCells = document.querySelectorAll('.timetable-cell');
    const selectedInfo = document.getElementById('selectedInfo');
    const dayTimeSelectors = document.getElementById('dayTimeSelectors');
    const daySelect = document.getElementById('daySelect');
    const timeSlotSelect = document.getElementById('timeSlotSelect');
    const formJourSemaine = document.getElementById('formJourSemaine');
    const formHeureDebut = document.getElementById('formHeureDebut');
    const formHeureFin = document.getElementById('formHeureFin');
    const formFiliereId = document.getElementById('formFiliereId');
    const branchSelect = document.getElementById('branchSelect');
    const typeSeanceSelect = document.getElementById('typeSeanceSelect');
    const salleSelect = document.getElementById('salleSelect');
    
    const dayNames = {
        'LUNDI': 'Lundi',
        'MARDI': 'Mardi',
        'MERCREDI': 'Mercredi',
        'JEUDI': 'Jeudi',
        'VENDREDI': 'Vendredi',
        'SAMEDI': 'Samedi'
    };
    
    // Function to open modal
    function openModal() {
        if (scheduleModal) {
            scheduleModal.classList.remove('hidden');
            document.body.style.overflow = 'hidden'; // Prevent body scroll
        }
    }
    
    // Function to close modal
    function closeModal() {
        if (scheduleModal) {
            scheduleModal.classList.add('hidden');
            document.body.style.overflow = ''; // Restore body scroll
        }
        resetForm();
    }
    
    // Show modal when Add Schedule button is clicked
    if (addScheduleBtn) {
        addScheduleBtn.addEventListener('click', function() {
            selectedInfo.textContent = '';
            if (dayTimeSelectors) dayTimeSelectors.classList.remove('hidden');
            resetForm();
            openModal();
        });
    }
    
    // Close modal when close button is clicked
    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', closeModal);
    }
    
    // Close modal when cancel button is clicked
    if (cancelFormBtn) {
        cancelFormBtn.addEventListener('click', closeModal);
    }
    
    // Close modal when clicking outside
    if (scheduleModal) {
        scheduleModal.addEventListener('click', function(e) {
            if (e.target === scheduleModal) {
                closeModal();
            }
        });
    }
    
    // Close modal on Escape key
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && scheduleModal && !scheduleModal.classList.contains('hidden')) {
            closeModal();
        }
    });
    
    // Handle table cell clicks
    if (tableCells) {
        tableCells.forEach(cell => {
            cell.addEventListener('click', function(e) {
                // Don't trigger if clicking on buttons inside
                if (e.target.closest('a, button')) {
                    return;
                }
                
                const day = this.getAttribute('data-day');
                const timeSlot = this.getAttribute('data-time');
                const heureDebut = this.getAttribute('data-heure-debut');
                const heureFin = this.getAttribute('data-heure-fin');
                
                // Only show modal if cell is empty (has plus icon)
                if (this.querySelector('.fa-plus')) {
                    selectedInfo.textContent = 'Sélectionné: ' + dayNames[day] + ' de ' + timeSlot;
                    if (dayTimeSelectors) dayTimeSelectors.classList.add('hidden');
                    
                    // Set the selected day and time in the form
                    if (formJourSemaine) {
                        formJourSemaine.value = day;
                    }
                    if (formHeureDebut) {
                        formHeureDebut.value = heureDebut;
                    }
                    if (formHeureFin) {
                        formHeureFin.value = heureFin;
                    }
                    
                    openModal();
                }
            });
        });
    }
    
    // Update form hidden fields when selects change
    if (daySelect) {
        daySelect.addEventListener('change', function() {
            if (formJourSemaine) formJourSemaine.value = this.value;
        });
    }
    
    if (timeSlotSelect) {
        timeSlotSelect.addEventListener('change', function() {
            const times = this.value.split('-');
            if (formHeureDebut) formHeureDebut.value = times[0];
            if (formHeureFin) formHeureFin.value = times[1];
        });
    }
    
    // Pour les coordinateurs, initialiser la filière et l'année automatiquement
    if (isCoordinateur && coordinateurFiliereId && formFiliereId) {
        formFiliereId.value = coordinateurFiliereId;
    }
    if (isCoordinateur && coordinateurAnnee && formAnnee) {
        formAnnee.value = coordinateurAnnee;
    }
    
    // Filter matieres when filiere is selected (for admin)
    if (branchSelect) {
        branchSelect.addEventListener('change', function() {
            const selectedFiliereId = this.value;
            if (selectedFiliereId && formFiliereId) {
                formFiliereId.value = selectedFiliereId;
                const selectedOption = this.options[this.selectedIndex];
                if (selectedOption && formAnnee) {
                    const annee = selectedOption.getAttribute('data-annee');
                    if (annee) {
                        formAnnee.value = annee;
                    }
                }
            }
            filterMatieresByFiliere();
        });
    }
    
    // Mettre à jour la filière quand une matière est sélectionnée (pour admin uniquement, si filiere not selected)
    if (isAdmin && subjectSelect && !branchSelect) {
        subjectSelect.addEventListener('change', function() {
            const selectedOption = this.options[this.selectedIndex];
            if (selectedOption && selectedOption.value && formFiliereId) {
                const matiereFiliereId = selectedOption.getAttribute('data-filiere');
                if (matiereFiliereId) {
                    formFiliereId.value = matiereFiliereId;
                }
            }
        });
    }
    
    // Auto-select type based on salle
    if (salleSelect && typeSeanceSelect) {
        salleSelect.addEventListener('change', function() {
            const salleText = this.options[this.selectedIndex].text;
            if (salleText.includes('TP')) {
                typeSeanceSelect.value = 'TP';
            } else if (salleText.includes('TD')) {
                typeSeanceSelect.value = 'TD';
            } else {
                typeSeanceSelect.value = 'COURS';
            }
        });
    }
    
    function resetForm() {
        if (daySelect) daySelect.value = '';
        if (timeSlotSelect) timeSlotSelect.value = '';
        if (subjectSelect) {
            subjectSelect.value = '';
            // Reset matieres dropdown to show all (for coordinateurs, they're already filtered)
            if (isCoordinateur) {
                filterMatieresByFiliere();
            } else if (isAdmin && branchSelect) {
                // Reset to show all matieres when branch is reset
                filterMatieresByFiliere();
            }
        }
        const professorId = document.getElementById('professorId');
        if (professorId) professorId.value = '';
        const professorNom = document.getElementById('professorNom');
        if (professorNom) professorNom.value = '';
        if (salleSelect) salleSelect.value = '';
        if (typeSeanceSelect) typeSeanceSelect.value = 'COURS';
        // Pour coordinateur, réinitialiser avec la première filière
        if (isCoordinateur && coordinateurFiliereId && formFiliereId) {
            formFiliereId.value = coordinateurFiliereId;
        } else if (isAdmin && branchSelect) {
            branchSelect.value = '';
            if (formFiliereId) formFiliereId.value = '';
        }
        if (isCoordinateur && coordinateurAnnee && formAnnee) {
            formAnnee.value = coordinateurAnnee;
        } else if (formAnnee) {
            formAnnee.value = '1';
        }
        if (formJourSemaine) formJourSemaine.value = '';
        if (formHeureDebut) formHeureDebut.value = '';
        if (formHeureFin) formHeureFin.value = '';
        // Reset filiere for admin, keep for coordinateur
        if (isAdmin && branchSelect) {
            branchSelect.value = '';
            if (formFiliereId) formFiliereId.value = '';
        } else if (isCoordinateur && formFiliereId && selectedFiliereId) {
            formFiliereId.value = selectedFiliereId;
        }
    }
    
    // Prevent event bubbling for buttons inside table cells
    document.querySelectorAll('.timetable-cell a, .timetable-cell button').forEach(button => {
        button.addEventListener('click', function(e) {
            e.stopPropagation();
        });
    });
});

// Filter matieres by selected filiere
function filterMatieresByFiliere() {
    const subjectSelect = document.getElementById('subjectSelect');
    const branchSelect = document.getElementById('branchSelect');
    
    if (!subjectSelect || !allMatieres.length) return;
    
    // Get selected filiere ID
    let targetFiliereId = null;
    if (branchSelect && branchSelect.value) {
        // Admin selected a filiere from dropdown
        targetFiliereId = branchSelect.value;
    } else if (isCoordinateur) {
        // For coordinateurs, use the selected filiere from page filter or default
        // Matieres are already filtered on server side, so show all available
        targetFiliereId = coordinateurFiliereId;
    }
    
    // Clear current options except the first one
    subjectSelect.innerHTML = '<option value="">Sélectionner une matière</option>';
    
    // Filter and add matieres
    allMatieres.forEach(matiere => {
        // For coordinateurs, matieres are already filtered on server
        // Just show all matieres that are in the allMatieres array
        // For admins, filter by selected filiere from dropdown
        if (isCoordinateur) {
            // Show all matieres (already filtered on server by coordinateur's filieres)
            const option = document.createElement('option');
            option.value = matiere.value;
            option.textContent = matiere.text;
            option.setAttribute('data-filiere', matiere.filiereId);
            option.setAttribute('data-prof', matiere.profId);
            option.setAttribute('data-prof-nom', matiere.profNom);
            subjectSelect.appendChild(option);
        } else if (isAdmin) {
            // For admins, filter by selected filiere
            if (!targetFiliereId || matiere.filiereId === targetFiliereId) {
                const option = document.createElement('option');
                option.value = matiere.value;
                option.textContent = matiere.text;
                option.setAttribute('data-filiere', matiere.filiereId);
                option.setAttribute('data-prof', matiere.profId);
                option.setAttribute('data-prof-nom', matiere.profNom);
                subjectSelect.appendChild(option);
            }
        }
    });
    
    // Reset professor fields
    const professorId = document.getElementById('professorId');
    const professorNom = document.getElementById('professorNom');
    if (professorId) professorId.value = '';
    if (professorNom) professorNom.value = '';
}

function updateProfesseur() {
    const matiereSelect = document.getElementById('subjectSelect');
    const professeurIdInput = document.getElementById('professorId');
    const professeurNomInput = document.getElementById('professorNom');
    
    if (matiereSelect && professeurIdInput && professeurNomInput) {
        const selectedOption = matiereSelect.options[matiereSelect.selectedIndex];
        if (selectedOption && selectedOption.value) {
            const professeurId = selectedOption.getAttribute('data-prof') || '';
            const professeurNom = selectedOption.getAttribute('data-prof-nom') || '';
            professeurIdInput.value = professeurId;
            professeurNomInput.value = professeurNom;
        } else {
            professeurIdInput.value = '';
            professeurNomInput.value = '';
        }
    }
}
</script>
</c:if>

<jsp:include page="../common/footer.jsp"/>
