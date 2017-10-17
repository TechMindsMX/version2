<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title>Menu Operations</title>
  </head>
  <body>

    <div class="row">
      <!-- BEGIN PAGE TITLE -->
      <div class="page-title">
        <h1>
          Menu operations
        </h1>
      </div>
      <!-- END OF PAGE TITLE -->
    </div>

    <g:if test="${flash.message}">
    <div class="alert alert-info" role="alert">
        <div class="message" role="status">${flash.message}</div>
    </div>
    </g:if>

    <div class="row">
      <div class="col-md-6">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <g:link class="btn btn-primary pull-right" controller="menuOperations">
              <i class="fa fa-reply" aria-hidden="true"></i>  Ver todos los roles
            </g:link>
            <div class="portlet-title">
              <h2>Role</h2>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">

              <g:message code="role.authority.${role.authority.toLowerCase()}" default="${role.authority}" />

              <h2>Menus asignados al role</h2>
              <table class="table table-striped">
                <tbody>
                  <g:each in="${menusOfInstance}" var="m">
                    <tr>
                      <td>${m.menu.name}</td>
                      <td>
                        <g:link class="btn btn-danger pull-right" controller="menuOperations" action="delete" id="${m.menu.id}" params="[roleId:role.id]">
                          Quitar
                        </g:link>
                      </td>
                    </tr>
                  </g:each>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-6">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h2>Menus Disponibles</h2>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <table class="table table-striped">
                <tbody>
                  <g:each in="${menus}" var="m">
                    <tr>
                      <td>${m.name}</td>
                      <td>
                        <g:link  class="btn btn-primary pull-right" controller="menuOperations" action="save" id="${m.id}" params="[roleId:role.id]">
                          Agregar
                        </g:link>
                      </td>
                    </tr>
                  </g:each>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

    </div>
  </body>
</html>
