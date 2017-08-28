<%! import com.modulus.uno.paysheet.IncidenceType %>
<%! import com.modulus.uno.paysheet.PaymentSchema %>
<div class="row">
  <div class="col-md-12">
    <div class="table-responsive">
      <table class="table table-condensed">
        <thead>
          <tr>
            <th class="col-xs-5">Descripción</th>
            <th class="col-xs-2">Tipo</th>
            <th class="col-xs-2">Esquema</th>
            <th class="col-xs-2">Monto</th>
            <th class="col-xs-1">&nbsp;</th>
          </tr>
        </thead>
        <tbody>
          <g:hiddenField name="prePaysheetEmployeeId" value="${prePaysheetEmployee.id}"/>
          <tr>
            <td>
              <div class="input-group">
                <input type="text" name="description" class="form-control" required="" maxlength="255" size="50" placeholder="Descripción de la incidencia"/>
              </div>
            </td>
            <td>
              <div class="input-group">
                <g:select name="type" from="${IncidenceType.values()}" class="form-control" noSelection="['':'Elija el tipo de incidencia']" required="" />
              </div>
            </td>
            <td>
              <div class="input-group">
                <g:select name="schema" from="${PaymentSchema.values()}" class="form-control" noSelection="['':'Elija el esquema de pago']" required="" />
              </div>
            </td>
            <td>
              <div class="input-group">
                <div class="input-group-addon">$</div>
                <input type="text" name="amount" class="form-control" required="" pattern="[0-9]+(\.[0-9]{1,2})?" title="Ingrese una cantidad en formato correcto (número sin decimales o hasta con 2 decimales)"/>
              </div>
            </td>
            <td>
              <div class="input-group">
                <button type="submit" class="btn btn-primary">
                  <i class="fa fa-plus"></i> Agregar
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>
