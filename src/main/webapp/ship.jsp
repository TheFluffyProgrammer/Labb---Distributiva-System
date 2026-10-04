<%@ page import="java.util.List, webshop.ui.*" %>
<%
    // Bara lagerpersonal och admin
    if (!UiController.isWarehouse(session)) {
        response.sendRedirect("login.jsp");
        return;
    }
    String message = null;
    if ("POST".equals(request.getMethod())) {
        message = UiController.shipOrder(request);
    }
    List<OrderDTO> orders = UiController.getOrdersToShip();
%>
<%@ include file="/WEB-INF/header.jspf" %>

<h2>Ordrar att skicka</h2>

<% if (message != null) { %>
    <p class="message"><%= message %></p>
<% } %>

<% if (orders.isEmpty()) { %>
    <p>Det finns inga ordrar att skicka.</p>
<% } %>

<% for (OrderDTO order : orders) { %>
    <h3>Order <%= order.getId() %> - <%= order.getCreated() %></h3>
    <table>
        <tr>
            <th>Vara</th><th>Antal</th>
        </tr>
    <% for (OrderLineDTO line : order.getLines()) { %>
        <tr>
            <td><%= line.getItemName() %></td>
            <td><%= line.getQuantity() %></td>
        </tr>
    <% } %>
    </table>
    <form method="post" action="ship.jsp">
        <input type="hidden" name="orderId" value="<%= order.getId() %>">
        <p><input type="submit" value="Skicka"></p>
    </form>
<% } %>

</body>
</html>
