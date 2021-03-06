<%! import com.modulus.uno.PaymentPeriod %>
<%! import com.modulus.uno.paysheet.ContractType %>
<%! import com.modulus.uno.paysheet.WorkDayType %>
<%! import com.modulus.uno.paysheet.RegimeType %>
<%! import com.modulus.uno.paysheet.JobRisk %>
<input type="hidden" name="idEmployee" value="${employee.id}"/>
<input type="hidden" name="businessEntityId" value="${businessEntity.id}"/>
<div class="row">
  <div class="col-md-4">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.nss"/></label>
      <input class="form-control" type="number" name="nss" value="${dataImssEmployee?.nss}" required=""/>
    </div>
  </div>
  <div class="col-md-4">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.registrationDate"/></label><br/>
      <input type="text" id="dpRegistration" name="registrationDate" value="${dataImssEmployee?.registrationDate?.format('dd/MM/yyyy')}" required="">
    </div>
  </div>
  <div class="col-md-4">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.dischargeDate"/></label><br/>
      <input type="text" id="dpDischarge" name="dischargeDate" value="${dataImssEmployee?.dischargeDate?.format('dd/MM/yyyy')}">
    </div>
  </div>
</div>

<div class="row">
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.baseImssMonthlySalary"/></label>
      <input class="form-control" id="saSalary" type="number" min="0.00" step="0.01" name="baseImssMonthlySalary" value="${dataImssEmployee?.baseImssMonthlySalary}" required=""/>
      <input class="form-control" id="iasSalary" type="hidden" name="assimilableMonthlySalary" value="${dataImssEmployee.id ? dataImssEmployee.totalMonthlySalary-dataImssEmployee.baseImssMonthlySalary : '0'}" />
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.totalMonthlySalary"/></label>
      <input class="form-control" id="totalSalary" type="number" min="0.00" step="0.01" name="totalMonthlySalary" value="${dataImssEmployee?.totalMonthlySalary}" required=""/>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.holidayBonusRate"/></label>
      <input class="form-control" type="number" min="0.00" max="100.00" step="0.01" name="holidayBonusRate" value="${dataImssEmployee?.holidayBonusRate ?: 25}" required=""/>
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.annualBonusDays"/></label>
      <input class="form-control" type="number" min="15" name="annualBonusDays" value="${dataImssEmployee?.annualBonusDays ?: 15}" required=""/>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.paymentPeriod"/></label>
      <g:select class="form-control" name="paymentPeriod" from="${PaymentPeriod.values()}" value="${dataImssEmployee?.paymentPeriod}" />
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.contractType"/></label>
      <g:select class="form-control" name="contractType" from="${ContractType.values()}" value="${dataImssEmployee?.contractType}" />
    </div>
  </div>
</div>

<div class="row">
  <div class="col-md-4">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.department"/></label>
      <input class="form-control" name="department" value="${dataImssEmployee?.department}" maxLength="150" required=""/>
    </div>
  </div>
  <div class="col-md-4">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.job"/></label>
      <input class="form-control" name="job" value="${dataImssEmployee?.job}" maxLength="150" required=""/>
    </div>
  </div>
  <div class="col-md-4">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.jobRisk"/></label>
      <g:select class="form-control" name="jobRisk" from="${JobRisk.values()}" value="${dataImssEmployee?.jobRisk}" />
    </div>
  </div>
</div>

<div class="row">
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.workDayType"/></label>
      <g:select class="form-control" name="workDayType" from="${WorkDayType.values()}" value="${dataImssEmployee?.workDayType}" />
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="dataImssEmployee.label.regimeType"/></label>
      <g:select class="form-control" name="regimeType" from="${RegimeType.values()}" value="${dataImssEmployee?.regimeType}" />
    </div>
  </div>
</div>

