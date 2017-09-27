<div class="row">
  <g:hiddenField name="bankingTransactionId" value="${bankingTransaction.id}"/>
  <div class="col-md-12">
    <label>Compras disponibles:</label>
    <g:select class="form-control" name="purchaseOrderId" from="${purchaseOrders}" noSelection="['':' Elegir compra...']" required="true" optionKey="id"/>
  </div>
</div><br/>
<div class="row">
  <div class="col-md-4"></div>
  <div class="col-md-3">
    <label>Tipo de Cambio:</label>
    <input class="form-control" type="number" min="0.01" step="0.01" required="true" id="changeType" name="changeType" readOnly="true" value="0.00"/>
  </div>
  <div class="col-md-3">
    <label>Monto a aplicar (MXN):</label>
    <input class="form-control" type="number" min="0.01" max="${toApply}" step="0.01" name="amountToApply" required="true"/>
  </div>
  <div class="col-md-2 text-right">
    <br/>
    <button class="btn btn-primary">Agregar</button>
  </div>
</div>

