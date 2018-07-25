<%! import com.modulus.uno.PaymentStatus %>
<div class="table-responsive">
  <table class="table">
    <tr>
      <th>Fecha</th>
      <th>Monto</th>
      <th>Cliente</th>
      <g:if test="${conciliated}">
        <th class="text-center">
          <g:link class="btn btn-primary" controller="payment" action="referencedPayments">Ver por conciliar</g:link>
        </th>
      </g:if>
      <g:else>
        <th class="text-center">
          <g:link class="btn btn-primary" controller="payment" action="referencedPaymentsConciliated">Ver conciliadas</g:link>
        </th>
      </g:else>
    </tr>
    <g:if test="${payments}">
    <g:each in="${payments.list}" var="payment">
    <g:form controller="conciliation" action="chooseInvoiceToConciliate" id="${payment.id}">
    <tr>
      <td>
        <g:if test="${conciliated}">
          <g:link controller="conciliation" action="showDetailPaymentConciliated" id="${payment.id}"><g:formatDate format="dd/MM/yyyy" date="${payment.dateCreated}" /></g:link>
        </g:if><g:else>
          <g:formatDate format="dd/MM/yyyy" date="${payment.dateCreated}"/>
        </g:else>
      </td>
      <td>${modulusuno.formatPrice(number: payment.amount)}</td>
      <td>${ payments.clients.find { it?.rfc == payment.rfc} ?: "EL CLIENTE CON RFC ${payment.rfc} YA NO FUE ENCONTRADO EN LOS REGISTROS DE RELACIONES COMERCIALES" } </td>
      <td class="text-center">
        <g:if test="${!conciliated}">
          <button class="btn btn-primary">Elegir Factura</button>
        </g:if>
      </td>
    </tr>
    </g:form>
    </g:each>
    </g:if>

  </table>
</div>

