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
            <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR">
            <g:link action="create" controller="dataImssEmployee" params="[businessEntityId:businessEntity.id]" class="btn btn-default">Registrar</g:link>
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
      <dd>${modulusuno.formatPrice(number:(dataImssEmployee.crudeMonthlySalary - dataImssEmployee.baseImssMonthlySalary))}</dd>
      <dt>Total Mensual</dt>
      <dd>${modulusuno.formatPrice(number:dataImssEmployee.crudeMonthlySalary)}</dd>
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
        <g:link class="btn btn-default" controller="dataImssEmployee" action="edit" id="${dataImssEmployee?.id}" params="[businessEntityId:businessEntity.id]">Editar</g:link>
      </div>
    </div>
  </div>
  </g:if>
</div>
