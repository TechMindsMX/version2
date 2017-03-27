<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%! import com.modulus.uno.TransactionType %>
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
  <h1>
    <g:message code="Estado de Cuenta"/>
    <small>${accountStatement.company.bussinessName} - ${accountStatement.company.rfc}</small>
  </h1>
  <div>
    <h3><p>Desde el: ${startDateFormatted}</p>
      <p>Hasta el: ${endDateFormatted}</p></h3>
  </div>
  <div>
    <table>
     <tr>
       <th style="width:20%">Fecha</th>
       <th style="width:25%">Concepto</th>
       <th style="width:5%">Id de Transacción</th>
       <th style="width:15%">Abono</th>
       <th style="width:15%">Cargo</th>
       <th style="width:20%">Saldo</th>
     </tr>
     <g:each in="${accountStatement.transactions}" var="mov">
      <tr>
        <td><g:formatDate format="dd-MM-yyyy hh:mm:ss" date="${mov.dateCreated}"/></td>
        <td>
          ${mov.paymentConcept}
        </td>
        <td style="text-align:center">${mov.keyTransaction?:""}</td>
        <td style="text-align:right">
          <g:if test="${mov.transactionType == TransactionType.DEPOSIT}">
            ${modulusuno.formatPrice(number: mov.amount)}
          </g:if>
        </td>
        <td style="text-align:right">
          <g:if test="${mov.transactionType == TransactionType.WITHDRAW}">
            ${modulusuno.formatPrice(number: mov.amount)}
          </g:if>
        </td>
        <td style="text-align:right">${modulusuno.formatPrice(number: mov.balance)}</td>
      </tr>
     </g:each>
   </table>
  </div>
</body>
</html>
