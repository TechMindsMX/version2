<div class="row">
  <div class="col-md-5">
    <label><strong><g:message code="prePaysheet.label.paysheetProject"/></strong></label><br/>
    <label>${prePaysheet.paysheetProject}</label>
  </div>
  <div class="col-md-5">
    <label><strong><g:message code="prePaysheet.label.accountExecutive"/></strong></label><br/>
    <label>${prePaysheet.accountExecutive}</label>
  </div>
  <div class="col-md-2">
    <label><strong><g:message code="prePaysheet.label.status"/></strong></label><br/>
    <label><g:message code="prePaysheet.status.${prePaysheet.status}"/></label>
  </div>
</div>
<div class="row">
  <div class="col-md-4">
    <label><strong><g:message code="prePaysheet.label.paymentPeriod"/></strong></label><br/>
    <label>${prePaysheet.paymentPeriod}</label>
  </div>
  <div class="col-md-4">
    <label><strong>Del:</strong></label><br/>
    <label><g:formatDate format="dd-MM-yyyy" date="${prePaysheet.initPeriod}"/></label>
  </div>
  <div class="col-md-4">
    <label><strong>Al:</strong></label><br/>
    <label><g:formatDate format="dd-MM-yyyy" date="${prePaysheet.endPeriod}"/></label>
  </div>
</div>
