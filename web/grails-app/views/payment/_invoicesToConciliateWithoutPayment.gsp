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
    <g:form controller="conciliation" action="conciliateInvoiceWithoutPayment">
      <g:hiddenField name="saleOrderId" value="${saleOrder.id}"/>
    <tr>
      <td>${saleOrder.id}</td>
      <td>${saleOrder.clientName}</td>
      <td>${modulusuno.formatPrice(number: saleOrder.total)}</td>
      <td>${modulusuno.formatPrice(number: saleOrder.amountToPay)}</td>
      <td>${saleOrder.currency}</td>
      <td class="text-center">
        <button class="btn btn-primary">Conciliar</button>
      </td>
    </tr>
    </g:form>
    </g:each>

  </table>
</div>

