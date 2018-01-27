<%@ page import="tablecloth.viewmodel.UserViewmodel" %>
<!DOCTYPE html>
<g:set var="users" value="${(List<UserViewmodel>) users}"/>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>User management</title>
</head>

<body>
<g:if test="${flash.message}"><h2>${flash.message}</h2></g:if>
<g:each in="${users}" var="user" status="i">
    <g:render template="/templates/dbform/user" model="[i: i, user: user]"/>
</g:each>
</body>
</html>