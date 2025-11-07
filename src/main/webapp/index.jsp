<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Rediriger vers le dashboard selon le rôle ou vers login
    if (session != null && session.getAttribute("user") != null) {
        com.esalle.entity.User user = (com.esalle.entity.User) session.getAttribute("user");
        String redirectUrl = "/";
        switch (user.getRole()) {
            case ADMIN:
                redirectUrl = request.getContextPath() + "/consultation/admin";
                break;
            case COORDINATEUR:
                redirectUrl = request.getContextPath() + "/consultation/coordinateur";
                break;
            case PROFESSEUR:
                redirectUrl = request.getContextPath() + "/consultation/professeur";
                break;
            case MEMBRE_CLUB:
                redirectUrl = request.getContextPath() + "/consultation/club";
                break;
        }
        response.sendRedirect(redirectUrl);
        return;
    } else {
        // Si non connecté, rediriger vers login
        response.sendRedirect(request.getContextPath() + "/auth/login");
        return;
    }
%>


