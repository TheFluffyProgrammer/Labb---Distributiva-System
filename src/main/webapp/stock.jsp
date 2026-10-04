<%@ page import="java.util.List, webshop.ui.*" %>
<%
    // Bara lagerpersonal och admin
    if (!UiController.isWarehouse(session)) {
        response.sendRedirect("login.jsp");
        return;
    }
    String message = null;
    if ("POST".equals(request.getMethod())) {
        message = UiController.updateStock(request);
    }
    List<ItemDTO> items = UiController.getAllItems();
%>
<%@ include file="/WEB-INF/header.jspf" %>

<h2>Varulager</h2>

<% if (message != null) { %>
    <p class="message"><%= message %></p>
<% } %>

<table>
    <tr>
        <th>Vara</th><th>I lager</th><th>Nytt antal</th>
    </tr>
<% for (ItemDTO item : items) { %>
    <tr>
        <td><%= item.getName() %></td>
        <td><%= item.getStock() %></td>
        <td>
            <form method="post" action="stock.jsp">
                <input type="hidden" name="itemId" value="<%= item.getId() %>">
                <input type="text" name="stock" value="<%= item.getStock() %>" size="4">
                <input type="submit" value="Spara">
            </form>
        </td>
    </tr>
<% } %>
</table>

</body>
</html>
