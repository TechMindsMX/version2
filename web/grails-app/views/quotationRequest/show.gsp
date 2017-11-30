<%! import com.modulus.uno.quotation.SatConcept %>
<%! import com.modulus.uno.quotation.QuotationRequestStatus %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'quotationContract.label', default: 'QuotationContract')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>

  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-list-alt fa-3x"></i>
        Solitudes de Cotización
      </h1>
    </div>

    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-default">
        <div class="portlet-heading">
          <div class="portlet-title">
            <div class="portlet-title"></div>
            <div class="clearfix"></div>
          </div>
        </div>

        <g:if test="${quotationRequest.status == QuotationRequestStatus.SEND}">
          <g:form action="requestProcessed">
            <div class="portlet-body">
              <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
              </g:if>
              <div class="row">
                <div class="col-md-6">
                  <dl>
                    <dt>Cliente para cotización</dt>
                    <dd>${quotationRequest.quotationContract.client}</dd>
                    <dt>Fecha de apertura de la cotización</dt>
                    <dd><g:formatDate format="dd-MM-yyyy" date="${quotationRequest.quotationContract.initDate}" class="form-control"/></dd>
                  </dl>
                </div>
                <div class="col-md-6">
                  <dl>
                    <dt>Descripción</dt>
                    <dd>${quotationRequest.description}</dd>
                    <dt>Monto para la cotización</dt>
                    <dd>${modulusuno.formatPrice(number:quotationRequest.total)}</dd>
                    <dt>Subtotal de la cotización</dt>
                    <dd>${modulusuno.formatPrice(number:quotationRequest.subtotal)}</dd>
                    <dt>Iva de la cotización</dt>
                    <dd>${modulusuno.formatPrice(number:quotationRequest.iva)}</dd>
                  </dl>
                </div>
                <div class="col-md-12">
                  <dt>Facturadora </dt>
                  <g:select class="form-control" name="biller" from="${billers}" optionKey="id"/>
                </div>
                <div class="col-md-12">
                  <br>
                  <dt>Producto </dt>
                  <g:select name="productId" class="form-control"
                  from="${products}"
                  optionValue="name"
                  optionKey="id">
                  </g:select>
                </div>
                <div class="col-md-3">
                  <br>
                  <dt><g:message code="Comisión"/></dt>
                  <input class="form-control" type="number" id="commission" name="commission" value="${quotationRequest.commission}" required="required">
                </div>
              </div>
            </div>
            <input id="id" name="id" type="hidden" value="${quotationRequest.id}"/>
            <input id="quotation" name="quotation" value="${quotationRequest.quotationContract.id}" type="hidden"/>
            <input id="total" name="total" value="${quotationRequest.total}" type="hidden"/>
            <input id="subtotal" name="subtotal" value="${quotationRequest.subtotal}" type="hidden"/>
            <input id="iva" name="iva" value="${quotationRequest.iva}" type="hidden"/>
            <input id="description" name="description" value="${quotationRequest?.description}" type="hidden"/>
            <div class=" portlet-footer">
              <div class="row">
                <div class="col-md-6">
                  <g:link class="btn btn-default" controller="quotationRequest" action="index">Regresar</g:link>
                </div>
                <div class="col-md-6 text-right">
                  <div class="col-md-6">
                    <button class="btn btn-primary" type="submit"  id="${quotationRequest.id}">Procesar</button>
                  </div>
                </div>
              </div>
            </div>
          </g:form>
        </g:if>

        <g:elseif test="${quotationRequest.status == QuotationRequestStatus.PROCESSED}">
          <div class="portlet-body">
            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div class="row">
              <div class="col-md-6">
                <dl>
                  <dt>Cliente para cotización</dt>
                  <dd>${quotationRequest.quotationContract.client}</dd>
                  <dt>Fecha de apertura de la cotización</dt>
                  <dd><g:formatDate format="dd-MM-yyyy" date="${quotationRequest.quotationContract.initDate}" class="form-control"/></dd>
                  <dt>Facturadora</dt>
                  <dd>${quotationRequest.biller}</dd>
                  <dt>Comisión</dt>
                  <dd>${quotationRequest.quotationContract.commission}</dd>
                </dl>
              </div>
              <div class="col-md-6">
                <dl>
                  <dt>Descripción</dt>
                  <dd>${quotationRequest.description}</dd>
                  <dt>Monto para la cotización</dt>
                  <dd>${modulusuno.formatPrice(number:quotationRequest.total)}</dd>
                  <dt>Subtotal de la cotización</dt>
                  <dd>${modulusuno.formatPrice(number:quotationRequest.subtotal)}</dd>
                  <dt>Iva de la cotización</dt>
                  <dd>${modulusuno.formatPrice(number:quotationRequest.iva)}</dd>
                  <dt>Concepto</dt>
                  <dd>${quotationRequest.satConcept}</dd>
                </dl>
              </div>
            </div>
          </div>

          <div class=" portlet-footer">
            <div class="row">
              <div class="col-md-6">
                <g:link class="btn btn-default" controller="quotationRequest" action="index">Regresar</g:link>
              </div>
            </div>
          </div>
        </g:elseif>

        <g:else>
          <div class="portlet-body">
            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div class="row">
              <div class="col-md-6">
                <dl>
                  <dt>Cliente para cotización</dt>
                  <dd>${quotationRequest.quotationContract.client}</dd>
                  <dt>Fecha de apertura de la cotización</dt>
                  <dd><g:formatDate format="dd-MM-yyyy" date="${quotationRequest.quotationContract.initDate}" class="form-control"/></dd>
                </dl>
              </div>
              <div class="col-md-6">
                <dl>
                  <dt>Descripción</dt>
                  <dd>${quotationRequest.description}</dd>
                  <dt>Monto para la cotización</dt>
                  <dd>${modulusuno.formatPrice(number:quotationRequest.total)}</dd>
                  <dt>Subtotal de la cotización</dt>
                  <dd>${modulusuno.formatPrice(number:quotationRequest.subtotal)}</dd>
                  <dt>Iva de la cotización</dt>
                  <dd>${modulusuno.formatPrice(number:quotationRequest.iva)}</dd>
                </dl>
              </div>
            </div>
          </div>

          <div class=" portlet-footer">
            <div class="row">
              <div class="col-md-6">
                <g:link class="btn btn-default" controller="quotationRequest" action="index">Regresar</g:link>
              </div>
              <div class="col-md-6 text-right">
                <div class="col-md-6">
                  <g:link class="btn btn-primary" controller="quotationRequest" action="sendQuotation" id="${quotationRequest.id}">Solicitar</g:link>
                </div>
                <div class="col-md-2">
                  <g:link class="btn btn-primary" controller="quotationRequest" action="edit" id="${quotationRequest.id}">Editar</g:link>
                </div>
                <div class="col-md-2">
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
                          ¿Está seguro de eliminar la solicitud de cotización?
                        </div>
                        <div class="modal-footer">
                          <g:link action="delete" id="${quotationRequest.id}" class="btn btn-primary">
                            Sí
                          </g:link>
                          <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                        </div>
                      </div>
                    </div>
                  </div>

                </div>
              </div>
            </div>
          </div>
        </g:else>

      </div>
    </div>
  </body>
</html>
