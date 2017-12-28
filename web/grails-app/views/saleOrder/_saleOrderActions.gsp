<%! import com.modulus.uno.SaleOrderStatus %>
<%! import com.modulus.uno.RejectReason %>

<sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
  <g:if test="${saleOrder.status == SaleOrderStatus.CREADA}">
    <div class="row">
      <div class="col-md-4 col-md-offset-8">
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
                <g:link action="deleteOrder" id="${saleOrder.id}" class="btn btn-primary">
                Sí
                </g:link>
                <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
  </g:if>
</sec:ifAnyGranted>
<!-- -->
<p>
<g:if test="${saleOrder.items}">
  <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
    <g:if test="${saleOrder.status == SaleOrderStatus.CREADA}">
      <g:form controller="saleOrder" action="sendOrderToConfirmation" id="${saleOrder.id}">
        <button type="submit" class="btn btn-success btn-block">
          Confirmar orden de venta
        </button>
      </g:form>
    </g:if>
    <g:if test="${saleOrder.status == SaleOrderStatus.EJECUTADA}">
      <g:link class="btn btn-danger" action="requestCancelBill" id="${saleOrder.id}">Solicitar Cancelación de Factura</g:link>
      <p>
    </g:if>
  </sec:ifAnyGranted>

  <sec:ifAnyGranted roles="ROLE_AUTHORIZER_EJECUTOR">
    <g:if test="${saleOrder.status == SaleOrderStatus.POR_AUTORIZAR}">
      <g:if test="!saleOrder.authorizations?.find{it.user == user}}">
        <div class="container-fluid">
          <div class="row">
            <div class="col-md-6">
              <g:link action="authorizeSaleOrder" class="btn btn-warning btn-block" id="${saleOrder.id}">Autorizar la Orden de Venta</g:link>
            </div>
            <div class="col-md-6">
              <a data-toggle="collapse" role="button" href="#inputReasonCancellation" class="btn btn-danger btn-block" aria-expanded="false" aria-controls="inputReasonCancellation">Cancelar la Orden de Venta</a>
            </div>
            <div class="row">
              <div class="col-md-12">
                <div class="collapse" id="inputReasonCancellation">
                  <div class="well">
                    <g:form action="cancelSaleOrder" id="${saleOrder.id}">
                    <div class="form-group">
                      <g:select name="rejectReason" from="${RejectReason.values()}" value="${saleOrder.rejectReason}" class="form-control" />
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
          </div>
        </div>
        <br/>
      </g:if>
      <g:else>
        <div class="alert alert-warning"><g:message code="order.already.authorized"/></div>
      </g:else>
    </g:if>

    <g:if test="${saleOrder.status == SaleOrderStatus.CANCELACION_POR_AUTORIZAR}">
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-6">
            <g:link action="authorizeCancelBill" class="btn btn-danger btn-block" id="${saleOrder.id}">Autorizar Cancelación de Factura</g:link>
          </div>
	        <div class="col-md-6">
            <g:link action="abortBillCancellation" class="btn btn-warning btn-block" id="${saleOrder.id}">Rechazar Cancelación de Factura</g:link>
          </div>
        </div>
      </div>
    </g:if>
  </sec:ifAnyGranted>

  <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR">
    <g:if test="${saleOrder.status == SaleOrderStatus.AUTORIZADA}">
      <div class="container-fluid">
        <g:form name="executeSale">
          <input type="hidden" id="saleOrderId" name="id" value="${saleOrder.id}"/>
          <companyInfo:listTemplatesPdfForCompany rfc="${saleOrder.company.rfc}" id="${saleOrder.company.id.toString()}"/>
          <g:if test="${!isEnabledToStamp}">
            <div class="alert alert-warning">
              No está habilitado para timbrar facturas, debe registrar su certificado y su domicilio fiscal
            </div><br/>
          </g:if>

          <g:if test="${isEnabledToStamp}">
            <div class="row text-right">
              <button id="btnPreview" type="button" class="btn btn-info">Vista Previa</button>
            </div>
            <br/>
          </g:if>
          <div class="row">
            <div class="col-md-6">
              <g:if test="${isEnabledToStamp}">
              <button id="btnExecute" type="button" class="btn btn-success btn-block">Ejecutar orden de venta</button>
              </g:if>
            </div>
        </g:form>
            <div class="col-md-6">
              <a data-toggle="collapse" role="button" href="#inputReasonCancellation" class="btn btn-danger btn-block" aria-expanded="false" aria-controls="inputReasonCancellation">Rechazar la orden de venta</a>
            </div>

        <div class="row">
          <div class="col-md-12">
            <div class="collapse" id="inputReasonCancellation">
              <div class="well">
                <g:form action="rejectSaleOrder" id="${saleOrder.id}">
                <div class="form-group">
                  <g:select name="rejectReason" from="${RejectReason.values()}" value="${saleOrder.rejectReason}" class="form-control" />
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
      </div>
    </g:if>

    <g:if test="${saleOrder.status == SaleOrderStatus.CANCELACION_AUTORIZADA}">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-9">
          <g:link action="executeCancelBill" class="btn btn-danger btn-block" id="${saleOrder.id}">Ejecutar Cancelación de Factura</g:link>
        </div>
      </div>
    </div>
    </g:if>
  </sec:ifAnyGranted>

  <g:if test="${saleOrder.status == SaleOrderStatus.EJECUTADA || saleOrder.status == SaleOrderStatus.PAGADA}">
  <a href="${modulusuno.invoiceUrl(saleOrder:saleOrder, format:'xml')}" class="btn btn-success">Descarga factura XML</a>
  <a href="${modulusuno.invoiceUrl(saleOrder:saleOrder, format:'pdf')}" class="btn btn-default">Descarga factura PDF</a>
  </g:if>

  <g:if test="${saleOrder.status == SaleOrderStatus.CANCELACION_EJECUTADA}">
    <a href="${modulusuno.invoiceAccuseUrl(saleOrder:saleOrder, format:'xml')}" class="btn btn-default">Acuse XML</a>
    <a href="${modulusuno.invoiceAccuseUrl(saleOrder:saleOrder, format:'pdf')}" class="btn btn-default">Acuse PDF</a>
  </g:if>

  <g:if test="${params.backController}">
    <g:link class="btn btn-default text-right" controller="${params.backController}" action="${params.backAction}" id="${params.backId}">Regresar</g:link>
  </g:if>

</g:if>
</p>
