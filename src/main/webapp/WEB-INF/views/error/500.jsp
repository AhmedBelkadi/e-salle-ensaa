<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Erreur serveur - E-Salle ENSAA"/>
</jsp:include>

<div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-red-50 to-gray-100 p-4">
    <div class="text-center max-w-md">
        <!-- Error Icon -->
        <div class="mb-8">
            <div class="inline-flex items-center justify-center w-24 h-24 bg-red-100 rounded-full">
                <i class="fas fa-exclamation-triangle w-12 h-12 text-red-600"></i>
            </div>
        </div>

        <!-- Error Message -->
        <h1 class="text-6xl font-bold text-gray-900 mb-4">500</h1>
        <h2 class="text-2xl font-semibold text-gray-800 mb-4">Erreur serveur</h2>
        <p class="text-gray-600 mb-8">
            Une erreur interne s'est produite. Notre équipe a été notifiée et travaille sur une solution.
        </p>

        <!-- Actions -->
        <div class="flex flex-col sm:flex-row gap-3 justify-center">
            <a href="${pageContext.request.contextPath}/" 
               class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-10 px-6 bg-blue-600 hover:bg-blue-700 text-white transition">
                <i class="fas fa-home w-4 h-4"></i>
                Retour à l'accueil
            </a>
            <button 
                onclick="location.reload()"
                class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-10 px-6 border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 transition">
                <i class="fas fa-redo w-4 h-4"></i>
                Réessayer
            </button>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
