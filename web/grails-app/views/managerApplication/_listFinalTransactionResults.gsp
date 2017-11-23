<div class="portlet portlet-default">
   <div class="portlet-body">
        <div class="row">
          <div class="col-md-12">
            <div class="table-responsive">
              <table class="table table-striped table-condensed">
                <tr>
                  <th>Empresa</th>
                  <th>Fecha de Transacción</th>
                  <th>Fecha de Ejecución</th>
                  <th>Estatus</th>
                  <th>Modo de Ejecución</th>
                </tr>
                <g:each in="${results.sort{it.company.bussinessName}}" var="result">
                  <tr>
                    <td>${result.company}</td>
                    <td class="text-center"><g:formatDate format="dd-MM-yyyy" date="${result.transactionDate}"/></td>
                    <td class="text-center"><g:formatDate format="dd-MM-yyyy" date="${result.dateCreated}"/></td>
                    <td class="text-center"><g:message code="finalTransaction.status.${result.status}"/></td>
                    <td class="text-center"><g:message code="finalTransaction.execution.${result.executionMode}"/></td>
                  </tr>
                </g:each>
              </table>
            </div>
          </div>
        </div>
  </div>
</div>
