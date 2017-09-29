<div class="row">
  <g:hiddenField name="bankingTransactionId" value="${bankingTransaction.id}"/>
  <div class="col-md-12">
    <label>Pagos de Compras disponibles:</label>
		<select class="form-control" name="paymentId" required="">
			<option value="">Elegir compra...</option>
			<g:each in="${paymentsToPurchase.paymentsFiltered}" var="payment" status="index">
				<option value="${payment.id}">${paymentsToPurchase.purchaseOrders[index].id} / ${paymentsToPurchase.purchaseOrders[index].providerName} / Total: ${modulusuno.formatPrice(number:paymentsToPurchase.purchaseOrders[index].total)} - Monto Pago: ${modulusuno.formatPrice(number:payment.amount)}</option>
			</g:each>
		</select>
  </div>
</div><br/>
<div class="row">
  <div class="col-md-7"></div>
  <div class="col-md-3">
    <label>Monto a aplicar (MXN):</label>
    <input class="form-control" type="number" min="0.01" max="${toApply}" step="0.01" name="amountToApply" required="true"/>
  </div>
  <div class="col-md-2 text-right">
    <br/>
    <button class="btn btn-primary">Agregar</button>
  </div>
</div>

