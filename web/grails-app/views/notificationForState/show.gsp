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
                <div class="row">
                  <div class="form-group col-lg-12">
                    <g:link action="create" id="${machine.id}" class="save btn btn-default">Agregar nueva notificación</g:link>
                  </div>
                </div>
                <div class="row center">
                  <div class="form-group col-xs-6 col-sm-4"></div>
                  <div class="form-group col-xs-6 col-sm-4">
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
