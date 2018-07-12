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
          <g:link class="btn btn-primary" controller="payment" action="conciliateBankingWithdraws">Ver todas</g:link>
        </th>
      </g:if>
      <g:else>
        <th class="text-center">
          <g:link class="btn btn-primary" controller="payment" action="conciliateBankingWithdrawsConciliated">Ver conciliadas</g:link>
        </th>
      </g:else>
    </tr>
    <g:each in="${bankingWithdraws}" var="transaction">
    <tr>
      <td><g:formatDate date="${transaction.dateEvent}" format="dd-MM-yyyy"/></td>
      <td>${transaction.cuenta}</td>
      <td>${transaction.concept}</td>
      <td>${transaction.reference}</td>
      <td>${modulusuno.formatPrice(number: transaction.amount)}</td>
      <td class="text-center">
        <g:link class="btn btn-primary" controller="conciliation" action="choosePaymentToPurchaseToConciliateWithBankingWithdraw" id="${transaction.id}">Elegir Compra</g:link>
      </td>
    </tr>
    </g:each>

  </table>
</div>

