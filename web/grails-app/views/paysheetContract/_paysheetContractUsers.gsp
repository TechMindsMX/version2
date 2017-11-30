<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Usuarios asignados</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
    <div class="row">
      <div class="col-md-12 text-right">
        <g:if test="${!availableEmployees}">
          <g:link class="btn btn-primary" action="chooseUsers" id="${paysheetContract.id}">Agregar Usuarios</g:link>
        </g:if>
      </div>
    </div>
    <br/>
    <div class="table-responsive">
      <div class="container-fluid">
      <table class="table table-striped table-condensed">
        <tr>
          <th class="text-center">Nombre</th>
          <th></th>
        </tr>

        <g:each in="${paysheetContract.users.sort{it.name}}" var="user">
        <tr>
          <td>${user.name}</td>
          <td class="text-center"><g:link class="btn btn-danger" action="deleteUser" id="${paysheetContract.id}" params="[userId:user.id]">Quitar</g:link></td>
        </tr>
        </g:each>
      </table>
      </div>
    </div>
  </div>

</div>

