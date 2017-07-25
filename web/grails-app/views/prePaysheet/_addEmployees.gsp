<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Empleados disponibles para agregar</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
    <g:hiddenField id="entities" name="entities" value=""/>
    <g:hiddenField name="prePaysheet.id" value="${prePaysheet.id}"/>
    <div class="table-responsive">
      <table class="table">
        <tr>
          <th width="5%"><g:checkBox id="selectAll" name="selectAll" title="Seleccionar Todos"/></th>
          <th width="10%">No.Empl</th>
          <th width="45%">Nombre</th>
          <th width="13%">RFC</th>
          <th width="14%">CURP</th>
          <th width="13%">NSS</th>
        </tr>

        <g:each in="${employeesAvailableToAdd}" var="employee" status="index">
        <tr>
          <td><g:checkBox class="entity" id="checkBe" name="checkBe" value="${employee.id}" checked="false"/></td>
          <td>${employee.number}</td>
          <td>${employee}</td>
          <td>${employee.rfc}</td>
          <td>${employee.curp}</td>
          <td>${dataImssEmployees[index]?.nss}</td>
        </tr>
        <tr>
          <td colspan="5">
            <table class="table">
              <tr>
                <td width="25%">
                    <g:select class="form-control" id="bankAccount${employee.id}" name="bankAccount${employee.id}" from="${employee.banksAccounts}" noSelection="['':'Sin datos bancarios']" optionKey="id"/>
                </td>
                <td width="35%">
                  <input type="text" id="netPayment${employee.id}" name="netPayment${employee.id}" class="form-control" pattern="[0-9]+(\.[0-9]{1,2})?" title="Ingrese una cantidad en formato correcto (número sin decimales o hasta 2 decimales)" value="${netPaymentEmployees[index]}" placeholder="Total a Pagar"/>
                </td>
                <td width="40%">
                  <g:textField class="form-control" id="note${employee.id}" name="note${employee.id}" value="" placeholder="Observaciones"/>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        </g:each>
      </table>
    </div>
  </div>

  <div class="portlet-footer">
    <button class="btn btn-primary text-right" id="add" type="button">Agregar</button>
  </div>
</div>

