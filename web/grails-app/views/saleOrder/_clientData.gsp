<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Datos del cliente</h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div id="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
      <dl class="dl-horizontal">
        <dt>RFC:</dt>
        <dd>${saleOrder.rfc}</dd>
        <dt>A nombre de:</dt>
        <dd>${saleOrder.clientName}</dd>
        <dt>Fecha de Cobro:</dt>
        <dd>${formatDate(date:saleOrder.fechaCobro?:saleOrder.dateCreated, format:'dd-MMMM-yyyy')}</dd>
        <g:each in="${saleOrder.addresses}" var="address">
        <dt>Dirección:</dt>
        <dd>
        ${address.street}<br>
        ${address.streetNumber} ${address.suite}<br>
        ${address.neighboorhood ?: address.colony}<br>
        ${address.country}, ${address.federalEntity}<br>
        ${address.zipCode}<br>
        ${address.city}, ${address.town}<br>
        </dd>
        </g:each>
      </dl>
    </div>
  </div>
</div>
