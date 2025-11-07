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
                <h1 class="text-3xl font-bold text-gray-900">Nouvelle Séance</h1>
                <p class="text-gray-600 mt-2">Créer une nouvelle séance dans l'emploi du temps</p>
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
                
                <!-- Filière -->
                <div>
                    <label for="filiereId" class="block text-sm font-medium text-gray-700 mb-2">
                        Filière <span class="text-red-500">*</span>
                    </label>
                    <select name="filiereId" id="filiereId" required onchange="updateMatieres()"
                            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">Sélectionner une filière</option>
                        <c:forEach var="filiere" items="${filieres}">
                            <option value="${filiere.id}">${filiere.nom} - ${filiere.cycle} ${filiere.annee}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Année -->
                <div>
                    <label for="annee" class="block text-sm font-medium text-gray-700 mb-2">
                        Année <span class="text-red-500">*</span>
                    </label>
                    <select name="annee" id="annee" required
                            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="1">Année 1</option>
                        <option value="2">Année 2</option>
                        <option value="3">Année 3</option>
                    </select>
                </div>

                <!-- Matière -->
                <div>
                    <label for="matiereId" class="block text-sm font-medium text-gray-700 mb-2">
                        Matière <span class="text-red-500">*</span>
                    </label>
                    <select name="matiereId" id="matiereId" required onchange="updateProfesseur()"
                            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">Sélectionner une matière</option>
                        <c:forEach var="mat" items="${matieres}">
                            <option value="${mat.id}" data-prof="${mat.professeurId}">${mat.nom}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Professeur (auto-rempli) -->
                <div>
                    <label for="professeurId" class="block text-sm font-medium text-gray-700 mb-2">
                        Professeur <span class="text-red-500">*</span>
                    </label>
                    <input type="number" name="professeurId" id="professeurId" required readonly
                           class="w-full rounded-md border border-gray-300 px-3 py-2 bg-gray-50 focus:outline-none">
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
                            <option value="${salle.id}">${salle.nom} (${salle.type})</option>
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
                        <option value="LUNDI">Lundi</option>
                        <option value="MARDI">Mardi</option>
                        <option value="MERCREDI">Mercredi</option>
                        <option value="JEUDI">Jeudi</option>
                        <option value="VENDREDI">Vendredi</option>
                        <option value="SAMEDI">Samedi</option>
                    </select>
                </div>

                <!-- Heures -->
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="heureDebut" class="block text-sm font-medium text-gray-700 mb-2">
                            Heure de début <span class="text-red-500">*</span>
                        </label>
                        <input type="time" name="heureDebut" id="heureDebut" required
                               class="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                    <div>
                        <label for="heureFin" class="block text-sm font-medium text-gray-700 mb-2">
                            Heure de fin <span class="text-red-500">*</span>
                        </label>
                        <input type="time" name="heureFin" id="heureFin" required
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
                        <option value="COURS">Cours</option>
                        <option value="TD">TD</option>
                        <option value="TP">TP</option>
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
                        <option value="1">Groupe 1</option>
                        <option value="2">Groupe 2</option>
                    </select>
                </div>

                <!-- Buttons -->
                <div class="flex gap-4 justify-end">
                    <a href="${pageContext.request.contextPath}/emploi/list"
                       class="px-4 py-2 rounded-md border border-gray-300 text-gray-700 hover:bg-gray-50">
                        Annuler
                    </a>
                    <button type="submit"
                            class="px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700">
                        Créer la séance
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function updateProfesseur() {
    const matiereSelect = document.getElementById('matiereId');
    const professeurInput = document.getElementById('professeurId');
    const selectedOption = matiereSelect.options[matiereSelect.selectedIndex];
    if (selectedOption.value) {
        professeurInput.value = selectedOption.getAttribute('data-prof');
    } else {
        professeurInput.value = '';
    }
}
</script>

<jsp:include page="../common/footer.jsp"/>

