<%! import com.modulus.uno.PurchaseOrderStatus %>
<%! import com.modulus.uno.SourcePayment %>
<div class="col-md-12">
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
					<tr>
						<th>Fecha</th>
						<th>Monto</th>
						<th>Estatus</th>
						<th>Orígen</th>
					</tr>
          <g:each in="${purchaseOrder.payments.sort{ it.dateCreated}}" var="payment" >
            <tr>
              <td>${payment.dateCreated.format("dd/MM/yyyy")}</td>
              <td>${modulusuno.formatPrice(number:payment.amount)}</td>
              <td><g:message code="purchase.payment.status.${payment.status}"/></td>
              <td><g:message code="purchase.payment.source.${payment.source}"/></td>
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
						<div class="form-group">
							<g:radioGroup class="form-control" name="source" values="[SourcePayment.MODULUS_UNO, SourcePayment.BANKING]" value="${SourcePayment.MODULUS_UNO}" labels="['STP-M1','Bancario']">
								${it.radio} ${it.label}
							</g:radioGroup>
						</div>
            <button type="submit" class="btn btn-primary">Agregar pago parcial</button>
            <g:if test="${!purchaseOrder.payments}">
							<button type="submit" class="btn btn-primary">Pagar Completo</button>
						</g:if>
          </g:form>
          <br />
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
