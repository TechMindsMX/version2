<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
  </head>
  <body>
    <!-- BEGIN PAGE TITLE -->
    <div class="page-title">
      <h1>
        <i class="fa fa-users fa-3x"></i>
        Administración / Lista de Usuarios
        <small>Usuarios registrados en el corporativo</small>
      </h1>
    </div>
    <!-- END OF PAGE TITLE -->

    <!-- BEGIN ROW -->
    <div class="row">
      <div class="portlet portlet-default">
        <div class="portlet-heading">
          <div class="portlet-title">
            <h4>Usuarios registrados en el corporativo</h4>
          </div>
          <div class="clearfix"></div>
        </div>

        <div class="portlet-body">
          <g:if test="${flash.message}">
          <div class="alert alert-success" role="alert">${flash.message}</div>
          </g:if>
          <g:if test="${users}">
            <table class="table">
              <tr>
                <th class="text-align:center;width:600px">Usuario</th>
                <th class="text-align:center;width:600px">Nombre de Usuario</th>
              </tr>
              <g:each in="${users}" var="user">
                <tr>
                  <td>
                    <g:link controller="corporate" action="assignRolesInCompaniesForUser" id="${user.id}">
                      ${user.profile.fullName}
                    </g:link>
                  </td>
                  <td>${user.username}</td>
                </tr>
              </g:each>
            </table>
          </g:if>
        </div>
      </div>
    </div>
    <!-- END OF ROW -->

  </body>
</html>
