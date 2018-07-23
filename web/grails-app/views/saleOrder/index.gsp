<%! import com.modulus.uno.status.SaleOrderStatus %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'saleOrder.label', default: 'SaleOrder')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>

    <div class="page-title">
      <h1>Ordenes de facturación y cobranza para ${company}</h1>
      <ol class="breadcrumb">
        <li><i class="fa fa-caret-square-o-up"></i> ${company}</li>
        <li class="active">Listado de órdenes</li>
      </ol>
    </div>

    <g:if test="${flash.message}">
    <div class="bg-primary">${flash.message}</div>
    </g:if>

    <g:link class="create" action="create" class="btn btn-default" params="[companyId:company.id]">
      Nueva orden de venta
    </g:link>
    <br>
    <br>

    <table class="table table-condensed table-striped">
      <thead>
        <tr>
          <th>&nbsp;</th>
          <th>Cliente</th>
          <th>Fecha de creación</th>
          <th>Estado</th>
          <th class="text-right">Subtotal</th>
          <th class="text-right">IEPS</th>
          <th class="text-right">IVA</th>
          <th class="text-right">Total</th>
          <th class="text-right"></th>
        </tr>
      </thead>
      <tbody>
        <g:each in="${saleOrderList}" var="saleOrder">
          <tr>
            <td><g:link action="show" id="${saleOrder.id}" class="btn btn-default btn-xs"><i class="fa fa-eye"></i> Ver</g:link></td>
            <td>
              <g:link action="show" id="${saleOrder.id}">${saleOrder.clientName}</g:link>
            </td>
            <td>${formatDate(date:saleOrder.dateCreated, format:'dd-MMMM-yyyy HH:mm')}</td>
            <td>${saleOrder.status}</td>
            <td class="text-right">${modulusuno.formatPrice(number:saleOrder.subtotal)}</td>
            <td class="text-right">${modulusuno.formatPrice(number:saleOrder.totalIEPS)}</td>
            <td class="text-right">${modulusuno.formatPrice(number:saleOrder.totalIVA)}</td>
            <td class="text-right">${modulusuno.formatPrice(number:saleOrder.total)}</td>
            <g:if test="${saleOrder.status == SaleOrderStatus.EJECUTADA}">
              <td>
                <a href="${modulusuno.invoiceUrl(saleOrder:saleOrder, format:'pdf')}" class="btn btn-success">PDF</a>
                <a href="${modulusuno.invoiceUrl(saleOrder:saleOrder, format:'xml')}" class="btn btn-default">XML</i></a>
              </td>
            </g:if>
          </tr>
        </g:each>
      </tbody>
    </table>

    <div class="pagination">
      <g:paginate total="${saleOrderCount ?: 0}" />
    </div>
  </body>
</html>
