<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'credit.label', default: 'Credits')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-usd fa-3x"></i>
        Créditos / ${company.bussinessName}
        <small>Información</small>
      </h1>
    </div>

    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div class="portlet-heading">
          <div class="portlet-title">
          </div>
          <div class="clearfix"></div>
        </div>

        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
            <dl class="dl-horizontal">
              <dt>Crédito (nombre)</dt>
              <dd>${credit.name}</dd>

              <dt>Tipo de producto</dt>
              <dd>${credit.productType.value}</dd>

              <dt>Tipo de Administración de Cartera</dt>
              <dd>${credit.portfolioManagementType.value}</dd>

              <dt>Frecuencia</dt>
              <dd>${credit.frequencyType.value}</dd>

              <dt>Múltiplo Solicitud</dt>
              <dd>${credit.creditLineType.value}</dd>

              <dt>Tipo dispersión</dt>
              <dd>${credit.dispersionType.value}</dd>

              <dt>Estado</dt>
              <dd>
              <g:if test="${credit.enabled}">
                <span class="label label-success">HABILITADO</span>
              </g:if>
              <g:else>
                <span class="label label-warning">DESHABILITADO</span>
              </g:else>
              </dd>
            </dl>
          </div>

          <div class="portlet-footer">
            <div class="row">
              <div class="col-md-12 text-right">
                <g:link class="list btn btn-default" action="index">Listado de créditos </g:link>
                <g:link class="create btn btn-default" action="edit" id="${credit.id}">Editar crédito</g:link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
