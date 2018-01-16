<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title>
    <asset:stylesheet src="auth.css"/>
</head>

<body>
<div id='login'>
	<div class='inner'>
		<div class='fheader'><g:message code="springSecurity.login.header"/></div>

		<g:if test='${flash.message}'>
			<div class='login_message'>${flash.message}</div>
		</g:if>

		<form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
			<div id="input_holder">
				<input type='text' class='text_' name='j_username' id='username' placeholder="<g:message code="springSecurity.login.username.label"/>"/>
			</div>

			<div id="input_holder">
				<input type='password' class='text_' name='j_password' id='password' placeholder="<g:message code="springSecurity.login.password.label"/>"/>
			</div>

			<div id="remember_me_holder">
				<label for='remember_me'>
					<input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if>/>
					<g:message code="springSecurity.login.remember.me.label"/>
				</label>
			</div>

			<p>
				<input type='submit' id="submit" value='${message(code: "springSecurity.login.button")}'/>
			</p>
		</form>
	</div>
</div>
<script type='text/javascript'>
	<!--
	(function() {
		document.forms['loginForm'].elements['j_username'].focus();
	})();
	// -->
</script>
</body>
</html>