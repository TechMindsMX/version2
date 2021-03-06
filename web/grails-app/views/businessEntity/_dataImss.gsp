<%! import com.modulus.uno.BusinessEntityStatus %>
<div class="panel panel-default">
  <div class="panel-heading">
    <div class="row">
      <div class="col-md-12">
        <div class="col-md-6">
          <span id="address-label" class="property-label"><h4>Datos IMSS</h4></span>
        </div>
        <div class="col-md-6" align="right">
          <g:if test="${!dataImssEmployee}">
          <div class="property-value" aria-labelledby="company-label">
            <sec:ifAnyGranted roles="ROLE_OPERATOR_EJECUTOR">
              <g:if test="${businessEntity.status == BusinessEntityStatus.ACTIVE || businessEntity.status == BusinessEntityStatus.TO_AUTHORIZE}">
                <g:link action="create" controller="dataImssEmployee" params="[businessEntityId:businessEntity.id]" class="btn btn-default">Registrar</g:link>
              </g:if>
            </sec:ifAnyGranted>
          </div>
          </g:if>
        </div>
      </div>
    </div>
  </div>
  <g:if test="${dataImssEmployee}">
  <div class="panel-body">
    <div class="row">
      <div class="col-md-6">
        <dl>
          <dt><g:message code="dataImssEmployee.label.nss"/></dt>
          <dd>${dataImssEmployee.nss}</dd>
          <dt><g:message code="dataImssEmployee.label.registrationDate"/></dt>
          <dd><g:formatDate date="${dataImssEmployee.registrationDate}" format="dd-MM-yyyy"/></dd>
          <dt><g:message code="dataImssEmployee.label.dischargeDate"/></dt>
          <dd><g:if test="${dataImssEmployee.dischargeDate}"><g:formatDate date="${dataImssEmployee.dischargeDate}" format="dd-MM-yyyy"/></g:if><g:else> - </g:else></dd>
          <dt><g:message code="dataImssEmployee.label.baseImssMonthlySalary"/></dt>
          <dd>${modulusuno.formatPrice(number:dataImssEmployee.baseImssMonthlySalary)}</dd>
          <dt><g:message code="dataImssEmployee.label.totalMonthlySalary"/></dt>
          <dd>${modulusuno.formatPrice(number:dataImssEmployee.totalMonthlySalary)}</dd>
          <dt><g:message code="dataImssEmployee.label.holidayBonusRate"/></dt>
          <dd><g:formatNumber number="${dataImssEmployee.holidayBonusRate}" type="number" maxFractionDigits="2"/></dd>
          <dt><g:message code="dataImssEmployee.label.annualBonusDays"/></dt>
          <dd><g:formatNumber number="${dataImssEmployee.annualBonusDays}" type="number" maxFractionDigits="2"/></dd>
        </dl>
      </div>
      <div class="col-md-6">
        <dl>
          <dt><g:message code="dataImssEmployee.label.paymentPeriod"/></dt>
          <dd>${dataImssEmployee.paymentPeriod}</dd>
          <dt><g:message code="dataImssEmployee.label.department"/></dt>
          <dd>${dataImssEmployee.department}</dd>
          <dt><g:message code="dataImssEmployee.label.job"/></dt>
          <dd>${dataImssEmployee.job}</dd>
          <dt><g:message code="dataImssEmployee.label.jobRisk"/></dt>
          <dd>${dataImssEmployee.jobRisk}</dd>
          <dt><g:message code="dataImssEmployee.label.contractType"/></dt>
          <dd>${dataImssEmployee.contractType}</dd>
          <dt><g:message code="dataImssEmployee.label.workDayType"/></dt>
          <dd>${dataImssEmployee.workDayType}</dd>
          <dt><g:message code="dataImssEmployee.label.regimeType"/></dt>
          <dd>${dataImssEmployee.regimeType}</dd>
        </dl>
      </div>
    </div>
  </div>
  <div class="panel-footer">
    <div class="row">
      <div class="col-md-12 text-right">
        <sec:ifAnyGranted roles="ROLE_OPERATOR_EJECUTOR">
          <g:if test="${businessEntity.status == BusinessEntityStatus.ACTIVE || businessEntity.status == BusinessEntityStatus.TO_AUTHORIZE}">
            <g:link class="btn btn-default" controller="dataImssEmployee" action="edit" id="${dataImssEmployee?.id}" params="[businessEntityId:businessEntity.id]">Editar</g:link>
          </g:if>
        </sec:ifAnyGranted>
      </div>
    </div>
  </div>
  </g:if>
</div>
