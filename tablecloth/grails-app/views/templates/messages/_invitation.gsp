<%@ page import="tablecloth.viewmodel.UserViewmodel; tablecloth.viewmodel.MessageViewmodel" %>
<!DOCTYPE html>
<g:set var="msg" value="${(MessageViewmodel) msg}"/>
<g:set var="user" value="${(UserViewmodel) user}"/>
<g:set var="readonly" value="${(boolean) readonly}"/>
<html>
<br>
<h4>From: ${msg.sender}</h4>
sent: ${msg.sent} , received: ${msg.received}
<br>
<br>
${msg.body}
<g:if test="${!readonly}">
    <g:if test="${msg.pending}">
        <g:link controller="inbox" action="acceptInvitation" params="${[id: msg.id]}">Accept</g:link>
        -
        <g:link controller="inbox" action="rejectInvitation" params="${[id: msg.id]}">Decline</g:link>
        -
    </g:if>
    <g:else>
        ${msg.invitationStatus}
        -
    </g:else>
    <g:link controller="inbox" action="delete" params="${[id: msg.id]}">Delete</g:link>
</g:if>
<br/>
</html>