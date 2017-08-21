  <div class="portlet portlet-blue">
    <div class="portlet-heading">Saldo Bancario</div>
    <div class="portlet-body">
      <div class="container-fluid">
      <g:if test="${mainAccount}">
        <div class="col-md-4">
          <b>${mainAccount.banco}</b>
        </div>
        <div class="col-md-4 text-center">
          <p><b>Cuenta: ${mainAccount.accountNumber}</b></p>
          <p><b>Clabe: ${mainAccount.clabe}</b></p>
        </div>
        <div class="col-md-4 text-right">
          <b><modulusuno:amountAccountToday id="${mainAccount.id}"/></b>
        </div>
      </g:if>
      <g:else>
        <div class="alert alert-warning text-center">
          No hay cuenta bancaria concentradora
        </div>
      </g:else>
      </div>
    </div>
  </div>
