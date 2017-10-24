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
        <table class="table">
          <thead>
            <tr>
              <th>Nombre</th>
              <th width="45%">Descripción</th>
              <th>F.I.</th>
              <th>R.T. (%)</th>
              <th>Comisión (%)</th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${paysheetContract.projects.sort{it.name}}" var="project">
            <tr>
              <td><g:link controller="paysheetProject" action="edit" id="${project.id}">${project.name}</g:link></td>
              <td>${project.description}</td>
              <td>${project.integrationFactor}</td>
              <td>${project.occupationalRiskRate}</td>
              <td><g:formatNumber number="${project.commission}" format="#0.000000"/></td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      </g:if>

  </div>

</div>

