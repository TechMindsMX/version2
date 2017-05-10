<ul class="collapse nav" id="administracion">
  <li>
    <g:link controller="company" action="show" id="${session.company}">Mi Empresa</g:link>
  </li>
  <li>
    <g:if test="${companyInfo.isAvailableForOperationInThisCompany() == 'true'}">
      <g:link controller="company" action="accountStatement">Estado de Cuenta</g:link>
    </g:if>
  </li>
  <li><g:link controller="company" action="pendingAccounts">Cuentas por Cobrar/Pagar</g:link></li>
  <li><g:link controller="company" action="pastDuePortfolio">Cartera Vencida</g:link></li>
</ul>
