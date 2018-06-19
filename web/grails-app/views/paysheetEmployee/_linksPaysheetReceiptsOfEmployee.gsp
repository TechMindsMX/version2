<%! import com.modulus.uno.paysheet.PaymentSchema %>
<%! import com.modulus.uno.paysheet.PaysheetEmployeeStatus %>
<div class="row">
  <div class="col-md-6">
    <label>SA</label><br/>
    <g:if test="${employee.paysheetReceiptUuidSA}"> 
      <span><a href="${modulusuno.paysheetReceiptUrl(employee:employee, schema:PaymentSchema.IMSS, format:'xml')}" class="btn btn-success" download>XML</a></span>
      <g:if test="${[PaysheetEmployeeStatus.IMSS_STAMPED, PaysheetEmployeeStatus.FULL_STAMPED].contains(employee.status)}">
        <span><a href="${modulusuno.paysheetReceiptUrl(employee:employee, schema:PaymentSchema.IMSS, format:'pdf')}" class="btn btn-success" download>PDF</a></span>
      </g:if>
      <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR">
        <g:if test="${[PaysheetEmployeeStatus.IMSS_STAMPED_XML, PaysheetEmployeeStatus.FULL_STAMPED_XML].contains(employee.status)}">
          <g:link class="btn btn-primary" action="generatePdfForSAPaysheetReceiptEmployee" id="${employee.id}">Generar PDF</g:link>
        </g:if>
      </sec:ifAnyGranted>
    </g:if>
    <g:else>NO GENERADOS</g:else>
  </div>
  <div class="col-md-6">
    <label>IAS</label><br/>
    <g:if test="${employee.paysheetReceiptUuidIAS}">
      <span><a href="${modulusuno.paysheetReceiptUrl(employee:employee, schema:PaymentSchema.ASSIMILABLE, format:'xml')}" class="btn btn-success" download>XML</a></span>
      <g:if test="${[PaysheetEmployeeStatus.ASSIMILABLE_STAMPED, PaysheetEmployeeStatus.FULL_STAMPED].contains(employee.status)}">
        <span><a href="${modulusuno.paysheetReceiptUrl(employee:employee, schema:PaymentSchema.ASSIMILABLE, format:'pdf')}" class="btn btn-success" download>PDF</a></span>
      </g:if>
      <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR">
        <g:if test="${[PaysheetEmployeeStatus.ASSIMILABLE_STAMPED_XML, PaysheetEmployeeStatus.FULL_STAMPED_XML].contains(employee.status)}">
          <g:link class="btn btn-primary" action="generatePdfForIASPaysheetReceiptEmployee" id="${employee.id}">Generar PDF</g:link>
        </g:if>
      </sec:ifAnyGranted>
    </g:if>
    <g:else>NO GENERADOS</g:else>
  </div>
</div>

