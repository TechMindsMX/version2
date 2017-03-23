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
        <i class="fa fa-user fa-3x"></i>
        Grupos de Usuarios a Notificar
        <small>Listado de registros</small>
      </h1>

    </div>

    <div class="row">

      <div class="portlet portlet-blue">
        <div class="portlet-heading">
        <!-- BEGIN PORTLET-WIDGETS -->
        <div class="portlet-widgets">
      </div>
      <!-- END OF PORTLET-WIDGETS -->
      <div class="clearfix"></div>
      </div>

      <!-- BEGIN BLUE-PORTLET -->
      <div class="panel-collapse collapse in">
        <!-- BEGIN PORTLET-BODY -->
        <div class="portlet-body">
          <div class="row">

                <div class="row text-center">
                  <label>Grupos Registrados</label>
                </div>

                <div class="row">
                  <br><div class="form-group col-md-4"></div>
                  <div class="form-group col-md-4">
                    <table class="table text-center">
                      <tr><td><strong>Nombre del Grupo</strong></td><td><strong>Opciones</strong></td></tr>
                      <g:each var="group" in="${groups}">
                      <tr><td>${group.name}</td>
                        <td><g:link controller="groupNotification" action="edit" id="${group.id}"><i class="fa fa-pencil"></i>&nbsp; Actualizar</g:link>
                        &nbsp; &nbsp;<g:link controller="groupNotification" action="delete" id="${group.id}"><i class="fa fa-close"></i>&nbsp; Eliminar</g:link> </td>
                      </g:each>
                    </table>
                  </div>
                </div>

                <div class="row text-center">
                  <g:link controller="notificationForState" action="index" class="text-center save btn btn-default">Regresar</g:link></div>
                </div>


            </div>
           </div>

        </div>
      </div>
      </div>
  </body>
</html>
