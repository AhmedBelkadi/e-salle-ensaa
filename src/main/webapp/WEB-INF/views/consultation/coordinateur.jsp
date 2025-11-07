<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Dashboard Coordinateur - E-Salle ENSAA"/>
</jsp:include>

<div class="flex-1 p-6">
    <div class="space-y-6">
        <!-- Header -->
        <div>
            <h1 class="text-3xl font-bold text-gray-900">Dashboard Coordinateur</h1>
            <p class="text-gray-600 mt-2">Gestion de votre filière</p>
        </div>

        <!-- Stats Cards -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            <!-- Total Users -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Total Utilisateurs</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${totalUsers}</p>
                        </div>
                        <div class="bg-blue-500 p-3 rounded-lg">
                            <i class="fas fa-users text-white text-xl"></i>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Total Rooms -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Salles Utilisées</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${totalRooms}</p>
                        </div>
                        <div class="bg-green-500 p-3 rounded-lg">
                            <i class="fas fa-door-open text-white text-xl"></i>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Pending Issues -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Problèmes en Attente</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">0</p>
                        </div>
                        <div class="bg-red-500 p-3 rounded-lg">
                            <i class="fas fa-exclamation-triangle text-white text-xl"></i>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Utilization Rate -->
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-gray-600 text-sm">Taux d'Utilisation</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2">${utilizationRate}%</p>
                        </div>
                        <div class="bg-purple-500 p-3 rounded-lg">
                            <i class="fas fa-chart-line text-white text-xl"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Filière Info -->
        <c:if test="${not empty filiere}">
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <h2 class="text-lg font-semibold text-gray-900 mb-4">Ma Filière</h2>
                    <div class="grid grid-cols-2 gap-4">
                        <div>
                            <p class="text-sm text-gray-600">Nom</p>
                            <p class="text-base font-medium text-gray-900">${filiere.nom}</p>
                        </div>
                        <div>
                            <p class="text-sm text-gray-600">Cycle</p>
                            <p class="text-base font-medium text-gray-900">${filiere.cycle} - Année ${filiere.annee}</p>
                        </div>
                        <div>
                            <p class="text-sm text-gray-600">Effectif</p>
                            <p class="text-base font-medium text-gray-900">${filiere.effectif} étudiants</p>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Emploi du Temps Filière -->
        <c:if test="${not empty emploiFiliere}">
            <div class="bg-white flex flex-col gap-6 rounded-xl border border-gray-200 py-6 shadow-sm">
                <div class="px-6 pt-0">
                    <div class="flex items-center justify-between mb-4">
                        <div>
                            <h2 class="text-lg font-semibold text-gray-900">Emploi du Temps de la Filière</h2>
                            <p class="text-sm text-gray-600">Séances planifiées</p>
                        </div>
                        <a href="${pageContext.request.contextPath}/emploi/list?filiereId=${filiere.id}" 
                           class="text-sm text-blue-600 hover:text-blue-800">
                            Voir tout
                        </a>
                    </div>
                    <div class="space-y-2">
                        <c:forEach var="emploi" items="${emploiFiliere}" begin="0" end="4">
                            <div class="p-3 bg-gray-50 rounded-lg border border-gray-200">
                                <div class="flex items-center justify-between">
                                    <div>
                                        <p class="font-medium text-gray-900">${emploi.matiereNom}</p>
                                        <p class="text-sm text-gray-600">${emploi.jourSemaine} - ${emploi.heureDebut} à ${emploi.heureFin}</p>
                                    </div>
                                    <span class="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded">${emploi.salleNom}</span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>

