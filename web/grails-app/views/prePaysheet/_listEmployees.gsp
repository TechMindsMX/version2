<div class="portlet portlet-default">
  <div class="row">
    <div class="col-md-2 col-md-offset-10 text-right">
      <g:if test="${prePaysheet.status == com.modulus.uno.paysheet.PrePaysheetStatus.CREATED}">
      <g:link class="btn btn-primary" action="addEmployees" id="${prePaysheet.id}">Agregar</g:link>
      </g:if>
    </div>
  </div>

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
          <th width="60%">Empleado/Datos Bancarios</th>
          <th width="10%">Monto</th>
          <th width="30%">Observaciones</th>
        </tr>

        <g:each in="${prePaysheet.employees.sort{ it.nameEmployee }}" var="employee">
        <tr>
          <td>
            <div class="table-responsive">
            <table class="table">
              <tr>
                <td><strong>No. Empl</strong></td>
                <td><strong>Nombre</strong></td>
                <td><strong>RFC</strong></td>
                <td><strong>CURP</strong></td>
              </tr>
              <tr>
                <td>${employee.numberEmployee}</td>
                <td>${employee.nameEmployee}</td>
                <td>${employee.rfc}</td>
                <td>${employee.curp}</td>
              </tr>
            </table>
            <table class="table">
              <tr>
                <td><strong>Código Banco</strong></td>
                <td><strong>Banco</strong></td>
                <td><strong>Clabe</strong></td>
                <td><strong>Cuenta</strong></td>
                <td><strong>Tarjeta</strong></td>
              </tr>
              <tr>
                <td>${employee.bank?.bankingCode}</td>
                <td>${employee.bank?.name}</td>
                <td>${employee.clabe}</td>
                <td>${employee.account}</td>
                <td>${employee.cardNumber}</td>
              </tr>
            </table>
            </div>
          </td>
          <td>${modulusuno.formatPrice(number:employee.netPayment)}</td>
          <td>${employee.note}</td>
        </tr>
        </g:each>
      </table>
    </div>
  </div>

  <div class="row">
    <g:if test="${prePaysheet.status == com.modulus.uno.paysheet.PrePaysheetStatus.CREATED && prePaysheet.employees}">
    <div class="col-md-12 text-right">
      <g:link class="btn btn-primary" action="sendToProcess" id="${prePaysheet.id}">Enviar a Procesar</g:link>
    </div>
    </g:if>
    <g:if test="${prePaysheet.status == com.modulus.uno.paysheet.PrePaysheetStatus.IN_PROCESS}">
    <div class="col-md-12 text-right">
      <g:link class="btn btn-default" action="exportToXls" id="${prePaysheet.id}">XLS</g:link>
      <g:link class="btn btn-default" controller="paysheet" action="createFromPrePaysheet" id="${prePaysheet.id}">Procesar</g:link>
    </div>
    </g:if>
  </div>

</div>

