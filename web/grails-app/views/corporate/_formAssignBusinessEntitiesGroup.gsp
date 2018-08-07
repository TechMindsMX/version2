<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Asociar Grupos de Relaciones Comerciales</h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div id="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
      <g:form action="addBusinessEntitiesGroupToUser" id="${user.id}">
      <div class="row">
        <div class="col-md-8">
          <div class="form-group">
            <label>Grupos disponibles a agregar:</label>
            <g:select class="form-control" name="businessEntitiesGroupId" from="${companyGroups.sort{it.description}}" optionKey="id" optionValue="description" noSelection="['':'Seleccione un grupo...']" required=""/>
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
                  <th class="col-md-10 text-center">Grupo</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                <g:each in="${user.businessEntitiesGroups.sort{it.description}}" var="group">
                  <tr>
                    <td>${group.description}</td>
                    <td class="text-right"><g:link class="btn btn-danger" action="deleteBusinessEntitiesGroupFromUser" id="${user.id}" params="[groupId:group.id]">Quitar</g:link></td>
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
