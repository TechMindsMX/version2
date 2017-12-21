<%! import com.modulus.uno.PaymentMethod%>
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
        Editar solicitud de pago de cotización
        <small>${quotationPaymentRequest.quotationContract.client}</small>
      </h1>
    </div>

    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div class="portlet-title">
          <div class="clearfix"></div>
        </div>

        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>

            <g:hasErrors bean="${paysheetProject}">
              <ul class="error alert alert-danger" role="alert">
                <g:eachError bean="${paysheetProject}" var="error">
                  <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message code="paysheetProject.error.${error.field}" args="${[error.defaultMessage.replace('{0}','')]}"/></li>
                </g:eachError>
              </ul>
            </g:hasErrors>

            <g:form action="update">
              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <label><g:message code="Cliente"/></label>
                    <input name="quotationClient" class="form-control" value="${quotationPaymentRequest.quotationContract.client}" disabled>
                    <input name="quotation" class="form-control" value="${quotationPaymentRequest.quotationContract.id}"type="hidden">
                    <input name="id" class="form-control" value="${quotationPaymentRequest.id}"type="hidden">
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-3">
                  <label><g:message code="Monto" /></label>
                  <input class="form-control" type="text" id="amonut" value="${quotationPaymentRequest.amount}" name="amount" required="required" pattern="([0-9]*[.])?[0-9]+">
                </div>
                <div class="col-md-9">
                  <label><g:message code="Nota" /></label>
                  <input class="form-control" name="note" value="${quotationPaymentRequest.note}" type="text" required=""/>
                </div>
              </div>
              <br>
              <div class="row">
                <div class="col-md-3">
                  <label><g:message code="Modo de Pago" /></label>
                  <g:select class="form-control" name="paymentMethod" value="${quotationPaymentRequest.paymentMethod}" from="${PaymentMethod.values()}"/>
                </div>
              </div>
              <br>

              <div class="portlet-footer">
                <div class="row">
                  <div class="col-md-6">
                    <g:link class="btn btn-default" controller="quotationContract" action="index">Cancelar</g:link>
                  </div>
                  <div class="col-md-6 text-right">
                    <g:submitButton name="Guardar" class="btn btn-primary" value="${message(code: 'default.button.guardar.label', default: 'Guardar')}" />
                  </div>
                </div>
              </div>
            </g:form>
          </div>
        </div>
      </div>
    </div>
    <asset:javascript src="quotationContract/create.js"/>
  </body>
</html>
