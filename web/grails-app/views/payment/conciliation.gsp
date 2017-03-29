<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'saleOrder.label', default: 'SaleOrder')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-usd fa-3x"></i>
        Conciliación de Pagos
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <div class="portlet portlet-blue">
          <div id="bluePortlet" class="panel-collapse collapse in">
            <div class="portlet-body">

              <!-- Nav tabs -->
              <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="${styleClasses?.tabReferenced}">
                  <g:link action="referencedPayments">Pagos Referenciados</g:link>
                </li>
                <li role="presentation" class="${styleClasses?.tabNotReferenced}">
                  <g:link action="notReferencedPayments">Pagos no Referenciados</g:link>
                </li>
                <li role="presentation" class="${styleClasses?.tabInvoiceWithoutPayment}">
                  <g:link action="conciliateInvoicesWithoutPayments">Conciliar Facturas sin pago</g:link>
                </li>
              </ul>

              <!-- Tab panes -->
              <div class="tab-content">
                <div role="tabpanel" class="tab-pane ${styleClasses?.tabReferenced}" id="referenced">
                  <g:render template="referencedPayments"/>
                </div>
                <div role="tabpanel" class="tab-pane ${styleClasses?.tabNotReferenced}" id="notReferenced">
                  <g:render template="notReferencedPayments"/>
                </div>
                <div role="tabpanel" class="tab-pane ${styleClasses?.tabInvoiceWithoutPayment}" id="invoiceWithoutPayment">Conciliar Factura sin pago</div>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>

  </body>
</html>
