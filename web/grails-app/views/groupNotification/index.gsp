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
                ${group.name}
                <g:link controller="groupNotification" action="edit" id="${group.id}">
                  <i class="fa fa-pencil"></i>
                </g:link>
                <g:link controller="groupNotification" action="delete"
                id="${group.id}">
                  <i class="fa fa-close"></i>
                </g:link>
              </li>
              </g:each>
            </ul>

            <br><g:link controller="notificationForState" action="index" class="save btn btn-default">Regresar</g:link></div>
            </div>
           </div>

        </div>
      </div>
      </div>
  </body>
</html>
