<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Inscription - E-Salle ENSAA"/>
</jsp:include>

<div class="bg-gradient-to-br from-blue-50 to-indigo-100 min-h-screen flex items-center justify-center p-4">
    
    <div class="w-full max-w-2xl">
        <!-- Logo / Header -->
        <div class="text-center mb-8">
            <h1 class="text-4xl font-bold text-indigo-900 mb-2">E-Salle ENSAA</h1>
            <p class="text-gray-600">Créer un nouveau compte</p>
        </div>

        <!-- Register Card -->
        <div class="bg-white rounded-2xl shadow-xl p-8">
            <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Inscription</h2>

            <!-- Messages d'erreur -->
            <c:if test="${not empty error}">
                <div class="mb-4 p-4 bg-red-50 border-l-4 border-red-500 text-red-700 rounded">
                    <p class="font-medium">❌ Erreur</p>
                    <p class="text-sm">${error}</p>
                </div>
            </c:if>

            <!-- Formulaire d'inscription -->
            <form action="${pageContext.request.contextPath}/auth/register" method="post" class="space-y-6">
                
                <!-- Nom et Prénom -->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label for="nom" class="block text-sm font-medium text-gray-700 mb-2">
                            Nom <span class="text-red-500">*</span>
                        </label>
                        <input 
                            type="text" 
                            id="nom" 
                            name="nom" 
                            required
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                            placeholder="Nom"
                            value="${nom}"
                        >
                    </div>

                    <div>
                        <label for="prenom" class="block text-sm font-medium text-gray-700 mb-2">
                            Prénom <span class="text-red-500">*</span>
                        </label>
                        <input 
                            type="text" 
                            id="prenom" 
                            name="prenom" 
                            required
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                            placeholder="Prénom"
                            value="${prenom}"
                        >
                    </div>
                </div>

                <!-- Email -->
                <div>
                    <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
                        Email <span class="text-red-500">*</span>
                    </label>
                    <input 
                        type="email" 
                        id="email" 
                        name="email" 
                        required
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                        placeholder="votre.email@ensaa.ma"
                        value="${email}"
                    >
                </div>

                <!-- Téléphone -->
                <div>
                    <label for="telephone" class="block text-sm font-medium text-gray-700 mb-2">
                        Téléphone (optionnel)
                    </label>
                    <input 
                        type="tel" 
                        id="telephone" 
                        name="telephone" 
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                        placeholder="+212600000000"
                        value="${telephone}"
                    >
                    <p class="mt-1 text-xs text-gray-500">
                        Pour recevoir les notifications WhatsApp
                    </p>
                </div>

                <!-- Rôle -->
                <div>
                    <label for="role" class="block text-sm font-medium text-gray-700 mb-2">
                        Rôle <span class="text-red-500">*</span>
                    </label>
                    <select 
                        id="role" 
                        name="role" 
                        required
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                    >
                        <option value="">-- Sélectionnez votre rôle --</option>
                        <option value="PROFESSEUR" ${role == 'PROFESSEUR' ? 'selected' : ''}>Professeur</option>
                        <option value="COORDINATEUR" ${role == 'COORDINATEUR' ? 'selected' : ''}>Coordinateur de Filière</option>
                        <option value="MEMBRE_CLUB" ${role == 'MEMBRE_CLUB' ? 'selected' : ''}>Membre de Club</option>
                    </select>
                    <p class="mt-2 text-xs text-gray-500">
                        ℹ️ Votre inscription sera soumise à l'approbation de l'administrateur
                    </p>
                </div>

                <!-- Mot de passe -->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
                            Mot de passe <span class="text-red-500">*</span>
                        </label>
                        <input 
                            type="password" 
                            id="password" 
                            name="password" 
                            required
                            minlength="6"
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                            placeholder="••••••••"
                        >
                    </div>

                    <div>
                        <label for="passwordConfirm" class="block text-sm font-medium text-gray-700 mb-2">
                            Confirmer mot de passe <span class="text-red-500">*</span>
                        </label>
                        <input 
                            type="password" 
                            id="passwordConfirm" 
                            name="passwordConfirm" 
                            required
                            minlength="6"
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                            placeholder="••••••••"
                        >
                    </div>
                </div>

                <!-- Bouton d'inscription -->
                <button 
                    type="submit"
                    class="w-full bg-indigo-600 text-white py-3 rounded-lg font-semibold hover:bg-indigo-700 transition duration-200 shadow-lg hover:shadow-xl"
                >
                    S'inscrire
                </button>
            </form>

            <!-- Lien vers connexion -->
            <div class="mt-6 text-center">
                <p class="text-gray-600">
                    Vous avez déjà un compte ? 
                    <a href="${pageContext.request.contextPath}/auth/login" class="text-indigo-600 hover:text-indigo-800 font-semibold">
                        Se connecter
                    </a>
                </p>
            </div>
        </div>

        <!-- Footer -->
        <div class="text-center mt-6 text-gray-600 text-sm">
            <p>&copy; 2025 ENSAA - Tous droits réservés</p>
        </div>
    </div>
</div>

<!-- Validation JavaScript -->
<script>
    document.querySelector('form').addEventListener('submit', function(e) {
        const password = document.getElementById('password').value;
        const passwordConfirm = document.getElementById('passwordConfirm').value;
        
        if (password !== passwordConfirm) {
            e.preventDefault();
            alert('Les mots de passe ne correspondent pas !');
            return false;
        }
        
        if (password.length < 6) {
            e.preventDefault();
            alert('Le mot de passe doit contenir au moins 6 caractères !');
            return false;
        }
    });
</script>

<jsp:include page="../common/footer.jsp"/>

