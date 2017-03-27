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
        Notificación #${notification.id}
        <small>Actualización</small>
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
              <fieldset class="form">
               <g:form controller="notificationForState" action="update">
                <div class="row">
                  <div class="col-md-2">
                    <g:hiddenField name="notification" value="${notification.id}"/>
                    <label class="text-right">Grupo de usuarios a notificar</label>
                  </div>
                  <div class="col-md-2">
                    <g:select optionKey="id" optionValue="name" name="groupId" value="${notification.groupNotification}" from="${groups}" />
                  </div>
                </div>

                <div class="row">
                  <div class="col-md-2 text-right">
                    <br><label>Estado de la Máquina</label>
                  </div>
                  <div class="col-md-2">
                    <br><g:select optionKey="id" optionValue="name" name="state" value="${notification.stateMachine}" from="${states}" />
                  </div>
                </div>

                <div class="row">
                  <div class="col-md-2"></div>
                  <div class="col-md-2 center-block">
                    <br><g:submitButton name="update" class="save btn btn-default" value="Actualizar Notificación" />
                  </div>
                </div>
               </g:form>
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
