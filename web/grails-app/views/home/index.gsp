<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Modulus UNO</title>

    <link rel="icon" href="../../favicon.ico">
    <asset:stylesheet src="third-party/modulus-uno-theme/css/carousel.css"/>
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>

    <style>
      .fixed-panel {
        min-height: 50%;
        max-height: 65%;
        overflow-y: scroll;
      }
    </style>

  </head>
  <body>
  <header>
     <nav class="navbar navbar-mdu navbar-fixed-top">
      <div class="container">
      <div class="row">
        <div class="navbar-header pull-right">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <ul class="nav navbar-nav">
            <sec:ifNotLoggedIn>
              <li><g:link controller="dashboard" >Ingresa a tu Cuenta</g:link></li>
            </sec:ifNotLoggedIn>
            <sec:ifLoggedIn>
              <li><g:link controller="dashboard">Entra al dashboard</g:link></li>
            </sec:ifLoggedIn>
          </ul>
          </div>
        </div>
      </div>
    </nav>
  </header>
  <main class="mdu-home-content">
  <div id="myCarousel" class="carousel slide" data-ride="carousel">
      <div class="carousel-inner" role="listbox">
        <div class="item active">
          <div class="container">
              <div class="carousel-caption"><asset:image width="40%" src="Logo-ModulusUno-vFINAL-big.png"/></div>
          </div>
        </div>
    </div>
</div>

 <div class="container marketing">
      <div class="row">
        <div class="col-sm-4">
          <h2>REGISTRA</h2>
          <p>Registra a todos tus clientes, proveedores, empleados, colaboradores y productos.
          Lleva fácilmente su control.</p>
        </div>
        <div class="col-sm-4">

          <h2>OPERA</h2>
          <p>Recibe cobranza, realiza retiros o pagos.
          Todo de una manera muy sencilla.</p>
        </div>
        <div class="col-sm-4">
          <h2>CONSULTA</h2>
          <p>¿Cuánto debo? ¿Cuánto me deben? ¿Qué hay que pagar esta semana?
          Tan fácil como consultarlo.</p>
        </div>
      </div>
      </div>
  </main>
  <footer>
    <div class="container">
      <div class="row">
        <p class="pull-right"><a href="#">Regresar Arriba</a></p>
        <p>&copy; 2017 Technology and Innovation Minds S.A. de C.V. &middot;

        <button type="button" class="btn btn-link" data-toggle="modal" data-target="#modalPrivacyNotice">
          Aviso de Privacidad
        </button>

        <div class="modal fade" id="modalPrivacyNotice" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
          <div class="modal-dialog fixed-panel" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <label><h3>Aviso de Privacidad</h3></label>
              </div>
              <div class="modal-body">
                <div class="panel panel-default">
                  <div class="panel-body">
                    <g:each in="${privacyNotice}" var="line">
                      <p class="text-justify text-muted">${line}</p>
                    </g:each>
                  </div>
                </div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
              </div>
            </div>
          </div>
        </div>
      </p>

      </div>
    </div>
  </footer>






  </body>
</html>
