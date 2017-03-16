<div class="table-responsive">
  <table class="table">
    <tr>
      <th>Fecha del Pago</th>
      <th>Monto</th>
      <th>Cliente</th>
      <th></th>
    </tr>
    <g:if test="${payments}">
    <g:each in="${payments.list}" var="payment">
    <g:form action="chooseInvoiceToConciliate" id="${payment.id}">
    <tr>
      <td><g:formatDate format="dd/MM/yyyy" date="${payment.dateCreated}"/></td>
      <td>${modulusuno.formatPrice(number: payment.amount)}</td>
      <td>${payments.clients.find { it.rfc == payment.rfc} }</td>
      <td class="text-center">
        <button class="btn btn-primary">Elegir Factura</button>
      </td>
    </tr>
    </g:form>
    </g:each>
    </g:if>

  </table>
</div>

