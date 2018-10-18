<%! import com.modulus.uno.ConciliationStatus %>
<div class="row">
  <div class="col-md-3 text-center">
    <strong>Fecha del Movimiento</strong><br>
    <g:formatDate format="dd-MM-yyyy" date="${bankingTransaction.dateEvent}"/>
  </div>
  <div class="col-md-3 text-center">
    <strong>Cuenta bancaria</strong><br>
    ${bankingTransaction.cuenta}
  </div>
  <div class="col-md-3 text-center">
    <strong>Total</strong>
    <br/>${modulusuno.formatPrice(number: bankingTransaction.amount)}
  </div>
  <g:if test="${bankingTransaction.conciliationStatus == ConciliationStatus.TO_APPLY}">
    <g:if test="${toApply == 0}">
      <div class="col-md-3 text-center alert alert-success">
    </g:if>
    <g:else>
      <div class="col-md-3 text-center alert alert-warning">
    </g:else>
      <strong>Por aplicar</strong><br/>
      ${modulusuno.formatPrice(number: toApply)}
    </div>
  </g:if>
</div>

