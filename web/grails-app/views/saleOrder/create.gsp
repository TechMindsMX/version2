<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'saleOrder.label', default: 'SaleOrder')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>

    <div class="page-title">
      <h1>
        <i class="icon-factura fa-3x"></i>
        Operaciones / Facturación
        <small>Creación de una orden de venta</small>
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-6">
        <g:render template="searchClient"/>
      </div>

      <g:if test="${client}">
      <g:form action="save">
      <div class="col-lg-6">
        <g:render template="formDataToCreate"/>
      </div>
      </g:form>
      </g:if>
    </div>
    <asset:javascript src="saleOrder/create.js"/>
  </body>
</html>
