<div class="table-responsive">
  <table class="table">
    <tr>
      <th>Folio</th>
      <th>Cliente</th>
      <th>Total</th>
      <th>Por pagar</th>
      <th>Moneda</th>
      <th></th>
    </tr>
    <g:each in="${saleOrders}" var="saleOrder">
    <tr>
      <td>${saleOrder.id}</td>
      <td>${saleOrder.clientName}</td>
      <td>${modulusuno.formatPrice(number: saleOrder.total)}</td>
      <td>${modulusuno.formatPrice(number: saleOrder.amountToPay)}</td>
      <td>${saleOrder.currency}</td>
      <td class="text-center">
        <g:link class="btn btn-primary" controller="conciliation" action="conciliateInvoiceWithoutPayment" id="${saleOrder.id}">Conciliar</g:link>
      </td>
    </tr>
    </g:each>

  </table>
</div>

