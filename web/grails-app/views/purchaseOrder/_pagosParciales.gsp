<%! import com.modulus.uno.PurchaseOrderStatus %>
<div class="col-lg-4">
  <div class="portlet portlet-default">
    <div class="portlet-heading">
      <div class="portlet-title">
        <h4>Pagos parciales de la orden</h4>
      </div>
      <div class="clearfix"></div>
    </div>
    <div id="defaultPortlet" class="panel-collapse collapse in">
      <div class="portlet-body">
        <table class="table table-condensed">
          <g:each in="${purchaseOrder.payments.sort{ it.dateCreated}}" var="payment" >
            <tr>
              <th>${payment.dateCreated.format("dd/MMM/yyyy")}</th><th> ${g.formatNumber(number:payment.amount, type:"currency", maxFractionDigits:"2", locale:"es_MX")}</th>
            </tr>
          </g:each>
        </table>
      </div>
      <div class="portlet-footer">
        <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR">
        <g:if test="${purchaseOrder.status == PurchaseOrderStatus.AUTORIZADA}">
        <g:if test="${enabledToPay}">
          <g:form class="form-inline" controller="purchaseOrder" action="executePurchaseOrder" id="${purchaseOrder.id}">
            <div class="form-group">
              <label class="sr-only" for="exampleInputAmount">Monto</label>
              <div class="input-group">
                <div class="input-group-addon">$</div>
                <input type="text" class="form-control" id="amount" placeholder="Monto" name="amount" pattern="[0-9]+(\.[0-9]{2})?">
              </div>
            </div>
            <button type="submit" class="btn btn-primary">Agregar pago parcial</button>
          </g:form>
          <br />
          <g:if test="${purchaseOrder.status == PurchaseOrderStatus.AUTORIZADA }">
            <g:if test="${purchaseOrder.bankAccount && !purchaseOrder.payments}">
              <g:form controller="purchaseOrder" action="executePurchaseOrder" id="${purchaseOrder.id}">
                <button type="submit" class="btn btn-info btn-block">Pagar Completo</button>
              </g:form>
            </g:if>
          </g:if>
        </g:if>
        <g:else>
          <div class="alert alert-warning">
            La empresa no está habilitada para hacer pagos, verifique que tiene configurada la comisión correspondiente y que se encuentre registrada su cuenta STP
          </div>
        </g:else>
        </g:if>
        </sec:ifAnyGranted>
      </div>
    </div>
  </div>
</div>
