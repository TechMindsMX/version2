<%! import com.modulus.uno.CommissionsInvoiceStatus %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'commission.label', default: 'Commission')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-info-circle fa-3x"></i>
        Factura de Comisiones
        <small>${invoice.receiver.bussinessName}</small>
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-9 col-lg-offset-1">
        <div class="portlet portlet-blue">
          <div class="portlet-heading">
          </div>
          <div id="bluePortlet" class="panel-collapse collapse in">
            <div class="portlet-body">

              <div class="row">
                <div class="col-md-3 text-center"><strong>Fecha</strong></div>
                <div class="col-md-3 text-center"><strong>Estatus</strong></div>
                <div class="col-md-2 text-center"><strong>Total</strong></div>
                <div class="col-md-2 text-center"><strong>Pagado</strong></div>
                <div class="col-md-2 text-center"><strong>Por pagar</strong></div>
              </div>
              <div class="row">
                <div class="col-md-3 text-center"><g:formatDate format="dd-MM-yyyy" date="${invoice.dateCreated}"/></div>
                <div class="col-md-3 text-center"><g:message code="commissions.invoice.status.${invoice.status}"/></div>
                <div class="col-md-2 text-center">${modulusuno.formatPrice(number:invoice.total)}</div>
                <div class="col-md-2 text-center">
                  ${modulusuno.formatPrice(number:invoice.totalPayed)}
                </div>
                <div class="col-md-2 text-center">${modulusuno.formatPrice(number:invoice.amountToPay)}</div>
              </div>
              </br>
              <div class="row">
                <div class="col-md-6"><strong>Detalle</strong></div>
                <div class="col-md-6 text-right">
                  <g:if test="${invoice.totalPayed > 0}">
                  <button type="button" class="btn btn-info" data-toggle="modal" data-target="#modalPayments">
                    Ver Pagos
                  </button>

                  <div class="modal fade text-left" id="modalPayments" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                    <div class="modal-dialog" role="document">
                      <div class="modal-content">
                        <div class="modal-header">
                          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                          <h4 class="modal-title" id="myModalLabel">Pagos realizados a la factura</h4>
                        </div>
                        <div class="modal-body">

                          <div class="table-responsive">
                            <table class="table">
                              <tr>
                                <th class="text-center"><strong>Fecha</strong></th>
                                <th class="text-center"><strong>Monto</strong></th>
                              </tr>
                              <g:each in="${invoice.payments}" var="payment">
                              <tr>
                                <td class="text-center"><g:formatDate format="dd-MM-yyyy" date="${payment.dateCreated}"/></td>
                                <td class="text-right">${modulusuno.formatPrice(number:payment.amount)}</td>
                              </tr>
                                  </g:each>
                            </table>
                          </div>

                        </div>
                        <div class="modal-footer">
                          <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                        </div>
                      </div>
                    </div>
                  </div>
                  </g:if>

                </div>
              </div>
              <div class="row">
                <div class="col-md-12">
                  <div class="table-responsive">
                    <table class="table">
                      <thead>
                      <tr>
                        <th class="text-center">Tipo</th>
                        <th class="text-center">Monto</th>
                      </tr>
                      </thead>
                      <tbody>
                      <g:each in="${commissionsSummary.sort{it.type.toString()}}" var="totalType">
                      <tr>
                        <td class="text-center">${totalType.type}</td>
                        <td class="text-right">${modulusuno.formatPrice(number:totalType.total)}</td>
                      </tr>
                      </g:each>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-12">
                  <g:if test="${invoice.status == CommissionsInvoiceStatus.CREATED}">

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
                            ¿Está seguro de eliminar la factura de comisiones?
                          </div>
                          <div class="modal-footer">
                            <g:link class="btn btn-danger" action="deleteInvoice" id="${invoice.id}" params="[corporateId:corporate.id]">Sí</g:link>
                            <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                          </div>
                        </div>
                      </div>
                    </div>

                  </g:if>

                  <g:if test="${invoice.status == CommissionsInvoiceStatus.STAMPED}">

                    <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalCancelConfirm">
                      <i class="fa fa-ban"></i> Cancelar
                    </button>

                    <div class="modal fade" id="modalCancelConfirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                      <div class="modal-dialog" role="document">
                        <div class="modal-content">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">Confirme la acción</h4>
                          </div>
                          <div class="modal-body">
                            ¿Está seguro de cancelar la factura de comisiones?
                          </div>
                          <div class="modal-footer">
                            <g:link class="btn btn-danger" action="cancelStampedInvoice" id="${invoice.id}" params="[corporateId:corporate.id]">Sí</g:link>
                            <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                          </div>
                        </div>
                      </div>
                    </div>

                  </g:if>

                </div>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-2 col-md-offset-10">
        <g:link class="btn btn-primary" action="listCommissionsInvoice" id="${invoice.receiver.id}" params="[corporateId:corporate.id]">Regresar</g:link>
      </div>
    </div>

  </body>
</html>
