<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="404 - Page non trouvée"/>
</jsp:include>

<div class="container mx-auto px-4 py-16">
    <div class="max-w-2xl mx-auto text-center">
        <!-- Error Icon & Code -->
        <div class="mb-8">
            <div class="text-9xl font-bold text-gray-300 mb-4">404</div>
            <div class="text-6xl mb-6">🔍</div>
        </div>

        <!-- Error Message -->
        <div class="bg-white rounded-2xl shadow-xl p-8 mb-8">
            <h1 class="text-3xl font-bold text-gray-800 mb-4">
                Page non trouvée
            </h1>
            <p class="text-lg text-gray-600 mb-4">
                Désolé, la page que vous recherchez n'existe pas.
            </p>
            <p class="text-gray-500">
                Il est possible que l'URL soit incorrecte ou que la page ait été déplacée.
            </p>
        </div>

        <!-- Action Buttons -->
        <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <a href="${pageContext.request.contextPath}/" 
               class="bg-indigo-600 text-white px-8 py-3 rounded-lg font-semibold hover:bg-indigo-700 transition transform hover:-translate-y-1 shadow-lg">
                🏠 Retour à l'accueil
            </a>
            <a href="${pageContext.request.contextPath}/salles/list" 
               class="bg-white text-indigo-600 border-2 border-indigo-600 px-8 py-3 rounded-lg font-semibold hover:bg-indigo-50 transition transform hover:-translate-y-1 shadow-lg">
                🚪 Voir les salles
            </a>
        </div>

        <!-- Helpful Links -->
        <div class="mt-12 bg-gray-50 rounded-xl p-6">
            <h3 class="text-lg font-semibold text-gray-700 mb-4">Pages populaires :</h3>
            <div class="flex flex-wrap gap-3 justify-center">
                <a href="${pageContext.request.contextPath}/auth/login" 
                   class="text-indigo-600 hover:text-indigo-800 hover:underline">
                    Connexion
                </a>
                <span class="text-gray-300">•</span>
                <a href="${pageContext.request.contextPath}/auth/register" 
                   class="text-indigo-600 hover:text-indigo-800 hover:underline">
                    Inscription
                </a>
                <span class="text-gray-300">•</span>
                <a href="${pageContext.request.contextPath}/salles/list" 
                   class="text-indigo-600 hover:text-indigo-800 hover:underline">
                    Liste des salles
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
