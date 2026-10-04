<%@ page import="java.util.List, webshop.ui.*" %>
<%
    // Bara admin
    if (!UiController.isAdmin(session)) {
        response.sendRedirect("login.jsp");
        return;
    }
    String message = null;
    if ("POST".equals(request.getMethod())) {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            message = UiController.addUser(request);
        } else if ("role".equals(action)) {
            message = UiController.setRole(request);
        } else if ("active".equals(action)) {
            message = UiController.setActive(request);
        }
    }
    List<UserDTO> users = UiController.getAllUsers();
    String[] roles = UiController.getRoles();
%>
<%@ include file="/WEB-INF/header.jspf" %>

<h2>Användare</h2>

<% if (message != null) { %>
    <p class="message"><%= message %></p>
<% } %>

<table>
    <tr>
        <th>Användarnamn</th><th>Roll</th><th>Status</th>
    </tr>
<% for (UserDTO user : users) { %>
    <tr>
        <td><%= user.getUsername() %></td>
        <td>
            <form method="post" action="admin.jsp">
                <input type="hidden" name="action" value="role">
                <input type="hidden" name="userId" value="<%= user.getId() %>">
                <select name="role">
                <% for (String role : roles) { %>
                    <option value="<%= role %>" <%= role.equals(user.getRole()) ? "selected" : "" %>><%= role %></option>
                <% } %>
                </select>
                <input type="submit" value="Ändra roll">
            </form>
        </td>
        <td>
            <form method="post" action="admin.jsp">
                <input type="hidden" name="action" value="active">
                <input type="hidden" name="userId" value="<%= user.getId() %>">
                <% if (user.isActive()) { %>
                    Aktiv
                    <input type="hidden" name="active" value="false">
                    <input type="submit" value="Stäng av">
                <% } else { %>
                    Avstängd
                    <input type="hidden" name="active" value="true">
                    <input type="submit" value="Aktivera">
                <% } %>
            </form>
        </td>
    </tr>
<% } %>
</table>

<h3>Lägg till användare</h3>
<form method="post" action="admin.jsp">
    <input type="hidden" name="action" value="add">
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
            <td>Roll</td>
            <td>
                <select name="role">
                <% for (String role : roles) { %>
                    <option value="<%= role %>"><%= role %></option>
                <% } %>
                </select>
            </td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="Lägg till"></td>
        </tr>
    </table>
</form>

</body>
</html>
