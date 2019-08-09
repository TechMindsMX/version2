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

        <modulusuno:showFilters controller="saleOrder"
          action="search"
          filters="['rfc', 'clientName', 'stampedDateInit', 'stampedDateEnd', 'status', 'currency']"
          labels="['RFC', 'Cliente', 'Timbrada Desde', 'Timbrada Hasta', 'Estatus', 'Moneda']"
          filterTypes="['text', 'text', 'text', 'text', 'select', 'select']"
          optionsSelectFilters="['', '', '', '', 'SaleOrderStatus', 'SaleOrderCurrency' ]"
          filterValues="${filterValues}"
          viewAll="list"/>

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
           <g:sortableColumn property="id" title="No. de Orden" />
           <g:sortableColumn property="dateCreated" title="Fecha de Creación" />
           <g:sortableColumn property="stampedDate" title="Fecha de Timbrado" />
           <g:sortableColumn property="clientName" title="Cliente" />
           <g:sortableColumn property="status" title="Estatus" />
           <g:sortableColumn property="invoiceSerie" title="Serie" />
           <g:sortableColumn property="invoiceFolio" title="Folio" />
           <g:sortableColumn property="currency" title="Moneda" />
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
                <g:link controller="saleOrder" action="stampedDateForSaleOrders" id="${sale.id}" class="glyphicon glyphicon-refresh"></g:link>
              </g:if>
              <g:else>
                <g:formatDate format="dd-MM-yyyy" date="${sale.stampedDate}"/>
              </g:else>
            </td>
            <td>${sale.clientName}<br/>${sale.rfc}</td>
            <td><g:message code="saleOrder.status.${sale.status}" default="${sale.status}"/> </td>
            <td class="text-center">
                <g:if test="${sale.invoiceSerie!=null}">
                  ${sale.invoiceSerie}
                </g:if><g:else>
                  <g:if test="${sale.status == SaleOrderStatus.EJECUTADA}">
                  <g:link class="btn btn-primary" action="loadSerieFromInvoice" id="${sale.id}">
                    <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
                  </g:link>
                  </g:if>
                </g:else>
            </td>
            <td class="text-center">
              <g:if test="${sale.invoiceFolio!=null}">
                ${sale.invoiceFolio}
              </g:if><g:else>
                <g:if test="${sale.status == SaleOrderStatus.EJECUTADA}">
                  <g:link class="btn btn-primary" action="loadFolioFromInvoice" id="${sale.id}">
                    <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
                  </g:link>
                </g:if>
              </g:else>
            </td>
            <td class="text-center">${sale.currency}</td>
            <td class="text-right">${modulusuno.formatPrice(number: sale.total)}</td>
          </tr>
         </g:each>
         <tbody>
         <tfoot>
          <g:if test="${params.currency}">
            <tr>
              <g:set var="currencyTotal" value="${saleOrders.findAll({v -> v.currency == params.currency})*.total.sum()}" />
              <td colspan="7"></td>
              <td class="text-right"><strong>${params.currency}</strong></td>
              <td class="text-right"><strong>${modulusuno.formatPrice(number: currencyTotal)}</strong></td>
            </tr>
          </g:if>
          <g:else>
            <g:each in="${['MXN', 'USD']}" var="currencyType">
              <tr>
                <g:set var="currencyTotal" value="${saleOrders.findAll({v -> v.currency == currencyType})*.total.sum() ?: 0}" />
                <td colspan="7"></td>
                <td class="text-right"><strong>${currencyType}</strong></td>
                <td class="text-right"><strong>${modulusuno.formatPrice(number: currencyTotal)}</strong></td>
              </tr>
            </g:each>
          </g:else>
          <tr>
            <td colspan="7"></td>
            <td class="text-right"><strong>Suma Total</strong></td>
            <td class="text-right"><strong>${modulusuno.formatPrice(number: saleOrders*.total.sum())}</strong></td>
          </tr>
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
<asset:javascript src="saleOrder/list.js"/>
</body>
</html>
