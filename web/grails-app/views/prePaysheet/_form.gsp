<input type="hidden" name="contractId" value="${prePaysheet?.paysheetContract?.id}"/>
<div class="row">
  <div class="col-md-12">
    <div class="form-group">
      <label><g:message code="prePaysheet.label.paysheetProject"/></label>
      <g:select class="form-control" name="paysheetProject" from="${prePaysheet?.paysheetContract?.projects.sort{it.name}}" optionKey="name" optionValue="name" value="${prePaysheet?.paysheetProject}"/>
    </div>
  </div>
</div>
<div class="row">
  <div class="col-md-4">
    <div class="form-group">
      <label><g:message code="prePaysheet.label.paymentPeriod"/></label><br/>
      <g:select class="form-control" name="paymentPeriod" from="${com.modulus.uno.PaymentPeriod.values()}" value="${prePaysheet?.paymentPeriod}" required=""/>
    </div>
  </div>
  <div class="col-md-4">
    <div class="form-group">
      <label>Del:</label><br/>
      <g:datePicker name="initDatePeriod" value="${prePaysheet?.initPeriod}" precision="day" years="${2017..new Date()[Calendar.YEAR]}" required=""/>
    </div>
  </div>
  <div class="col-md-4">
    <div class="form-group">
      <label>Al:</label><br/>
      <g:datePicker name="endDatePeriod" value="${prePaysheet?.endPeriod}" precision="day" years="${2017..new Date()[Calendar.YEAR]}" required=""/>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-md-12">
    <div class="form-group">
      <label><g:message code="prePaysheet.label.accountExecutive"/></label>
      <input class="form-control" type="text" name="accountExecutive" value="${prePaysheet?.accountExecutive?:prePaysheet?.paysheetContract?.executive?.name}" required="" maxLength="200"/>
    </div>
  </div>
</div>

