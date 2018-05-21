<%@ page import="tablecloth.viewmodel.gen.ClassListViewmodel" %>
<!DOCTYPE html>
<g:set var="classes" value="${(List<ClassListViewmodel>) classes}"/>
<g:set var="tags" value="${(List<String>) tags}"/>
<g:set var="totalSize" value="${(Long) totalSize}"/>
<g:set var="totalUrban" value="${(Long) totalUrban}"/>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Country</title>
</head>

<body>
<g:if test="${flash.message}"><h2>${flash.message}</h2></g:if>
<h3>Country:</h3>
With tags = ${tags.join(', ')}
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
            <g:render template="/templates/gen/cls" model="[cls: cls, totalSize: totalSize]"/>
        </g:if>
    </g:each>
</table>
</body>
</html>