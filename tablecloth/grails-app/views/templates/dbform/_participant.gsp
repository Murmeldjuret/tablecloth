<%@ page import="tablecloth.viewmodel.CampaignViewmodel; tablecloth.viewmodel.UserViewmodel; tablecloth.viewmodel.ParticipantViewmodel; tablecloth.viewmodel.PersonViewmodel" %>
<!DOCTYPE html>
<g:set var="ownership" value="${(boolean) ownership}"/>
<g:set var="participant" value="${(ParticipantViewmodel) participant}"/>
<g:set var="camp" value="${(CampaignViewmodel) camp}"/>
<g:set var="readonly" value="${(boolean) readonly}"/>
<html>
${participant.username} (${participant.status.desc}) - Permissions: [${participant.permissions.desc.join(', ')}]
<g:if test="${!readonly}">
    <g:if test="${!participant.owner && ownership}">
        -
        <g:link controller="campaign" action="removeParticipant"
                params="${[id: camp.id, username: participant.username]}">KICK</g:link>
    </g:if>
</g:if>
<br/>
</html>