<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="500 - Erreur serveur"/>
</jsp:include>

<div class="container mx-auto px-4 py-16">
    <div class="max-w-2xl mx-auto text-center">
        <!-- Error Icon & Code -->
        <div class="mb-8">
            <div class="text-9xl font-bold text-red-300 mb-4">500</div>
            <div class="text-6xl mb-6">⚠️</div>
        </div>

        <!-- Error Message -->
        <div class="bg-white rounded-2xl shadow-xl p-8 mb-8 border-l-4 border-red-500">
            <h1 class="text-3xl font-bold text-gray-800 mb-4">
                Erreur interne du serveur
            </h1>
            <p class="text-lg text-gray-600 mb-4">
                Une erreur inattendue s'est produite.
            </p>
            <p class="text-gray-500">
                Notre équipe technique a été notifiée et travaille à résoudre le problème.
            </p>
        </div>

        <!-- What to do -->
        <div class="bg-yellow-50 border border-yellow-200 rounded-xl p-6 mb-8 text-left">
            <h3 class="text-lg font-semibold text-yellow-800 mb-3">💡 Que pouvez-vous faire ?</h3>
            <ul class="space-y-2 text-gray-700">
                <li class="flex items-start">
                    <span class="text-yellow-600 mr-2">•</span>
                    <span>Rafraîchir la page dans quelques instants</span>
                </li>
                <li class="flex items-start">
                    <span class="text-yellow-600 mr-2">•</span>
                    <span>Vérifier votre connexion internet</span>
                </li>
                <li class="flex items-start">
                    <span class="text-yellow-600 mr-2">•</span>
                    <span>Revenir à la page précédente</span>
                </li>
                <li class="flex items-start">
                    <span class="text-yellow-600 mr-2">•</span>
                    <span>Contacter l'administrateur si le problème persiste</span>
                </li>
            </ul>
        </div>

        <!-- Action Buttons -->
        <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <a href="${pageContext.request.contextPath}/" 
               class="bg-indigo-600 text-white px-8 py-3 rounded-lg font-semibold hover:bg-indigo-700 transition transform hover:-translate-y-1 shadow-lg">
                🏠 Retour à l'accueil
            </a>
            <button onclick="history.back()" 
                    class="bg-white text-gray-700 border-2 border-gray-300 px-8 py-3 rounded-lg font-semibold hover:bg-gray-50 transition transform hover:-translate-y-1 shadow-lg">
                ← Page précédente
            </button>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
