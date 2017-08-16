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
                <th>Nombre</th>
                <th>Usuario</th>
                <th>Email</th>
                <th></th>
              </tr>
              <g:each in="${users}" var="user">
                <tr>
                  <td>
                    <g:link controller="corporate" action="assignRolesInCompaniesForUser" id="${user.id}">
                      ${user.profile.fullName}
                    </g:link>
                  </td>
                  <td>${user.username}</td>
                  <td>${user.profile.email}</td>
                  <td class="text-right">
                    <g:link class="btn btn-warning" action="changeStatusUser" id="${user.id}">
                      <g:if test="${user.enabled}">Desactivar</g:if><g:else>Activar</g:else>
                    </g:link>
                  </td>
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
