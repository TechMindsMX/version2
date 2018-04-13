<%! import com.modulus.uno.paysheet.PaysheetStatus %>
<%! import com.modulus.uno.paysheet.PaymentSchema %>
<%! import com.modulus.uno.paysheet.PaymentWay %>
<%! import com.modulus.uno.paysheet.PaysheetEmployeeStatus %>

<style>
  th,td {
    white-space: nowrap;
    width: 1px;
  }
</style>

<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Empleados</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
    <div class="table-responsive">
      <div class="container-fluid">
        <table class="table table-striped table-condensed">
          <thead>
            <tr>
              <th></th>
              <th class="text-center">No. Empl</th>
              <th class="text-center">Nombre</th>
              <th class="text-center">RFC</th>
              <th class="text-center">CURP</th>
              <th class="text-center">Cód. Banco</th>
              <th class="text-center">Banco</th>
              <th class="text-center">Clabe</th>
              <th class="text-center">Cuenta</th>
              <th class="text-center">Tarjeta</th>
							<th class="text-center">Forma de Pago</th>
              <th class="text-center">SA Bruto</th>
              <th class="text-center">Cuota Social</th>
              <th class="text-center">Subsidio</th>
              <th class="text-center">ISR SA</th>
              <th class="text-center">Incidencias Percep</th>
              <th class="text-center">Incidencias Deduc</th>
              <th class="text-center">SA Neto</th>
              <th class="text-center">IAS Bruto</th>
              <th class="text-center">ISR IAS</th>
              <th class="text-center">IAS Neto</th>
              <th class="text-center">Subtotal</th>
              <th class="text-center">Costo Nominal</th>
              <th class="text-center">Comisión</th>
              <th class="text-center">IVA</th>
              <th class="text-center">Total</th>
              <th class="text-center">Estatus</th>
              <th class="text-center"></th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${paysheet.employees.sort{ it.prePaysheetEmployee.nameEmployee }}" var="employee">
              <tr>
                <td>
                  <g:if test="${![PaysheetEmployeeStatus.IMSS_STAMPED, PaysheetEmployeeStatus.ASSIMILABLE_STAMPED, PaysheetEmployeeStatus.FULL_STAMPED].contains(employee.status)}">
                    <g:link class="btn btn-primary" controller="paysheetEmployee" action="reloadData" id="${employee.id}">
                      <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
                    </g:link>
                  </g:if>
                </td>
                <td>${employee.prePaysheetEmployee.numberEmployee}</td>
                <td>${employee.prePaysheetEmployee.nameEmployee}</td>
                <td>${employee.prePaysheetEmployee.rfc}</td>
                <td>${employee.prePaysheetEmployee.curp}</td>
                <td>${employee.prePaysheetEmployee.bank?.bankingCode}</td>
                <td>${employee.prePaysheetEmployee.bank?.name}</td>
                <td>${employee.prePaysheetEmployee.clabe}</td>
                <td>${employee.prePaysheetEmployee.account}</td>
                <td>${employee.prePaysheetEmployee.cardNumber}</td>
                <td>
									<g:if test="${employee.paymentWay != PaymentWay.ONLY_CASH && employee.status == PaysheetEmployeeStatus.PENDING}">
										<g:link action="changePaymentWayFromEmployee" id="${employee.id}" title="Cambiar forma de pago">
											<g:message code="paysheet.payment.way.${employee.paymentWay}"/>
										</g:link>
									</g:if><g:else>
										<g:message code="paysheet.payment.way.${employee.paymentWay}"/>
									</g:else>
								</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.salaryImss)}</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.socialQuota)}</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.subsidySalary)}</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.incomeTax)}</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.totalIncidencesImssPerceptions)}</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.totalIncidencesImssDeductions)}</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.imssSalaryNet)}</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.crudeAssimilable)}</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.incomeTaxAssimilable)}</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.netAssimilable)}</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.totalSalaryEmployee)}</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.paysheetCost)}</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.commission)}</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.paysheetIva)}</td>
                <td class="text-right">${modulusuno.formatPrice(number:employee.totalToInvoice)}</td>
                <td class="text-center">
                  <g:message code="paysheet.employee.status.${employee.status}"/>
                </td>
                <td class="text-center">
                  <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR">
                    <g:if test="${(employee.paymentWay == PaymentWay.CASH || employee.paymentWay == PaymentWay.ONLY_CASH) && employee.status == PaysheetEmployeeStatus.PENDING && paysheet.status == PaysheetStatus.AUTHORIZED}">
                      <g:link class="btn btn-primary" action="setPayedToEmployee" id="${employee.id}">Pagar</g:link>
                    </g:if>
                    <g:if test="${[PaysheetEmployeeStatus.IMSS_STAMPED, PaysheetEmployeeStatus.ASSIMILABLE_STAMPED, PaysheetEmployeeStatus.FULL_STAMPED].contains(employee.status)}">
                      <g:link class="btn btn-primary" controller="paysheetEmployee" action="showPaysheetReceipts" id="${employee.id}">Recibos</g:link>
                    </g:if>
                  </sec:ifAnyGranted>
                </td>
              </tr>
            </g:each>
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <div class="row">
    <g:if test="${paysheet.status == PaysheetStatus.TO_AUTHORIZE || paysheet.status == PaysheetStatus.AUTHORIZED || paysheet.status == PaysheetStatus.CREATED}">
      <div class="col-md-6">
				<sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR">
        <g:if test="${paysheet.status == PaysheetStatus.AUTHORIZED && !dispersionSummary}">
					<g:link class="btn btn-primary" action="prepareDispersion" id="${paysheet.id}">Dispersar Pagos</g:link>
        </g:if>
        <g:if test="${paysheet.employees.findAll{ [PaysheetEmployeeStatus.PAYED, PaysheetEmployeeStatus.IMSS_PAYED, PaysheetEmployeeStatus.ASSIMILABLE_PAYED, PaysheetEmployeeStatus.ASSIMILABLE_STAMPED].contains(it.status) && it.imssSalaryNet }}">
          <g:link class="btn btn-primary" action="generatePaysheetReceipts" id="${paysheet.id}" params="[schema:'IMSS']">Timbrar SA</g:link>
        </g:if>
        <g:if test="${paysheet.employees.findAll{ [PaysheetEmployeeStatus.PAYED, PaysheetEmployeeStatus.IMSS_PAYED, PaysheetEmployeeStatus.ASSIMILABLE_PAYED, PaysheetEmployeeStatus.IMSS_STAMPED].contains(it.status) && it.netAssimilable }}">
          <g:link class="btn btn-primary" action="generatePaysheetReceipts" id="${paysheet.id}" params="[schema:'Asimilable']">Timbrar IAS</g:link>
        </g:if>
				</sec:ifAnyGranted>
      </div>
      <div class="col-md-6 text-right">
        <g:link class="btn btn-default" action="exportToXlsCash" id="${paysheet.id}">XLS EFECTIVO/CHEQUE</g:link>
        <g:link class="btn btn-default" action="exportToXlsImss" id="${paysheet.id}">XLS IMSS</g:link>
        <g:link class="btn btn-default" action="exportToXlsAssimilable" id="${paysheet.id}">XLS Asimilables</g:link>
        <g:link class="btn btn-default" action="exportToXls" id="${paysheet.id}">XLS</g:link>
      </div>
    </g:if>
  </div>

</div>

