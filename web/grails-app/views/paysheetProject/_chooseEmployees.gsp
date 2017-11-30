<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Empleados disponibles para agregar</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
    <g:hiddenField id="entities" name="entities" value=""/>
    <g:if test="${flash.message}">
      <div class="alert alert-danger">
        <span>${flash.message}</span>
      </div>
    </g:if>
    <div class="table-responsive">
      <div class="container-fluid">
      <table class="table table-striped table-condensed">
        <tr>
          <th><g:checkBox id="selectAll" name="selectAll" title="Seleccionar Todos"/></th>
          <th>Nombre</th>
          <th>RFC</th>
        </tr>

        <g:each in="${employeesList}" var="employee" status="index">
        <tr>
          <td><g:checkBox class="entity" id="checkBe" name="checkBe" value="${employee.id}" checked="false"/></td>
          <td>${employee}</td>
          <td>${employee.rfc}</td>
        </tr>
        </g:each>
      </table>
      </div>
    </div>
  </div>

  <div class="portlet-footer text-right">
    <button class="btn btn-primary" type="submit">Agregar</button>
  </div>

</div>

