<%@ page import="tablecloth.viewmodel.gen.ClassListViewmodel" %>
<!DOCTYPE html>
<g:set var="cls" value="${(ClassListViewmodel) cls}"/>
<g:set var="percent" value="${(Double) cls.size / totalSize}"/>
<html>
<tr>
    <td>
        ${cls.name}
    </td>
    <td>
        ${cls.size}
    </td>
    <td>
        ${cls.wealth}
    </td>
    <td>
        ${(percent * 100).trunc(1)}%
    </td>
</tr>
</html>