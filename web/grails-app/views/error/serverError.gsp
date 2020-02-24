<!doctype html>
<html>
  <head>
    <title><g:if env="development">Grails Runtime Exception</g:if><g:else>Error</g:else></title>
    <meta name="layout" content="main">
    <g:if env="development"><asset:stylesheet src="errors.css"/></g:if>
  </head>
  <body>
    <div class="panel panel-warning">
      <div class="panel-heading">
        <h3 class="panel-title">Ha ocurrido algo inesperado</h3>
      </div>
      <div class="panel-body">
        <g:if env="development">
          <g:if test="${Throwable.isInstance(exception)}">
            <g:renderException exception="${exception}" />
          </g:if>
          <g:elseif test="${request.getAttribute('javax.servlet.error.exception')}">
            <g:renderException exception="${request.getAttribute('javax.servlet.error.exception')}" />
          </g:elseif>
          <g:else>
            <ul class="errors">
              <li>An error has occurred</li>
              <li>Exception: ${exception}</li>
              <li>Message: ${message}</li>
              <li>Path: ${path}</li>
            </ul>
          </g:else>
        </g:if>
        <g:else>
          <div class="row">
            <div class="col-md-6">
              <a href="${originUrl}" class="btn btn-primary">Regresar</a>
            </div>
            <div class="col-md-6 text-right">
              <button class="btn btn-warning text-right" type="button" data-toggle="collapse" data-target="#errorMessage" aria-expanded="false" aria-controls="errorMessage">
                Ver mensaje
              </button>
            </div>
          </div>
          <br/>
          <div class="row">
            <div class="collapse" id="errorMessage">
              <div class="well">
                <p>Mensaje: ${message ?: "Por favor intente más tarde o bien comuníquese con el equipo de soporte"}</p>
                <p>URL: ${destinyUrl}</p>
              </div>
            </div>
          </div>
        </g:else>
      </div>
    </div>
  </body>
</html>
