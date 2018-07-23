<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
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
    <title>Sincronizar Dispositivo</title>
  </head>
  <body class="login">
    <div class="container-fluid">
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
                <h3>Sincronizar Dispositivo para Autenticación 2FA</h3>
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

                <g:form name="twoFactorAuth" action="reSynchronizeUser" class="form-signin form-horizontal">
                  <div class="row">
                  <div class="col-md-12">
                    <label class="control-label" for="username">Nombre de Usuario</label>
                    <input type="text" name="username" class="form-control" placeholder="Ingrese su nombre de usuario">
                    <label class="control-label" for="email"><g:message code="email.label" /></label>
                    <input type="email" name="email" class="form-control" placeholder="Ingrese su correo electrónico">
                    <br/>
                  </div>
                  <div class="row">
                    <div class="col-md-12 text-center">
                    <button type="submit" class="btn btn-primary">Sincronizar</button>
                    </div>
                  </div>
                </g:form>
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
