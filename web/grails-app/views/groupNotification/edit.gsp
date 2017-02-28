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
        Grupo a editar: ${group.name}
        <small>Emailer Id: ${group.notificationId}</small>
        <g:each var="user" in="${group.users}">
        ${user.username}<br>
        </g:each>
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
          <g:form controller="groupNotification" action="update">
            <div class="form-group">
              <label for="">Nombre para actualizar grupo</label>
                <g:hiddenField name="idGroup" value="${group.id}" />
                <div class="input-group col-md-4">
                  <input type="text" class="form-control" id="" name="nameGroup" placeholder=""/>
                </div>
            </div>

            <div class="form-group">
              <label for="">Emailer para actualizar  ---</label>
              <div class="input-group col-md-4">
                <g:select optionKey="id" optionValue="subject" name="notificationId" from="${emailer}" />
              </div>
            </div>

            <div class="form-group">
              <label for="">Usuarios que serán notificados</label>
              <div class="input-group col-md-4">
                <g:each var="user" in="${users}">
                <g:checkBox name="userList" value="${user.id}" checked="false" />
                ${user.username}
                <br>
                </g:each>
              </div>
            </div>

            <div class="form-group">
              <div class="input-group col-md-4">
                <button type="submit" class="save btn btn-default">Actualizar Grupo</button>
              </div>
            </div>
          </g:form>
        </div>
      </div>
    </div>
  </body>
</html>
