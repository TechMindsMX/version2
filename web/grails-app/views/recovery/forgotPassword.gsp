<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <meta name="layout" content="home">
    <asset:stylesheet src="third-party/modulus-uno-theme/css/style.css"/>
    <asset:stylesheet src="third-party/modulus-uno-theme/css/plugins.css"/>
    <asset:stylesheet src="third-party/modulus-uno-theme/css/demo.css"/>
    <title><g:message code="recovery.password.label" /></title>
  </head>
  <body class="login">
    <div class="container">
      <div class="row">
        <div class="col-lg-4 col-lg-offset-4">
          <div class="login-banner text-center">
            <h1>
              <asset:image src="Logo-ModulusUno-vFINAL-big.png" width="40%"/>
            </h1>
          </div>
          <div class="portlet portlet-green">
            <div class="portlet-heading">
              <div class="portlet-title">
                <h3><g:message code="recovery.password.title" /></h3>
              </div>
              <div class="clearfix"></div>
            </div>
            <div id="greenPortlet" class="panel-collapse collapse in">
              <div class="portlet-body">

                <g:if test="${flash.message}">
                <div class="alert alert-danger">
                  ${flash.message}
                </div>
                </g:if>

                <g:form name="recovery" action="save" class="form-signin form-horizontal">
                  <br/>
                  <div class="col-md-12">
                    <label class="control-label" for="email"><g:message code="email.label" /></label>
                    <br/><br/>
                    <input type="email" name="email" class="form-control" placeholder="${message(code:'recovery.password.text.placeholder')}">
                    <br/>
                    <button type="submit" class="btn btn-primary"><g:message code="recovery.password.button.label" /></button>
                  </div>
                </g:form>
              <br/><br/><br/><br/>
              <br/><br/><br/><br/>
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
