<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4></h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div id="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
      <g:form action="saveGrantsMenusForUser" id="${user.id}">
      <div class="row">
        <div class="col-md-12">
          <div class="form-group">
            <label>Rol a configurar:</label>
            <select class="form-control" name="roleId">
              <option value="">Seleccione el rol a configurar...</option>
              <g:each in="${companyRolesForUser}" var="role">
                <option value="${role.id}"><g:message code="role.authority.${role.authority.toLowerCase()}"/></option>
              </g:each>
            </select>
            
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
          <div class="form-group">
            <label>Seleccione los menús disponibles para el usuario</label>
          </div>
        </div>
      </div>
      </g:form>
      </div>
    </div>
  </div>
</div>
