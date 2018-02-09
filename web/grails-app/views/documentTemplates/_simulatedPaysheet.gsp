<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title></title>
  <style>
      body {
        font-family: arial;
        font-size: 12px;
        line-height: 1.428571429;
        color: #333333;
        background-color: #ffffff;
      }
      tbody td { border:1px solid black; }
      tbody tr:nth-child(even) {background-color: #f2f2f2;}
      tfoot td { border-top:2px solid black; border-right:1px solid black; border-left:1px solid black; }
      tfoot tr { background:#AED6F1 }
      table { width:100%; cellspacing:0; border-collapse: collapse; border:1px solid black; }
      thead th {
        border-bottom:1px solid black;
        text-align: center; 
      }
      thead tr {
        background: #1B4F72;
        color:white;
      }
      @page {
        size: landscape;
      }
  </style>
</head>
<body>
  <h1>
    SIMULACIÓN DE NÓMINA
    <small>${date}</small>
  </h1>
  <div>
    <table>
    <thead>
     <tr>
       <th style="width:5%">No.</th>
       <th style="width:10%">Stat</th>
       <th style="width:5%">Frec</th>
       <th style="width:5%">SA Bruto</th>
       <th style="width:5%">Ret IMSS</th>
       <th style="width:5%">Subsidio</th>
       <th style="width:5%">Ret ISR SA</th>
       <th style="width:5%">SA Neto</th>
       <th style="width:5%">IAS Bruto</th>
       <th style="width:5%">Ret ISR IAS</th>
       <th style="width:5%">IAS Neto</th>
       <th style="width:5%">Total Neto</th>
       <th style="width:5%">Carga Social Empresa</th>
       <th style="width:5%">ISN</th>
       <th style="width:5%">Costo Nómina</th>
       <th style="width:5%">Comisión</th>
       <th style="width:5%">Total Nómina</th>
       <th style="width:5%">IVA</th>
       <th style="width:5%">Gran Total</th>
     </tr>
     </thead>
     <tbody>
      <g:each in="${importResultList}" var="result" status="index">
        <tr>
          <td style="text-align:center">${index+1}</td>
          <td >${result.result}</td>
          <td style="text-align:center">${result.period.shortName}</td>
          <td style="text-align:right">${result.result == "OK" ?  modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.salaryImss) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.socialQuota) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.subsidySalary) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.incomeTax) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.imssSalaryNet) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.crudeAssimilable) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.incomeTaxAssimilable) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.netAssimilable) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.totalSalaryEmployee) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.socialQuotaEmployer) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.paysheetTax) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.paysheetCost) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.commission) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.paysheetTotal) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.paysheetIva) : "-"}</td>
          <td style="text-align:right">${result.result == "OK" ? modulusuno.formatPrice(number:result.simulatedPaysheetEmployee.totalToInvoice) : "-"}</td>
        </tr>
      </g:each>
      </tbody>
      <tfoot>
      <tr>
        <td><strong>TOTALES</strong></td>
        <td></td>
        <td></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.salaryImss.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.socialQuota.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.subsidySalary.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.incomeTax.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.imssSalaryNet.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.crudeAssimilable.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.incomeTaxAssimilable.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.netAssimilable.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.totalSalaryEmployee.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.socialQuotaEmployer.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.paysheetTax.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.paysheetCost.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.commission.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.paysheetTotal.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.paysheetIva.sum())}</strong></td>
        <td style="text-align:right"><strong>${modulusuno.formatPrice(number:importResultList*.simulatedPaysheetEmployee.totalToInvoice.sum())}</strong></td>
      </tr>
      </tfoot>
   </table>
  </div>
</body>
</html>
