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
          <div class="form-group">
           <label for="">Grupos Registrados</label>
           <ul>
              <g:each var="group" in="${groups}">
              <li>
                <g:link controller="groupNotification" action="edit" id="${group.id}"> Grupo: ${group.name}
                </g:link>
                <br>
                <small>: ${group.notificationId}</small><br></li>
                <g:link controller="groupNotification" action="delete"
                id="${group.id}"> Eliminar
                </g:link>

              </g:each>
            </ul>
            </div>
           </div>

        </div>
      </div>
      </div>
  </body>
</html>
