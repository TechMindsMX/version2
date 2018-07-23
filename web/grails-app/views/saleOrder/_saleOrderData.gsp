<%! import com.modulus.uno.status.SaleOrderStatus %>

<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Datos de orden de venta</h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div id="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
			<div class="row">
			<div class="col-md-6">
      <dl class="dl-horizontal">
        <dt>No. de Orden</dt>
        <dd>${saleOrder.id}</dd>
        <dt>Fecha de Creación</dt>
        <dd>${formatDate(date:saleOrder.dateCreated, format:'dd-MMMM-yyyy')}</dd>
        <g:hiddenField name="saleCurrency" value="${saleOrder.currency}"/>
        <g:hiddenField name="saleChangeType" value="${saleOrder.changeType}"/>
        <dt>Moneda</dt>
        <dd>${saleOrder.currency}</dd>
        <g:if test="${saleOrder.currency == 'USD'}">
        	<dt>Tipo de Cambio</dt>
        	<dd>${modulusuno.formatPrice(number:saleOrder.changeType)}</dd>
        </g:if>
        <dt>Estatus</dt>
        <dd><g:message code="saleOrder.status.${saleOrder.status}" default="${saleOrder.status}"/></dd>
      </dl>
      <dl class="dl-horizontal">
        <dt>Subtotal</dt>
        <dd>${modulusuno.formatPrice(number:saleOrder.subtotal)}</dd>
        <dt>IVA</dt>
        <dd>${modulusuno.formatPrice(number:saleOrder.totalIVA)}</dd>
        <dt>Retención IVA</dt>
        <dd>${modulusuno.formatPrice(number:saleOrder.totalIvaRetention)}</dd>
        <dt>Total</dt>
        <dd>${modulusuno.formatPrice(number:saleOrder.total)}</dd>
        <dt>Por pagar</dt>
        <dd>${modulusuno.formatPrice(number:saleOrder.amountToPay > 0 ? saleOrder.amountToPay : 0)}</dd>
      </dl>
      <dl class="dl-horizontal">
	      <dt>Forma de Pago:</dt>
        <dd>${saleOrder.paymentWay}</dd>	
	      <dt>Método de Pago:</dt>
        <dd>${saleOrder.paymentMethod}</dd>
	      <dt>Uso del CFDI:</dt>
        <dd>${saleOrder.invoicePurpose}</dd>
      </dl>
      <dl class="dl-horizontal">
        <dt>Notas</dt>
        <dd>${saleOrder?.note}</dd>
      </dl>
			</div>
			<div class="col-md-6">
			<g:if test="${saleOrder.status == SaleOrderStatus.EJECUTADA || saleOrder.status == SaleOrderStatus.PAGADA}">
				<h4>Pagos registrados</h4>
				<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th>Fecha</th>
								<th>Monto</th>
							</tr>
						</thead>
						<tbody>
							<g:each in="${saleOrder.payments.sort{it.dateCreated}}" var="payment">
								<tr>
									<td><g:formatDate format="dd-MM-yyyy" date="${payment.dateCreated}"/></td>
									<td>${modulusuno.formatPrice(number:payment.amount)}</td>
								</tr>
							</g:each>
						</tbody>
					</table>
				</div>
			</g:if>
			</div>
			</div>
      <p>
      <g:if test="${saleOrder.status == SaleOrderStatus.CANCELADA || saleOrder.status == SaleOrderStatus.RECHAZADA}">
      <div class="alert alert-danger" role="alert">
        <label class="control-label">Motivo ${message(code:'saleOrder.rejectReason.'+saleOrder.status)}:</label>
        <g:message code="rejectReason.${saleOrder.rejectReason}" default="${saleOrder.rejectReason}"/>
        <textarea class="form-control" rows="3" cols="60" readonly>${saleOrder.comments}</textarea>
      </div>
        </g:if>
      </p>
      <!-- actions section -->
      <g:render template="saleOrderActions"/>
      <!-- end actions section -->
    </div>
  </div>
</div>
