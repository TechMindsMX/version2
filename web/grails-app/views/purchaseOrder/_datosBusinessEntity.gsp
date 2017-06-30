<%! import com.modulus.uno.PurchaseOrderStatus %>
<%! import com.modulus.uno.RejectReason %>
<<<<<<< HEAD
<div class="col-md-offset-2 col-md-8">
=======
<div class="col-md-12">
>>>>>>> feature/329
  <div class="portlet portlet-default">
    <div class="portlet-heading">
      <div class="portlet-title">
        <h4>Datos del ${messageBusinessEntityOrder}</h4>
      </div>
      <div class="clearfix"></div>
    </div>
    <div id="defaultPortlet" class="panel-collapse collapse in">
      <div class="portlet-body">
        <dl class="dl-horizontal">
          <dt>Nombre:</dt>
          <dd>${purchaseOrder.providerName}</dd>
          <dt>Cuenta Bancaria:</dt>
          <dd>${purchaseOrder.bankAccount}</dd>
          <dt>Fecha de creación:</dt>
          <dd>${formatDate(date:purchaseOrder.dateCreated, format:'dd-MMMM-yyyy HH:mm')}</dd>
          <dt>Fecha de Pago:</dt>
          <dd>${formatDate(date:purchaseOrder.fechaPago?:purchaseOrder.dateCreated, format:'dd-MMMM-yyyy')}</dd>
          <dt>Notas </dt>
          <dd>${purchaseOrder?.note}</dd>
          <g:if test="${purchaseOrder.isAnticipated}">
            <p>
            <div class="alert alert-info" style="text-align:center" >
              Orden de Compra sin Factura
            </div>
          </g:if>
        </dl>
      </div>
    </div>
  </div>
</div>
