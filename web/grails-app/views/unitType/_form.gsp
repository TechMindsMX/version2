<div class="portlet portlet-default">
  <div class="portlet-body">
    <g:hiddenField name="id" value="${unitType?.id}"/>
    <g:hiddenField name="version" value="${unitType?.version}"/>
    <div class="row">
      <div class="col-md-3">
        <div class="form-group">
          <label>Clave:</label>
          <input class="form-control" type="text" name="unitKey" value="${unitType?.unitKey}" required=""/>
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label>Nombre:</label>
          <input class="form-control" type="text" name="name" value="${unitType?.name}" required=""/>
        </div>
      </div>
      <div class="col-md-3">
        <div class="form-group">
          <label>Símbolo:</label>
          <input class="form-control" type="text" name="symbol" value="${unitType?.symbol}"/>
        </div>
      </div>
    </div>
  </div>

  <div class="portlet-footer">
    <div class="row">
      <div class="col-md-12 text-right">
        <button class="btn btn-primary">Guardar</button>
      </div>
    </div>
  </div>

</div>

