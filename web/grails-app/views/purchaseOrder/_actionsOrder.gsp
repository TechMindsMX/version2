<%! import com.modulus.uno.PurchaseOrderStatus %>
<%! import com.modulus.uno.RejectReason %>
<div class="btn-group" role="group">
  <!-- -->
  <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
  <g:if test="${purchaseOrder.status == PurchaseOrderStatus.CREADA}">
  <div class="row">
    <div class="col-md-4">
      <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalConfirm">
        <i class="fa fa-trash"></i> Borrar
      </button>

      <div class="modal fade" id="modalConfirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title" id="myModalLabel">Confirme la acción</h4>
            </div>
            <div class="modal-body">
              ¿Está seguro de eliminar la orden seleccionada?
            </div>
            <div class="modal-footer">
              <g:link action="deleteOrder" id="${purchaseOrder.id}" class="btn btn-primary">
              Sí
              </g:link>
              <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div><p>
  </g:if>
  </sec:ifAnyGranted>
  <!-- -->

  <g:if test="${purchaseOrder.items}">
    <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
      <g:if test="${(purchaseOrder.status == PurchaseOrderStatus.CREADA && (purchaseOrder.documents && ((!purchaseOrder.isMoneyBackOrder && purchaseOrder.documents?.size()>=2) || (purchaseOrder.isMoneyBackOrder && purchaseOrder.documents.size()==3) ))) || (purchaseOrder.status == PurchaseOrderStatus.CREADA && purchaseOrder.isAnticipated)}">
        <g:form controller="purchaseOrder" action="sendOrderToConfirmation" id="${purchaseOrder.id}">
          <button type="submit" class="btn btn-warning">
            Solicitar Autorización
          </button>
        </g:form>
      </g:if>
    </sec:ifAnyGranted>
    <sec:ifAnyGranted roles="ROLE_AUTHORIZER_EJECUTOR">
      <g:if test="${purchaseOrder.status == PurchaseOrderStatus.POR_AUTORIZAR &&
         !purchaseOrder.authorizations?.find{it.user == user}}">
        <div class="row">
          <div class="col-md-12">
            <div class="col-md-6">
              <g:if test="${purchaseOrder.bankAccount}">
              <g:form controller="purchaseOrder" action="confirmThePurchaseOrder" id="${purchaseOrder.id}">
              <button type="submit" class="btn btn-primary">
                Autorizar el pago
              </button>
              </g:form>
              </g:if>
            </div>
            <div class="col-md-6">
              <a data-toggle="collapse" role="button" href="#inputReasonCancellation" class="btn btn-danger btn-block" aria-expanded="false" aria-controls="inputReasonCancellation">Cancelar la orden</a>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-12">
            <div class="collapse" id="inputReasonCancellation">
              <div class="well">
                <g:form action="cancelPurchaseOrder" id="${purchaseOrder.id}">
                <div class="form-group">
                    <g:select name="rejectReason" from="${RejectReason.values()}" noSelection="['':'- Elija el motivo de cancelación...']" required="" class="form-control" valueMessagePrefix="rejectReason" />
                    <g:textArea name="rejectComment" placeholder="Ingrese un comentario de la cancelación" rows="3" cols="60" maxLength="255" class="form-control"/>
                  <button type="submit" class="btn btn-danger">Ejecutar Cancelación</button>
                </div>
                </g:form>
              </div>
            </div>
          </div>
        </div>
      </g:if>
      <g:else>
        <g:if test="${purchaseOrder.authorizations?.find{it.user == user}}">
          <div class="alert alert-warning"><g:message code="order.already.authorized"/></div>
        </g:if>
      </g:else>
    </sec:ifAnyGranted>
  </g:if>


  <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR">
    <g:if test="${purchaseOrder.status == PurchaseOrderStatus.AUTORIZADA && !purchaseOrder.payments}">
      <a data-toggle="collapse" role="button" href="#inputReasonRejected" class="btn btn-danger" aria-expanded="false" aria-controls="inputReasonRejected">Rechazar la orden</a>
      <div class="row">
        <div class="col-md-12">
          <div class="collapse" id="inputReasonRejected">
            <div class="well">
              <g:form action="rejectPurchaseOrder" id="${purchaseOrder.id}">
                <div class="form-group">
                  <g:select name="rejectReason" from="${RejectReason.values()}" noSelection="['':'- Elija el motivo de rechazo...']" required="" class="form-control" valueMessagePrefix="rejectReason" />
                  <g:textArea name="rejectComment" placeholder="Ingrese un comentario del rechazo" rows="3" cols="60" maxLength="255" class="form-control"/>
                  <button type="submit" class="btn btn-danger">Ejecutar Rechazo</button>
                </div>
              </g:form>
            </div>
          </div>
        </div>
      </div>
    </g:if>
  </sec:ifAnyGranted>

  <g:if test="${params.backController}">
    <g:link class="btn btn-default text-right" controller="${params.backController}" action="${params.backAction}">Regresar</g:link>
  </g:if>
</div>
