<div class="row">
  <div class="col-md-2 text-center">
    <strong>Folio</strong><br>
    ${saleOrder.id}
  </div>
  <div class="col-md-4">
    <strong>Cliente</strong>
    <br/>${saleOrder.clientName}
  </div>
  <div class="col-md-2 text-center">
    <strong>Total</strong>
    <br/>${modulusuno.formatPrice(number:saleOrder.total)}
  </div>
  <div class="col-md-2 text-center">
    <strong>Por pagar</strong>
    <br/>${modulusuno.formatPrice(number:saleOrder.amountToPay)}
  </div>
  <div class="col-md-2 text-center">
    <strong>Moneda</strong>
    <br/>${saleOrder.currency}
  </div>
</div>

