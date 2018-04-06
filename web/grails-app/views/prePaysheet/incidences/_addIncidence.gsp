<%! import com.modulus.uno.paysheet.IncidenceType %>
<%! import com.modulus.uno.paysheet.PaymentSchema %>
<%! import com.modulus.uno.paysheet.PerceptionType %>
<%! import com.modulus.uno.paysheet.DeductionType %>
<%! import com.modulus.uno.paysheet.ExtraHourType %>
<div class="row">
  <div class="col-md-12">
    <div class="table-responsive">
      <table class="table table-condensed table-striped">
        <thead>
          <tr>
            <th class="col-xs-2">Tipo</th>
            <th class="col-xs-3">Incidencia</th>
            <th class="col-xs-2">Esquema</th>
            <th class="col-xs-2">Monto Exento</th>
            <th class="col-xs-2">Monto Gravado</th>
            <th class="col-xs-1">&nbsp;</th>
          </tr>
        </thead>
        <tbody>
          <g:hiddenField name="prePaysheetEmployeeId" value="${prePaysheetEmployee.id}"/>
          <tr>
            <td>
              <div class="input-group">
                <g:select id="incidenceType" name="type" from="${IncidenceType.values()}" class="form-control" noSelection="['':'Elija el tipo de incidencia']" required="" />
              </div>
            </td>
            <td>
              <div class="input-group">
                <g:hiddenField name="incidence" value="" required=""/>
                <g:select id="perceptions" name="perceptions" from="${PerceptionType.values()}" class="form-control" noSelection="['':'Elija la incidencia']"/>
                <g:select id="deductions" name="deductions" from="${DeductionType.values()}" class="form-control" noSelection="['':'Elija la incidencia']"/>
              </div>
            </td>
            <td>
              <div class="input-group">
                <g:select name="schema" from="${PaymentSchema.values()}" class="form-control" noSelection="['':'Elija el esquema de pago']" required="" />
              </div>
            </td>
            <td>
              <div id="exemptAmount" class="input-group">
                <div class="input-group-addon">$</div>
                <input type="text" name="exemptAmount" class="form-control" required="" value="0" pattern="[0-9]+(\.[0-9]{1,2})?" title="Ingrese una cantidad en formato correcto (número sin decimales o hasta con 2 decimales)"/>
              </div>
            </td>
            <td>
              <div id="taxedAmount" class="input-group">
                <div class="input-group-addon">$</div>
                <input type="text" name="taxedAmount" class="form-control" required="" value="0" pattern="[0-9]+(\.[0-9]{1,2})?" title="Ingrese una cantidad en formato correcto (número sin decimales o hasta con 2 decimales)"/>
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
      <div id="extraHours">
        <div class="row">
          <div class="col-md-4">
            <div class="input-group">
              <label><strong>Días</strong></label><br/>
              <input id="extraHoursDays" name="extraHoursDays" class="form-control" type="number" min="1" step="1"/>
            </div>
          </div>
          <div class="col-md-4">
            <div class="input-group">
              <label><strong>Tipo</strong></label><br/>
              <g:select id="extraHoursType" name="extraHoursType" from="${ExtraHourType.values()}" class="form-control" noSelection="['':'Elija el tipo de horas extra']" />
            </div>
          </div>
          <div class="col-md-4">
            <div class="input-group">
              <label><strong>Horas</strong></label><br/>
              <input id="extraHoursQuantity" name="extraHoursQuantity" class="form-control" type="number" min="1" step="1"/>
            </div>
          </div>
      </div>
    </div>
  </div>
</div>
