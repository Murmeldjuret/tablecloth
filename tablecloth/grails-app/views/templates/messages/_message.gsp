<%@ page import="tablecloth.viewmodel.UserViewmodel; tablecloth.viewmodel.MessageViewmodel" %>
<!DOCTYPE html>
<g:set var="msg" value="${(MessageViewmodel) msg}"/>
<g:set var="user" value="${(UserViewmodel) user}"/>
<g:set var="readonly" value="${(boolean) readonly}"/>
<html>
<h4>
    From: ${msg.sender}
    <g:if test="${msg.type.serverMessage}">  <p style="color:red">- SERVER MESSAGE -</p></g:if>
</h4>
sent: ${msg.sent} , received: ${msg.received}
<br>
<hr>
${msg.body}
<br>
<g:if test="${!readonly}">
    <g:link controller="inbox" action="delete" params="${[id: msg.id]}">Delete</g:link>
</g:if>
<br>
</html>