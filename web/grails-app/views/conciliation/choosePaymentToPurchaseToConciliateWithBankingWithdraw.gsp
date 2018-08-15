<%! import com.modulus.uno.status.ConciliationStatus %>
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
              <label>Pagos de Compras seleccionadas:</label>
              <div class="table-responsive">
                <table class="table">
                  <tr>
                    <th class="col-md-4">Pago</th>
                    <th>Monto del Pago</th>
                    <th><g:if test="${bankingTransaction.conciliationStatus == ConciliationStatus.TO_APPLY}"> Monto a aplicar (MXN)</g:if></th>
                    <th>Nuevo Saldo</th>
                    <th></th>
                  </tr>
                  <g:each in="${conciliations}" var="conciliation">
                  <tr>
                    <td>${conciliation.paymentToPurchase.id}</td>
                    <td class="text-right">${modulusuno.formatPrice(number: conciliation.paymentToPurchase.amount)}</td>
                    <td class="text-right"><g:if test="${bankingTransaction.conciliationStatus == ConciliationStatus.TO_APPLY}">${modulusuno.formatPrice(number: conciliation.amount)}</g:if></td>
                    <td class="text-right">${modulusuno.formatPrice(number: (conciliation.paymentToPurchase.amount - conciliation.amount)) }</td>
                    <td class="text-center">
                      <g:if test="${bankingTransaction.conciliationStatus == ConciliationStatus.TO_APPLY}"> 
                        <g:form action="deleteConciliation" id="${conciliation.id}">
                          <button class="btn btn-danger">Quitar</button>
                        </g:form>
                      </g:if>
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
  </body>
</html>
