<%! import com.modulus.uno.PaymentStatus %>
<div class="row">
  <div class="col-md-4 text-center">
    <strong>Fecha del Pago</strong><br>
    <g:formatDate format="dd-MM-yyyy" date="${payment.dateCreated}"/>
  </div>
  <div class="col-md-4 text-center">
    <strong>Total</strong>
    <br/>${modulusuno.formatPrice(number: payment.amount)}
  </div>
  <g:if test="${payment.status == PaymentStatus.PENDING}">
    <g:if test="${toApply == 0}">
      <div class="col-md-4 text-center alert alert-success">
    </g:if>
    <g:else>
      <div class="col-md-4 text-center alert alert-warning">
    </g:else>
      <strong>Por aplicar</strong><br/>
      ${modulusuno.formatPrice(number: toApply)}
    </div>
  </g:if>
</div>

