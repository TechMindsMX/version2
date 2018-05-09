<%! import com.modulus.uno.PaymentWay %>
<%! import com.modulus.uno.PaymentMethod %>
<div class="portlet portlet-default">
  <div class="portlet-body">

    <g:hiddenField name="saleOrderId" value="${saleOrder.id}"/>
    <div class="form-group">
      <label>Forma de Pago:</label>
      <g:select class="form-control" name="paymentWay" from="${PaymentWay.values()}"/>
    </div>
    <div class="form-group">
      <label>Método de Pago:</label>
      <g:select class="form-control" name="paymentMethod" from="${PaymentMethod.values()}"/>
    </div>

  </div>
  <div class="portlet-foot">
    <div class="row">
      <div class="col-md-6">
        <g:link class="btn btn-primary" controller="saleOrder" action="show" id="${saleOrder.id}">Cancelar</g:link>
      </div>
      <div class="col-md-6 text-right">
        <button class="btn btn-primary" type="submit">Guardar</button>
      </div>
    </div>
  </div>
</div>

