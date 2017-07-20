<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'prePaysheet.label', default: 'PrePaysheet')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-credit-card-alt fa-3x"></i>
        Crear Pre-Nómina - Seleccionar empleados
        <small>${prePaysheet.company}</small>
      </h1>
    </div>
    <div class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-heading">
            <g:render template="prePaysheetData"/>
          </div>

          <div class="portlet-body">

          </div>

          <div class="portlet-footer">
            <div class="row">
              <div class="col-md-12 text-right">
                <g:link class="btn btn-primary" action="list">Lista</g:link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>


