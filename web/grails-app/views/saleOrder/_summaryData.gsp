<%! import com.modulus.uno.status.SaleOrderStatus %>
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

