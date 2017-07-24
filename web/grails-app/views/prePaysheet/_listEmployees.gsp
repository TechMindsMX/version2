<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Empleados agregados en la pre-nómina</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
    <div class="table-responsive">
      <table class="table">
        <tr>
          <th>No. Empl</th>
          <th>Nombre</th>
          <th>RFC</th>
          <th>CURP</th>
          <th>Código Banco</th>
          <th>Banco</th>
          <th>Clabe</th>
          <th>Cuenta</th>
          <th>Tarjeta</th>
          <th>Total a pagar</th>
          <th>Observaciones</th>
        </tr>

        <g:each in="${prePaysheetEmployeeList}" var="employee">
        <tr>
          <td>${employee.numberEmployee}</td>
          <td>${employee.nameEmployee}</td>
          <td>${employee.rfc}</td>
          <td>${employee.curp}</td>
          <td>${employee.bank?.bankingCode}</td>
          <td>${employee.bank?.name}</td>
          <td>${employee.clabe}</td>
          <td>${employee.account}</td>
          <td>${employee.cardNumber}</td>
          <td>${employee.netPayment}</td>
          <td>${employee.note}</td>
        </tr>
        </g:each>
      </table>
    </div>
  </div>
</div>

