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
            <g:link action="create" controller="dataImssEmployee" params="[businessEntity:businessEntity.id]" class="btn btn-default">Registrar</g:link>
            </sec:ifAnyGranted>
          </div>
          </g:if>
        </div>
      </div>
    </div>
  </div>
  <g:if test="${dataImssEmployee}">
  <div class="panel-body">
    <dl class="dl-horizontal">
      <dt>Fecha de Alta</dt>
      <dd>dd-MM-yyyy</dd>
      <dt>Salario Base Mensual</dt>
      <dd>$ 00,000.00</dd>
      <dt>Salario Neto Mensual</dt>
      <dd>$ 00,000.00</dd>
      <dt>Prima vacacional (%)</dt>
      <dd>00</dd>
      <dt>Días de Aguinaldo</dd>
      <dd>00</dd>
      <dt>Periodicidad de pago</dt>
      <dd>SEMANAL</dd>
    </dl>
  </div>
  <div class="panel-footer">
    <div class="row">
      <div class="col-md-12 text-right">
        <g:link class="btn btn-default" controller="dataImssEmployee" action="edit" id="${dataImssEmployee?.id}">Editar</g:link>
      </div>
    </div>
  </div>
  </g:if>
</div>
