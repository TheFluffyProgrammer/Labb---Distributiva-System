<%@ page import="java.util.List, webshop.ui.*" %>
<%
    String message = null;
    if ("POST".equals(request.getMethod())) {
        message = UiController.addToCart(request);
    }
    List<ItemDTO> items = UiController.getAllItems();
%>
<%@ include file="/WEB-INF/header.jspf" %>

<h2>Varor</h2>

<% if (message != null) { %>
    <p class="message"><%= message %></p>
<% } %>

<table>
    <tr>
        <th>Vara</th><th>Beskrivning</th><th>Kategori</th><th>Pris</th><th>I lager</th><th></th>
    </tr>
<% for (ItemDTO item : items) { %>
    <tr>
        <td><%= item.getName() %></td>
        <td><%= item.getDescription() %></td>
        <td><%= item.getCategory() %></td>
        <td><%= item.getPrice() %> kr</td>
        <% if (item.isInStock()) { %>
            <td><%= item.getStock() %> st</td>
            <td>
                <form method="post" action="index.jsp">
                    <input type="hidden" name="itemId" value="<%= item.getId() %>">
                    <input type="text" name="quantity" value="1" size="3">
                    <input type="submit" value="Lägg i korgen">
                </form>
            </td>
        <% } else { %>
            <td>Slut i lager</td>
            <td></td>
        <% } %>
    </tr>
<% } %>
</table>

</body>
</html>
