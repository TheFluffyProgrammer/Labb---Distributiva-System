<%@ page import="webshop.ui.*" %>
<%
    String message = null;
    if ("POST".equals(request.getMethod())) {
        message = UiController.login(request);
        if (message == null) {
            response.sendRedirect("index.jsp"); // inloggningen lyckades
            return;
        }
    }
%>
<%@ include file="/WEB-INF/header.jspf" %>

<h2>Logga in</h2>

<% if (message != null) { %>
    <p class="message"><%= message %></p>
<% } %>

<form method="post" action="login.jsp">
    <table>
        <tr>
            <td>Användarnamn</td>
            <td><input type="text" name="username"></td>
        </tr>
        <tr>
            <td>Lösenord</td>
            <td><input type="password" name="password"></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="Logga in"></td>
        </tr>
    </table>
</form>

</body>
</html>
