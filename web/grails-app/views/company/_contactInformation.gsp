<div class="property-value">
  <ul>
    <g:each var="contact" in="${company.contacts}">
        ${contact.toString()}
      <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_LEGAL_REPRESENTATIVE_VISOR">
        <li class="subList"><g:link controller="telephone" action="editForCompany" id="${contact.id}" >${contact.toString()}</g:link></li>
      </sec:ifAnyGranted>
    </g:each>
  </ul>
</div>

<sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR">
  <div class="text-right">
  	<g:link action="createForCompany" controller="telephone" class="btn btn-default"><i class="fa fa-plus"></i></g:link>
  </div>
</sec:ifAnyGranted>
