<%! import com.modulus.uno.paysheet.PrePaysheetStatus %>

<style>
	th {
	  text-align:center;
	}
	
  th, td {
    white-space: nowrap;
    width: 1px;
  }

	.fixwidth {
	  width: 300px;
	}
</style>

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
      <table class="table table-striped table-condensed">
        <tr>
          <th>No. Empl.</th>
          <th>Nombre</th>
          <th>RFC</th>
					<th>CURP</th>
					<th>NSS</th>
					<th>Cód. Banco</th>
					<th>Banco</th>
					<th>Clabe</th>
					<th>Cuenta</th>
					<th>Tarjeta</th>
					<th>Neto a Pagar</th>
					<th>Observaciones</th>
          <th></th>
        </tr>

        <g:each in="${prePaysheet.employees.sort{ it.nameEmployee }}" var="employee">
        <tr>
					<td>${employee.numberEmployee}</td>
        	<td>${employee.nameEmployee}</td>
        	<td>${employee.rfc}</td>
        	<td>${employee.curp}</td>
        	<td></td>					
        	<td>${employee.bank?.bankingCode}</td>
        	<td>${employee.bank?.name}</td>
        	<td>${employee.clabe}</td>
        	<td>${employee.account}</td>
        	<td>${employee.cardNumber}</td>
          <td>${modulusuno.formatPrice(number:employee.netPayment)}</td>
          <td>${employee.note}</td>
          <td>
            <g:if test="${prePaysheet.status == PrePaysheetStatus.CREATED}">
              <g:link action="deleteEmployee" id="${employee.id}" class="btn btn-danger">
                <i class="fa fa-minus"></i> Quitar
              </g:link>
            </g:if>
            <g:link action="incidencesFromEmployee" id="${employee.id}" class="btn btn-default">
              <i class="fa fa-tasks"></i> Incidencias
            </g:link>            
          </td>
        </tr>
        </g:each>
      </table>
    </div>
  </div>

  <div class="row">
    <g:if test="${prePaysheet.status == PrePaysheetStatus.CREATED && prePaysheet.employees}">
    <div class="col-md-12 text-right">
      <g:link class="btn btn-primary" action="sendToProcess" id="${prePaysheet.id}">Enviar a Procesar</g:link>
    </div>
    </g:if>
    <g:if test="${prePaysheet.status == PrePaysheetStatus.IN_PROCESS}">
    <div class="col-md-12 text-right">
      <g:link class="btn btn-default" action="exportToXls" id="${prePaysheet.id}">XLS</g:link>
			<sec:ifAnyGranted roles="ROLE_AUTHORIZER_PAYSHEET">
      <g:link class="btn btn-default" controller="paysheet" action="createFromPrePaysheet" id="${prePaysheet.id}">Procesar</g:link>
			</sec:ifAnyGranted>
    </div>
    </g:if>
  </div>

</div>
