<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title>Modulus UNO | Notificación por Estado </title>
    <asset:javascript src="notificationForState/notification_for_state_controller.js" />
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
              <div class="col-lg-12">
                <h4>Selecciona una máquina de estados.</h4> 
              </div>
            </div>
            <g:form name="notificationForStateForm" >
              <!-- BEGIN CENTER-BLOCK -->
              <div class="row center-block">
                <div class="col-md-4 form-group">
                  <label>Máquina de estados para:</label>
                  <g:select name="entity" class="form-control" from="${entities}" optionKey="key" optionValue="value" noSelection="${['':'Seleccionar']}" ></g:select>
                </div>

                  <div class="col-md-4 form-group">
                    <label>Empresa:</label>
                    <g:select name="company" class="form-control" from="${companies}" optionKey="id" optionValue="bussinessName" noSelection="${['':'Seleccionar']}" ></g:select>
                  </div>

                  <div class="col-md-4 text-center" border="solid">
                    <!-- BEGIN FORM -->
                    <!--  <div class="form-group">
                        <label for="machine">Selecciona una máquina de estados</label>
                        <g:if test="${machines}">
                          <g:select optionKey="id" optionValue="id" name="machineId"  from="${machines}" class="center-block"/>
                        </g:if>
                        <g:else>
                          <small>Agrega la primera máquina de estados</small>
                        </g:else>
                      </div>
                      <g:submitButton name="create" class="btn btn-default" value="Agregar"/> -->
                    <!-- END OF FORM -->
                  </div>
                
              </div>
              <!-- END OF CENTER-BLOCK -->

              <!-- BEGIN ROW -->
              <div class="row center-block">
                <div class="col-lg-12">
                  <g:submitButton name="search" class="btn btn-default" value="Buscar" />
                </div>
              </div>
              <!-- END ROW -->
            </g:form>
            <input type="hidden" value="${createLink(action:'list')}" id="actionListURL" />

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
