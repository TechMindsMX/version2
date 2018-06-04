<div class="table-responsive">
  <table class="table">
    <tr>
      <th>Fecha</th>
      <th>Monto</th>
      <th>Cliente</th>
      <th class="text-center">
        <g:link class="btn btn-primary" controller="payment" action="referencedPaymentsConciliated">Ver conciliadas</g:link>
      </th>
    </tr>
    <g:if test="${payments}">
    <g:each in="${payments.list}" var="payment">
    <g:form controller="conciliation" action="chooseInvoiceToConciliate" id="${payment.id}">
    <tr>
      <td><g:formatDate format="dd/MM/yyyy" date="${payment.dateCreated}"/></td>
      <td>${modulusuno.formatPrice(number: payment.amount)}</td>
      <td>${ payments.clients.find { it?.rfc == payment.rfc} ?: "EL CLIENTE CON RFC ${payment.rfc} YA NO FUE ENCONTRADO EN LOS REGISTROS DE RELACIONES COMERCIALES" }</td>
      <td class="text-center">
        <button class="btn btn-primary">Elegir Factura</button>
      </td>
    </tr>
    </g:form>
    </g:each>
    </g:if>

  </table>
</div>

