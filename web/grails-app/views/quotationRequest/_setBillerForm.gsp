<%! import com.modulus.uno.PaymentWay %>
<%! import com.modulus.uno.PaymentMethod %>
<%! import com.modulus.uno.InvoicePurpose %>

<g:hiddenField name="id" value="${quotationRequest.id}"/>
<div class="row">
  <div class="col-md-12">
    <div class="form-group">
      <label>Facturadora:</label>
      <g:select class="form-control" name="biller" from="${billers}" optionKey="id"/>
    </div>
    <div class="form-group">
      <label>Producto:</label>
      <g:select name="product" class="form-control" from="${products}" optionValue="name" optionKey="id"/>
    </div>
    <div class="form-group">
      <label>Comisión:</label>
      <input class="form-control" type="number" id="commission" name="commission" value="${quotationRequest.commission}" required="required">
    </div>
    <div class="form-group">
      <label>Forma de Pago:</label>
      <g:select class="form-control" name="paymentWay" from="${PaymentWay.values()}"/>
    </div>
    <div class="form-group">
      <label>Método de Pago:</label>
      <g:select class="form-control" name="paymentMethod" from="${PaymentMethod.values()}"/>
    </div>
    <div class="form-group">
      <label>Uso del CFDI:</label>
      <g:select class="form-control" name="invoicePurpose" from="${InvoicePurpose.values()}"/>
    </div>
  </div>
</div>
<div class="row">
  <div class="col-md-12 text-right">
    <button class="btn btn-primary" type="submit"  id="${quotationRequest.id}">Procesar</button>
  </div>
</div>
