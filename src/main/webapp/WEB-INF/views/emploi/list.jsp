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
                        <c:when test="${isProfessor}">
                            Mes horaires de cours
                        </c:when>
                        <c:otherwise>
                            Créer et gérer les horaires des classes par filière
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
            <c:if test="${canEdit}">
                <button id="addScheduleBtn" class="gap-2 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md flex items-center">
                    <i class="fas fa-plus w-4 h-4"></i>
                    Ajouter Séance
                </button>
            </c:if>
        </div>

        <!-- Message si coordinateur sans filières -->
        <c:if test="${isCoordinateur && (empty filieres || filieres.size() == 0)}">
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

        <!-- Navigation par onglets (Cycles et Années) -->
        <c:if test="${not empty emploisByCycleAndAnnee}">
            <div class="bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden">
                <!-- Barre d'onglets -->
                <div class="border-b border-gray-200 bg-gray-50">
                    <nav class="flex flex-wrap gap-2 px-4" aria-label="Tabs">
                        <!-- Cycle Préparatoire -->
                        <c:set var="preparatoireMap" value="${emploisByCycleAndAnnee['PREPARATOIRE']}" />
                        <c:if test="${not empty preparatoireMap}">
                            <c:forEach var="anneeEntry" items="${preparatoireMap}">
                                <c:set var="annee" value="${anneeEntry.key}" />
                                <c:set var="anneeLabel" value="${annee == 1 ? '1ère année préparatoire' : '2ème année préparatoire'}" />
                                <c:set var="preparatoireUrl" value="${pageContext.request.contextPath}/emploi/list?cycle=PREPARATOIRE&annee=${annee}" />
                                <c:if test="${not empty selectedFiliereId}">
                                    <c:set var="preparatoireUrl" value="${preparatoireUrl}&filiereId=${selectedFiliereId}" />
                                </c:if>
                                <a href="${preparatoireUrl}" 
                                   class="px-4 py-3 text-sm font-medium border-b-2 transition-colors whitespace-nowrap
                                   ${selectedCycle == 'PREPARATOIRE' && (selectedAnnee == null || selectedAnnee == '' || selectedAnnee == annee) ? 'border-blue-500 text-blue-600 bg-white' : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'}">
                                    ${anneeLabel}
                                </a>
                            </c:forEach>
                        </c:if>
                        
                        <!-- Cycle Ingénieur -->
                        <c:set var="ingenieurMap" value="${emploisByCycleAndAnnee['INGENIEUR']}" />
                        <c:if test="${not empty ingenieurMap}">
                            <c:forEach var="anneeEntry" items="${ingenieurMap}">
                                <c:set var="annee" value="${anneeEntry.key}" />
                                <c:set var="anneeLabel" value="${annee == 1 ? '1ère année ingénieur' : annee == 2 ? '2ème année ingénieur' : '3ème année ingénieur'}" />
                                <c:set var="ingenieurUrl" value="${pageContext.request.contextPath}/emploi/list?cycle=INGENIEUR&annee=${annee}" />
                                <c:if test="${not empty selectedFiliereId}">
                                    <c:set var="ingenieurUrl" value="${ingenieurUrl}&filiereId=${selectedFiliereId}" />
                                </c:if>
                                <a href="${ingenieurUrl}" 
                                   class="px-4 py-3 text-sm font-medium border-b-2 transition-colors whitespace-nowrap
                                   ${selectedCycle == 'INGENIEUR' && (selectedAnnee == null || selectedAnnee == '' || selectedAnnee == annee) ? 'border-blue-500 text-blue-600 bg-white' : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'}">
                                    ${anneeLabel}
                                </a>
                            </c:forEach>
                        </c:if>
                    </nav>
                </div>
            </div>
        </c:if>

        <!-- Filters (only for admin/coordinateur) -->
        <c:if test="${canEdit || isAdmin}">
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <form method="get" action="${pageContext.request.contextPath}/emploi/list" class="flex gap-4 flex-col md:flex-row">
                        <c:if test="${isAdmin}">
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
                        </c:if>
                        <c:if test="${isCoordinateur && not empty filieres}">
                            <div>
                                <select name="filiereId" class="rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                                    <option value="">Toutes mes filières</option>
                                    <c:forEach var="filiere" items="${filieres}">
                                        <option value="${filiere.id}" ${selectedFiliereId == filiere.id ? 'selected' : ''}>
                                            ${filiere.nom} - ${filiere.cycle} ${filiere.annee}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </c:if>
                        <div>
                            <select name="groupe" class="rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                                <option value="">Tous les groupes</option>
                                <option value="1" ${selectedGroupe == '1' ? 'selected' : ''}>Groupe 1</option>
                                <option value="2" ${selectedGroupe == '2' ? 'selected' : ''}>Groupe 2</option>
                            </select>
                        </div>
                        <button type="submit" class="px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700">
                            <i class="fas fa-filter"></i> Filtrer
                        </button>
                    </form>
                </div>
            </div>
        </c:if>

        <!-- Add Schedule Form (Initially Hidden) -->
        <c:if test="${canEdit}">
            <div id="scheduleForm" class="bg-white border border-gray-200 rounded-lg shadow-sm hidden">
                <div class="p-6 border-b border-gray-200">
                    <h2 class="text-xl font-semibold text-gray-900">Ajouter une Nouvelle Séance</h2>
                    <p id="selectedInfo" class="text-gray-600 mt-1"></p>
                </div>
                <div class="p-6">
                    <form method="POST" action="${pageContext.request.contextPath}/emploi/save" class="space-y-4">
                        <input type="hidden" name="filiereId" id="formFiliereId" required>
                        <input type="hidden" name="annee" id="formAnnee" value="1">
                        <input type="hidden" name="jourSemaine" id="formJourSemaine" required>
                        <input type="hidden" name="heureDebut" id="formHeureDebut" required>
                        <input type="hidden" name="heureFin" id="formHeureFin" required>
                        
                        <div>
                            <label class="text-gray-700 font-medium block mb-2">Filière <span class="text-red-500">*</span></label>
                            <select name="filiereId" id="branchSelect" required
                                    class="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded text-gray-900 focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500">
                                <option value="">Sélectionner une filière</option>
                                <c:forEach var="filiere" items="${filieres}">
                                    <option value="${filiere.id}">${filiere.nom} - ${filiere.cycle} ${filiere.annee}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
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
                                    <option value="${matiere.id}" data-filiere="${matiere.filiereId}" data-prof="${matiere.professeurId}">${matiere.nom}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div>
                            <label class="text-gray-700 font-medium block mb-2">Professeur <span class="text-red-500">*</span></label>
                            <input type="number" name="professeurId" id="professorId" required readonly
                                   class="w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded text-gray-900">
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
                        
                        <div class="flex gap-2">
                            <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md">
                                Ajouter Séance
                            </button>
                            <button type="button" id="cancelFormBtn" class="border border-gray-300 text-gray-700 hover:bg-gray-50 px-4 py-2 rounded-md">
                                Annuler
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </c:if>

        <!-- Timetable Schedule Table -->
        <div class="bg-white border border-gray-200 rounded-lg shadow-sm">
            <div class="p-6 border-b border-gray-200">
                <h2 class="text-xl font-semibold text-gray-900">
                    <c:choose>
                        <c:when test="${selectedCycle == 'PREPARATOIRE' && selectedAnnee == '1'}">
                            Emploi du Temps - 1ère année préparatoire
                        </c:when>
                        <c:when test="${selectedCycle == 'PREPARATOIRE' && selectedAnnee == '2'}">
                            Emploi du Temps - 2ème année préparatoire
                        </c:when>
                        <c:when test="${selectedCycle == 'INGENIEUR' && selectedAnnee == '1'}">
                            Emploi du Temps - 1ère année ingénieur
                        </c:when>
                        <c:when test="${selectedCycle == 'INGENIEUR' && selectedAnnee == '2'}">
                            Emploi du Temps - 2ème année ingénieur
                        </c:when>
                        <c:when test="${selectedCycle == 'INGENIEUR' && selectedAnnee == '3'}">
                            Emploi du Temps - 3ème année ingénieur
                        </c:when>
                        <c:otherwise>
                            Emploi du Temps
                        </c:otherwise>
                    </c:choose>
                </h2>
                <p class="text-gray-600">
                    <c:choose>
                        <c:when test="${canEdit}">
                            Cliquez sur une cellule vide pour ajouter une matière
                        </c:when>
                        <c:when test="${isProfessor}">
                            Vos horaires de cours
                        </c:when>
                        <c:otherwise>
                            Sélectionnez un onglet ci-dessus pour voir l'emploi du temps d'une année spécifique
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
document.addEventListener('DOMContentLoaded', function() {
    const addScheduleBtn = document.getElementById('addScheduleBtn');
    const scheduleForm = document.getElementById('scheduleForm');
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
    const subjectSelect = document.getElementById('subjectSelect');
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
    
    // Show/hide form when Add Schedule button is clicked
    if (addScheduleBtn) {
        addScheduleBtn.addEventListener('click', function() {
            scheduleForm.classList.toggle('hidden');
            selectedInfo.textContent = '';
            if (dayTimeSelectors) dayTimeSelectors.classList.remove('hidden');
            resetForm();
        });
    }
    
    // Hide form when Cancel button is clicked
    if (cancelFormBtn) {
        cancelFormBtn.addEventListener('click', function() {
            scheduleForm.classList.add('hidden');
            resetForm();
        });
    }
    
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
                
                // Only show form if cell is empty (has plus icon)
                if (this.querySelector('.fa-plus')) {
                    scheduleForm.classList.remove('hidden');
                    selectedInfo.textContent = 'Sélectionné: ' + dayNames[day] + ' de ' + timeSlot;
                    if (dayTimeSelectors) dayTimeSelectors.classList.add('hidden');
                    
                    // Set the selected day and time in the form
                    if (daySelect) {
                        daySelect.value = day;
                    }
                    if (formJourSemaine) {
                        formJourSemaine.value = day;
                    }
                    
                    if (timeSlotSelect) {
                        timeSlotSelect.value = timeSlot;
                    }
                    if (formHeureDebut) {
                        formHeureDebut.value = heureDebut;
                    }
                    if (formHeureFin) {
                        formHeureFin.value = heureFin;
                    }
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
    
    // Update filiere when branch is selected
    if (branchSelect) {
        branchSelect.addEventListener('change', function() {
            if (formFiliereId) formFiliereId.value = this.value;
            // Filter matières by filière
            if (subjectSelect) {
                const selectedFiliere = this.value;
                Array.from(subjectSelect.options).forEach(option => {
                    if (option.value === '') return;
                    const optionFiliere = option.getAttribute('data-filiere');
                    option.style.display = (!selectedFiliere || optionFiliere === selectedFiliere) ? 'block' : 'none';
                });
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
        if (subjectSelect) subjectSelect.value = '';
        const professorId = document.getElementById('professorId');
        if (professorId) professorId.value = '';
        if (salleSelect) salleSelect.value = '';
        if (typeSeanceSelect) typeSeanceSelect.value = 'COURS';
        if (branchSelect) branchSelect.value = '';
        if (formFiliereId) formFiliereId.value = '';
        if (formJourSemaine) formJourSemaine.value = '';
        if (formHeureDebut) formHeureDebut.value = '';
        if (formHeureFin) formHeureFin.value = '';
    }
    
    // Prevent event bubbling for buttons inside table cells
    document.querySelectorAll('.timetable-cell a, .timetable-cell button').forEach(button => {
        button.addEventListener('click', function(e) {
            e.stopPropagation();
        });
    });
});

function updateProfesseur() {
    const matiereSelect = document.getElementById('subjectSelect');
    const professeurInput = document.getElementById('professorId');
    if (matiereSelect && professeurInput) {
        const selectedOption = matiereSelect.options[matiereSelect.selectedIndex];
        if (selectedOption && selectedOption.value) {
            professeurInput.value = selectedOption.getAttribute('data-prof') || '';
        } else {
            professeurInput.value = '';
        }
    }
}
</script>
</c:if>

<jsp:include page="../common/footer.jsp"/>
