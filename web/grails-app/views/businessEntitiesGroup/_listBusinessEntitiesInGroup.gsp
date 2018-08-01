<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Relaciones Comerciales</h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div id="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
      <g:form action="addBusinessEntity" id="${businessEntitiesGroup.id}">
      <div class="row">
        <div class="col-md-8">
          <div class="form-group">
            <label>Relación Comercial a agregar:</label>
            <g:select class="form-control" name="businessEntityId" from="${businessEntitiesAvailables.sort{it.toString()}}" optionKey="id" noSelection="['':'Seleccione una relación comercial...']" required=""/>
          </div>
        </div>
        <div class="col-md-4 text-right">
          <br/>
          <button class="btn btn-primary" type="submit">Agregar</button>
        </div>

      </div>
      </g:form>
      <hr>
      <div class="row">
        <div class="col-md-12">
          <div class="table-responsive">
            <table class="table table-condensed table-striped">
              <thead>
                <tr>
                  <th class="col-md-8 text-center">Relación Comercial</th>
                  <th class="col-md-2 text-center">RFC</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                <g:each in="${businessEntitiesGroup.businessEntities.sort{it.toString()}}" var="businessEntity">
                  <tr>
                    <td>${businessEntity}</td>
                    <td class="text-center">${businessEntity.rfc}</td>
                    <td class="text-right"><g:link class="btn btn-danger" action="deleteBusinessEntity" id="${businessEntitiesGroup.id}" params="[businessEntityId:businessEntity.id]">Quitar</g:link></td>
                  </tr>
                </g:each>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
