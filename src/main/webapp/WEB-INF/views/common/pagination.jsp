<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Pagination Component - Reusable for all list views --%>
<c:if test="${totalPages > 1}">
    <%-- Build query string preserving all filters --%>
    <c:set var="queryParams" value=""/>
    <c:if test="${not empty param.type}">
        <c:set var="queryParams" value="${queryParams}&type=${param.type}"/>
    </c:if>
    <c:if test="${not empty param.disponible}">
        <c:set var="queryParams" value="${queryParams}&disponible=${param.disponible}"/>
    </c:if>
    <c:if test="${not empty param.capaciteMin}">
        <c:set var="queryParams" value="${queryParams}&capaciteMin=${param.capaciteMin}"/>
    </c:if>
    <c:if test="${not empty param.search}">
        <c:set var="queryParams" value="${queryParams}&search=${param.search}"/>
    </c:if>
    <c:if test="${not empty param.statut}">
        <c:set var="queryParams" value="${queryParams}&statut=${param.statut}"/>
    </c:if>
    <c:if test="${not empty param.urgence}">
        <c:set var="queryParams" value="${queryParams}&urgence=${param.urgence}"/>
    </c:if>
    <c:if test="${not empty param.cycle}">
        <c:set var="queryParams" value="${queryParams}&cycle=${param.cycle}"/>
    </c:if>
    <c:if test="${not empty param.annee}">
        <c:set var="queryParams" value="${queryParams}&annee=${param.annee}"/>
    </c:if>
    <c:if test="${not empty param.keyword}">
        <c:set var="queryParams" value="${queryParams}&keyword=${param.keyword}"/>
    </c:if>
    
    <div class="flex items-center justify-between border-t border-gray-200 bg-white px-4 py-3 sm:px-6 mt-6">
        <div class="flex flex-1 justify-between sm:hidden">
            <%-- Mobile Previous --%>
            <c:if test="${hasPreviousPage}">
                <a href="?page=${currentPage - 1}&size=${pageSize}${queryParams}"
                   class="relative inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50">
                    Précédent
                </a>
            </c:if>
            <c:if test="${!hasPreviousPage}">
                <span class="relative inline-flex items-center rounded-md border border-gray-300 bg-gray-100 px-4 py-2 text-sm font-medium text-gray-400 cursor-not-allowed">
                    Précédent
                </span>
            </c:if>
            
            <%-- Mobile Next --%>
            <c:if test="${hasNextPage}">
                <a href="?page=${currentPage + 1}&size=${pageSize}${queryParams}"
                   class="relative ml-3 inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50">
                    Suivant
                </a>
            </c:if>
            <c:if test="${!hasNextPage}">
                <span class="relative ml-3 inline-flex items-center rounded-md border border-gray-300 bg-gray-100 px-4 py-2 text-sm font-medium text-gray-400 cursor-not-allowed">
                    Suivant
                </span>
            </c:if>
        </div>
        
        <div class="hidden sm:flex sm:flex-1 sm:items-center sm:justify-between">
            <div>
                <p class="text-sm text-gray-700">
                    Affichage de
                    <span class="font-medium">${startIndex}</span>
                    à
                    <span class="font-medium">${endIndex}</span>
                    sur
                    <span class="font-medium">${totalItems}</span>
                    résultats
                </p>
            </div>
            <div>
                <nav class="isolate inline-flex -space-x-px rounded-md shadow-sm" aria-label="Pagination">
                    <%-- Previous Button --%>
                    <c:if test="${hasPreviousPage}">
                        <a href="?page=${currentPage - 1}&size=${pageSize}${queryParams}"
                           class="relative inline-flex items-center rounded-l-md px-2 py-2 text-gray-400 ring-1 ring-inset ring-gray-300 hover:bg-gray-50 focus:z-20 focus:outline-offset-0">
                            <span class="sr-only">Précédent</span>
                            <i class="fas fa-chevron-left w-4 h-4"></i>
                        </a>
                    </c:if>
                    <c:if test="${!hasPreviousPage}">
                        <span class="relative inline-flex items-center rounded-l-md px-2 py-2 text-gray-300 ring-1 ring-inset ring-gray-300 cursor-not-allowed">
                            <span class="sr-only">Précédent</span>
                            <i class="fas fa-chevron-left w-4 h-4"></i>
                        </span>
                    </c:if>
                    
                    <%-- Page Numbers --%>
                    <c:forEach var="i" begin="0" end="${totalPages - 1}">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <span aria-current="page" class="relative z-10 inline-flex items-center bg-blue-600 px-4 py-2 text-sm font-semibold text-white focus:z-20 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-blue-600">
                                    ${i + 1}
                                </span>
                            </c:when>
                            <c:when test="${i == 0 || i == totalPages - 1 || (i >= currentPage - 2 && i <= currentPage + 2)}">
                                <a href="?page=${i}&size=${pageSize}${queryParams}"
                                   class="relative inline-flex items-center px-4 py-2 text-sm font-semibold text-gray-900 ring-1 ring-inset ring-gray-300 hover:bg-gray-50 focus:z-20 focus:outline-offset-0">
                                    ${i + 1}
                                </a>
                            </c:when>
                            <c:when test="${i == currentPage - 3 || i == currentPage + 3}">
                                <span class="relative inline-flex items-center px-4 py-2 text-sm font-semibold text-gray-700 ring-1 ring-inset ring-gray-300">
                                    ...
                                </span>
                            </c:when>
                        </c:choose>
                    </c:forEach>
                    
                    <%-- Next Button --%>
                    <c:if test="${hasNextPage}">
                        <a href="?page=${currentPage + 1}&size=${pageSize}${queryParams}"
                           class="relative inline-flex items-center rounded-r-md px-2 py-2 text-gray-400 ring-1 ring-inset ring-gray-300 hover:bg-gray-50 focus:z-20 focus:outline-offset-0">
                            <span class="sr-only">Suivant</span>
                            <i class="fas fa-chevron-right w-4 h-4"></i>
                        </a>
                    </c:if>
                    <c:if test="${!hasNextPage}">
                        <span class="relative inline-flex items-center rounded-r-md px-2 py-2 text-gray-300 ring-1 ring-inset ring-gray-300 cursor-not-allowed">
                            <span class="sr-only">Suivant</span>
                            <i class="fas fa-chevron-right w-4 h-4"></i>
                        </span>
                    </c:if>
                </nav>
            </div>
        </div>
    </div>
</c:if>

