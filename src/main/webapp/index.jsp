<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title != null ? param.title : 'E-Salle ENSAA'}</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-gray-50 min-h-screen flex">

 

    <!-- Main Content Wrapper -->
    <div class="flex-1 flex flex-col min-h-screen">

        <!-- Main Content -->
        <main class="flex-1 overflow-auto">


<!-- Home / Welcome page -->
<div class="min-h-screen bg-gradient-to-br from-blue-50 to-gray-100">
    <div class="container mx-auto px-6 py-12">
        <div class="flex flex-col lg:flex-row items-center gap-8">

            <!-- Hero -->
            <div class="lg:w-1/2">
                <h1 class="text-4xl font-extrabold text-gray-900 mb-4">Bienvenue sur E-Salle ENSAA</h1>
                <p class="text-lg text-gray-600 mb-6">Gestion centralisée des salles, réservations et emplois du temps pour l'ENSAA. Créez, planifiez et suivez vos réservations simplement.</p>

                <div class="flex flex-wrap gap-3">
                    <a href="${pageContext.request.contextPath}/salles" class="inline-block px-5 py-3 bg-blue-600 text-white rounded shadow hover:bg-blue-700">Voir les salles</a>
                    <a href="${pageContext.request.contextPath}/timetable" class="inline-block px-5 py-3 bg-gray-100 text-gray-800 rounded hover:bg-gray-200">Emplois du temps</a>
                    <a href="${pageContext.request.contextPath}/reservations" class="inline-block px-5 py-3 bg-gray-100 text-gray-800 rounded hover:bg-gray-200">Réservations</a>
                </div>

                <div class="mt-6 text-sm text-gray-500">
                    <p>Vous êtes nouveau ? <a href="${pageContext.request.contextPath}/register" class="text-blue-600 hover:underline">Créer un compte</a>.</p>
                </div>
            </div>

            <!-- Quick panel / greeting -->
            <div class="lg:w-1/2">
                <div class="bg-white rounded-lg shadow-md p-6">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <h2 class="text-xl font-semibold">Bonjour, ${sessionScope.user.nom}</h2>
                            <p class="text-gray-600 mt-2">Accédez rapidement à votre tableau de bord :</p>
                            <ul class="mt-4 space-y-2">
                                <li><a href="${pageContext.request.contextPath}/dashboard" class="text-blue-600 hover:underline">Mon tableau de bord</a></li>
                                <li><a href="${pageContext.request.contextPath}/reservations/mine" class="text-blue-600 hover:underline">Mes réservations</a></li>
                                <li><a href="${pageContext.request.contextPath}/auth/logout" class="text-red-600 hover:underline">Se déconnecter</a></li>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <h2 class="text-xl font-semibold">Commencez dès maintenant</h2>
                            <p class="text-gray-600 mt-2">Connectez-vous pour réserver une salle ou gérer votre planning.</p>
                            <div class="mt-4">
                                <a href="${pageContext.request.contextPath}/auth/login" class="inline-block px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">Se connecter</a>
                                <a href="${pageContext.request.contextPath}/register" class="inline-block ml-3 px-4 py-2 bg-gray-100 text-gray-800 rounded hover:bg-gray-200">S'inscrire</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="mt-6 text-xs text-gray-400">
                    <p>© ENSAA - Système de gestion des salles</p>
                </div>
            </div>

        </div>
    </div>
</div>

        </main>
        
        <!-- Footer -->
        <footer class="bg-white border-t border-gray-200 px-6 py-4 mt-auto">
            <div class="flex flex-col md:flex-row justify-between items-center text-sm text-gray-600">
                <div class="mb-2 md:mb-0">
                    <p>&copy; 2025 E-Salle ENSAA. Tous droits réservés.</p>
                </div>
                <div>
                    <p>Développé avec <span class="text-red-500">❤️</span> pour l'ENSAA</p>
                </div>
            </div>
        </footer>
    </div>


</body>
</html>


