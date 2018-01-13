<%@ page import="tablecloth.gen.viewmodel.ParticipantViewmodel; tablecloth.gen.viewmodel.PersonViewmodel" %>
<!DOCTYPE html>
<g:set var="participant" value="${(ParticipantViewmodel) participant}"/>
<g:set var="readonly" value="${(boolean) readonly}"/>
<html>
${participant.username} (${participant.status.desc}) - Permissions: [${participant.permissions.desc.join(', ')}]
<g:if test="${!readonly}">
    -
</g:if>
<br/>
</html>