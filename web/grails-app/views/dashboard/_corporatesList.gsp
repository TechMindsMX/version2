<%! import com.modulus.uno.CorporateStatus %>
<div class="table-responsive">
  <table class="table table-condensed table-striped">
    <thead>
    <tr>
      <th class="text-center">Corporativo</th>
      <th class="text-center">URL</th>
      <th class="text-center">No. de Empresas</th>
      <th></th>
      <th></th>
      <th></th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${corporates}" var="corp">
    <tr>
      <td><g:link controller="corporate" action="show" id="${corp.id}"><strong>${corp.nameCorporate}</strong></g:link></td>
      <td>${corp.corporateUrl}</td>
      <td class="text-center">${corp.companies ? corp.companies.size() : 0}</td>
      <td>
        <g:link class="btn btn-primary" controller="corporate" action="commissions" id="${corp.id}">
          Comisiones
        </g:link>
      </td>
      <td>
        <g:link class="btn btn-primary" controller="corporate" action="defineCostCenters" id="${corp.id}">
          Centros de Costos
        </g:link>
      </td>
      <td>
        <g:if test="${corp.status == CorporateStatus.ENABLED}"> 
          <g:link class="btn btn-danger" controller="corporate" action="enableOrDisableCorporate" id="${corp.id}">
            Deshabilitar
          </g:link>
        </g:if>
        <g:else>
          <g:link class="btn btn-danger" controller="corporate" action="enableOrDisableCorporate" id="${corp.id}">
            Habilitar
          </g:link>
        </g:else>
      </td>
    </tr>
    </g:each>
    </tbody>
  </table>
 <nav>
    <div class="pagination">
      <g:paginate class="pagination" controller="dashboard" action="index" total="${countCorporates}" />
    </div>
  </nav>
</div>
