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
          Máquina #${machine.id}
          <small>Notificaciones existentes</small>
      </h1>
    </div>


<!-- BEGIN PAGE TITLE -->
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
                  <label> Notificaciones Registradas </label><br>
                  <g:link action="create" id="${machine.id}" class="save btn btn-default">Agregar nueva notificación</g:link>
                  <br><br>
                </div>
                <div class="row">
                  <div class="form-group col-sm-2"></div>
                  <div class="form-group col-sm-8">
                    <table class="table text-center">
                      <tr><td><strong>Grupo a notificar</strong></td><td><strong>Estado de la máquina</strong></td><td colspan=2><strong>Opciones</strong></td></tr>
                      <g:each var="notification" in="${notifications}">
                        <tr>
                          <td> <g:select optionKey="id" optionValue="name" value="${notification.groupNotification}" name="notificationId" from="${groups}" disabled="${mode == 'edit'}"/></td>
                          <td> <g:select optionKey="id" optionValue="name" value="${notification.stateMachine}" name="notificationId" from="${states}" disabled="${mode == 'edit'}"/> </td>
                          <td> <g:link controller="notificationForState" action="edit" id="${notification.id}"><i class="fa fa-pencil"></i> Actualizar  </g:link> &nbsp;
                                &nbsp;<g:link controller="notificationForState" action="delete" id="${notification.id}">   <i class="fa fa-close"></i> Eliminar</g:link></td>
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
