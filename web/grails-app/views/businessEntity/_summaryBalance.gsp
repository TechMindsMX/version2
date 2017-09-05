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
            <td>${modulusuno.formatPrice(number:clientData.totalSoldForClient)}</td>
            <td>${modulusuno.formatPrice(number:clientData.totalSoldForClientStatusConciliated)}</td>
            <td>${modulusuno.formatPrice(number:clientData.paymentsFromClientToPay)}</td>
            <td>${modulusuno.formatPrice(number:clientData.totalPending)}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>
</g:if>


