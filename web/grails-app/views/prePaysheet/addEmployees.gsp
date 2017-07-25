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
        Agregar Empleados a Pre-Nómina
        <small>${prePaysheet.company}</small>
      </h1>
    </div>

    <g:if test="${flash.message}">
      <div class="alert alert-warning" role="status">${flash.message}</div>
    </g:if>


    <div class="content scaffold-edit" role="main">
      <div class="portlet portlet-default">
        <div class="portlet-heading">
          <div class="row">
            <div class="col-md-12 text-right">
              <g:link class="btn btn-default" action="list">Cancelar</g:link>
            </div>
          </div>
        </div>
        <div class="portlet-body">
          <g:render template="prePaysheetData"/>
        </div>
      </div>

      <div class="row">
        <div class="col-md-12">
          <g:form name="formAddEmployees" controller="prePaysheet" action="saveEmployees">
            <g:render template="addEmployees"/>
          </g:form>
        </div>
      </div>

    </div>
    <asset:javascript src="businessEntity/selectEntities.js"/>
    <asset:javascript src="prePaysheet/addEmployees.js"/>
  </body>
</html>


