<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="UTF-8">
    <meta name="layout" content="main" />
    <title></title>
  </head>
  <body>
    <!-- BEGIN PAGE TITLE -->     
    <div class="page-title">
      <h1>
        <i class="fa fa-cogs fa-3x"></i>
        <g:message code="machine.create" args="[entity]" />
        <small>
          <g:message code="machine.new" args="[entity]" />
        </small>
      </h1>
    </div>
    <!-- END OF PAGE TITLE -->
    <!-- BEGIN OF PORTLET -->    
    <div class="portlet portlet-blue">
      <div class="portlet-body">
        <!-- BEGIN ROW -->
        <div class="row">
          <div class="col-sm-6">
            <!-- BEGIN ROW -->
            <div class="row">
              <div class="form-group col-sm-6">
                <label for="entity">
                  ${message(code:'machine.action.from')}
                </label>
                <g:select name="actionFrom" class="form-control" from="${actions}" optionKey="id" optionValue="name" noSelection="${['':'Seleccionar']}"></g:select>
              </div>

              <div class="form-group col-sm-6">
                <label for="entity">
                  ${message(code:'machine.action.to')}
                </label>
                <g:select name="actionTo" class="form-control" from="${actions}" optionKey="id" optionValue="name" noSelection="${['':'Seleccionar']}"></g:select>
              </div>
            </div>
            <!-- END OF ROW -->
            <!-- BEGIN ROW -->
            <div class="row">
              <div class="col-sm-12">
                <g:submitButton name="create" class="save btn btn-default" value="Agregar" />
              </div>
            </div>
            <!-- END OF ROW -->
          </div>
          <div class="col-sm-6">

          </div>
        </div>
      </div>
    </div>
    <!-- END OF PORTLET -->
  </body>
</html>
