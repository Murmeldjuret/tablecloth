<%@ page import="tablecloth.utils.Formatter; tablecloth.viewmodel.gen.CountryDataViewmodel" %>
<!DOCTYPE html>
<g:set var="country" value="${(CountryDataViewmodel) country}"/>
<html>
<br>
Total number of households is ${Formatter.number(country.totalHouseholds)}.
The country has about ${Formatter.number(country.totalPop)} residents where
${Formatter.number(country.totalUrban)} or
<g:formatNumber number="${country.urbanPercent}" type="percent" maxFractionDigits="2" />
live in towns of more than 1000 people.
The country has a military manpower of about
${Formatter.number(country.totalMil)} men.
<g:if test="${country.totalFood > country.totalHouseholds}">
    The country exports enough food for ${Formatter.number(country.totalFood - country.totalHouseholds)} households.
</g:if>
<g:if test="${country.totalFood < country.totalHouseholds}">
    The country must import enough food for ${Formatter.number(country.totalHouseholds - country.totalFood)} households.
</g:if>
<br>
</html>