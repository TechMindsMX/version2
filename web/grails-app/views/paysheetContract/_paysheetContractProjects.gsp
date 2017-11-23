<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Proyectos</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">

      <div class="row">
        <div class="col-md-12 text-right">
          <g:link class="btn btn-default" controller="paysheetProject" action="create" id="${paysheetContract.id}">Nuevo</g:link>
        </div>
      </div>
      <g:if test="${paysheetContract.projects}">
      <div class="table-responsive">
        <table class="table table-striped table-condensed">
          <thead>
            <tr>
              <th class="text-center">Nombre</th>
              <th class="text-center" width="45%">Descripción</th>
              <th class="text-center">F.I.</th>
              <th class="text-center">R.T. (%)</th>
              <th class="text-center">Comisión (%)</th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${paysheetContract.projects.sort{it.name}}" var="project">
            <tr>
              <td><g:link controller="paysheetProject" action="show" id="${project.id}">${project.name}</g:link></td>
              <td>${project.description}</td>
              <td class="text-right">${project.integrationFactor}</td>
              <td class="text-right">${project.occupationalRiskRate}</td>
              <td class="text-right"><g:formatNumber number="${project.commission}" format="#0.000000" locale="es_MX"/></td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      </g:if>

  </div>

</div>

