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
        Cotización
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
                <dd><g:formatNumber number="${quotationRequest.amount}" type="currency" currencyCode="MXN" /></dd>
              </dl>
            </div>
          </div>
        </div>    

        <div class="portlet-footer">
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
                <g:link class="btn btn-primary" controller="quotationRequest" action="delete" id="${quotationRequest.id}">Borrar</g:link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>