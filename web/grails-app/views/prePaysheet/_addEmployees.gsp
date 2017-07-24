<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Empleados disponibles para agregar</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
    <g:hiddenField id="entities" name="entities" value=""/>
    <g:hiddenField name="prePaysheet.id" value="${prePaysheet.id}"/>
    <div class="table-responsive">
      <table class="table">
        <tr>
          <th><g:checkBox id="selectAll" name="selectAll" title="Seleccionar Todos"/></th>
          <th>No. Empl</th>
          <th>Nombre</th>
          <th>RFC</th>
          <th>CURP</th>
        </tr>

        <g:each in="${employeesAvailableToAdd}" var="employee">
        <tr>
          <td><g:checkBox class="entity" id="checkBe" name="checkBe" value="${employee.id}" checked="false"/></td>
          <td>${employee.number}</td>
          <td>${employee}</td>
          <td>${employee.rfc}</td>
          <td>${employee.curp}</td>
        </tr>
        </g:each>
      </table>
    </div>
  </div>

  <div class="portlet-footer">
    <button class="btn btn-primary text-right" id="add">Agregar</button>
  </div>
</div>

