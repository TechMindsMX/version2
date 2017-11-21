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
      td { border-bottom:1px solid black; }
      table { width:100%; }
      th { text-align: center; }
  </style>
</head>
<body>
  <h1></h1>
  <h1>Balance general ${company}</h1>
  <h3>Desde el: <g:formatDate format="yyyy-MM-dd" date="${period.init}"/></h3>
  <h3>Hasta el: <g:formatDate format="yyyy-MM-dd" date="${period.end}"/></h3>
  <div>
    <table>
      <tr>
        <th style="width:40%">Cliente</th>
        <th style="width:20%">Solicitudes</th>
        <th style="width:20%">Pagos</th>
        <th style="width:20%">Saldo</th>
      </tr>
      <g:each in="${detailGeneralBalance}" var="generalBalance">
      <tr>
        <td style="text-align:center">${generalBalance.quotationContract?.client}</td>
        <td style="text-align:right">${modulusuno.formatPrice(number:generalBalance.request)}</td>
        <td style="text-align:right">${modulusuno.formatPrice(number:generalBalance.payment)}</td>
        <td style="text-align:right">${modulusuno.formatPrice(number:generalBalance.balance)}</td>
      </tr>
     </g:each>
      <tr>
        <th style="width:10%">Total</th>
        <th style="width:10%; text-align:right">${modulusuno.formatPrice(number:detailGeneralBalance.sum { it.request })}</th>
        <th style="width:10%; text-align:right">${modulusuno.formatPrice(number:detailGeneralBalance.sum { it.payment })}</th>
        <th style="width:10%; text-align:right">${modulusuno.formatPrice(number:detailGeneralBalance.sum { it.balance })}</th>
      </tr>
    </table>
  </div>
</body>
</html>

