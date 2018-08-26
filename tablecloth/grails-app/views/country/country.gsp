<%@ page import="tablecloth.viewmodel.gen.CountryDataViewmodel; tablecloth.viewmodel.gen.ClassListViewmodel" %>
<!DOCTYPE html>
<g:set var="classes" value="${(List<ClassListViewmodel>) classes}"/>
<g:set var="chosenTags" value="${(Collection<String>) chosenTags}"/>
<g:set var="availableTags" value="${(Collection<String>) availableTags}"/>
<g:set var="country" value="${(CountryDataViewmodel) countryData}"/>
<asset:javascript src="countrygen.js"/>
<asset:javascript src="select2.min.js"/>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Country</title>
</head>

<body>
<asset:stylesheet src="tooltip.css"/>
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
        <br>
        <g:submitButton name="GENERATE"/>
    </g:form>
</div>

<br>
Total number of households is ${country.totalSize} where ${country.totalUrban} are urban.
The country has about ${country.totalSize * 7} residents.
The country has a military strength of about ${(Math.sqrt(country.totalMil) * 100) as Long} soldiers.
<g:if test="${country.totalFood > country.totalSize}">
    The country exports enough food for ${country.totalFood - country.totalSize} households.
</g:if>
<g:if test="${country.totalFood < country.totalSize}">
    The country must import enough food for ${country.totalSize - country.totalFood} households.
</g:if>
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
            <g:render template="/templates/gen/cls"
                      model="[cls: cls, totalSize: country.totalSize, totalWealth: country.totalWealth]"/>
        </g:if>
    </g:each>
</table>
</body>
<script>
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });
</script>
</html>