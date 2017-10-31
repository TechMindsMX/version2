<!DOCTYPE html>
<%! import com.modulus.uno.paysheet.PrePaysheetStatus %>
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
        Pre-Nómina / ${prePaysheetEmployee.prePaysheet.paysheetContract.client}
        <small>
          Proyecto / ${prePaysheetEmployee.prePaysheet.paysheetProject}<br/>
          Incidencias / ${prePaysheetEmployee.rfc} - ${prePaysheetEmployee.nameEmployee}
        </small>
      </h1>
    </div>

    <div class="content scaffold-edit" role="main">
      <div class="portlet portlet-default">
        <div class="portlet-heading">
          <div class="row">
            <div class="col-md-12 text-right">
              <g:link class="btn btn-default" controller="prePaysheet" action="show" id="${prePaysheetEmployee.prePaysheet.id}">Terminar</g:link>
            </div>
          </div>
        </div>

        <div class="portlet-body">
          <g:render template="incidences/prePaysheetEmployeeData"/>
        </div>
      </div>

      <g:if test="${prePaysheetEmployee.prePaysheet.status == PrePaysheetStatus.CREATED}">
      <div class="row">
        <div class="col-md-12">
          <div class="portlet">
            <h2>Agregar incidencia</h2>
            <g:form controller="prePaysheet" action="addIncidence">
              <g:render template="incidences/addIncidence"/>
            </g:form>
          </div>
        </div>
      </div>
      </g:if>

      <div class="row">
        <div class="col-md-12">
          <div class="portlet">
            <h2>Incidencias Registradas</h2>
            <g:render template="incidences/listIncidences"/>
          </div>
        </div>
      </div>

    </div>
  </body>
</html>
