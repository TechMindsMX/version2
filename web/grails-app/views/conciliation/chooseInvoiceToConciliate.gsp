<%! import com.modulus.uno.ConciliationStatus %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-usd fa-3x"></i>
        Conciliación de Cobros M1
        <small>Elegir factura(s)</small>
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <div class="portlet">
          <div class="portlet-body">
            <div class="container-fluid">
              <g:render template="dataPayment"/>
              <hr>
              <g:if test="${toApply > 0}">
                <g:if test="${flash.message}">
                  <div class="alert alert-danger" role="alert">${flash.message}</div>
                </g:if>

                <g:form action="addSaleOrderToConciliate">
                  <g:render template="addInvoiceToConciliate"/>
                </g:form>
                <hr>
              </g:if>

              <g:if test="${conciliations}">
              <label>Facturas seleccionadas:</label>
              <div class="table-responsive">
                <table class="table table-condensed table-striped">
                  <thead>
                  <tr>
                    <th class="col-md-4 text-center">Factura</th>
                    <th class="text-center">Total</th>
                    <th class="text-center">Por pagar</th>
                    <th class="text-center">Monto a aplicar (MXN)</th>
                    <th class="text-center">Nuevo Saldo</th>
                    <th class="text-center">Moneda</th>
                    <th class="text-center">Tipo Cambio</th>
                    <th></th>
                  </tr>
                  </thead>
                  <tbody>
                  <g:each in="${conciliations}" var="conciliation">
                  <tr>
                    <td>${conciliation.saleOrder.id} / ${conciliation.saleOrder.clientName}</td>
                    <td class="text-right">${modulusuno.formatPrice(number: conciliation.saleOrder.total)}</td>
                    <td class="text-right">${modulusuno.formatPrice(number: conciliation.saleOrder.amountToPay)}</td>
                    <td class="text-right">${modulusuno.formatPrice(number: conciliation.amount)}</td>
                    <td class="text-right">${modulusuno.formatPrice(number: conciliation.saleOrder.currency == "MXN" ? conciliation.saleOrder.amountToPay - conciliation.amount : conciliation.saleOrder.amountToPay - (conciliation.amount/conciliation.changeType)) }</td>
                    <td class="text-center">${conciliation.saleOrder.currency}</td>
                    <td class="text-right">${conciliation.changeType ?: "NA"}</td>
                    <td class="text-center">
                      <g:if test="${conciliation.status == ConciliationStatus.TO_APPLY}">
                        <g:form action="deleteConciliation" id="${conciliation.id}">                
                          <button class="btn btn-danger">Quitar</button>
                        </g:form>
                      </g:if>  
                    </td>
                  </tr>
                  </g:each>
                  </tbody>
                </table>
              </div>
              <hr>
              </g:if>

              <g:render template="actions"/>

            </div>
          </div>
        </div>
      </div>
    </div>
    <asset:javascript src="conciliation/chooseInvoice.js"/>
  </body>
</html>
