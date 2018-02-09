<div class="col-md-12">
  <div class="table-responsive">
    <table class="table table-striped table-condensed">
      <thead>
      <tr>
        <th class="text-center">No.</th>
        <th class="text-center">Stat</th>
        <th class="text-center">Frec</th>
        <th class="text-center">SA Bruto</th>
        <th class="text-center">Ret IMSS</th>
        <th class="text-center">Subsidio</th>
        <th class="text-center">Ret ISR SA</th>
        <th class="text-center">SA Neto</th>
        <th class="text-center">IAS Bruto</th>
        <th class="text-center">Ret ISR IAS</th>
        <th class="text-center">IAS Neto</th>
        <th class="text-center">Total Neto</th>
        <th class="text-center">Carga Social Empresa</th>
        <th class="text-center">ISN</th>
        <th class="text-center">Costo Nómina</th>
        <th class="text-center">Comisión</th>
        <th class="text-center">Total Nómina</th>
        <th class="text-center">IVA</th>
        <th class="text-center">Gran Total</th>
      </tr>
      </thead>
      <tbody>
      <g:each in="${resultList}" var="result" status="index">
        <tr>
          <td class="text-center">${index+1}</td>
          <td class="text-center">${result.result}</td>
          <td class="text-center">${result.period.shortName}</td>
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
      </tbody>
      <tfoot>
        <tr>
          <td><strong>TOTALES</strong></td>
          <td class="text-center"></td>
          <td class="text-center"></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.salaryImss.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.socialQuota.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.subsidySalary.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.incomeTax.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.imssSalaryNet.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.crudeAssimilable.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.incomeTaxAssimilable.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.netAssimilable.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.totalSalaryEmployee.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.socialQuotaEmployer.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.paysheetTax.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.paysheetCost.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.commission.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.paysheetTotal.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.paysheetIva.sum())}</strong></td>
          <td class="text-right"><strong>${modulusuno.formatPrice(number:resultList*.simulatedPaysheetEmployee.totalToInvoice.sum())}</strong></td>
        </tr>
      </tfoot>
    </table>
  </div>
</div>
