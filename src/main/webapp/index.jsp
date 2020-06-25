<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html xml:lang="en">
<head>
    <title>Redirect</title>
</head>
<%
    response.setStatus(307);
    response.setHeader("Location", "webjars/swagger-ui/3.18.2/index.html?url=/openapi");
    response.setHeader("Connection", "close");
%>
</html>
