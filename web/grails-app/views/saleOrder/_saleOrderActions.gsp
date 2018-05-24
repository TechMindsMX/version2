<%! import com.modulus.uno.status.SaleOrderStatus %>
<%! import com.modulus.uno.RejectReason %>

<!-- Modal de confirmación de eliminación -->
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
        <g:link action="deleteOrder" id="${saleOrder.id}" class="btn btn-primary">
        Sí
        </g:link>
        <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
      </div>
    </div>
  </div>
</div>

<g:if test="${!isEnabledToStamp && [SaleOrderStatus.AUTORIZADA, SaleOrderStatus.EJECUTADA, SaleOrderStatus.CANCELACION_AUTORIZADA, SaleOrderStatus.CANCELACION_EJECUTADA].contains(saleOrder.status)}">
  <div class="alert alert-warning">
    No está habilitado para timbrar facturas, debe registrar su certificado y su domicilio fiscal
  </div>
</g:if>

<div class="col-md-4">
  <g:if test="${!params.backController}">
    <g:link class="btn btn-primary" action="list">Regresar</g:link>
  </g:if><g:else>
    <g:link class="btn btn-default text-right" controller="${params.backController}" action="${params.backAction}" id="${params.backId}">Regresar</g:link>
  </g:else>

  <g:if test="${[SaleOrderStatus.EJECUTADA, SaleOrderStatus.PAGADA].contains(saleOrder.status) && isEnabledToStamp}">
    <a href="${modulusuno.invoiceUrl(saleOrder:saleOrder, format:'xml')}" class="btn btn-success" download>XML</a>
    <a href="${modulusuno.invoiceUrl(saleOrder:saleOrder, format:'pdf')}" class="btn btn-default" download>PDF</a>
  </g:if>

  <g:if test="${saleOrder.status == SaleOrderStatus.CANCELACION_EJECUTADA && isEnabledToStamp}">
    <a href="${modulusuno.invoiceAccuseUrl(saleOrder:saleOrder, format:'xml')}" class="btn btn-default" download>Acuse XML</a>
    <a href="${modulusuno.invoiceAccuseUrl(saleOrder:saleOrder, format:'pdf')}" class="btn btn-default" download>Acuse PDF</a>
  </g:if>
</div>
<div class="col-md-8">
  <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
    <div class="text-right">
      <g:if test="${saleOrder.status == SaleOrderStatus.CREADA}">
        <g:if test="${saleOrder.items}">
          <g:link class="btn btn-primary" action="sendOrderToConfirmation" id="${saleOrder.id}">Solicitar Autorización</g:link>
        </g:if>
        <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalConfirm">
          <i class="fa fa-trash"></i> Borrar
        </button>
      </g:if>
      <g:if test="${saleOrder.status == SaleOrderStatus.EJECUTADA}">
        <g:link class="btn btn-danger" action="requestCancelBill" id="${saleOrder.id}">Solicitar Cancelación de Factura</g:link>
      </g:if>
    </div>
  </sec:ifAnyGranted>

  <sec:ifAnyGranted roles="ROLE_AUTHORIZER_EJECUTOR">
    <g:if test="${saleOrder.status == SaleOrderStatus.POR_AUTORIZAR}">
      <g:if test="!saleOrder.authorizations?.find{it.user == user}}">
        <div class="text-right">
          <g:link action="authorizeSaleOrder" class="btn btn-warning" id="${saleOrder.id}">Autorizar la Orden de Venta</g:link>
          <a data-toggle="collapse" role="button" href="#inputReasonCancellation" class="btn btn-danger" aria-expanded="false" aria-controls="inputReasonCancellation">Cancelar la Orden de Venta</a>
        </div>
           <div class="row">
              <div class="col-md-12">
                <br/>
                <div class="collapse" id="inputReasonCancellation">
                  <div class="well">
                    <g:form action="cancelSaleOrder" id="${saleOrder.id}">
                    <div class="form-group">
                      <g:select name="rejectReason" from="${RejectReason.values()}" optionKey="name" optionValue="description" value="${saleOrder.rejectReason}" class="form-control" />
                      <br/>
                      <g:textArea name="comments" placeholder="Comentarios opcionales" rows="3" cols="60" maxLength="255" class="form-control"/>
                      <br/>
                      <button type="submit" class="btn btn-danger">Ejecutar Cancelación</button>
                    </div>
                    </g:form>
                  </div>
                </div>
              </div>
            </div>
      </g:if>
      <g:else>
        <div class="alert alert-warning"><g:message code="order.already.authorized"/></div>
      </g:else>
    </g:if>

    <g:if test="${saleOrder.status == SaleOrderStatus.CANCELACION_POR_AUTORIZAR}">
      <div class="text-right">
        <g:link action="authorizeCancelBill" class="btn btn-danger" id="${saleOrder.id}">Autorizar Cancelación de Factura</g:link>
        <g:link action="abortBillCancellation" class="btn btn-warning" id="${saleOrder.id}">Rechazar Cancelación de Factura</g:link>
      </div>
    </g:if>
  </sec:ifAnyGranted>

  <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR">
    <g:if test="${saleOrder.status == SaleOrderStatus.AUTORIZADA}">
      <div class="container-fluid">
        <g:form name="executeSale">
          <input type="hidden" id="saleOrderId" name="id" value="${saleOrder.id}"/>
          <g:if test="${isEnabledToStamp}">
            <div class="row">
              <div class="col-md-6 text-right">
                <companyInfo:listTemplatesPdfForCompany rfc="${saleOrder.company.rfc}" id="${saleOrder.company.id.toString()}"/>
              </div>
              <div class="col-md-6 text-right">
                <button id="btnPreview" type="button" class="btn btn-info">Vista Previa</button>
                <button id="btnExecute" type="button" class="btn btn-success">Ejecutar</button>
                <a data-toggle="collapse" role="button" href="#inputReasonCancellation" class="btn btn-danger" aria-expanded="false" aria-controls="inputReasonCancellation">Rechazar</a>
              </div>
            </div>
          </g:if>
        </g:form>
          <div class="row">
            <div class="col-md-12">
              <br/>       
              <div class="collapse" id="inputReasonCancellation">
                <div class="well">
                  <g:form action="rejectSaleOrder" id="${saleOrder.id}">
                    <div class="form-group">
                      <g:select name="rejectReason" from="${RejectReason.values()}" optionKey="name" optionValue="description" value="${saleOrder.rejectReason}" class="form-control" />
                      <br/>
                      <g:textArea name="comments" placeholder="Comentarios opcionales" rows="3" cols="60" maxLength="255" class="form-control"/>
                      <br/>
                      <button type="submit" class="btn btn-danger">Rechazar</button>
                    </div>
                  </g:form>
                </div>
              </div>
            </div>
          </div>
      </div>
    </g:if>

    <g:if test="${saleOrder.status == SaleOrderStatus.CANCELACION_AUTORIZADA && isEnabledToStamp}">
      <div class="text-right">
        <g:link action="executeCancelBill" class="btn btn-danger" id="${saleOrder.id}">Ejecutar Cancelación de Factura</g:link>
      </div>
    </g:if>
  </sec:ifAnyGranted>

</div>

