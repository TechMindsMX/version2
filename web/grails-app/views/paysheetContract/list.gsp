<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'paysheetContract.label', default: 'PaysheetContract')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-credit-card-alt fa-3x"></i>
        Lista de Contratos de Nómina
        <small>${company}</small>
      </h1>
    </div>
    <div class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div id="horizontalFormExample" class="panel-collapse collapse in">

          <div class="portlet-body">
            <div class="table-responsive">
              <table class="table">
                <tr>
                  <th>Cliente</th>
                  <th>Fecha Inicio</th>
                  <th>Ejecutivo</th>
                </tr>
                <g:each in="${paysheetContractList}" var="paysheetContract">
                  <tr>
                    <td><g:link action="show" id="${paysheetContract.id}">${paysheetContract.client}</g:link></td>
                    <td><g:formatDate format="dd-MM-yyyy" date="${paysheetContract.initDate}"/></td>
                    <td>${paysheetContract.executive.name}</td>
                  </tr>
                </g:each>
              </table>
            </div>
          </div>

          <div class="portlet-footer">
            <div class="row">
              <div class="col-md-12">
                <div class="pagination">
                    <g:paginate total="${paysheetContractCount ?: 0}" />
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-12 text-right">
                <g:link class="btn btn-default" controller="dashboard" action="index">Salir</g:link>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>
  </body>
</html>

