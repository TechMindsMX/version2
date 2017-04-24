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
        Conciliar Facturas de Comisiones
        <small>${company.bussinessName}</small>
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <div class="portlet portlet-blue">
          <div id="bluePortlet" class="panel-collapse collapse in">
            <div class="portlet-body">

              <div class="table-responsive">
                <table class="table">
                  <tr>
                    <th>Fecha</th>
                    <th>Monto</th>
                    <th></th>
                  </tr>
                  <g:each in="${payments}" var="payment">
                  <g:form controller="conciliation" action="chooseInvoiceToConciliate" id="${payment.id}">
                  <tr>
                    <td><g:formatDate format="dd/MM/yyyy" date="${payment.dateCreated}"/></td>
                    <td>${modulusuno.formatPrice(number: payment.amount)}</td>
                    <td class="text-center">
                      <button class="btn btn-primary">Elegir Factura</button>
                    </td>
                  </tr>
                  </g:form>
                  </g:each>

                </table>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>

  </body>
</html>
