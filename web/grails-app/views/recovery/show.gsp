<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="home">
    <asset:stylesheet src="third-party/modulus-uno-theme/css/style.css"/>
    <asset:stylesheet src="third-party/modulus-uno-theme/css/plugins.css"/>
    <asset:stylesheet src="third-party/modulus-uno-theme/css/demo.css"/>
    <style>
      body {
      background-image: url(/assets/third-party/modulus-uno-theme/img/m1-back.png);
      background-position: center center;
      background-repeat: no-repeat;
      background-attachment: fixed;
      background-size: cover;
      background-color: #FFFFFF;
      }
    </style>
    <title><g:message code="recovery.password.label" /></title>
  </head>
  <body class="login">
      <div class="container">
        <div class="row">
          <div class="col-md-4 col-md-offset-4">
            <div class="login-banner text-center">
              <h1>
                <asset:image src="Logo-ModulusUno-vFINAL-big.png" width="40%"/>
              </h1>
            </div>
            <div class="portlet portlet-green">
              <div class="portlet-heading login-heading">
                <div class="portlet-title">
                  <h4><g:message code="recovery.password.title" /></h4>
                </div>
                <div class="clearfix"></div>
              </div>
              <div class="portlet-body">
                <g:renderErrors />
                <p>
                  <g:message code="recovery.password.change.intro" />
                </p>
                <g:form name="recovery" action="update" id="${params.token}" class="form-signin form-horizontal">
                  <label class="${session.labelWidth} control-label" for="password"><g:message code="recovery.password.label" /></label>
                  <div class="${session.inputWidth}">
                    <input class="form-control" type="password" name="password" required="" />
                  </div>
                  <label class="${session.labelWidth} control-label" for="confirmPassword"><g:message code="recovery.password.confirm.label" /></label
                    >
                    <div class="${session.inputWidth}">
                      <input class="form-control" type="password" name="confirmPassword" required=""/>
                    </div>
                    <br/>
                    <button type="submit" class="btn btn-primary"><g:message code="recovery.password.button.label" /></button>
                </g:form>
              </div>
            </div>
        </div>
      </div>
    </div>
  </body>
</html>
