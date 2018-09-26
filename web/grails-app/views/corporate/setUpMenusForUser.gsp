<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title>Configurar Menús del Usuario</title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-tasks fa-3x"></i>
        Configurar Menús del Usuario
      </h1>
    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="portlet portlet-default">
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <dl class="dl-horizontal">
                <dt>Empresa:</dt>
                <dd>${company}</dd>
                <dt>Usuario:</dt>
                <dd>${user.username}</dd>
              </dl>
              <div class="row">
                <div class="col-md-12 text-right">
                  <g:link class="btn btn-primary" action="assignRolesInCompaniesForUser" id="${user.id}">Regresar</g:link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class"row">
      <div class"col-md-12">
        <g:render template="formMenusForRoleUserCompany"/>        
      </div>
    </div>

    <div class"row">
      <div class"col-md-12 text-right">
      </div>
    </div>

  </body>
</html>

