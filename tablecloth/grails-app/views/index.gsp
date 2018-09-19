<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Goblin Cupboard</title>

    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
    <asset:stylesheet src="application.css"/>
</head>
<body>
<div class="navbar" role="navigation">
    <div class="navbar row">
        <div class="navbar column">
            <sec:ifLoggedIn>
                Logged in as: <sec:username/>
            </sec:ifLoggedIn>
        </div>

        <div class="navbar column">
            <sec:ifNotLoggedIn>
                <g:link controller="login">Log in</g:link>
            </sec:ifNotLoggedIn>
            <sec:ifLoggedIn>
                <g:link controller="logoff">Log off</g:link>
            </sec:ifLoggedIn>
        </div>
    </div>
</div>

<div class="footer" role="contentinfo">
</div>

<div id="spinner" class="spinner" style="display:none;">
    <g:message code="spinner.alt" default="Loading&hellip;"/>
</div>

<asset:javascript src="application.js"/>

</body>
</html>
