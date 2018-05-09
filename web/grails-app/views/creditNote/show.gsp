<!DOCTYPE html>
<%! import com.modulus.uno.status.CreditNoteStatus %>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'creditNote.label', default: 'CreditNote')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="icon-factura fa-3x"></i>
        Operaciones / Nota de Crédito
        <small><g:message code="creditNote.show"/></small>
      </h1>
    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="portlet portlet-default">
          <div class="protlet-heading">
            <h4>Factura</h4>
          </div>
          <div class="portlet-body">
            <g:render template="/saleOrder/summaryData"/>
          </div>
        </div>

      </div>
      <div class="col-md-12">
        <g:render template="creditNoteData"/>
      </div>
    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="portlet">
          <g:if test="${flash.message}">
            <div class="alert alert-info">${flash.message}</div>
          </g:if>
          <div class="portlet-heading">
            <h4>Detalle de la Nota de Crédito</h4>
          </div>
          <div class="portlet-body">
            <g:form controller="creditNoteItem" action="save">
              <g:render template="addItems"/>
            </g:form>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
