<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'cashOutOrder.label', default: 'CashOutOrder')}" />
    <title><g:message code="cashoutOrder.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="icon-retiro fa-3x"></i>
        Operaciones / Retiro
        <small><g:message code="cashoutOrder.list.label" args="[entityName]" /></small>
      </h1>
      <ol class="breadcrumb">
        <li><i class="fa fa-caret-square-o-up"></i> Administración</li>
        <li class="active">Listado de tus ordenes de retiro</li>
      </ol>
    </div>
    <div class="col-lg-12">
        <div class="portlet portlet-blue">
          <div class="portlet-heading">
            <div class="portlet-title">
              <br /><br />
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <div id="list-cashOutOrder" class="content portlet-blue" role="main">
                <g:if test="${flash.message}">
                    <div class="alert alert-danger" role="alert">${flash.message}</div>
                </g:if>
                <g:if test="${messageSuccess}">
                  <div class="well well-sm alert-success">${messageSuccess}</div>
                </g:if>
              <div class="table-responsive">
                <table class="table">
                <tr>
                  <th>No. de Orden</th>
                  <th>Monto de Retiro</th>
                  <th>Cuenta Destino</th>
                  <th>Empresa</th>
                  <th>Estatus</th>
                </tr>
                <g:if test="${cashOutOrderList.isEmpty()}">
                  <div class="alert alert-danger" role="alert">
                    <g:message code="cashOutOrder.list.empty"/>
                  </div>
                </g:if>
                <g:each in="${cashOutOrderList}" var="cashoutOrder">
                <tr class="${message(code: 'cashOutOrder.style.background.'+cashoutOrder.status)}">
                  <td class="text-center">
                    <g:link action="show" id="${cashoutOrder.id}" params="[company:"${cashoutOrder.company.id}"]">
                      ${cashoutOrder.id}
                    </g:link>
                  </td>
                  <td>
                    ${modulusuno.formatPrice(number: cashoutOrder.amount)}
                  </td>
                  <td>
                    ${cashoutOrder.account}
                  </td>
                  <td>
                    ${cashoutOrder.company}
                  </td>
                  <td><g:message code="cashOutOrder.status.${cashoutOrder.status}" default="${cashoutOrder.status}"/></td>
                </tr>
                </g:each>
                </table>
                </div>
                <div class="pagination">
                    <g:paginate total="${cashOutOrderCount ?: 0}" />
                </div>
              </div>
              <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
                <g:link class="create btn btn-default" action="create"><g:message code="cashoutOrder.new.label" args="[entityName]" /></g:link>
              </sec:ifAnyGranted>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
