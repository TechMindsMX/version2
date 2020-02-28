<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'credit.label', default: 'Creditos')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-usd fa-3x"></i>
        Créditos
        <small>${company}</small>
      </h1>
    </div>
    <div class="portlet portlet-blue">
      <div class="portlet-heading">
        <div class="portlet-title">
          <br />
          <br />
        </div>
        <div class="clearfix"></div>
      </div>
      <div id="list-product" class="panel-collapse collapse in">
        <div class="portlet-body">
          <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
          </g:if>
          <div class="table-responsive">
            <table class="table table-condensed table-striped">
              <tr>
                <th class="col-xs-1 text-center"><g:message code="credit.name" default="Nombre" /></th>
                <th class="col-xs-1 text-center"><g:message code="credit.productType" default="Tipo de producto" /></th>
                <th class="col-xs-1 text-center"><g:message code="credit.portfolioManagementType" default="Tipo de Admon de Cartera" /></th>
                <th class="col-xs-1 text-center"><g:message code="credit.frequencyType" default="Frecuencia" /></th>
                <th class="col-xs-1 text-center"><g:message code="credit.creditLineType" default="Múltiplo Solicitud" /></th>
                <th class="col-xs-1 text-center"><g:message code="credit.dispersionType" default="Tipo dispersión" /></th>
                <th class="col-xs-1 text-center"><g:message code="credit.enabled" default="Habilitado"/></th>
                <th class="col-xs-1 text-center"><g:message code="credit.enabled" default="Autorizar"/></th>
              </tr>
              <g:each in="${credits.sort{it.id}}" var="credit">
              <tr>
                <td class="text-center">
                  <%-- <g:link action="show" id="${credit.id}"> --%>
                  ${credit.name}
                  <%-- </g:link> --%>
                </td>
                <td class="text-center">${credit.productType.value}</td>
                <td class="text-right">${credit.portfolioManagementType.value}</td>
                <td class="text-right">${credit.frequencyType.value}</td>
                <td class="text-center">${credit.creditLineType.value}</td>
                <td class="text-center">${credit.dispersionType.value}</td>
                <td class="text-center">
                  <g:if test="${credit.enabled}">
                    <span class="label label-primary">
                      <i class="fa fa-check fa-1x"></i>
                    </span>
                  </g:if>
                  <g:else>
                    <span class="label label-warning">
                      <i class="fa fa-times fa-1x"></i>
                    </span>
                  </g:else>
                </td>
                <td class="text-center">
                    <input type="checkbox" name="hasCredit"/>
                </td>
              </tr>
              </g:each>
            </table>
          </div>
          <div class="pagination">
            <g:paginate total="${total ?: 0}" />
          </div>
        </div>
      </div>

      <g:link class="create btn btn-primary" action="update">
        <g:message code="credit.view.create.label" default="Autorizar ${entityName}" />
      </g:link>
    </div>
    </div>
  </body>
</html>
