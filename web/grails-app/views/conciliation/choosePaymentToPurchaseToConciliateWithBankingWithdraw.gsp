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
        Conciliación de Retiros Bancarios
        <small>Elegir compra(s)</small>
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <div class="panel panel-primary">
          <div class="panel-body">
            <div class="container-fluid">

              <g:render template="dataBankingTransaction"/>
              <hr>
              <g:if test="${toApply > 0}">
                <g:if test="${flash.message}">
                  <div class="alert alert-danger" role="alert">${flash.message}</div>
                </g:if>

                <g:form action="addPaymentToPurchaseToConciliate">
                  <g:render template="addPaymentToPurchaseToConciliateBankingWithdraw"/>
                </g:form>
                <hr>
              </g:if>

              <g:if test="${conciliations}">
              <label>Compras seleccionadas:</label>
              <div class="table-responsive">
                <table class="table">
                  <tr>
                    <th class="col-md-4">Compra</th>
                    <th>Total</th>
                    <th>Por pagar</th>
                    <th>Monto a aplicar (MXN)</th>
                    <th>Nuevo Saldo</th>
                    <th>Moneda</th>
                    <th>Tipo Cambio</th>
                    <th></th>
                  </tr>
                  <g:each in="${conciliations}" var="conciliation">
                  <tr>
                    <td>${conciliation.purchaseOrder.id} / ${conciliation.purchaseOrder.providerName}</td>
                    <td class="text-right">${modulusuno.formatPrice(number: conciliation.purchaseOrder.total)}</td>
                    <td class="text-right">${modulusuno.formatPrice(number: conciliation.purchaseOrder.amountToPay)}</td>
                    <td class="text-right">${modulusuno.formatPrice(number: conciliation.amount)}</td>
                    <td class="text-right">${modulusuno.formatPrice(number: conciliation.purchaseOrder.currency == "MXN" ? conciliation.purchaseOrder.amountToPay - conciliation.amount : conciliation.purchaseOrder.amountToPay - (conciliation.amount/conciliation.changeType)) }</td>
                    <td>${conciliation.purchaseOrder.currency}</td>
                    <td>${conciliation.changeType ?: "NA"}</td>
                    <td class="text-center">
                      <g:form action="deleteConciliation" id="${conciliation.id}">
                        <button class="btn btn-danger">Quitar</button>
                      </g:form>
                    </td>
                  </tr>
                  </g:each>
                </table>
              </div>
              <hr>
              </g:if>

              <g:render template="actionsToConciliateBankingTransaction"/>

            </div>
          </div>
        </div>
      </div>
    </div>
    <asset:javascript src="conciliation/chooseInvoice.js"/>
  </body>
</html>
