<div class="property-value" aria-labelledby="${label}-label">
  <g:each var="contact" in="${company.contacts.sort{ it.id }}">

      <sec:ifAnyGranted roles="ROLE_CORPORATIVE">
        ${contact.toString()}
      </sec:ifAnyGranted>

      <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_LEGAL_REPRESENTATIVE_VISOR">

        <div class="panel-group" id="contactInformation" role="tablist" aria-multiselectable="true">
          <div class="panel panel-default">
            <div class="panel-heading" role="tab" id="heading_${contact.id}">
              <h4 class="panel-title">
                <a role="button" data-toggle="collapse" data-parent="#contactInformation" href="#collapse_${contact.id}" aria-expanded="true" aria-controls="collapse_${contact.id}" style="color:inherit !important;">
                  ${contact.department} - ${contact.position}
                </a>

                <span class="pull-right">
                  <g:link controller="contactInformation" action="editForCompany" id="${contact.id}">Editar</g:link>
                <span>
              </h4>
            </div>
            <div id="collapse_${contact.id}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_${contact.id}">
              <div class="panel-body">
                <address>
                  <strong>${contact.name}</strong><br>
                  <g:each var="telephone" in="${contact.telephones.sort{ it.id }}">
                    ${telephone.type}: ${telephone.number} <g:if test="${telephone.extension}">,${telephone.extension}</g:if>
                  </g:each>
                </address>

                <span class="pull-right">
                  <g:link action="createForContact" controller="telephone" id="${contact.id}" class="btn btn-default">Agregar Teléfono</g:link>
                </span>
              </div>
            </div>
          </div>
        </div>

      </sec:ifAnyGranted>

    </g:each>
</div>
<sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR">
  <div class="text-right">
  	<g:link action="createForCompany" controller="contactInformation" class="btn btn-default">Agregar contacto</g:link>
  </div>
</sec:ifAnyGranted>


