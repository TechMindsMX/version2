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
        Crear contrato de Cotización
        <small>${company}</small>
      </h1>
    </div>
	<sec:ifAnyGranted roles="ROLE_EXECUTOR_QUOTATION">
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
              
            <g:hasErrors bean="${paysheetProject}">
              <ul class="error alert alert-danger" role="alert">
                <g:eachError bean="${paysheetProject}" var="error">
                  <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message code="paysheetProject.error.${error.field}" args="${[error.defaultMessage.replace('{0}','')]}"/></li>
                </g:eachError>
              </ul>
            </g:hasErrors>

        <g:form action="save">
          <div class="row">
            <div class="col-md-11">
              <div class="form-group">
                <label><g:message code="Clientes"/></label>
                <g:select name="clients" class="form-control"
                  from="${clients.sort{it.toString()}}"
                  optionKey="id" />
              </div>
            </div>
          </div>
          <br>
          <div class="row">
            <div class="col-md-3">
              <label><g:message code="Fecha Inicio" /></label>
              <input class="form-control" type="text" id="datepickerQuotation" name="initDate" required="required">
            </div>
            <div class="col-md-4">
              <label><g:message code="Comisión"/></label>
              <input class="form-control" type="number" min="0.00" max="16.00" step="0.01" name="commission" required=""/>
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
                <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
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
