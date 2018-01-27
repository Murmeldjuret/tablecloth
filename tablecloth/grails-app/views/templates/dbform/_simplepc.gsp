<%@ page import="tablecloth.viewmodel.PersonViewmodel" %>
<!DOCTYPE html>
<g:set var="pc" value="${(PersonViewmodel) pc}"/>
<g:set var="username" value="${(String) username}"/>
<g:set var="readonly" value="${(boolean) readonly}"/>
<html>
${pc.name} - Stats: [Str: ${pc.strength}, Int: ${pc.intelligence}, Dex: ${pc.dexterity}]
<g:if test="${!readonly}">
    -
    <g:link controller="characters" action="delete"
            params="${[charId: pc.charId, characterName: pc.name, username: username]}">Delete</g:link>
</g:if>
<br/>
</html>