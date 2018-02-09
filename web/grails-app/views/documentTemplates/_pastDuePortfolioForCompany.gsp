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
      tbody td { border-bottom:1px solid black; }
      tbody tr:nth-child(even) {background-color: #f2f2f2;}
      tfoot td { border-top:2px solid black; solid black; }
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
  </style>
</head>
<body>
  <h1>${company.bussinessName} - ${company.rfc}</h1>
  <h3>Cartera Vencida de ${days} días ${days==120 ? "y más" : ""}</h3>
  <div>
    <table>
      <thead>
      <tr>
        <th style="width:10%">No. de Orden</th>
        <th style="width:30%">Cliente</th>
        <th style="width:20%">Fecha de Cobro</th>
        <th style="width:20%">Fecha de Vencimiento</th>
        <th style="width:10%">Estatus</th>
        <th style="width:10%">Total</th>
      </tr>
      </thead>
      <tbody>
      <g:each in="${detail.sort {it.fechaCobro}}" var="sale">
      <tr>
        <td style="text-align:center">${sale.id}</td>
        <td>${sale.clientName}</td>
        <td style="text-align:center"><g:formatDate format="dd-MM-yyyy" date="${sale.fechaCobro}"/></td>
        <td style="text-align:center"><g:formatDate format="dd-MM-yyyy" date="${sale.originalDate ?: sale.fechaCobro}"/></td>
        <td style="text-align:center">${sale.status}</td>
        <td style="text-align:center">${modulusuno.formatPrice(number: sale.total)}</td>
      </tr>
     </g:each>
     </tbody>
     <tfoot>
     <tr>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td><strong>TOTAL</strong></td>
       <td class="text-right"><strong>${modulusuno.formatPrice(number: detail*.total.sum())}</strong></td>
     </tr>    
     </tfoot>
   </table>
  </div>
</body>
</html>
