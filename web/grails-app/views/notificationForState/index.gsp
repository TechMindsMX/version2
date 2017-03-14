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
        <i class="fa fa-envelope-o fa-3x"></i>
          Gestión de Notificaciones
      </h1>
    </div>

    <!-- BEGIN PORTLET -->
    <div class="portlet portlet-blue">
      <!-- BEGIN BLUE-PORTLET -->
      <div class="panel-collapse collapse in">
        <!-- BEGIN PORTLET-BODY -->
        <div class="portlet-body">
          <!-- BEGIN CREATE-ADDRESS -->
          <div class="content scaffold-create">
            <g:form action="show" method="POST">
              <fieldset class="form">
                <div class="row">
                  <div class="form-group col-lg-12 text-center">
                    <label>
                      Selecciona una máquina de estados
                    </label>
                  </div>
                </div>
                <div class="row center">
                  <div class="form-group col-xs-6 col-sm-4"></div>
                  <div class="form-group col-xs-6 col-sm-4">
                    <g:select optionKey="id" optionValue="id"
                      name="machineId"  from="${machines}" class="center-block"/>
                    <br>
                    <g:submitButton name="create" class="save btn btn-default center-block" value="Gestionar Notificaciones" />
                  </div>
                </div>
              </fieldset>
            </g:form>
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
