<div class="row">
  <div class="col-md-6">
    <label><strong><g:message code="prePaysheet.label.paysheetProject"/></strong></label><br/>
    <label>${paysheet.prePaysheet.paysheetProject}</label>
  </div>
  <div class="col-md-6">
    <label><strong><g:message code="prePaysheet.label.accountExecutive"/></strong></label><br/>
    <label>${paysheet.prePaysheet.accountExecutive}</label>
  </div>
</div>
<div class="row">
  <div class="col-md-3">
    <label><strong><g:message code="prePaysheet.label.paymentPeriod"/></strong></label><br/>
    <label>${paysheet.prePaysheet.paymentPeriod}</label>
  </div>
  <div class="col-md-3">
    <label><strong>Del:</strong></label><br/>
    <label><g:formatDate format="dd-MM-yyyy" date="${paysheet.prePaysheet.initPeriod}"/></label>
  </div>
  <div class="col-md-3">
    <label><strong>Al:</strong></label><br/>
    <label><g:formatDate format="dd-MM-yyyy" date="${paysheet.prePaysheet.endPeriod}"/></label>
  </div>
  <div class="col-md-3">
    <label><strong>Total:</strong></label><br/>
    <label>${modulusuno.formatPrice(number:paysheet.total)}</label>
  </div>
</div>
