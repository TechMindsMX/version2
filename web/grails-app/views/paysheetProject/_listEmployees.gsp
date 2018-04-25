<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Empleados del Proyecto</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
    <div class="row">
      <div class="col-md-12 text-right">
        <g:if test="${!availableEmployees}">
          <g:if test="${paysheetProject.employees}">
            <g:link class="btn btn-primary" action="chooseEmployees" id="${paysheetProject.id}">Crear Usuarios</g:link>
          </g:if>
          <g:link class="btn btn-primary" action="chooseEmployees" id="${paysheetProject.id}">Agregar Empleados</g:link>
        </g:if>
      </div>
    </div>
    <br/>
    <div class="table-responsive">
      <div class="container-fluid">
      <table class="table table-striped table-condensed">
        <tr>
          <th class="text-center">Nombre</th>
          <th class="text-center">RFC</th>
          <th class="text-center">Usuario</th>
          <th></th>
        </tr>

        <g:each in="${paysheetProject.employees.sort{it.toString()}}" var="employee" status="index">
        <tr>
          <td>${employee}</td>
          <td>${employee.rfc}</td>
          <td>${(paysheetProject.users.find { userEmployee -> userEmployee.businessEntity.id == employee.id })?.user?.username }</td>
          <td class="text-center"><g:link class="btn btn-danger" action="deleteEmployee" id="${paysheetProject.id}" params="[employeeId:employee.id]">Quitar</g:link></td>
        </tr>
        </g:each>
      </table>
      </div>
    </div>
  </div>

</div>

