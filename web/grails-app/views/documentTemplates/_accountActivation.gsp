<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title></title>
</head>
<body>
  <h1>${accountsLegalReporesentatives.first().company}</h1>
  <ul>
    <g:each in="${accountsLegalReporesentatives}" var="account">
      <li>${account.user.profile.fullName} url: ${account.urlVerification}</li>
      <br />
    </g:each>
  </ul>
</body>
</html>
