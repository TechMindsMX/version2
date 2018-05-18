<%! import com.modulus.uno.status.SaleOrderStatus %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'company.label', default: 'Company')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
     <h1><g:message code="company.list.label" args="[entityName]" /></h1>
      <ol class="breadcrumb">
        <li><i class="fa fa-caret-square-o-up"></i> Compañia</li>
        <li class="active">Listado de Ordenes de Venta</li>
      </ol>
    </div>
    <div align="center">
      <!--p class="text-primary">Selecciona una de las empresar para poder operar</p>
      <p class="text-primary">Al seleccionar una empresa encontraras en ella las actividades que puedes efectuar para esa empresa</p-->
      <br />
    </div>
    <div id="list-company" class=" portlet-blue" role="main">
      <g:if test="${flash.message}">
          <div class="message alert alert-info" align="center" role="status">${flash.message}</div>
      </g:if>
      <div class="table-responsive">
        <table class="table table-hover">
          <thead>
            <tr>
              <th>Cliente</th>
              <th>Estatus</th>
              <th>Compañia</th>
              <th>Subtotal</th>
              <th>Impuestos</th>
              <th>Total</th>
              <th>Operaciones</th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${companies}" var="company">
              <g:if test="${company?.salesOrders}">
                <g:each in="${company.salesOrders.findAll { order -> order.status != SaleOrderStatus.CREADA}}" var="saleOrder">
                  <tr>
                    <td>${saleOrder.clientName}</td>
                    <td>${saleOrder.status}</td>
                    <td>${saleOrder.company.toString()}</td>
                    <td><modulusuno:formatPrice number="${saleOrder.subtotal}"/></td>
                    <td><modulusuno:formatPrice number="${saleOrder.taxes}"/></td>
                    <td><modulusuno:formatPrice number="${saleOrder.total}"/></td>
                    <td>
                    <g:link controller="saleOrder" action="show" id="${saleOrder.id}" class="btn btn-default btn-sm">
                        Ver
                      </g:link>
                     <g:if test="${saleOrder.status == SaleOrderStatus.EJECUTADA}">
                        <a href="${modulusuno.invoiceUrl(saleOrder:saleOrder, format:'pdf')}" class="btn btn-success">PDF</a>
                        <a href="${modulusuno.invoiceUrl(saleOrder:saleOrder, format:'xml')}" class="btn btn-default">XML</i></a>
                     </g:if>
                      <g:if test="${saleOrder.status == SaleOrderStatus.POR_AUTORIZAR}">
                        <g:form controller="saleOrder" action="authorizeSaleOrder" id="${saleOrder.id}">
                        <button type="submit" class="btn btn-success btn-sm">
                          Autorizar
                        </button>
                        </g:form>
                      </g:if>
                    </td>
                  </tr>
                </g:each>
              </g:if>
            </g:each>
          </tbody>
        </table>
        <div class="pagination" >
          <g:paginate total="${companies ?: 0}" />
        </div>
      </div>
    </div>
  </body>
</html>
