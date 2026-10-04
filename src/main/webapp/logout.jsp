<%@ page import="webshop.ui.*" %>
<%
    UiController.logout(session);
    response.sendRedirect("index.jsp");
%>
