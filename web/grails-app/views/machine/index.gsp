<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta name="layout" content="main" />
    <title></title>
  </head>
  <body>
    <!-- BEGIN PAGE TITLE -->
    <div class="page-title">
      <h1>
        <i class="fa fa-cog fa-3x"></i>
        <g:message code="machine.create" args="[entityName]" />
        <small>
          <g:message code="machine.new" />
        </small>
      </h1>
    </div>
    <!-- END OF PAGE TITLE -->
    <!-- BEGIN PORTLET -->
    <div class="portlet portlet-blue">
      <!-- BEGIN BLUE-PORTLET -->
      <div class="panel-collapse collapse in">
        <!-- BEGIN PORTLET-BODY -->
        <div class="portlet-body">
          <!-- BEGIN CREATE-ADDRESS -->
          <div class="content scaffold-create">
            <form action="create">
              <fieldset class="form">
                <div class="form-group col-lg-3">
                  <label for="entity">
                    ${message(code:'machine.for.label')}
                  </label>
                  <g:select name="entity" class="form-control" from="${entities}" optionKey="key" optionValue="value" noSelection="${['':'Seleccionar']}"></g:select>
                </div>
                <div class="form-group col-lg-3">
                  
                </div>
              </fieldset>
            </form>
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
