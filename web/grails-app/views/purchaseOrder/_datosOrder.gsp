<%! import com.modulus.uno.PurchaseOrderStatus %>
<%! import com.modulus.uno.RejectReason %>
<div class="col-md-12">
  <div class="portlet portlet-default">
    <div class="portlet-heading">
      <div class="portlet-title">
        <h4>Datos de la orden</h4>
      </div>
      <div class="clearfix"></div>
    </div>
    <div id="defaultPortlet" class="panel-collapse collapse in">
     <div class="portlet-body">
        <dl class="dl-horizontal">
          <dt>No. de Orden</dt>
          <dd>
          ${purchaseOrder.id}
          </dd>
          <dt>Compañía</dt>
          <dd>
          ${purchaseOrder.company}
          </dd>
          <dt>Cantidad de detalles</dt>
          <dd>
          ${purchaseOrder.items.size()}
          </dd>
          <dt>Subtotal</dt>
          <dd>
          ${modulusuno.formatPrice(number:purchaseOrder.subtotal)}
          </dd>
          <dt>Descuentos</dt>
          <dd>${modulusuno.formatPrice(number:purchaseOrder.totalDiscount)}</dd>
          <dt>IVA</dt>
          <dd>${modulusuno.formatPrice(number:purchaseOrder.totalIVA)}</dd>
          <dt>Retencion IVA</dt>
          <dd>${modulusuno.formatPrice(number:purchaseOrder.totalIvaRetention)}</dd>
          <dt>Total</dt>
          <dd>${modulusuno.formatPrice(number:purchaseOrder.total)}</dd>
          <dt>Estatus </dt>
          <dd><g:message code="purchaseOrder.status.${purchaseOrder.status}" default="${purchaseOrder.status}"/></dd>
          <g:if test="${purchaseOrder.payments.size()}" >
          <font color="#00BFFF">
            <dt>Monto Pagado </dt>
            <dd>${modulusuno.formatPrice(number: purchaseOrder.totalPayments)}</dd>
            <dt>Monto por Pagar</dt>
            <dd>${modulusuno.formatPrice(number: purchaseOrder.amountToPay)}</dd>
          </font>
          </g:if>
        </dl>
       <p>
        <g:if test="${purchaseOrder.status == PurchaseOrderStatus.CANCELADA || purchaseOrder.status == PurchaseOrderStatus.RECHAZADA}">
          <div class="alert alert-danger" role="alert">
          <label class="control-label">Motivo ${message(code:'purchaseOrder.rejectReason.'+purchaseOrder.status)}:</label>
          <g:message code="rejectReason.${purchaseOrder.rejectReason}" default="${purchaseOrder.rejectReason}"/>
          <textarea class="form-control" rows="3" cols="60" readonly>${purchaseOrder.rejectComment}</textarea>
          </div>
        </g:if>
      </p>
      </div>
      <div class="portlet-footer">
        <g:render template="actionsOrder" />
      </div>
    </div>
  </div>
</div>
