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
          Notificaciones existentes
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
                  <label> NOTIFICACIONES</label><br><br>
                </div>

                <div class="row center-block">
                  <div class="col-md-4"></div>
                  <div class="col-md-4 text-center" border="solid">
                    <g:form action="create" class="form-inline" method="POST">
                    <div class="form-group">
                      <label for="machine">Máquina</label>
                      <g:select optionKey="id" optionValue="id" name="machineId"  from="${machines}" class="center-block"/>
                    </div>
                    <g:submitButton name="create" class="btn btn-default" value="Nueva Notificación"/>
                    <br>
                    </g:form>
                  </div>
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
