<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'prePaysheet.label', default: 'PrePaysheet')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-credit-card-alt fa-3x"></i>
        Lista de Pre-Nóminas
        <small>${paysheetContract?.client}</small>
      </h1>
    </div>
    <div class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div id="horizontalFormExample" class="panel-collapse collapse in">

        <g:if test="${!prePaysheetList}">
          <g:form action="listPrePaysheetsForPaysheetContract">
            <g:render template="choosePaysheetContract"/>
          </g:form>
        </g:if>

        <g:if test="${prePaysheetList}">

          <div class="portlet-body">
            <div class="table-responsive">
              <table class="table">
                <tr>
                  <th>No.</th>
                  <th>Proyecto</th>
                  <th>Período de Pago</th>
                  <th>Del</th>
                  <th>Al</th>
                  <th>Ejecutivo</th>
                  <th>Estatus</th>
                </tr>
                <g:each in="${prePaysheetList}" var="prePaysheet">
                  <tr>
                    <td><g:link action="show" id="${prePaysheet.id}">${prePaysheet.id}</g:link></td>
                    <td>${prePaysheet.paysheetProject}</td>
                    <td>${prePaysheet.paymentPeriod}</td>
                    <td><g:formatDate format="dd-MM-yyyy" date="${prePaysheet.initPeriod}"/></td>
                    <td><g:formatDate format="dd-MM-yyyy" date="${prePaysheet.endPeriod}"/></td>
                    <td>${prePaysheet.accountExecutive}</td>
                    <td><g:message code="prePaysheet.status.${prePaysheet.status}"/></td>
                  </tr>
                </g:each>
              </table>
            </div>
          </div>

          <div class="portlet-footer">
            <div class="row">
              <div class="col-md-12">
                <div class="pagination">
                    <g:paginate total="${prePaysheetCount ?: 0}" params="[paysheetContractId:paysheetContract?.id]"/>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-12 text-right">
                <g:link class="btn btn-default" controller="dashboard" action="index">Salir</g:link>
              </div>
            </div>
          </div>
        </g:if>

        </div>
      </div>
    </div>
  </body>
</html>

