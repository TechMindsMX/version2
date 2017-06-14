<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
  </head>
  <body>
    <!-- BEGIN PAGE TITLE -->
    <div class="page-title">
      <h1>
        <i class="fa fa-plus-circle fa-3x"></i>
        Usuarios
        <small>Editar Usuario</small>
      </h1>
    </div>
    <!--END OF PAGE TITLE -->

    <!-- BEGIN CONTAINER -->
    <div class="container-fluid">
      <div class=row">
        <div class="col-lg-12 col-md-12">
          <!-- BEGIN PORTLET -->
          <div class="portlet portlet-blue">
            <!-- BEGIN PORTLET-HEADING -->
            <div class="portlet-heading">
              <div class="clearfix"></div>
            </div>
            <!-- END OF PORTLET-HEADING -->
            <!-- BEGIN PORTLET-BODY -->
            <div class="portlet-body">
              <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
              </g:if>
              <g:hasErrors bean="${user}">
                <g:eachError bean="${user}" var="error">
                  <div class="alert alert-danger">
                    <g:message error="${error}"/>
                  </div>
                </g:eachError>
              </g:hasErrors>
              <g:form action="updateUser">
                <g:render template="editUser"/>
              </g:form>
            </div>
            <!-- END OF PORTLET-BODY -->
          </div>
          <!-- END OF PORTLET -->
        </div>
      </div>
    </div>
    <!-- END OF CONTAINER -->
  </body>
</html>
