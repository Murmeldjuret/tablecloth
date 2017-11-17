<%@ page import="tablecloth.gen.viewmodel.UserViewmodel" %>
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
Nr of PCs: ${user.pcCount}
<br/>
</html>