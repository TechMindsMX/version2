<input type="hidden" name="idEmployee" value="${employee.id}"/>
<input type="hidden" name="businessEntityId" value="${businessEntity.id}"/>
<div class="row">
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.nss"/></label>
      <input class="form-control" type="number" name="nss" value="${dataImssEmployee?.nss}" required=""/>
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.registrationDate"/></label><br/>
      <g:datePicker class="form-control" name="registrationDate" value="${dataImssEmployee?.registrationDate}" precision="day" relativeYears="[-20..0]" required=""/>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.baseImssMonthlySalary"/></label>
      <input class="form-control" type="number" min="0.00" step="0.01" name="baseImssMonthlySalary" value="${dataImssEmployee?.baseImssMonthlySalary}" required=""/>
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.netMonthlySalary"/></label>
      <input class="form-control" type="number" min="0.00" step="0.01" name="netMonthlySalary" value="${dataImssEmployee?.netMonthlySalary}" required=""/>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.holidayBonusRate"/></label>
      <input class="form-control" type="number" min="0.00" max="100.00" step="0.01" name="holidayBonusRate" value="${dataImssEmployee?.holidayBonusRate}" required=""/>
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.annualBonusDays"/></label>
      <input class="form-control" type="number" min="15" name="annualBonusDays" value="${dataImssEmployee?.annualBonusDays}" required=""/>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.paymentPeriod"/></label>
      <g:select class="form-control" name="paymentPeriod" from="${com.modulus.uno.PaymentPeriod.values()}" value="${dataImssEmployee?.paymentPeriod}" />
    </div>
  </div>
</div>
