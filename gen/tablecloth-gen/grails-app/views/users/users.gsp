<%@ page import="tablecloth.gen.model.domain.users.User" %>
<!DOCTYPE html>
<g:set var="users" value="${(List<User>) users}"/>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>User management</title>
</head>

<body>
<g:if test="${flash.message}"><h2>${flash.message}</h2></g:if>
<g:each in="${users}" var="user" status="i">
    <h3>${i + 1}. ${user.username}</h3>
        <g:if test="${user.authorities.find{it.authority == 'ROLE_ADMIN'}}">
            is an admin
        </g:if>
        <g:else>
            <g:link controller="user" action="delete"
                    params="${[name: user.username]}">Delete</g:link>
        </g:else>
    <br/>
</g:each>
</body>
</html>