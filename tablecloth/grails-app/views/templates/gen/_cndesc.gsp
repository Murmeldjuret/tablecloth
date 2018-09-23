<%@ page import="tablecloth.utils.Formatter; tablecloth.viewmodel.gen.CountryDataViewmodel" %>
<!DOCTYPE html>
<g:set var="country" value="${(CountryDataViewmodel) country}"/>
<html>
<br>
Total number of households is ${Formatter.number(country.totalHouseholds)}
where ${Formatter.number(country.totalUrban)} are urban.
The country has about ${Formatter.number(country.totalPop)} residents.
The country has a military strength of about
${Formatter.number((Math.sqrt(country.totalMil) * 100) as Long)} soldiers.
<g:if test="${country.totalFood > country.totalHouseholds}">
    The country exports enough food for ${Formatter.number(country.totalFood - country.totalHouseholds)} households.
</g:if>
<g:if test="${country.totalFood < country.totalHouseholds}">
    The country must import enough food for ${Formatter.number(country.totalHouseholds - country.totalFood)} households.
</g:if>
<br>
</html>