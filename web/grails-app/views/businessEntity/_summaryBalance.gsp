<g:if test="${relation.equals('CLIENTE_PROVEEDOR') || relation.equals('CLIENTE')}">
<div class="panel panel-default">
  <div class="panel-heading">
    <div class="row">
      <div class="col-md-12">
        <div class="col-md-6">
          <span class="property-label"><h4>Pagos del cliente</h4></span>
        </div>
        <div class="col-md-6" align="right">
        </div>
      </div>
    </div>
  </div>

  <div class="panel-body">
    <div class="table-responsive">
      <table class="table table-bordered">
        <thead>
          <tr>
            <th>Facturación total(Ejecutadas y autorizadas)</th>
            <th>Total Cobrado (ya conciliado)</th>
            <th>Total Cobrado (por conciliar)</th>
            <th>Pendiente de cobro</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td class="text-right">
              <g:link controller="saleOrder" action="listTotalAmountExecutedAndAuthorized" id="${businessEntity.id}">${modulusuno.formatPrice(number:clientData.totalSoldForClient)}</g:link>
            </td>
            <td class="text-right">
              <g:link controller="saleOrder" action="listOrdersAlreadyConciliate" id="${businessEntity.id}">${modulusuno.formatPrice(number:clientData.totalSoldForClientStatusConciliated)}</g:link>
            </td>
            <td class="text-right">
              <g:link controller="payment" action="referencedPaymentsByRfc" id="${businessEntity.id}" params="[rfc: businessEntity.rfc]">${modulusuno.formatPrice(number:clientData.paymentsFromClientToPay)}</g:link>
            </td>
            <td class="text-right">
              <g:link controller="saleOrder" action="listOrdersWithAmountToPayForClient" id="${businessEntity.id}">${modulusuno.formatPrice(number:clientData.totalPending)}</g:link>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>
</g:if>


