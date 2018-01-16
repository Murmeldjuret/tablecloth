<%@ page import="tablecloth.gen.viewmodel.UserViewmodel; tablecloth.gen.viewmodel.InboxViewmodel" %>
<!DOCTYPE html>
<g:set var="inbox" value="${(InboxViewmodel) inbox}"/>
<g:set var="user" value="${(UserViewmodel) user}"/>
<g:set var="readonly" value="${(boolean) readonly}"/>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Inbox</title>
</head>

<body>
<g:if test="${flash.message}"><h2>${flash.message}</h2></g:if>
<h3>Messages of ${user.name}:</h3>
<g:each in="${inbox.messages}" var="msg" status="i">
    <hr style="height:5px;border:none;color:#333;background-color:#333;">
    <g:if test="${msg.type.isInvitation()}">
        <g:render template="/templates/messages/invitation" model="[msg: msg, user: user, readonly: readonly]"/>
    </g:if>
    <g:else>
        <g:render template="/templates/messages/message" model="[msg: msg, user: user, readonly: readonly]"/>
    </g:else>
    <br>
</g:each>
<hr style="height:5px;border:none;color:#333;background-color:#333;">
<br>
<g:if test="${!readonly}">
    <h5>Send message to:</h5>
    <br>
    <g:form controller="inbox" action="send">
        <div>
            User:
            <g:textField name="username" value="" id="usernameField"/>
        </div>
        <div>
            Message:
            <g:textArea name="body" value="" id="descriptionArea"/>
        </div>
        <g:submitButton name="sendMessage" value="Send Message"/>
    </g:form>
    <sec:ifAllGranted roles="ROLE_ADMIN">
        <br>
        Broadcast message to whole server:
        <g:form controller="inbox" action="broadcast">
            <div>
                Message:
                <g:textArea name="msg" value="" id="msgArea"/>
            </div>
            <g:submitButton name="broadcastMessage" value="Send Broadcast"/>
        </g:form>
    </sec:ifAllGranted>
</g:if>
</body>
</html>