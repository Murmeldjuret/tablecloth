<%@ page import="tablecloth.viewmodel.gen.ClassListViewmodel; tablecloth.utils.Formatter" %>
<!DOCTYPE html>
<g:set var="cls" value="${(ClassListViewmodel) cls}"/>
<g:set var="percentSize" value="${(Double) cls.population / totalPop}"/>
<g:set var="percentWealth" value="${(Double) cls.wealth / totalWealth}"/>
<html>
<tr>
    <td>
        <a data-toggle="tooltip" title="${cls.desc}">${cls.name}</a>
    </td>
    <td>
        ${Formatter.number(cls.households)}
    </td>
    <td>
        ${Formatter.number(cls.population)}
    </td>
    <td>
        <g:formatNumber number="${percentWealth}" type="percent" maxFractionDigits="2"/>
    </td>
    <td>
        <g:formatNumber number="${percentSize}" type="percent" maxFractionDigits="2"/>
    </td>
</tr>
</html>