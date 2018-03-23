<%! import com.modulus.uno.paysheet.PrePaysheetStatus %>
<%! import com.modulus.uno.paysheet.ExtraHourType %>
<div class="table-responsive">
  <table class="table table-condensed table-striped">
    <tbody>
    <g:each in="${prePaysheetEmployee.incidences.sort{it.id}}" var="incidence">
    <tr>
      <td class="col-xs-2 text-center">${incidence.type}</td>
      <td class="col-xs-3">${incidence.description}</td>
      <td class="col-xs-2 text-center">${incidence.paymentSchema}</td>
      <td class="col-xs-2 text-right">${modulusuno.formatPrice(number:incidence.exemptAmount)}</td>
      <td class="col-xs-2 text-right">${modulusuno.formatPrice(number:incidence.taxedAmount)}</td>
      <td class="col-xs-1 text-center">
        <g:if test="${prePaysheetEmployee.prePaysheet.status == PrePaysheetStatus.CREATED || prePaysheetEmployee.prePaysheet.status == PrePaysheetStatus.REJECTED}">
          <g:link action="deleteIncidence" id="${incidence.id}" class="btn btn-danger">
            <i class="fa fa-minus"></i> Quitar
          </g:link>
        </g:if>
      </td>
    </tr>
    <g:if test="${incidence.extraHourIncidence}">
      <tr>
        <td class="col-xs-2 text-center"></td>
        <td class="col-xs-3"><label>Días:</label> ${incidence.extraHourIncidence.days}</td>
        <td class="col-xs-2 text-center"><label>Tipo:</label> ${ExtraHourType.values().find { it.key == incidence.extraHourIncidence.type }.toString()}</td>
        <td class="col-xs-2 text-right"><label>Horas:</label> ${incidence.extraHourIncidence.quantity}</td>
        <td class="col-xs-2 text-right"><label>Importe:</label> ${modulusuno.formatPrice(number:incidence.extraHourIncidence.amount)}</td>
        <td class="col-xs-1 text-center"></td>
      </tr
    </g:if>
    </g:each>
    </tbody>
  </table>
</div>
