<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Persons</title>
	</head>
	<body>
        <g:each in="${users}" var="user" status="i">
            <h3>${i+1}. ${user.username}</h3>
            <br/>
        </g:each>
	</body>
</html>