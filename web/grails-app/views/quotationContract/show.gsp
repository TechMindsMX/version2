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
        <small>${company}</small>
      </h1>
    </div>

    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div class="portlet-heading">
          <div class="portlet-title">
           <div class="portlet-title">
            </div>
            <div class="clearfix"></div>
          </div>

        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>
          </div>
        </div>

        <g:form name="saveCommission" url="[action:'save',controller:'QuotationContract']">

        <div class="row">
          <div class="col-md-11">
            <div class="form-group">
              <label><g:message code="Clientes"/></label>
              <g:select name="clients" class="form-control"
              from="${clients}"
              optionKey="id" />
            </div>
          </div>
        </div>
        <br>
        <div class="row">
          <div class="col-md-3">
            <label><g:message code="Fecha Inicio" /></label>
            <input class="form-control" type="text" id="datepicker" name="fechaInicio" required="required">
          </div>
          <div class="col-md-4">
            <label><g:message code="Comisión"/></label>
            <input class="form-control" type="number" min="0.00" max="16.00" step="0.01" name="commission" required=""/>
          </div>
        </div>
        <br>
        <br>
        <div class="row">
          <div class="col-md-6">
            <g:link class="btn btn-default" controller="dashboard" action="index">Cancelar</g:link>
          </div>
          <div class="col-md-6 text-right">
            <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
          </div>
        </div>
        </g:form>

        </div>
      </div>
    </div>

    <asset:javascript src="quotationContract/create.js"/>
    </body>
</html>
