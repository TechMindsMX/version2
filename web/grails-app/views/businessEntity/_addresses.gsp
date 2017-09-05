<g:if test="${!relation.equals('EMPLEADO')}">
<div class="panel panel-default">
  <div class="panel-heading">
    <div class="row">
      <div class="col-md-12">
        <div class="col-md-6">
          <span id="address-label" class="property-label"><h4>Dirección</h4></span>
        </div>
        <div class="col-md-6" align="right">
          <div class="property-value" aria-labelledby="company-label">
            <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR">
            <g:link action="create" controller="address" params="[businessEntity:businessEntity.id]" class="btn btn-default">Agregar Dirección</g:link>
            </sec:ifAnyGranted>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="panel-body">
    <g:if test="${businessEntity.addresses}" >

    <div class="property-value" aria-labelledby="telefono-label">
      <ul>
        <g:each var="address" in="${businessEntity.addresses.sort{it.id}}">
        <g:link controller="address" action="edit" id="${address.id}" params="[relation:relation, businessEntityId:businessEntity.id]">
        <li class="subList">${address.addressType}: ${address.street} #${address.streetNumber} - ${address.suite} CP ${address.zipCode}, ${address.colony}, ${address.city}, ${address.town}. ${address.federalEntity}, ${address.country}</li>
        </g:link>
        </g:each>
      </ul>
    </div>
    </g:if>
  </div>
</div>
</g:if>
