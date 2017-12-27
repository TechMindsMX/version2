<%! import com.modulus.uno.quotation.QuotationPaymentRequestStatus %>
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
        Solicitud Pago
        <small>${quotationPaymentRequest.quotationContract.client}</small>
      </h1>
    </div>

    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div class="portlet-body">
          <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
          </g:if>
          <div class="row">
            <div class="col-md-6">
              <dl>
            	  <dt>Cliente </dt>
        			  <dd>${quotationPaymentRequest.quotationContract.client}</dd>
        			  <dt>Monto</dt>
                <dd>${modulusuno.formatPrice(number:quotationPaymentRequest.amount)}</dd>
                <dt>Metodo de pago</dt>
        			  <dd><g:message code="${quotationPaymentRequest.paymentMethod}"/></dd>
                <dt>Nota</dt>
        			  <dd>${quotationPaymentRequest.note}</dd>
                <dt>Estado</dt>
        			  <dd><g:message code="quotationPaymentRequest.status.${quotationPaymentRequest.status}"/></dd>
        		  </dl>
            </div>
          </div>
        </div>

        <div class="portlet-footer">
          <div class="row">
            <div class="col-md-6">
              <g:link class="btn btn-default" controller="quotationPaymentRequest" action="index">Regresar</g:link>
            </div>
            <g:if test="${quotationPaymentRequest.status == QuotationPaymentRequestStatus.SEND}">
	          <sec:ifAnyGranted roles="ROLE_EXECUTOR_QUOTATION">
            <div class="col-md-2 text-right">
              <g:link class="btn btn-default" controller="quotationPaymentRequest" action="process" id="${quotationPaymentRequest.id}">Procesar</g:link>
            </div>
            </sec:ifAnyGranted>
            </g:if>

            <g:elseif test="${quotationPaymentRequest.status == QuotationPaymentRequestStatus.CREATED}">
              <div class="col-md-2 text-right">
              <g:link class="btn btn-default" controller="quotationPaymentRequest" action="edit" id="${quotationPaymentRequest.id}">Editar</g:link>
            </div>
              <div class="col-md-2 text-right">
                <g:link class="btn btn-default" controller="quotationPaymentRequest" action="send" id="${quotationPaymentRequest.id}">Enviar</g:link>
              </div>
            <div class="col-md-2 text-right">

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
                      ¿Está seguro de eliminar la solicitud de pago?
                    </div>
                    <div class="modal-footer">
                      <g:link action="delete" id="${quotationPaymentRequest.id}" class="btn btn-primary">
                        Sí
                      </g:link>
                      <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                    </div>
                  </div>
                </div>
              </div>


            </div>
            </g:elseif>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
