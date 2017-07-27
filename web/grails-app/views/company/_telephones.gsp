<div class="property-value" aria-labelledby="${label}-label">
  <ul>
    <g:each var="telephone" in="${company.telephones}">
      <sec:ifAnyGranted roles="ROLE_CORPORATIVE">
        ${telephone.toString()}
      </sec:ifAnyGranted>
      <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_LEGAL_REPRESENTATIVE_VISOR">
        <li class="subList"><g:link controller="telephone" action="editForCompany" id="${telephone.id}" >${telephone.toString()}</g:link></li>
      </sec:ifAnyGranted>
    </g:each>
  </ul>
</div>
<sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR">
  <div class="text-right">
  	<g:link action="createForCompany" controller="telephone" class="btn btn-default"><i class="fa fa-plus"></i></g:link>
  </div>
</sec:ifAnyGranted>
