<%! import com.modulus.uno.SaleOrderStatus %>

<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Datos de orden de venta</h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div id="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
      <dl class="dl-horizontal">
        <dt>No. de Orden</dt>
        <dd>
        ${saleOrder.id}
        </dd>
        <dt>Cantidad de detalles</dt>
        <dd>
        ${saleOrder.items.size()}
        </dd>
        <dt>Subtotal</dt>
        <dd>
        ${modulusuno.formatPrice(number:saleOrder.subtotal)}
        </dd>
        <dt>IVA</dt>
        <dd>${modulusuno.formatPrice(number:saleOrder.totalIVA)}</dd>
        <dt>IEPS</dt>
        <dd>${modulusuno.formatPrice(number:saleOrder.totalIEPS)}</dd>
        <dt>Total</dt>
        <dd>${modulusuno.formatPrice(number:saleOrder.total)}</dd>
        <dt>Estado</dt>
        <dd><g:message code="saleOrder.status.${saleOrder.status}" default="${saleOrder.status}"/></dd>
        <dt>Notas</dt>
        <dd>${saleOrder?.note}</dd>
      </dl>
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
