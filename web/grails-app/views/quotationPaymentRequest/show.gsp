
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
        			  <dd>${quotationPaymentRequest.paymentWay}</dd>
                <dt>Nota</dt>
        			  <dd>${quotationPaymentRequest.note}</dd>
                <dt>Estado</dt>
        			  <dd>${quotationPaymentRequest.status}</dd>
        		  </dl>
            </div>
          </div>
        </div>

        <div class="portlet-footer">
          <div class="row">
            <div class="col-md-6">
              <g:link class="btn btn-default" controller="quotationPaymentRequestController" action="index">Regresar</g:link>
            </div>
            <div class="col-md-6 text-right">
              <g:link class="btn btn-default" controller="quotationPaymentRequestController" action="edit" id="${quotationPaymentRequest.id}">Editar</g:link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
