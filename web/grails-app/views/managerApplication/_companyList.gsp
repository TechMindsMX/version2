<%! import com.modulus.uno.CompanyStatus %>
<div class="portlet portlet-blue">
  <div class="portlet-body">
    <div class="table-responsive">
      <table class="table table-condensed table-striped">
        <thead>
          <th><g:message code="company.rfc" /></th>
          <th><g:message code="company.bussinessName" /></th>
          <th><g:message code="company.aliasCompany" /></th>
          <th><g:message code="company.grossAnnualBilling" /></th>
          <th><g:message code="company.status" /></th>
        </thead>
        <tbody>
          <g:each var="company" in="${companies.sort{it.rfc}}">
          <tr>
            <td>
              <g:link controller="company" action="show" id="${company.id}">${company.rfc}</g:link>
            </td>
            <td>${company.bussinessName}</td>
            <td>${company.aliasCompany}</td>
            <td class="text-right"><modulusuno:formatPrice number="${company.grossAnnualBilling}"/></td>
            <td><modulusuno:statusForCompany status="${company.status}"/></td>
          </tr>
          </g:each>
        </tbody>
      </table>
    </div>
  </div>
</div>
