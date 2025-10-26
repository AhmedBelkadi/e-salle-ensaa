<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Erreur - E-Salle ENSAA"/>
</jsp:include>

<div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-orange-50 to-gray-100 p-4">
    <div class="text-center max-w-md">
        <!-- Error Icon -->
        <div class="mb-8">
            <div class="inline-flex items-center justify-center w-24 h-24 bg-orange-100 rounded-full">
                <i class="fas fa-exclamation-circle w-12 h-12 text-orange-600"></i>
            </div>
        </div>

        <!-- Error Message -->
        <h1 class="text-4xl font-bold text-gray-900 mb-4">Une erreur est survenue</h1>
        <p class="text-gray-600 mb-2">
            ${not empty errorMessage ? errorMessage : 'Une erreur inattendue s\'est produite.'}
        </p>
        
        <c:if test="${not empty errorDetails}">
            <details class="mt-4 text-left bg-gray-100 rounded-lg p-4">
                <summary class="cursor-pointer text-sm text-gray-700 font-medium">Détails techniques</summary>
                <pre class="mt-2 text-xs text-gray-600 overflow-auto">${errorDetails}</pre>
            </details>
        </c:if>

        <!-- Actions -->
        <div class="flex flex-col sm:flex-row gap-3 justify-center mt-8">
            <a href="${pageContext.request.contextPath}/" 
               class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-10 px-6 bg-blue-600 hover:bg-blue-700 text-white transition">
                <i class="fas fa-home w-4 h-4"></i>
                Retour à l'accueil
            </a>
            <button 
                onclick="history.back()"
                class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-10 px-6 border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 transition">
                <i class="fas fa-arrow-left w-4 h-4"></i>
                Page précédente
            </button>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
