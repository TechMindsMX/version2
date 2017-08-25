<%! import com.modulus.uno.paysheet.PaysheetStatus %>
<%! import com.modulus.uno.paysheet.PaymentSchema %>
<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Empleados</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
    <div class="table-responsive">
      <table class="table">
        <tr>
          <th width="40%">Empleado/Datos Bancarios</th>
          <th width="40%">Detalle del Pago</th>
          <th width="20%">Observaciones</th>
        </tr>

        <g:each in="${paysheet.employees.sort{ it.prePaysheetEmployee.nameEmployee }}" var="employee">
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
                <td>${employee.prePaysheetEmployee.numberEmployee}</td>
                <td>${employee.prePaysheetEmployee.nameEmployee}</td>
                <td>${employee.prePaysheetEmployee.rfc}</td>
                <td>${employee.prePaysheetEmployee.curp}</td>
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
                <td>${employee.prePaysheetEmployee.bank?.bankingCode}</td>
                <td>${employee.prePaysheetEmployee.bank?.name}</td>
                <td>${employee.prePaysheetEmployee.clabe}</td>
                <td>${employee.prePaysheetEmployee.account}</td>
                <td>${employee.prePaysheetEmployee.cardNumber}</td>
              </tr>
            </table>
            </div>
          </td>
          <td>
            <div class="table-responsive">
            <table class="table">
              <tr>
                <td><strong>IMSS</strong></td>
                <td><strong>Asimilable</strong></td>
                <td><strong>Subtotal</strong></td>
                <td><strong>Costo Nominal</strong></td>
              </tr>
              <tr>
                <td>${modulusuno.formatPrice(number:employee.imssSalaryNet)}</td>
                <td>${modulusuno.formatPrice(number:employee.salaryAssimilable)}</td>
                <td>${modulusuno.formatPrice(number:employee.totalSalaryEmployee)}</td>
                <td>${modulusuno.formatPrice(number:employee.paysheetCost)}</td>
              </tr>
            </table>
            <table class="table">
              <tr>
                <td><strong>Comisión</strong></td>
                <td><strong>IVA</strong></td>
                <td><strong>Total</strong></td>
              </tr>
              <tr>
                <td>${modulusuno.formatPrice(number:employee.commission)}</td>
                <td>${modulusuno.formatPrice(number:employee.paysheetIva)}</td>
                <td>${modulusuno.formatPrice(number:employee.totalToInvoice)}</td>
              </tr>
            </table>
            </div>
          </td>
          <td>${employee.prePaysheetEmployee.note}</td>
        </tr>
        </g:each>
      </table>
    </div>
  </div>

  <div class="row">
    <g:if test="${paysheet.status == PaysheetStatus.TO_AUTHORIZE || paysheet.status == PaysheetStatus.AUTHORIZED}">
      <div class="col-md-4">
        <g:if test="${paysheet.status == PaysheetStatus.AUTHORIZED}">
          <a data-toggle="collapse" role="button" href="#paymentDispersion" class="btn btn-primary" aria-expanded="false" aria-controls="paymentDispersion">Dispersar Pagos</a>

        </g:if>      
      </div>
      <div class="col-md-8 text-right">
        <g:link class="btn btn-default" action="exportToXlsImss" id="${paysheet.id}">XLS IMSS</g:link>
        <g:link class="btn btn-default" action="exportToXlsAssimilable" id="${paysheet.id}">XLS Asimilables</g:link>
        <g:link class="btn btn-default" action="exportToXls" id="${paysheet.id}">XLS</g:link>
      </div>
    </g:if>
  </div>
  <div class="row">&nbsp;</div>
  <div class="row">
    <div class="col-md-12">
      <div class="collapse" id="paymentDispersion">
        <div class="well">
          <g:form action="generatePaymentDispersion" id="${paysheet.id}">
          <div class="row">
            <div class="col-md-4">
              <div class="form-group">
                <label>Cuenta Origen de Dispersión:</label>
                <g:select class="form-control" name="chargeBankAccountId" from="${chargeBanksAccounts}" required="" optionKey="id"/>
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label>Esquema de Pago</label>
                <g:select class="form-control" name="paymentSchema" from="${PaymentSchema.values()}"/>
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label>Modo de Transferencia</label>
                <g:select class="form-control" name="dispersionWay" from="${[[key:'SameBank', value:'MISMO BANCO'], [key:'InterBank', value:'INTERBANCARIO']]}" optionKey="key" optionValue="value"/>
              </div>
            </div>            
          </div>
          <div class="row">
            <div class="col-md-12 text-right">
              <div class="form-group"> 
                <button type="submit" class="btn btn-default">Generar Dispersión</button>
              </div>
            </div>
          </div>
          </g:form>
        </div>
      </div>
    </div>
  </div>