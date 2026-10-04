<%@ page import="java.util.List, webshop.ui.*" %>
<%
    if (!UiController.isLoggedIn(session)) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<OrderDTO> orders = UiController.getMyOrders(session);
%>
<%@ include file="/WEB-INF/header.jspf" %>

<h2>Mina ordrar</h2>

<% if (orders.isEmpty()) { %>
    <p>Du har inga ordrar än.</p>
<% } %>

<% for (OrderDTO order : orders) { %>
    <h3>Order <%= order.getId() %> - <%= order.getCreated() %></h3>
    <table>
        <tr>
            <th>Vara</th><th>Antal</th><th>Pris</th>
        </tr>
    <% for (OrderLineDTO line : order.getLines()) { %>
        <tr>
            <td><%= line.getItemName() %></td>
            <td><%= line.getQuantity() %></td>
            <td><%= line.getPrice() %> kr</td>
        </tr>
    <% } %>
    </table>
    <p>Totalt: <%= order.getTotal() %> kr</p>
    <p>Status: <b><%= order.getStatus() %></b></p>
<% } %>

</body>
</html>
