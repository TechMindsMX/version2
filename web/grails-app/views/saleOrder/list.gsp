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
        <modulusuno:showFilters controller="saleOrder" action="search" filters="['rfc', 'clientName']" labels="['RFC', 'CLIENTE']"/>
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
           <th>RFC</th>
           <th>Cliente</th>
           <th>Estatus</th>
           <th>Fecha de Cobro</th>
           <th>Moneda</th>
           <th>Total</th>
          </tr>
          <g:if test="${saleOrders.isEmpty()}">
            <div class="alert alert-danger" role="alert">
              <g:message code="saleOrder.list.empty"/>
            </div>
          </g:if>
         <g:each in="${saleOrders}" var="sale">
         <tr class="${message(code: 'saleOrder.style.background.'+sale.status)}">
            <td class="text-center"><g:link action="show" id="${sale.id}">${sale.id}</g:link></td>
            <td>${sale.rfc}</td>
            <td>${sale.clientName}</td>
            <td><g:message code="saleOrder.status.${sale.status}" default="${sale.status}"/> </td>
            <td><g:formatDate format="dd-MM-yyyy" date="${sale.fechaCobro}"/></td>
            <td>${sale.currency}</td>
            <td class="text-right">${modulusuno.formatPrice(number: sale.total)}</td>
          </tr>
         </g:each>
       </table>
       <nav>
          <div class="pagination">
            <g:paginate class="pagination" controller="saleOrder" action="list" total="${saleOrderCount}" />
          </div>
        </nav>
      </div>
    </div>
  </div>
</div>
</div>
</body>
</html>
