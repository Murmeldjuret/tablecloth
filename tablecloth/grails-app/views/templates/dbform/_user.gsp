<%@ page import="tablecloth.viewmodel.UserViewmodel" %>
<!DOCTYPE html>
<g:set var="user" value="${(UserViewmodel) user}"/>
<g:set var="i" value="${(int) i}"/>
<html>
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
-
<g:if test="${user.isCurrentUser}">
    <g:link controller="characters" action="index">Nr of PCs: ${user.pcCount}</g:link>
</g:if>
<g:else>
    <g:link controller="characters" action="forUser"
            params="${[username: user.name]}">Nr of PCs: ${user.pcCount}</g:link>
</g:else>
<br/>
</html>