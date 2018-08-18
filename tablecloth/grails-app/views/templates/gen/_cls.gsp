<%@ page import="tablecloth.viewmodel.gen.ClassListViewmodel" %>
<!DOCTYPE html>
<g:set var="cls" value="${(ClassListViewmodel) cls}"/>
<g:set var="percentSize" value="${(Double) cls.size / totalSize}"/>
<g:set var="percentWealth" value="${(Double) cls.wealth / totalWealth}"/>
<html>
<tr>
    <td>
        ${cls.name}
    </td>
    <td>
        ${cls.size}
    </td>
    <td>
        ${(percentWealth * 100).trunc(1)}%
    </td>
    <td>
        ${(percentSize * 100).trunc(1)}%
    </td>
</tr>
</html>