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
        Editar contrato de Cotización
        <small>${quotationContract.client}</small>
      </h1>
    </div>

	<sec:ifAnyGranted roles="ROLE_OPERATOR_QUOTATION">
    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div class="portlet-heading">
          <div class="portlet-title">
           <div class="portlet-title">
            </div>
            <div class="clearfix"></div>
          </div>
        </div>

        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>
          </div>
        </div>

        <g:form action="update">

        <div class="row">
          <div class="col-md-11">
            <div class="form-group">
              <label><g:message code="Clientes"/></label>
              <input name="id" value="${quotationContract.id}" type="hidden"/>
              <input name="clientsName"  class="form-control" value="${quotationContract.client}" disabled/>
              <input name="clients" type="hidden" class="form-control" value="${quotationContract.client.id}"/>
            </div>
          </div>
        </div>
        <br>
        <div class="row">
          <div class="col-md-3">
            <label><g:message code="Fecha Apertura" /></label>
            <input class="form-control" id="datepickerQuotation" name="initDate" required="required" value="${formatDate(format:'dd/MM/yyyy',  date: quotationContract.initDate)}">
          </div>
          <div class="col-md-4">
            <label><g:message code="Comisión"/></label>
            <input class="form-control" type="number" min="0.00" max="16.00" step="0.01" name="commission" value="${quotationContract.commission}" required=""/>
          </div>
        </div>
        <br>
        <br>
        <div class="portlet-footer">
          <div class="row">
            <div class="col-md-6">
              <g:link class="btn btn-default" controller="quotationContract" action="index">Cancelar</g:link>
            </div>
            <div class="col-md-6 text-right">
              <g:submitButton name="save" class="btn btn-primary" value="${message(code: 'default.button.save.label', default: 'Actualizar')}" />
            </div>
          </div>
        </div>
      </g:form>

        </div>
      </div>
	</sec:ifAnyGranted>
    <asset:javascript src="quotationContract/create.js"/>
    </body>
</html>
