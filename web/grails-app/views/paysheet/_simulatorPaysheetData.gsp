<div class="col-md-12">
  <div class="table-responsive">
    <table class="table table-striped table-condensed">
      <tr>
        <th>Consecutivo</th>
        <th>Resultado</th>
        <th>Periodo</th>
        <th>Salario IMSS Bruto</th>
        <th>Carga social del trabajador</th>
        <th>Subsidio</th>
        <th>ISR IMSS</th>
        <th>Salario IMSS Neto</th>
        <th>Asimilable Bruto</th>
        <th>ISR Asimilable</th>
        <th>Asimilable Neto</th>
        <th>Subtotal</th>
        <th>Carga social de la empresa</th>
        <th>ISN</th>
        <th>Costo nominal</th>
        <th>Comisión</th>
        <th>Total nómina</th>
        <th>Iva</th>
        <th>Total a Facturar</th>
      </tr>
      <g:each in="${resultList}" var="result" status="index">
        <tr>
          <td class="text-center">${index+1}</td>
          <td class="text-center">${result.result}</td>
          <td class="text-center">${result.row.PERIODO}</td>
          <td class="text-right">${result.result == "OK" ?  modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.salaryImss) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.socialQuota) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.subsidySalary) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.incomeTax) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.imssSalaryNet) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.crudeAssimilable) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.incomeTaxAssimilable) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.netAssimilable) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.totalSalaryEmployee) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.socialQuotaEmployer) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.paysheetTax) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.paysheetCost) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.commission) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.paysheetTotal) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.paysheetIva) : "-"}</td>
          <td class="text-right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.totalToInvoice) : "-"}</td>
        </tr>
      </g:each>
    </table>
  </div>
</div>
