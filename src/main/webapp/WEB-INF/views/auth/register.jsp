<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Inscription - E-Salle ENSAA"/>
</jsp:include>

<div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-gray-100 p-4">
    <div class="w-full max-w-2xl">
        <!-- Register Card -->
        <div class="border border-gray-200 bg-white shadow-lg rounded-xl py-6">
            <!-- Header -->
            <div class="space-y-2 px-6">
                <div class="text-2xl text-gray-900 font-semibold">E-Salle ENSAA</div>
                <p class="text-gray-600 text-sm">Créer un nouveau compte</p>
            </div>

            <!-- Content -->
            <div class="space-y-6 px-6 mt-6">
                <!-- Messages -->
                <c:if test="${not empty error}">
                    <div class="p-3 text-sm text-red-600 bg-red-50 border border-red-200 rounded-md">
                        ${error}
                    </div>
                </c:if>

                <!-- Form -->
                <form action="${pageContext.request.contextPath}/auth/register" method="post" class="space-y-4">
                    
                    <!-- Nom et Prénom -->
                    <div class="grid grid-cols-2 gap-4">
                        <div class="space-y-2">
                            <label for="nom" class="text-gray-700 font-medium text-sm">
                                Nom <span class="text-red-600">*</span>
                            </label>
                            <input 
                                type="text" id="nom" name="nom" required value="${nom}"
                                placeholder="Nom"
                                class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 placeholder:text-gray-500 text-sm outline-none"
                            >
                        </div>

                        <div class="space-y-2">
                            <label for="prenom" class="text-gray-700 font-medium text-sm">
                                Prénom <span class="text-red-600">*</span>
                            </label>
                            <input 
                                type="text" id="prenom" name="prenom" required value="${prenom}"
                                placeholder="Prénom"
                                class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 placeholder:text-gray-500 text-sm outline-none"
                            >
                        </div>
                    </div>

                    <!-- Email -->
                    <div class="space-y-2">
                        <label for="email" class="text-gray-700 font-medium text-sm">
                            Email <span class="text-red-600">*</span>
                        </label>
                        <input 
                            type="email" id="email" name="email" required value="${email}"
                            placeholder="votre.email@ensaa.ma"
                            class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 placeholder:text-gray-500 text-sm outline-none"
                        >
                    </div>

                    <!-- Téléphone -->
                    <div class="space-y-2">
                        <label for="telephone" class="text-gray-700 font-medium text-sm">
                            Téléphone
                        </label>
                        <input 
                            type="tel" id="telephone" name="telephone" value="${telephone}"
                            placeholder="+212600000000"
                            class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 placeholder:text-gray-500 text-sm outline-none"
                        >
                        <p class="text-xs text-gray-500">Pour les notifications WhatsApp</p>
                    </div>

                    <!-- Rôle -->
                    <div class="space-y-2">
                        <label for="role" class="text-gray-700 font-medium text-sm">
                            Rôle <span class="text-red-600">*</span>
                        </label>
                        <select 
                            id="role" name="role" required
                            class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 text-sm outline-none"
                        >
                            <option value="">Sélectionnez votre rôle</option>
                            <option value="PROFESSEUR" ${role == 'PROFESSEUR' ? 'selected' : ''}>Professeur</option>
                            <option value="COORDINATEUR" ${role == 'COORDINATEUR' ? 'selected' : ''}>Coordinateur de Filière</option>
                            <option value="MEMBRE_CLUB" ${role == 'MEMBRE_CLUB' ? 'selected' : ''}>Membre de Club</option>
                        </select>
                    </div>

                    <!-- Mot de passe -->
                    <div class="grid grid-cols-2 gap-4">
                        <div class="space-y-2">
                            <label for="password" class="text-gray-700 font-medium text-sm">
                                Mot de passe <span class="text-red-600">*</span>
                            </label>
                            <input 
                                type="password" id="password" name="password" required
                                placeholder="Min 6 caractères"
                                class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 placeholder:text-gray-500 text-sm outline-none"
                            >
                        </div>

                        <div class="space-y-2">
                            <label for="confirmPassword" class="text-gray-700 font-medium text-sm">
                                Confirmer <span class="text-red-600">*</span>
                            </label>
                            <input 
                                type="password" id="confirmPassword" name="confirmPassword" required
                                placeholder="Confirmer mot de passe"
                                class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 text-gray-900 placeholder:text-gray-500 text-sm outline-none"
                            >
                        </div>
                    </div>

                    <!-- Info Box -->
                    <div class="bg-blue-50 border border-blue-200 rounded-md p-3">
                        <p class="text-sm text-blue-700 flex items-center gap-2">
                            <i class="fas fa-info-circle w-4 h-4"></i>
                            Votre inscription sera soumise à l'approbation de l'administrateur
                        </p>
                    </div>

                    <!-- Submit button -->
                    <button 
                        type="submit"
                        class="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-md py-2 transition"
                    >
                        S'inscrire
                    </button>
                </form>

                <!-- Link to login -->
                <div class="text-center">
                    <p class="text-sm text-gray-600">
                        Déjà un compte ? 
                        <a href="${pageContext.request.contextPath}/auth/login" class="text-blue-600 hover:underline font-medium">
                            Se connecter
                        </a>
                    </p>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <p class="text-center text-xs text-gray-600 mt-4">
            &copy; 2025 ENSAA - Tous droits réservés
        </p>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
