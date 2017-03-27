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
          Registro de Nueva Notificación
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
            <div class="row center-block">
                  <div class="col-md-4"></div>
                  <div class="col-md-4 text-center" border="solid">
                    <g:form action="create" method="POST">
                    <div class="form-group">
                      <label for="machine">Selecciona una máquina de estados</label>

                      <g:if test="${machines}">
                      <g:select optionKey="id" optionValue="id" name="machineId"  from="${machines}" class="center-block"/>
                      </g:if>
                      <g:else>
                      <small>Agrega la primera máquina de estados</small>
                      </g:else>
                    </div>
                    <g:submitButton name="create" class="btn btn-default" value="Agregar"/>
                    </g:form>
                  </div>
                </div>
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
