<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title></title>
  </head>
  <body>

<!-- BEGIN PAGE TITLE -->
    <div class="page-title">
      <h1>
        <i class="fa fa-envelope fa-3x"></i>
          Notificaciones
          <small>Registro</small>
      </h1>
    </div>

    <div class="row">
    <ul class="nav nav-tabs navbar-right">
        <li role="presentation"><g:link controller="notificationForState" action="register"> <i class="fa fa-plus fa-1"></i> &nbsp; Nueva Notificación</g:link></li>
        <li role="presentation"><g:link controller="groupNotification" action="index"><i class="fa fa-users fa-1"></i> &nbsp; Grupos a Notificar</g:link></li>
        <li role="presentation"><g:link controller="groupNotification" action="create"><i class="fa fa-user-plus fa-1"></i> &nbsp; Añadir Grupo</g:link></li>
    </ul>
    </div>

    <!-- BEGIN PORTLET -->
    <div class="portlet portlet-blue">
      <!-- BEGIN BLUE-PORTLET -->
      <div class="panel-collapse collapse in">
        <!-- BEGIN PORTLET-BODY -->
        <div class="portlet-body">
          <!-- BEGIN CREATE-ADDRESS -->
          <div class="content scaffold-create">
              <fieldset class="form">
                <div class="row text-center">
                  <label>NOTIFICACIONES REGISTRADAS</label>
                </div>

                <div class="row">
                  <br><div class="form-group col-sm-2"></div>
                  <div class="form-group col-sm-8">
                    <table class="table text-center">
                      <tr><td><strong>Grupo a notificar</strong></td><td><strong>Estado de la máquina</strong></td><td colspan=2><strong>Opciones</strong></td></tr>

                      <g:each var="notify" in="${notifications}">
                        <tr>
                         <g:form controller="notificationForState" action="update">
                         <td> ${notify.groupName} </td>
                         <td> ${notify.stateName} </td>
                          <td>
                            <g:link controller="notificationForState" action="edit" id="${notify.id}"><i class="fa fa-pencil"></i> Actualizar  </g:link> &nbsp;  &nbsp;
                            <g:link controller="notificationForState" action="delete" id="${notify.id}">   <i class="fa fa-close"></i> Eliminar</g:link></td>
                         </g:form>
                        </tr>
                      </g:each>

                    </table>
                  </div>
                </div>
              </fieldset>
          </div>
          <!-- END OF CREATE-ADDRESS -->
        </div>
        <!-- END OF PORTLET-BODY -->
      </div>
      <!-- END OF BLUE PORTLET -->
    </div>
    <!-- END OF PORTLET -->
  </body>
</html>
