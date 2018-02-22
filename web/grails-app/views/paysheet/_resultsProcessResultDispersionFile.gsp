<g:if test="${!processResults}">
  <div class="alert alert-warning" role="alert">No se pudo procesar el archivo</div>
</g:if><g:else>
  <div class="table-responsive">
    <table class="table table-striped table-condensed">
      <thead>
      <tr>
        <th class="text-center">No.</th>
        <th class="text-center">Cuenta</th>
        <th class="text-center">Empleado</th>
        <th class="text-center">Esquema</th>
        <th class="text-center">Estatus</th>
        <th class="text-center">Mensaje</th>
      </tr>
      </thead>
      <tbody>
      <g:each in="${processResults}" var="result" status="index">
        <tr>
          <td class="text-center">${index+1}</td>
          <td class="text-center">${result.account}</td>
          <td class="text-center">${result.employee ? result.employee.prePaysheetEmployee.nameEmployee : "NO SE ENCONTRÓ EN LA NÓMINA"}</td>
          <td class="text-center">${result.schema}</td>
          <td class="text-center">${result.status}</td>
          <td>${result.resultMessage}</td>
        </tr>
      </g:each>
      </tbody>
    </table>
  </div>
</g:else>
