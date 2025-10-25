<jsp:include page="WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Accueil - E-Salle ENSAA"/>
</jsp:include>

<div class="container mx-auto px-4 py-8">
    
    <!-- Hero Section -->
    <div class="bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-2xl shadow-2xl p-8 md:p-12 mb-8">
        <div class="flex flex-col md:flex-row items-center justify-between">
            <div class="md:w-2/3 mb-6 md:mb-0">
                <h1 class="text-4xl md:text-5xl font-bold mb-4">
                    🏫 E-Salle ENSAA
                </h1>
                <p class="text-xl md:text-2xl mb-6 text-indigo-100">
                    Système de gestion des salles de l'École Nationale Supérieure d'Architecture et d'Art
                </p>
                <div class="bg-white/10 backdrop-blur-sm rounded-lg p-4 border border-white/20">
                    <p class="text-indigo-100">
                        ℹ️ Bienvenue sur E-Salle ENSAA. Plateforme moderne de gestion des ressources académiques.
                    </p>
                </div>
            </div>
            <div class="md:w-1/3 text-center">
                <div class="text-8xl opacity-75">🏛️</div>
            </div>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <a href="${pageContext.request.contextPath}/salles/list" 
           class="bg-white rounded-xl shadow-lg p-6 hover:shadow-2xl transition transform hover:-translate-y-1">
            <div class="text-center">
                <div class="text-5xl mb-4">🚪</div>
                <h3 class="text-xl font-bold text-gray-800 mb-2">Gérer les Salles</h3>
                <p class="text-gray-600">Consulter, créer et modifier les salles</p>
            </div>
        </a>

        <a href="${pageContext.request.contextPath}/auth/login" 
           class="bg-white rounded-xl shadow-lg p-6 hover:shadow-2xl transition transform hover:-translate-y-1">
            <div class="text-center">
                <div class="text-5xl mb-4">🔐</div>
                <h3 class="text-xl font-bold text-gray-800 mb-2">Connexion</h3>
                <p class="text-gray-600">Accéder à votre espace personnel</p>
            </div>
        </a>

        <a href="${pageContext.request.contextPath}/auth/register" 
           class="bg-white rounded-xl shadow-lg p-6 hover:shadow-2xl transition transform hover:-translate-y-1">
            <div class="text-center">
                <div class="text-5xl mb-4">📝</div>
                <h3 class="text-xl font-bold text-gray-800 mb-2">Inscription</h3>
                <p class="text-gray-600">Créer un nouveau compte</p>
            </div>
        </a>
    </div>

    <!-- Features Section -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div class="bg-white rounded-xl shadow-md p-6 text-center">
            <div class="text-5xl mb-4">🔍</div>
            <h3 class="text-xl font-bold text-gray-800 mb-3">Recherche Avancée</h3>
            <p class="text-gray-600">
                Trouvez rapidement les salles selon vos critères : type, capacité, disponibilité.
            </p>
        </div>

        <div class="bg-white rounded-xl shadow-md p-6 text-center">
            <div class="text-5xl mb-4">⚙️</div>
            <h3 class="text-xl font-bold text-gray-800 mb-3">Gestion Complète</h3>
            <p class="text-gray-600">
                Gérez facilement toutes les informations des salles : création, modification, suppression.
            </p>
        </div>

        <div class="bg-white rounded-xl shadow-md p-6 text-center">
            <div class="text-5xl mb-4">📊</div>
            <h3 class="text-xl font-bold text-gray-800 mb-3">Statistiques</h3>
            <p class="text-gray-600">
                Consultez les statistiques d'utilisation et la disponibilité des salles en temps réel.
            </p>
        </div>
    </div>

    <!-- Modules Status -->
    <div class="bg-white rounded-xl shadow-lg p-8">
        <div class="flex items-center mb-6">
            <div class="text-4xl mr-4">🚀</div>
            <h2 class="text-3xl font-bold text-gray-800">État des Modules</h2>
        </div>
        
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <!-- Module Auth & Users -->
            <div class="flex items-center p-4 bg-green-50 border-l-4 border-green-500 rounded-lg">
                <span class="text-2xl mr-3">✅</span>
                <div>
                    <h4 class="font-bold text-green-800">Auth & Users</h4>
                    <p class="text-sm text-green-600">Authentification et gestion des utilisateurs</p>
                </div>
            </div>

            <!-- Module Salle -->
            <div class="flex items-center p-4 bg-green-50 border-l-4 border-green-500 rounded-lg">
                <span class="text-2xl mr-3">✅</span>
                <div>
                    <h4 class="font-bold text-green-800">Salle</h4>
                    <p class="text-sm text-green-600">Gestion des salles (COURS/TP/TD)</p>
                </div>
            </div>

            <!-- Module Filière -->
            <div class="flex items-center p-4 bg-yellow-50 border-l-4 border-yellow-500 rounded-lg">
                <span class="text-2xl mr-3">🚧</span>
                <div>
                    <h4 class="font-bold text-yellow-800">Filière</h4>
                    <p class="text-sm text-yellow-600">En développement...</p>
                </div>
            </div>

            <!-- Module Réclamation -->
            <div class="flex items-center p-4 bg-gray-50 border-l-4 border-gray-300 rounded-lg">
                <span class="text-2xl mr-3">⏳</span>
                <div>
                    <h4 class="font-bold text-gray-600">Réclamation</h4>
                    <p class="text-sm text-gray-500">À venir...</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Getting Started -->
    <div class="mt-8 bg-gradient-to-r from-blue-50 to-indigo-50 rounded-xl shadow-md p-8 border border-blue-200">
        <div class="flex items-center mb-6">
            <div class="text-4xl mr-4">📚</div>
            <h2 class="text-2xl font-bold text-gray-800">Documentation</h2>
        </div>
        
        <div class="space-y-3">
            <div class="flex items-start">
                <span class="text-indigo-600 font-bold mr-3">1.</span>
                <p class="text-gray-700">
                    Consultez <code class="bg-indigo-100 px-2 py-1 rounded text-sm">documents/dev1.tex</code> ou 
                    <code class="bg-indigo-100 px-2 py-1 rounded text-sm">documents/dev2.tex</code> pour votre guide de développement
                </p>
            </div>
            <div class="flex items-start">
                <span class="text-indigo-600 font-bold mr-3">2.</span>
                <p class="text-gray-700">
                    Suivez le <code class="bg-indigo-100 px-2 py-1 rounded text-sm">PROJECT_MANAGEMENT_PLAN.tex</code> pour la répartition des tâches
                </p>
            </div>
            <div class="flex items-start">
                <span class="text-indigo-600 font-bold mr-3">3.</span>
                <p class="text-gray-700">
                    Référez-vous à <code class="bg-indigo-100 px-2 py-1 rounded text-sm">STRATEGIE_GIT_GITHUB.tex</code> pour le workflow Git
                </p>
            </div>
            <div class="flex items-start">
                <span class="text-indigo-600 font-bold mr-3">4.</span>
                <p class="text-gray-700">
                    Consultez le <code class="bg-indigo-100 px-2 py-1 rounded text-sm">README.md</code> pour le setup rapide avec Docker
                </p>
            </div>
        </div>
    </div>

</div>

<jsp:include page="WEB-INF/views/common/footer.jsp"/>


