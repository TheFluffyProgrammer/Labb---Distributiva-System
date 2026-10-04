<%@ page import="java.util.List, webshop.ui.*" %>
<%
    String message = null;
    if ("POST".equals(request.getMethod())) {
        String action = request.getParameter("action");
        if ("remove".equals(action)) {
            message = UiController.removeFromCart(request);
        } else if ("order".equals(action)) {
            message = UiController.placeOrder(request);
        }
    }
    List<CartItemDTO> cartItems = UiController.getCartItems(session);
%>
<%@ include file="/WEB-INF/header.jspf" %>

<h2>Kundkorg</h2>

<% if (message != null) { %>
    <p class="message"><%= message %></p>
<% } %>

<% if (cartItems.isEmpty()) { %>
    <p>Kundkorgen är tom.</p>
<% } else { %>
    <table>
        <tr>
            <th>Vara</th><th>Pris</th><th>Antal</th><th>Summa</th><th></th>
        </tr>
    <% for (CartItemDTO ci : cartItems) { %>
        <tr>
            <td><%= ci.getName() %></td>
            <td><%= ci.getPrice() %> kr</td>
            <td><%= ci.getQuantity() %></td>
            <td><%= ci.getTotal() %> kr</td>
            <td>
                <form method="post" action="cart.jsp">
                    <input type="hidden" name="action" value="remove">
                    <input type="hidden" name="itemId" value="<%= ci.getItemId() %>">
                    <input type="submit" value="Ta bort">
                </form>
            </td>
        </tr>
    <% } %>
    </table>

    <p>Totalt: <b><%= UiController.getCartTotal(session) %> kr</b></p>

    <% if (currentUser == null) { %>
        <p><a href="login.jsp">Logga in</a> för att skicka ordern.</p>
    <% } else { %>
        <form method="post" action="cart.jsp">
            <input type="hidden" name="action" value="order">
            <input type="submit" value="Skicka order">
        </form>
    <% } %>
<% } %>

</body>
</html>
