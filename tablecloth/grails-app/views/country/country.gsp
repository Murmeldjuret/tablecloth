<%@ page import="tablecloth.viewmodel.gen.ClassListViewmodel" %>
<!DOCTYPE html>
<g:set var="classes" value="${(List<ClassListViewmodel>) classes}"/>
<g:set var="chosenTags" value="${(Collection<String>) chosenTags}"/>
<g:set var="availableTags" value="${(Collection<String>) availableTags}"/>
<g:set var="totalSize" value="${(Long) totalSize}"/>
<g:set var="totalWealth" value="${(Long) totalWealth}"/>
<g:set var="totalUrban" value="${(Long) totalUrban}"/>
<asset:javascript src="countrygen.js"/>
<asset:javascript src="select2.min.js"/>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Country</title>
</head>

<body>
<asset:stylesheet src="select2.css"/>
<g:if test="${flash.message}"><h2>${flash.message}</h2></g:if>
<h3>Country:</h3>

<div>
    With Tags: ${chosenTags}
    <g:form controller="generator" action="country" id="tag_select_form">
        <g:select from="${availableTags.sort()}"
                  value="${chosenTags}"
                  id="tags"
                  class="select2-country select2-selection--multiple form-control select2-search--inline"
                  multiple="multiple"
                  name="tags"/>
        <g:submitButton name="GENERATE"/>
    </g:form>
</>

<br>
Total number of households is ${totalSize} where ${totalUrban} are urban.
The country has about ${totalSize * 7} residents.
<br>
<table>
    <tr>
        <th>Name</th>
        <th>#Households</th>
        <th>Power</th>
        <th>% of Country</th>
    </tr>
    <g:each in="${classes}" var="cls" status="i">
        <g:if test="${cls.size > 0}">
            <g:render template="/templates/gen/cls" model="[cls: cls, totalSize: totalSize, totalWealth: totalWealth]"/>
        </g:if>
    </g:each>
</table>
</body>
</html>