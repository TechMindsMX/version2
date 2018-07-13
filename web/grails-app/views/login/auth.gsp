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
  </head>
  <body class="login">

    <div class="container-fluid">
      <div class="row">
        <div class="col-md-8 col-md-offset-2">
          <div class="login-banner text-center">
            <h1>
              <asset:image src="Logo-ModulusUno-vFINAL-big.png" class="shadow" width="30%" />
            </h1>
          </div>
          <form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
          <div class="row">
            <div class="col-md-6">

              <div class="portlet portlet-green">
                <div class="portlet-heading login-heading">
                  <div class="portlet-title">
                    <h4>B I E N V E N I D O</h4>
                  </div>
                  <div class="clearfix"></div>
                </div>
                <div class="portlet-body">
                  <g:if test="${flash.message || session.message}">
                  <p class="text-info">${flash.message ?: session.message}</p>
                  </g:if>
                  <fieldset>
                    <label for="username">Nombre del usuario</label>
                    <input type="text" name='username' class="form-control" placeholder="Nombre de usuario" id='username' value="${flash.username ?: session.username}" >
                    <label for="password">Contraseña</label>
                    <input type="password" name='password' class="form-control" placeholder="Contraseña" id='password' >
                  </fieldset>
                  <hr>
                  <br>
                  <br>
                  <p class="small">
                    <g:link controller="recovery" action="forgotPassword">Olvidaste tu Contraseña? Haz clic aquí!</g:link>
                  </p>
                </div>
              </div>

            </div>
            <div class="col-md-6">
              <div class="portlet portlet-green">
                <div class="portlet-heading login-heading">
                  <div class="portlet-title">
                    <h4>AUTENTICACIÓN 2FA</h4>
                  </div>
                  <div class="clearfix"></div>
                </div>
                <div class="portlet-body">
                  <p>Si ya cuenta con un dispositivo sincronizado, ingrese el código de verificación</p>
                  <br/>
                  <label for="password">Código de Verificación</label>
                  <input type="text" name='code' class="form-control" placeholder="Si no lo ha activado, deje vacío" id='code' >
                  <hr>
                  <br/>
                  <p class="small text-right"><g:link controller="recovery" action="reSynchronizeTwoFactor">Sincronizar otro dispositivo</g:link></p>
                </div>
              </div>
            </div>
          </div>
          <div class"row">
            <div class="col-md-12 text-right">
              <button id="btn-success" type="submit" class="btn btn-lg btn-primary">Ingresar</button>
            </div>
          </div>
          </form>

        </div>
      </div>
    </div>
  </body>
</html>
