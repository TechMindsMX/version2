<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4></h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div id="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
      <g:form name="formUserMenus" action="saveGrantsMenusForUser" id="${user.id}">
      <g:hiddenField id="companyId" name="companyId" value="${company.id}"/>
      <g:hiddenField id="userId" name="userId" value="${user.id}"/>
      <div class="row">
        <div class="col-md-12">
          <div class="form-group">
            <label>Rol a configurar:</label>
            <select class="form-control" id="roleId" name="roleId">
              <option value="">Seleccione el rol a configurar...</option>
              <g:each in="${companyRolesForUser}" var="role">
                <option value="${role.id}"><g:message code="role.authority.${role.authority.toLowerCase()}"/></option>
              </g:each>
            </select>
            
          </div>
        </div>
      </div>
      <div id="divListMenus" class="row">
        <div class="col-md-12">
          <label>Seleccione los menús disponibles para el usuario</label>
          <div class="table-responsive">
            <table id="listMenus" class="table table-condensed table-striped">
            </table>
          </div>
          <div class="row text-right">
            <input class="save btn btn-default" id="buttonApplyUserMenus" type="submit" value="Aplicar" />
            <g:link class="btn btn-default" action="assignRolesInCompaniesForUser" id="${user.id}">Salir</g:link>
          </div>
        </div>
      </div>
      </g:form>

      <div class="modal fade" id="noSelectionModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title">ModulusUno</h4>
            </div>
            <div class="modal-body">
              <p>Por favor seleccione al menos un submenú</p>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
            </div>
          </div>
        </div>
      </div>

      </div>
    </div>
  </div>
</div>
