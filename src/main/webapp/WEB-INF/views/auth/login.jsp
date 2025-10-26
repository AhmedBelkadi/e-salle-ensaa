<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Connexion - E-Salle ENSAA"/>
</jsp:include>

<div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-gray-100 p-4">
    <div class="w-full max-w-md">
        <!-- Login Card -->
        <div class="border border-gray-200 bg-white shadow-lg rounded-xl py-6">
            <!-- Header -->
            <div class="space-y-2 px-6">
                <div class="text-2xl text-gray-900 font-semibold">E-Salle ENSAA</div>
                <p class="text-gray-600 text-sm">Connectez-vous à votre compte</p>
            </div>

            <!-- Content -->
            <div class="space-y-6 px-6 mt-6">
                <!-- Messages d'erreur -->
                <c:if test="${not empty error}">
                    <div class="p-3 text-sm text-red-600 bg-red-50 border border-red-200 rounded-md">
                        ${error}
                    </div>
                </c:if>

                <!-- Messages de succès -->
                <c:if test="${not empty success}">
                    <div class="p-3 text-sm text-green-600 bg-green-50 border border-green-200 rounded-md">
                        ${success}
                    </div>
                </c:if>

                <!-- Form -->
                <form action="${pageContext.request.contextPath}/auth/login" method="post" class="space-y-4">
                    <!-- Email -->
                    <div class="space-y-2">
                        <label for="email" class="text-gray-700 font-medium text-sm">Email</label>
                        <input
                            id="email"
                            name="email"
                            type="email"
                            placeholder="votre.email@ensaa.ma"
                            value="${email}"
                            class="bg-gray-50 border border-gray-300 text-gray-900 placeholder:text-gray-500 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 w-full rounded-md px-3 py-2 text-sm outline-none"
                            required
                        />
                    </div>

                    <!-- Password -->
                    <div class="space-y-2">
                        <label for="password" class="text-gray-700 font-medium text-sm">Mot de passe</label>
                        <input
                            id="password"
                            name="password"
                            type="password"
                            placeholder="Entrez votre mot de passe"
                            class="bg-gray-50 border border-gray-300 text-gray-900 placeholder:text-gray-500 focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-100 w-full rounded-md px-3 py-2 text-sm outline-none"
                            required
                        />
                    </div>

                    <!-- Submit button -->
                    <button
                        type="submit"
                        class="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-md py-2 transition"
                    >
                        Se connecter
                    </button>
                </form>

                <!-- Link to register -->
                <div class="text-center">
                    <p class="text-sm text-gray-600">
                        Pas encore de compte ? 
                        <a href="${pageContext.request.contextPath}/auth/register" class="text-blue-600 hover:underline font-medium">
                            S'inscrire
                        </a>
                    </p>
                </div>

                <!-- Divider -->
                <div class="relative">
                    <div class="absolute inset-0 flex items-center">
                        <div class="w-full border-t border-gray-300"></div>
                    </div>
                    <div class="relative flex justify-center text-sm">
                        <span class="px-2 bg-white text-gray-500">Comptes de Test</span>
                    </div>
                </div>

                <!-- Demo Accounts -->
                <div class="space-y-2">
                    <button
                        type="button"
                        onclick="document.getElementById('email').value='admin@ensaa.ma'; document.getElementById('password').value='Test@2024';"
                        class="w-full border border-gray-300 text-gray-700 hover:bg-gray-50 font-medium rounded-md py-2 transition text-sm"
                    >
                        👑 ADMIN: admin@ensaa.ma
                    </button>
                    <button
                        type="button"
                        onclick="document.getElementById('email').value='coordinateur@ensaa.ma'; document.getElementById('password').value='Test@2024';"
                        class="w-full border border-gray-300 text-gray-700 hover:bg-gray-50 font-medium rounded-md py-2 transition text-sm"
                    >
                        📚 COORDINATEUR: coordinateur@ensaa.ma
                    </button>
                    <button
                        type="button"
                        onclick="document.getElementById('email').value='professeur@ensaa.ma'; document.getElementById('password').value='Test@2024';"
                        class="w-full border border-gray-300 text-gray-700 hover:bg-gray-50 font-medium rounded-md py-2 transition text-sm"
                    >
                        👨‍🏫 PROFESSEUR: professeur@ensaa.ma
                    </button>
                    <button
                        type="button"
                        onclick="document.getElementById('email').value='club@ensaa.ma'; document.getElementById('password').value='Test@2024';"
                        class="w-full border border-gray-300 text-gray-700 hover:bg-gray-50 font-medium rounded-md py-2 transition text-sm"
                    >
                        🎭 MEMBRE_CLUB: club@ensaa.ma
                    </button>
                    <p class="text-xs text-gray-500 text-center mt-2">
                        Mot de passe: <span class="font-mono bg-gray-100 px-2 py-1 rounded">Test@2024</span>
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

