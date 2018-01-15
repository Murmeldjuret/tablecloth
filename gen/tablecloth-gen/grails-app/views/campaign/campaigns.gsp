<%@ page import="tablecloth.gen.viewmodel.CampaignViewmodel; tablecloth.gen.viewmodel.UserViewmodel" %>
<!DOCTYPE html>
<g:set var="user" value="${(UserViewmodel) user}"/>
<g:set var="camps" value="${(List<CampaignViewmodel>) campaigns}"/>
<g:set var="readonly" value="${(boolean) readonly}"/>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Campaigns</title>
</head>

<body>
<g:if test="${flash.message}"><h2>${flash.message}</h2></g:if>
<h3>${user.name}'s Campaigns:</h3>
<hr>

<div>
    <g:each in="${camps}" var="camp" status="i">
        <h3>${camp.name}</h3> - ${camp.desc}
        <br>
        Party:
        <br>
        <g:each in="${camp.party}" var="pc" status="j">
            <g:render template="/templates/dbform/simplepc" model="[pc: pc, readonly: false]"/>
            <br>
        </g:each>
        <br>
        Participants:
        <br>
        <g:each in="${camp.participants}" var="participant" status="j">
            <g:render template="/templates/dbform/participant" model="[participant: participant]"/>
            <br>
        </g:each>
        <g:if test="${camp.hasInviteRights(user.name)}">
            <g:form controller="campaign" action="inviteUser">
                <g:hiddenField name="id" value="${camp.id}"/>
                <div>
                    Invite user: <g:textField name="username" value="Name" id="inviteField"/>
                </div>
            </g:form>
        </g:if>
        <g:if test="${camp.isOwner(user.name)}">
            <br>
            <g:form controller="campaign" action="delete">
                <g:hiddenField name="id" value="${camp.id}"/>
                <g:submitButton name="Delete"/>
            </g:form>
        </g:if>
        <hr>
    </g:each>
</div>
<br>
<g:if test="${!readonly}">
    Add new campaign:
    <g:form controller="campaign" action="create">
        <div>
            Name:
            <g:textField name="name" value="Name" id="nameField"/>
        </div>

        <div>
            Description:
            <g:textArea name="description" value="Description" id="descriptionArea"/>
        </div>
        <g:submitButton name="addCampaign" value="Add new Campaign"/>
    </g:form>
</g:if>
</body>
</html>