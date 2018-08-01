<div class="table-responsive">
  <table class="table">
    <tr>
      <th>Fecha</th>
      <th>Cuenta Bancaria</th>
      <th>Concepto</th>
      <th>Referencia</th>
      <th>Monto</th>
      <g:if test="${conciliated}">
        <th class="text-center">
          <g:link class="btn btn-primary" controller="payment" action="conciliateBankingDeposits">Ver por conciliar</g:link>
        </th>
      </g:if>
      <g:else>
        <th class="text-center">
          <g:link class="btn btn-primary" controller="payment" action="conciliateBankingDepositsConciliated">Ver conciliadas</g:link>
        </th>
      </g:else>
    </tr>
    <g:each in="${bankingDeposits}" var="transaction">
    <tr>
      <td>
        <g:if test="${conciliated}">
          <g:link controller="conciliation" action="showDetailBankingDepositConciliated" id="${transaction.id}">
            <g:formatDate date="${transaction.dateEvent}" format="dd-MM-yyyy"/>
          </g:link>
        </g:if><g:else>
          <g:formatDate date="${transaction.dateEvent}" format="dd-MM-yyyy"/>
      </g:else>
      </td>
      <td>${transaction.cuenta}</td>
      <td>${transaction.concept}</td>
      <td>${transaction.reference}</td>
      <td>${modulusuno.formatPrice(number: transaction.amount)}</td>
      <g:if test="${conciliated}">
      </g:if>
      <g:else>
        <td class="text-center">
          <g:link class="btn btn-primary" controller="conciliation" action="chooseInvoiceToConciliateWithBankingDeposit" id="${transaction.id}">Elegir Factura</g:link>
        </td>
      </g:else>
    </tr>
    </g:each>

  </table>
</div>

