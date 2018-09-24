<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <asset:stylesheet src="application.css"/>

    <g:layoutHead/>
</head>
<body>
    <div class="navbar navbar-default navbar-static-top" role="navigation">
        <div class="container">
            <div class="navbar-header">

            </div>
            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav navbar-left">
                    <li class="nav-item">
                        <a href="${createLink(controller: 'generator', action: 'country')}">Generate Country</a>
                    </li>
                    <li class="nav-item">
                        <a href="${createLink(controller: 'inbox')}">Inbox</a>
                    </li>
                </ul>
                <div class="nav navbar-nav navbar-right">
                    <sec:ifNotLoggedIn>
                        <g:link controller="login">Log in</g:link>
                    </sec:ifNotLoggedIn>
                    <sec:ifLoggedIn>
                        <g:link controller="logoff">Log off</g:link>
                    </sec:ifLoggedIn>
                    <sec:ifLoggedIn>
                        Logged in as: <sec:username/>
                    </sec:ifLoggedIn>
                </div>
            </div>
        </div>
    </div>

    <g:layoutBody/>

    <div class="footer" role="contentinfo"></div>

    <div id="spinner" class="spinner" style="display:none;">
        <g:message code="spinner.alt" default="Loading&hellip;"/>
    </div>

    <asset:javascript src="application.js"/>

</body>
</html>
