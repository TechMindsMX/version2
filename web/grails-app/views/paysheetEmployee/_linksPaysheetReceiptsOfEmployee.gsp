<%! import com.modulus.uno.paysheet.PaymentSchema %>
<div class="row">
  <div class="col-md-6">
    <label>SA</label><br/>
    <g:if test="${employee.paysheetReceiptUuidSA}"> 
      <span><a href="${modulusuno.paysheetReceiptUrl(employee:employee, schema:PaymentSchema.IMSS, format:'xml')}" class="btn btn-success" download>XML</a></span>
      <span><a href="${modulusuno.paysheetReceiptUrl(employee:employee, schema:PaymentSchema.IMSS, format:'pdf')}" class="btn btn-success" download>PDF</a></span>
    </g:if><g:else>NO GENERADOS</g:else>
  </div>
  <div class="col-md-6">
    <label>IAS</label><br/>
    <g:if test="${employee.paysheetReceiptUuidIAS}">
      <span><a href="${modulusuno.paysheetReceiptUrl(employee:employee, schema:PaymentSchema.ASSIMILABLE, format:'xml')}" class="btn btn-success" download>XML</a></span>
      <span><a href="${modulusuno.paysheetReceiptUrl(employee:employee, schema:PaymentSchema.ASSIMILABLE, format:'pdf')}" class="btn btn-success" download>PDF</a></span>
    </g:if><g:else>NO GENERADOS</g:else>
  </div>
</div>

