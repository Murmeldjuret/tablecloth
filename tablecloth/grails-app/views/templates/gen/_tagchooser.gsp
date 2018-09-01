<%@ page import="tablecloth.viewmodel.gen.TagChoicesViewmodel" %>
<!DOCTYPE html>
<g:set var="chosenTags" value="${(Collection<String>) chosenTags}"/>
<g:set var="availableTags" value="${(TagChoicesViewmodel) availableTags}"/>
<html>
With Tags: ${chosenTags}
<g:form controller="generator" action="country" id="tag_select_form">
    Age:
    <g:select name="ages"
              from="${availableTags.ages}"
              value="${availableTags.getChosenAge(chosenTags)}"/>
    <br>
    Realm Size:
    <g:select name="sizes"
              from="${availableTags.size}"
              value="${availableTags.getChosenSize(chosenTags)}"/>
    <br>
    Fortunes:
    <g:select name="fortunes"
              from="${availableTags.fortunes}"
              value="${availableTags.getChosenFortune(chosenTags)}"/>
    <br>
    Other Tags:
    <g:select from="${availableTags.generic}"
              value="${chosenTags}"
              id="tags"
              class="select2-country select2-selection--multiple form-control select2-search--inline"
              multiple="multiple"
              name="tags"/>
    <br>
    <g:submitButton name="GENERATE"/>
</g:form>
</html>