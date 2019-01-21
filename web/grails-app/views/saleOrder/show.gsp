<!DOCTYPE html>
<%! import com.modulus.uno.status.SaleOrderStatus %>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'saleOrder.label', default: 'SaleOrder')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
    <!-- JS file -->
    <script src="/assets/third-party/EasyAutocomplete/dist/jquery.easy-autocomplete.min.js"></script>
    <!-- CSS file -->
    <link rel="stylesheet" href="/assets/third-party/EasyAutocomplete/dist/easy-autocomplete.min.css">
    <!--sobreescribir estilo selected-->
    <style>
      #eac-container-product-name li.selected {
        text-decoration: none;
      }

      #eac-container-sku li.selected {
        text-decoration: none;
      }

      .behind {
        z-index:0;
      }
    </style>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="icon-factura fa-3x"></i>
        Operaciones / Facturación
        <small><g:message code="saleOrder.show"/></small>
      </h1>
    </div>

    <div class="row">
      <div class="col-md-12">
        <g:render template="clientData"/>
      </div>
      <div class="col-md-12">
        <g:render template="saleOrderData"/>
      </div>
      <div id="showReplacementInvoice" class="col-md-12" hidden>
        <g:render template="replacementInvoice"/>
      </div>
      <g:if test="${saleOrder.status == SaleOrderStatus.EJECUTADA}">
      <div class="col-md-12">
        <g:render template="creditNotes"/>
      </div>
      </g:if>
    </div>

    <div class="row">
    <div class="col-md-12">
    <div class="portlet">
      <g:if test="${flash.message}">
      <div class="alert alert-info">${flash.message}</div>
      </g:if>
      <g:form controller="saleOrderItem" action="save">
        <g:render template="addItems"/>
      </g:form>
      <g:form controller="saleOrder" action="applyDiscount">
        <g:render template="listItems"/>
      </g:form>
      </div>
      </div>
    </div>
    <asset:javascript src="saleOrder/show.js"/>
  </body>
</html>
