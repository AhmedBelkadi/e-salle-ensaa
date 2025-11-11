<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur - E-Salle ENSAA</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-gray-50">
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
                <% 
                    String message = (String) request.getAttribute("errorMessage");
                    if (message == null || message.isEmpty()) {
                        message = request.getParameter("message");
                    }
                    if (message == null || message.isEmpty()) {
                        message = "Une erreur inattendue s'est produite.";
                    }
                %>
                <%= message %>
            </p>

            <!-- Actions -->
            <div class="flex flex-col sm:flex-row gap-3 justify-center mt-8">
                <a href="<%= request.getContextPath() %>/auth/login" 
                   class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium h-10 px-6 bg-blue-600 hover:bg-blue-700 text-white transition">
                    <i class="fas fa-sign-in-alt w-4 h-4"></i>
                    Page de connexion
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
</body>
</html>
