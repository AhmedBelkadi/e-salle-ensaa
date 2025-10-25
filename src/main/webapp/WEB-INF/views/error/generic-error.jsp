<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Erreur - E-Salle ENSAA"/>
</jsp:include>

<div class="container mx-auto px-4 py-16">
    <div class="max-w-3xl mx-auto">
        <!-- Error Header -->
        <div class="bg-red-600 text-white rounded-t-2xl shadow-xl p-6">
            <div class="flex items-center">
                <div class="text-4xl mr-4">⚠️</div>
                <h1 class="text-2xl font-bold">Erreur Système</h1>
            </div>
        </div>

        <!-- Error Body -->
        <div class="bg-white rounded-b-2xl shadow-xl p-8">
            <!-- Error Message -->
            <div class="bg-red-50 border-l-4 border-red-500 rounded-lg p-6 mb-8">
                <div class="flex items-start">
                    <div class="text-3xl mr-4">❌</div>
                    <div class="flex-1">
                        <h2 class="text-xl font-bold text-red-800 mb-2">
                            Une erreur s'est produite
                        </h2>
                        <c:choose>
                            <c:when test="${not empty errorMessage}">
                                <p class="text-red-700 font-medium">${errorMessage}</p>
                            </c:when>
                            <c:otherwise>
                                <p class="text-red-700 font-medium">
                                    Une erreur inattendue s'est produite lors du traitement de votre demande.
                                </p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- What to do -->
            <div class="bg-blue-50 border border-blue-200 rounded-xl p-6 mb-8">
                <h3 class="text-lg font-semibold text-blue-800 mb-4 flex items-center">
                    <span class="text-2xl mr-2">💡</span>
                    Que pouvez-vous faire ?
                </h3>
                <ul class="space-y-3 text-gray-700">
                    <li class="flex items-start">
                        <span class="bg-blue-200 text-blue-800 rounded-full w-6 h-6 flex items-center justify-center mr-3 mt-0.5 flex-shrink-0">1</span>
                        <span>Vérifiez que l'URL est correcte</span>
                    </li>
                    <li class="flex items-start">
                        <span class="bg-blue-200 text-blue-800 rounded-full w-6 h-6 flex items-center justify-center mr-3 mt-0.5 flex-shrink-0">2</span>
                        <span>Réessayez dans quelques instants</span>
                    </li>
                    <li class="flex items-start">
                        <span class="bg-blue-200 text-blue-800 rounded-full w-6 h-6 flex items-center justify-center mr-3 mt-0.5 flex-shrink-0">3</span>
                        <span>Contactez l'administrateur si le problème persiste</span>
                    </li>
                </ul>
            </div>

            <!-- Technical Details (if available) -->
            <c:if test="${not empty exception}">
                <details class="bg-gray-50 rounded-xl p-6 mb-8">
                    <summary class="cursor-pointer font-semibold text-gray-700 hover:text-gray-900">
                        🔧 Détails techniques (pour les développeurs)
                    </summary>
                    <div class="mt-4 p-4 bg-gray-100 rounded-lg overflow-x-auto">
                        <pre class="text-sm text-gray-800">${exception}</pre>
                    </div>
                </details>
            </c:if>

            <!-- Action Buttons -->
            <div class="flex flex-col sm:flex-row gap-4 justify-center">
                <a href="${pageContext.request.contextPath}/" 
                   class="bg-indigo-600 text-white px-8 py-3 rounded-lg font-semibold hover:bg-indigo-700 transition transform hover:-translate-y-1 shadow-lg text-center">
                    🏠 Retour à l'accueil
                </a>
                <button onclick="history.back()" 
                        class="bg-white text-gray-700 border-2 border-gray-300 px-8 py-3 rounded-lg font-semibold hover:bg-gray-50 transition transform hover:-translate-y-1 shadow-lg">
                    ← Page précédente
                </button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
