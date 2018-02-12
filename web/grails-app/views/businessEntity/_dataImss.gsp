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
    <dl>
      <dt>Número de Seguro Social</dt>
      <dd>${dataImssEmployee.nss}</dd>
      <dt>Fecha de Alta</dt>
      <dd><g:formatDate date="${dataImssEmployee.registrationDate}" format="dd-MM-yyyy"/></dd>
      <dt>SA Bruto Mensual</dt>
      <dd>${modulusuno.formatPrice(number:dataImssEmployee.baseImssMonthlySalary)}</dd>
      <dt>IAS</dt>
      <dd>${modulusuno.formatPrice(number:(dataImssEmployee.totalMonthlySalary - dataImssEmployee.baseImssMonthlySalary))}</dd>
      <dt>Total Mensual</dt>
      <dd>${modulusuno.formatPrice(number:dataImssEmployee.totalMonthlySalary)}</dd>
      <dt>Prima vacacional (%)</dt>
      <dd><g:formatNumber number="${dataImssEmployee.holidayBonusRate}" type="number" maxFractionDigits="2"/></dd>
      <dt>Días de Aguinaldo</dt>
      <dd><g:formatNumber number="${dataImssEmployee.annualBonusDays}" type="number" maxFractionDigits="2"/></dd>
      <dt>Periodicidad de pago</dt>
      <dd>${dataImssEmployee.paymentPeriod}</dd>
    </dl>
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
