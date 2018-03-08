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
    <title><g:message code="active.account.label" /></title>
  </head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <div class="col-lg-4 col-lg-offset-4">
          <div class="portlet portlet-green">
            <div class="portlet-heading">
              <div class="portlet-title">
                <h3><g:message code="active.account.title" /></h3>
              </div>
              <div class="clearfix"></div>
            </div>
            <div id="greenPortlet" class="panel-collapse collapse in">
              <div class="portlet-body">
                <g:renderErrors />
                <p>
                <g:message code="active.account.change.intro" />
                </p>
                <g:form name="active" action="activePassword" id="${token}" class="form-signin form-horizontal">
                  <input type="hidden" name="token" value="${token}">
                  <div class="col-md-6">
                    <label class="${session.labelWidth} control-label" for="password"><g:message code="recovery.password.label" /></label>
                    <div class="${session.inputWidth}">
                      <input class="form-control" type="password" name="password">
                    </div>
                    <label class="${session.labelWidth} control-label" for="confirmPassword"><g:message code="recovery.password.confirm.label" /></label
                    >
                    <div class="${session.inputWidth}">
                      <input class="form-control" type="password" name="confirmPassword">
                    </div>
                    <br/>
                    <button type="submit" class="btn btn-primary"><g:message code="recovery.password.button.label" /></button>
                  </div>
                </g:form>
                <br/><br/><br/><br/><br/><br/><br/><br/>
              </div>
            </div>
            <div class="portlet-footer">
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
