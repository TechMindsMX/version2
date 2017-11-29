<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Usuarios disponibles para agregar</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
    <g:hiddenField id="entities" name="entities" value=""/>
    <g:hiddenField name="paysheetContract.id" value="${paysheetContract.id}"/>
    <div class="table-responsive">
      <div class="container-fluid">
      <table class="table table-striped table-condensed">
        <tr>
          <th><g:checkBox id="selectAll" name="selectAll" title="Seleccionar Todos"/></th>
          <th>Nombre</th>
        </tr>

        <g:each in="${availableUsers}" var="user">
        <tr>
          <td><g:checkBox class="entity" id="checkBe" name="checkBe" value="${user.id}" checked="false"/></td>
          <td>${user.name}</td>
        </tr>
        </g:each>
      </table>
      </div>
    </div>
  </div>

  <div class="portlet-footer">
    <button class="btn btn-primary text-right" id="add" type="submit">Agregar</button>
  </div>

</div>

