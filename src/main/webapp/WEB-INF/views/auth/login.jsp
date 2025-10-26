<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Connexion - E-Salle ENSAA"/>
</jsp:include>

<div class="bg-gradient-to-br from-blue-50 to-indigo-100 min-h-screen flex items-center justify-center p-4">
    
    <div class="w-full max-w-md">
        <!-- Logo / Header -->
        <div class="text-center mb-8">
            <h1 class="text-4xl font-bold text-indigo-900 mb-2">E-Salle ENSAA</h1>
            <p class="text-gray-600">Système de Gestion des Salles</p>
        </div>

        <!-- Login Card -->
        <div class="bg-white rounded-2xl shadow-xl p-8">
            <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Connexion</h2>

            <!-- Messages d'erreur -->
            <c:if test="${not empty error}">
                <div class="mb-4 p-4 bg-red-50 border-l-4 border-red-500 text-red-700 rounded">
                    <p class="font-medium">❌ Erreur</p>
                    <p class="text-sm">${error}</p>
                </div>
            </c:if>

            <!-- Messages de succès -->
            <c:if test="${not empty success}">
                <div class="mb-4 p-4 bg-green-50 border-l-4 border-green-500 text-green-700 rounded">
                    <p class="font-medium">✅ Succès</p>
                    <p class="text-sm">${success}</p>
                </div>
            </c:if>

            <!-- Formulaire de connexion -->
            <form action="${pageContext.request.contextPath}/auth/login" method="post" class="space-y-6">
                
                <!-- Email -->
                <div>
                    <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
                        Email
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

                <!-- Mot de passe -->
                <div>
                    <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
                        Mot de passe
                    </label>
                    <input 
                        type="password" 
                        id="password" 
                        name="password" 
                        required
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                        placeholder="••••••••"
                    >
                </div>

                <!-- Bouton de connexion -->
                <button 
                    type="submit"
                    class="w-full bg-indigo-600 text-white py-3 rounded-lg font-semibold hover:bg-indigo-700 transition duration-200 shadow-lg hover:shadow-xl"
                >
                    Se connecter
                </button>
            </form>

            <!-- Lien vers inscription -->
            <div class="mt-6 text-center">
                <p class="text-gray-600">
                    Pas encore de compte ? 
                    <a href="${pageContext.request.contextPath}/auth/register" class="text-indigo-600 hover:text-indigo-800 font-semibold">
                        S'inscrire
                    </a>
                </p>
            </div>

            <!-- Comptes de test disponibles -->
            <div class="mt-6 p-4 bg-gradient-to-br from-blue-50 to-indigo-50 rounded-lg border-2 border-indigo-200">
                <p class="text-sm font-semibold text-indigo-900 text-center mb-3">
                    🧪 Comptes de Test (Dev)
                </p>
                <div class="space-y-2 text-xs">
                    <!-- ADMIN -->
                    <div class="flex items-center justify-between p-2 bg-white rounded border border-red-200 hover:shadow-md transition cursor-pointer"
                         onclick="document.getElementById('email').value='admin@ensaa.ma'; document.getElementById('password').value='Test@2024';">
                        <div class="flex items-center gap-2">
                            <span class="px-2 py-1 bg-red-100 text-red-800 rounded text-xs font-semibold">👑 ADMIN</span>
                            <code class="text-gray-700">admin@ensaa.ma</code>
                        </div>
                        <span class="text-gray-400 text-xs">Cliquer pour remplir</span>
                    </div>
                    
                    <!-- COORDINATEUR -->
                    <div class="flex items-center justify-between p-2 bg-white rounded border border-green-200 hover:shadow-md transition cursor-pointer"
                         onclick="document.getElementById('email').value='coordinateur@ensaa.ma'; document.getElementById('password').value='Test@2024';">
                        <div class="flex items-center gap-2">
                            <span class="px-2 py-1 bg-green-100 text-green-800 rounded text-xs font-semibold">📚 COORDINATEUR</span>
                            <code class="text-gray-700">coordinateur@ensaa.ma</code>
                        </div>
                        <span class="text-gray-400 text-xs">Cliquer pour remplir</span>
                    </div>
                    
                    <!-- PROFESSEUR -->
                    <div class="flex items-center justify-between p-2 bg-white rounded border border-blue-200 hover:shadow-md transition cursor-pointer"
                         onclick="document.getElementById('email').value='professeur@ensaa.ma'; document.getElementById('password').value='Test@2024';">
                        <div class="flex items-center gap-2">
                            <span class="px-2 py-1 bg-blue-100 text-blue-800 rounded text-xs font-semibold">👨‍🏫 PROFESSEUR</span>
                            <code class="text-gray-700">professeur@ensaa.ma</code>
                        </div>
                        <span class="text-gray-400 text-xs">Cliquer pour remplir</span>
                    </div>
                    
                    <!-- MEMBRE_CLUB -->
                    <div class="flex items-center justify-between p-2 bg-white rounded border border-purple-200 hover:shadow-md transition cursor-pointer"
                         onclick="document.getElementById('email').value='club@ensaa.ma'; document.getElementById('password').value='Test@2024';">
                        <div class="flex items-center gap-2">
                            <span class="px-2 py-1 bg-purple-100 text-purple-800 rounded text-xs font-semibold">🎭 MEMBRE_CLUB</span>
                            <code class="text-gray-700">club@ensaa.ma</code>
                        </div>
                        <span class="text-gray-400 text-xs">Cliquer pour remplir</span>
                    </div>
                </div>
                <p class="text-xs text-gray-500 text-center mt-3">
                    Mot de passe pour tous: <code class="bg-white px-2 py-1 rounded font-mono">Test@2024</code>
                </p>
            </div>
        </div>

        <!-- Footer -->
        <div class="text-center mt-6 text-gray-600 text-sm">
            <p>&copy; 2025 ENSAA - Tous droits réservés</p>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>

