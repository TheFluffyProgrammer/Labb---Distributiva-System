<%@ page isErrorPage="true" %>
<%-- Visas när en sida kastar ett undantag, t.ex. om databasen inte går att nå. --%>
<% application.log("Fel i webbshoppen", exception); %>
<html>
<head>
<title>Fel</title>
<link rel="stylesheet" href="style.css">
</head>
<body>
<h1>Något gick fel</h1>
<p><%= exception.getMessage() %></p>
<% if (exception.getCause() != null) { %>
    <p>Orsak: <%= exception.getCause().getMessage() %></p>
<% } %>
<p><a href="index.jsp">Till startsidan</a></p>
</body>
</html>
