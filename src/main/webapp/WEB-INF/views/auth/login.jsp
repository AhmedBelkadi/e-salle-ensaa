<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - E-Salle ENSAA</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gradient-to-br from-blue-50 to-indigo-100 min-h-screen flex items-center justify-center p-4">
    
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

            <!-- Compte admin par défaut -->
            <div class="mt-6 p-4 bg-gray-50 rounded-lg border border-gray-200">
                <p class="text-xs text-gray-500 text-center mb-2">
                    <strong>Compte Admin par défaut :</strong>
                </p>
                <p class="text-xs text-gray-600 text-center">
                    Email: <code class="bg-gray-200 px-2 py-1 rounded">admin@ensaa.ma</code><br>
                    Mot de passe: <code class="bg-gray-200 px-2 py-1 rounded">admin123</code>
                </p>
            </div>
        </div>

        <!-- Footer -->
        <div class="text-center mt-6 text-gray-600 text-sm">
            <p>&copy; 2025 ENSAA - Tous droits réservés</p>
        </div>
    </div>

</body>
</html>

