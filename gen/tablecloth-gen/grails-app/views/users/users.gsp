<%@ page import="tablecloth.gen.viewmodel.UserDisplayViewmodel" %>
<!DOCTYPE html>
<g:set var="users" value="${(List<UserDisplayViewmodel>) users}"/>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>User management</title>
</head>

<body>
<g:if test="${flash.message}"><h2>${flash.message}</h2></g:if>
<g:each in="${users}" var="user" status="i">
    <h3>${i + 1}. ${user.name}</h3>
    <g:if test="${user.isCurrentUser}">
        This is you!
    </g:if>
    <g:elseif test="${user.isAdmin}">
        Admin!
    </g:elseif>
    <g:else>
        <g:link controller="user" action="delete"
                params="${[name: user.name]}">Delete</g:link>
    </g:else>
    Nr of PCs: ${user.pcCount}
    <br/>
</g:each>
</body>
</html>