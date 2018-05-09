<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'creditNote.label', default: 'SaleOrder')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>

    <div class="page-title">
      <h1>
        <i class="icon-factura fa-3x"></i>
        Operaciones / Nota de Crédito
        <small>Creación de una Nota de Crédito</small>
      </h1>
    </div>

    <g:if test="${flash.message}">
      <div class="alert alert-info">${flash.message}</div>
    </g:if>

    <div class="row">
      <div class="col-md-12">
        <div class="portlet portlet-default">
          <div class="portlet-body">
            <g:render template="/saleOrder/summaryData"/>
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <g:form action="save">
      <div class="col-md-12">
        <g:render template="form"/>
      </div>
      </g:form>
    </div>
  </body>
</html>
