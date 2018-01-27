<%@ page import="tablecloth.viewmodel.PersonViewmodel; tablecloth.viewmodel.UserViewmodel" %>
<!DOCTYPE html>
<g:set var="user" value="${(UserViewmodel) user}"/>
<g:set var="chars" value="${(List<PersonViewmodel>) chars}"/>
<g:set var="readonly" value="${(boolean) readonly}"/>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Characters</title>
</head>

<body>
<g:if test="${flash.message}"><h2>${flash.message}</h2></g:if>
<h3>${user.name}'s Characters:</h3>
<g:each in="${chars}" var="pc" status="i">
    <g:render template="/templates/dbform/simplepc" model="[pc: pc, username: user.name, readonly: readonly]"/>
</g:each>
<br>
<g:if test="${!readonly}">
    <g:form controller="characters" action="addPerson">
        <div>
            <g:textField name="name" value="Name" id="name"/>
        </div>
        <g:submitButton name="addPersonButton" value="RollNewCharacter"/>
    </g:form>
</g:if>
</body>
</html>