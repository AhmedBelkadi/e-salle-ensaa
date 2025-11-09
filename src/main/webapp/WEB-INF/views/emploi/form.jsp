<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Nouvelle Séance - E-Salle ENSAA"/>
</jsp:include>

<div class="flex-1 p-6">
    <div class="max-w-2xl mx-auto">
        <div class="space-y-6">
            <!-- Header -->
            <div>
                <h1 class="text-3xl font-bold text-gray-900">${not empty emploi ? 'Modifier la Séance' : 'Nouvelle Séance'}</h1>
                <p class="text-gray-600 mt-2">${not empty emploi ? 'Modifier une séance dans l\'emploi du temps' : 'Créer une nouvelle séance dans l\'emploi du temps'}</p>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="bg-red-50 border border-red-200 rounded-lg p-4">
                    <p class="text-red-800">${error}</p>
                </div>
            </c:if>

            <!-- Form -->
            <form method="POST" action="${pageContext.request.contextPath}/emploi/save" 
                  class="bg-white rounded-xl border border-gray-200 p-6 shadow-sm space-y-6">
                
                <!-- Filière et Année (masquées pour coordinateur, déterminées automatiquement) -->
                <c:choose>
                    <c:when test="${isCoordinateur == true && not empty filieres}">
                        <!-- Coordinateur: filière et année déterminées automatiquement -->
                        <c:choose>
                            <c:when test="${not empty emploi && not empty emploi.filiereId}">
                                <!-- Mode édition: utiliser la filière et l'année de l'emploi -->
                                <input type="hidden" name="filiereId" id="filiereId" value="${emploi.filiereId}">
                                <input type="hidden" name="annee" id="annee" value="${emploi.annee}">
                            </c:when>
                            <c:otherwise>
                                <!-- Mode création: utiliser la première filière disponible et son année -->
                                <input type="hidden" name="filiereId" id="filiereId" value="${filieres[0].id}">
                                <input type="hidden" name="annee" id="annee" value="${filieres[0].annee}">
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <!-- Admin: afficher les champs filière et année -->
                        <div>
                            <label for="filiereId" class="block text-sm font-medium text-gray-700 mb-2">
                                Filière <span class="text-red-500">*</span>
                            </label>
                            <select name="filiereId" id="filiereId" required onchange="updateMatieres()"
                                    class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                                <option value="">Sélectionner une filière</option>
                                <c:forEach var="filiere" items="${filieres}">
                                    <option value="${filiere.id}" ${not empty emploi && emploi.filiereId == filiere.id ? 'selected' : ''}>${filiere.nom} - ${filiere.cycle} ${filiere.annee}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div>
                            <label for="annee" class="block text-sm font-medium text-gray-700 mb-2">
                                Année <span class="text-red-500">*</span>
                            </label>
                            <select name="annee" id="annee" required
                                    class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                                <option value="1" ${not empty emploi && emploi.annee == 1 ? 'selected' : ''}>Année 1</option>
                                <option value="2" ${not empty emploi && emploi.annee == 2 ? 'selected' : ''}>Année 2</option>
                                <option value="3" ${not empty emploi && emploi.annee == 3 ? 'selected' : ''}>Année 3</option>
                            </select>
                        </div>
                    </c:otherwise>
                </c:choose>

                <!-- Matière -->
                <div>
                    <label for="matiereId" class="block text-sm font-medium text-gray-700 mb-2">
                        Matière <span class="text-red-500">*</span>
                    </label>
                    <select name="matiereId" id="matiereId" required onchange="updateProfesseur()"
                            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">Sélectionner une matière</option>
                        <c:forEach var="mat" items="${matieres}">
                            <option value="${mat.id}" 
                                    data-prof="${mat.professeurId}" 
                                    data-prof-nom="${mat.professeurNom}"
                                    ${not empty emploi && emploi.matiereId == mat.id ? 'selected' : ''}>${mat.nom}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Professeur (affichage du nom au lieu de l'ID) -->
                <div>
                    <label for="professeurNom" class="block text-sm font-medium text-gray-700 mb-2">
                        Professeur <span class="text-red-500">*</span>
                    </label>
                    <input type="hidden" name="professeurId" id="professeurId" required 
                           value="${not empty emploi ? emploi.professeurId : ''}">
                    <input type="text" id="professeurNom" readonly
                           class="w-full rounded-md border border-gray-300 px-3 py-2 bg-gray-50 focus:outline-none"
                           placeholder="Sélectionnez une matière pour voir le professeur"
                           value="${not empty emploi && not empty emploi.professeurNom ? emploi.professeurNom : ''}">
                </div>

                <!-- Salle -->
                <div>
                    <label for="salleId" class="block text-sm font-medium text-gray-700 mb-2">
                        Salle <span class="text-red-500">*</span>
                    </label>
                    <select name="salleId" id="salleId" required
                            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">Sélectionner une salle</option>
                        <c:forEach var="salle" items="${salles}">
                            <option value="${salle.id}" ${not empty emploi && emploi.salleId == salle.id ? 'selected' : ''}>${salle.nom} (${salle.type})</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Jour de la semaine -->
                <div>
                    <label for="jourSemaine" class="block text-sm font-medium text-gray-700 mb-2">
                        Jour <span class="text-red-500">*</span>
                    </label>
                    <select name="jourSemaine" id="jourSemaine" required
                            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="LUNDI" ${not empty emploi && emploi.jourSemaine == 'LUNDI' ? 'selected' : ''}>Lundi</option>
                        <option value="MARDI" ${not empty emploi && emploi.jourSemaine == 'MARDI' ? 'selected' : ''}>Mardi</option>
                        <option value="MERCREDI" ${not empty emploi && emploi.jourSemaine == 'MERCREDI' ? 'selected' : ''}>Mercredi</option>
                        <option value="JEUDI" ${not empty emploi && emploi.jourSemaine == 'JEUDI' ? 'selected' : ''}>Jeudi</option>
                        <option value="VENDREDI" ${not empty emploi && emploi.jourSemaine == 'VENDREDI' ? 'selected' : ''}>Vendredi</option>
                        <option value="SAMEDI" ${not empty emploi && emploi.jourSemaine == 'SAMEDI' ? 'selected' : ''}>Samedi</option>
                    </select>
                </div>

                <!-- Heures -->
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="heureDebut" class="block text-sm font-medium text-gray-700 mb-2">
                            Heure de début <span class="text-red-500">*</span>
                        </label>
                        <input type="time" name="heureDebut" id="heureDebut" required
                               value="${not empty emploi && not empty emploi.heureDebut ? emploi.heureDebut : ''}"
                               class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                    <div>
                        <label for="heureFin" class="block text-sm font-medium text-gray-700 mb-2">
                            Heure de fin <span class="text-red-500">*</span>
                        </label>
                        <input type="time" name="heureFin" id="heureFin" required
                               value="${not empty emploi && not empty emploi.heureFin ? emploi.heureFin : ''}"
                               class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                </div>

                <!-- Type de séance -->
                <div>
                    <label for="typeSeance" class="block text-sm font-medium text-gray-700 mb-2">
                        Type de séance <span class="text-red-500">*</span>
                    </label>
                    <select name="typeSeance" id="typeSeance" required
                            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="COURS" ${not empty emploi && emploi.typeSeance == 'COURS' ? 'selected' : ''}>Cours</option>
                        <option value="TD" ${not empty emploi && emploi.typeSeance == 'TD' ? 'selected' : ''}>TD</option>
                        <option value="TP" ${not empty emploi && emploi.typeSeance == 'TP' ? 'selected' : ''}>TP</option>
                    </select>
                </div>

                <!-- Groupe (optionnel) -->
                <div>
                    <label for="groupe" class="block text-sm font-medium text-gray-700 mb-2">
                        Groupe (optionnel)
                    </label>
                    <select name="groupe" id="groupe"
                            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">Tous les groupes</option>
                        <option value="1" ${not empty emploi && emploi.groupe == '1' ? 'selected' : ''}>Groupe 1</option>
                        <option value="2" ${not empty emploi && emploi.groupe == '2' ? 'selected' : ''}>Groupe 2</option>
                    </select>
                </div>

                <!-- Hidden field for edit mode -->
                <c:if test="${not empty emploi}">
                    <input type="hidden" name="id" value="${emploi.id}">
                </c:if>

                <!-- Buttons -->
                <div class="flex gap-4 justify-end">
                    <a href="${pageContext.request.contextPath}/emploi/list"
                       class="px-4 py-2 rounded-md border border-gray-300 text-gray-700 hover:bg-gray-50">
                        Annuler
                    </a>
                    <button type="submit"
                            class="px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700">
                        ${not empty emploi ? 'Modifier la séance' : 'Créer la séance'}
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function updateProfesseur() {
    const matiereSelect = document.getElementById('matiereId');
    const professeurIdInput = document.getElementById('professeurId');
    const professeurNomInput = document.getElementById('professeurNom');
    
    if (!matiereSelect || !professeurIdInput || !professeurNomInput) {
        return;
    }
    
    const selectedOption = matiereSelect.options[matiereSelect.selectedIndex];
    if (selectedOption && selectedOption.value) {
        const profId = selectedOption.getAttribute('data-prof') || '';
        const profNom = selectedOption.getAttribute('data-prof-nom') || '';
        
        professeurIdInput.value = profId;
        professeurNomInput.value = profNom;
        
        // Update label to show professor name if available
        if (profNom) {
            professeurNomInput.style.color = '#1f2937'; // Dark gray text
        }
    } else {
        professeurIdInput.value = '';
        professeurNomInput.value = '';
        professeurNomInput.placeholder = 'Sélectionnez une matière pour voir le professeur';
    }
}

function updateMatieres() {
    // This function can be expanded if needed to filter matieres by filiere
    // For now, all matieres are already filtered server-side
    // But we should clear professor fields when filiere changes
    const professeurIdInput = document.getElementById('professeurId');
    const professeurNomInput = document.getElementById('professeurNom');
    const matiereSelect = document.getElementById('matiereId');
    
    if (matiereSelect) {
        matiereSelect.selectedIndex = 0; // Reset to first option
    }
    
    if (professeurIdInput) {
        professeurIdInput.value = '';
    }
    if (professeurNomInput) {
        professeurNomInput.value = '';
        professeurNomInput.placeholder = 'Sélectionnez une matière pour voir le professeur';
    }
    
    // Trigger update if a matiere is already selected
    if (matiereSelect && matiereSelect.value) {
        updateProfesseur();
    }
}

// Initialize professor name when page loads (for edit mode or if matiere is pre-selected)
document.addEventListener('DOMContentLoaded', function() {
    const matiereSelect = document.getElementById('matiereId');
    if (matiereSelect && matiereSelect.value) {
        updateProfesseur();
    }
    
    // Add change listener to filiere select if it exists
    const filiereSelect = document.getElementById('filiereId');
    if (filiereSelect) {
        filiereSelect.addEventListener('change', function() {
            // When filiere changes, we might need to reload matieres
            // For now, the server provides all matieres, so we just reset
            updateMatieres();
        });
    }
});
</script>

<jsp:include page="../common/footer.jsp"/>


