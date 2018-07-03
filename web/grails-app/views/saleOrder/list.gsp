<%! import com.modulus.uno.status.SaleOrderStatus%>
<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'depositOrder.label', default: 'DepositOrder')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
  <div class="page-title">
    <h1>
      <i class="icon-factura fa-3x"></i>
        Facturación &amp; Cobranza<small><g:message code="saleOrder.list" args="[entityName]" /></small>
    </h1>
  </div>
<div id="edit-address" class="content scaffold-edit" role="main">
  <div class="portlet portlet-blue">
    <div id="horizontalFormExample" class="panel-collapse collapse in">
      <div class="portlet-body">
        
        <modulusuno:showFilters controller="saleOrder" action="search" filters="['rfc', 'clientName']" labels="['RFC', 'CLIENTE']" filterValues="${filterValues}" viewAll="list"/>
        
        <g:if test="${flash.message}">
          <div class="alert alert-danger" role="alert">${flash.message}</div>
        </g:if>
        <g:if test="${messageSuccess}">
          <div class="well well-sm alert-success">${messageSuccess}</div>
        </g:if>
      <div class="table-responsive">
      <div class="container-fluid">
        <table class="table table-condensed table-striped">
        <thead>
         <tr>
           <th class="text-center">No. de Orden</th>
           <th class="text-center">Fecha de Creación</th>
           <th class="text-center">Fecha de Timbrado</th>
           <th class="text-center col-xs-4">Cliente</th>
           <th class="text-center col-xs-2">Estatus</th>
           <th class="text-center">Serie</th>
           <th class="text-center">Folio</th>
           <th class="text-center"></th>
           <th class="text-center">Total</th>
          </tr>
          <thead>
          <g:if test="${saleOrders.isEmpty()}">
            <div class="alert alert-danger" role="alert">
              <g:message code="saleOrder.list.empty"/>
            </div>
          </g:if>
         <tbody>
         <g:each in="${saleOrders}" var="sale">
         <tr class="${message(code: 'saleOrder.style.background.'+sale.status)}">
            <td class="text-center"><g:link action="show" id="${sale.id}">${sale.id}</g:link></td>
            <td class="text-center"><g:formatDate format="dd-MM-yyyy" date="${sale.dateCreated}"/></td>
            <td class="text-center">
              <g:if test="${sale.status == SaleOrderStatus.EJECUTADA && sale.stampedDate == null}">
                <g:link class="glyphicon glyphicon-refresh"></g:link>
              </g:if>
              <g:else>
                <g:formatDate format="dd-MM-yyyy" date="${sale.stampedDate}"/>
              </g:else>
            </td>
            <td>${sale.clientName}<br/>${sale.rfc}</td>
            <td><g:message code="saleOrder.status.${sale.status}" name="${sale.status}" id="status"/> </td>
            <td class="text-center">${sale.invoiceSerie}</td>
            <td class="text-center">${sale.invoiceFolio}</td>
            <td class="text-center">${sale.currency}</td>
            <td class="text-right">${modulusuno.formatPrice(number: sale.total)}</td>
          </tr>
         </g:each>
         <tbody>
         <tfoot>
            <td colspan="7"></td>
            <td class="text-right"><strong>Suma Total</strong></td>
            <td class="text-right"><strong>${modulusuno.formatPrice(number: saleOrders*.total.sum())}</strong></td>
         </tfoot>
       </table>
       </div>
       <g:if test="${!filterValues}">
       <nav>
          <div class="pagination">
            <g:paginate class="pagination" controller="saleOrder" action="list" total="${saleOrderCount}" />
          </div>
        </nav>
        </g:if>
      </div>
    </div>
  </div>
</div>
</div>
</body>
</html>
