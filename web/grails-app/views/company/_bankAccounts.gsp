<div class="property-value" aria-labelledby="${label}-label">
  <ul>
    <g:each var="bankAccount" in="${company.banksAccounts.sort{it.banco.name}}">
      <sec:ifAnyGranted roles="ROLE_CORPORATIVE">
        ${bankAccount}
      </sec:ifAnyGranted>
      <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_LEGAL_REPRESENTATIVE_VISOR">
    <li class="subList">
      <g:link action="edit" controller="bankAccount" params="[company:company.id, companyBankAccount:true]" id="${bankAccount.id}">
        ${bankAccount}
      </g:link> <g:if test="${bankAccount.concentradora}"><span class="label label-info">Concentradora</span></g:if>
    </li>
      </sec:ifAnyGranted>
    </g:each>
  </ul>
</div>
<sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR">
<div class="text-right">
  <g:link action="create" controller="bankAccount" params="[company:company.id, companyBankAccount:true]" class="btn btn-default"><i class="fa fa-plus"></i></g:link>
  </div>
</sec:ifAnyGranted>
