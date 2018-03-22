<%! import com.modulus.uno.paysheet.PrePaysheetStatus %>
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
    </g:each>
    </tbody>
  </table>
</div>
