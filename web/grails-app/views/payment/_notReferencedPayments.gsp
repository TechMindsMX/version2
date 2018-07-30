<div class="table-responsive">
  <table class="table table-condensed table-striped">
    <thead>
    <tr>
      <th>Fecha</th>
      <th>Monto</th> 
      <th class="text-right">
        <div class="col-md-3 text-center"></div>
        <div class="col-md-3 text-center">
          <g:if test="${conciliated}">
            <g:link class="btn btn-primary" controller="payment" action="notReferencedPayments">Ver por conciliar</g:link>
          </g:if><g:else>
            <g:link class="btn btn-primary" controller="payment" action="notReferencedPaymentsConciliated">Ver conciliadas</g:link>
          </g:else>
        </div>
      </th>
    </tr>
    </thead>
    <g:if test="${payments}">
    <tbody>
    <g:each in="${payments.list}" var="payment">
    <tr> 
      <td>
        <g:if test="${conciliated}">
        <g:link controller="conciliation" action="showDetailPaymentConciliated" id="${payment.id}">
          <g:formatDate format="dd/MM/yyyy" date="${payment.dateCreated}"/>
        </g:link>
      </g:if><g:else>
        <g:formatDate format="dd/MM/yyyy" date="${payment.dateCreated}"/>
      </g:else>  
      <td>${modulusuno.formatPrice(number: payment.amount)}</td>
        <td class="text-center">
          <g:if test="${!conciliated}">
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
          </g:if>
        </td>
      
    </tr>
    </g:each>
    </tbody>
    </g:if>

  </table>
</div>

