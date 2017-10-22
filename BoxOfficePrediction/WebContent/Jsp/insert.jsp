<%@ page import="java.sql.*" %>
<% Class.forName("com.mysql.jdbc.Driver"); %>

<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert Page</title>
</head>

<body style="background-image:url('/images/tweed.png');">

<div id="container">
<h2>Details of movie <%=request.getAttribute("movie") %> inserted in Database Successfully... !!!</h2>
</div>


</body>
</html>


