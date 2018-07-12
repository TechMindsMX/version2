<div class="table-responsive">
  <table class="table">
    <tr>
      <th>Fecha</th>
      <th>Monto</th> 
      <g:if test="${conciliated}">
        <th class="text-right">
          <div class="col-md-3 text-center"></div>
          <div class="col-md-3 text-center">
            <g:link class="btn btn-primary" controller="payment" action="notReferencedPayments">Ver todas</g:link>
          </div>
        </th>
      </g:if>
      <g:else>
        <th class="text-right">
          <div class="col-md-3 text-center"></div>
          <div class="col-md-3 text-center">
            <g:link class="btn btn-primary" controller="payment" action="notReferencedPaymentsConciliated">Ver conciliadas</g:link>
          </div>
        </th>
      </g:else>
    </tr>
    <g:if test="${payments}">
    <g:each in="${payments.list}" var="payment">
    <tr>
      <td><g:formatDate format="dd/MM/yyyy" date="${payment.dateCreated}"/></td>
      <td>${modulusuno.formatPrice(number: payment.amount)}</td>
      <td class="text-center">
        <div class="col-md-3 text-center">
        <g:link class="btn btn-primary" controller="conciliation" action="chooseInvoiceToConciliate" id="${payment.id}">
          Elegir Factura
        </g:link>
        </div>
        <div class="col-md-3 text-center">
          <g:link class="btn btn-primary" controller="conciliation" action="conciliationWithoutInvoice" id="${payment.id}">
            Sin Factura
          </g:link>
        </div>
      </td>
    </tr>
    </g:each>
    </g:if>

  </table>
</div>

