<%@ page import="tablecloth.utils.Formatter; tablecloth.viewmodel.gen.TagChoicesViewmodel; tablecloth.viewmodel.gen.CountryDataViewmodel; tablecloth.viewmodel.gen.ClassListViewmodel" %>
<!DOCTYPE html>
<g:set var="chosenTags" value="${(Collection<String>) chosenTags}"/>
<g:set var="availableTags" value="${(TagChoicesViewmodel) availableTags}"/>
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
    <g:render template="/templates/gen/tagchooser" model="[availableTags: availableTags, chosenTags: chosenTags]"/>
</div>

<g:render template="/templates/gen/cndesc" model="[country: country]"/>
<table>
    <tr>
        <th>Name</th>
        <th>#Households</th>
        <th>Population</th>
        <th>Power</th>
        <th>% of Population</th>
    </tr>
    <g:each in="${countryData.clsList}" var="cls" status="i">
        <g:if test="${cls.households > 0}">
            <g:render template="/templates/gen/cls"
                      model="[cls: cls, totalPop: country.totalPop, totalWealth: country.totalWealth]"/>
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