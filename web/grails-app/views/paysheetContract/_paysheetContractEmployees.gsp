<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Empleados</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
    <div class="table-responsive">
      <div class="container-fluid">
      <table class="table table-striped table-condensed">
        <tr>
          <th>Nombre</th>
          <th>RFC</th>
          <th></th>
        </tr>

        <g:each in="${paysheetContract.employees}" var="employee" status="index">
        <tr>
          <td>${employee}</td>
          <td>${employee.rfc}</td>
          <td><g:link class="btn btn-danger" action="deleteEmployee" id="${employee.id}">Quitar</g:link></td>
        </tr>
        </g:each>
      </table>
      </div>
    </div>
  </div>

</div>

