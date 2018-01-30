<%! import com.modulus.uno.paysheet.PrePaysheetStatus %>
<div class="table-responsive">
  <table class="table table-condensed">
    <tbody>
    <g:each in="${prePaysheetEmployee.incidences.sort{it.id}}" var="incidence">
    <tr>
      <td class="col-xs-5">${incidence.description}</td>
      <td class="col-xs-2">${incidence.type}</td>
      <td class="col-xs-2">${incidence.paymentSchema}</td>
      <td class="col-xs-2">${modulusuno.formatPrice(number:incidence.amount)}</td>
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
