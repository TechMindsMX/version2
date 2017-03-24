<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta name="layout" content="main" />
    <title>Modulus UNO | </title>
    <asset:javascript src="third-party/handlebars/handlebars.js"/>
    <asset:javascript src="machine/machine_edit_controller.js"/>
    <asset:stylesheet src="machine/style.css" />
  </head>
  <body>
    <!-- BEGIN PAGE TITLE -->
    <div class="page-title">
      <h1>
        <i class="fa fa-cogs fa-3x"></i>
        <g:message code="machine.edit" args="[entity]" />
        <small>
          <g:message code="machine.update" args="[entity]" />
        </small>
      </h1>
    </div>
    <!-- END OF PAGE TITLE -->
    
    <!-- BEGIN OF PORTLET -->
    <div class="portlet portlet-blue">
      <!-- BEGIN PORTLET-BODY -->
      <div class="portlet-body">
        <!-- BEGIN ROW -->
        <div class="row">
          <!-- BEGIN COL-LG-6 -->
          <div class="col-lg-6">
            <!-- BEGIN MACHINE FORM -->
            <form name="machineForm">
              <!-- BEGIN ROW -->
              <div class="row" id="transitionsDiv">
                <div class="form-group col-lg-3">
                  <label for="stateFrom">
                    ${message(code:'machine.initial.state')}
                  </label>
                  <g:select name="stateFrom" class="form-control" from="${states}" optionKey="name" optionValue="name" noSelection="['':'-SELECCIONAR-']"/>
                </div>

                <div class="form-group col-lg-4">
                  <label for="">
                    ${message(code:'machine.action')}
                  </label>   
                  <input type="text" name="action" class="form-control"/>
                </div>

                <div class="form-group col-lg-3">
                  <label for="stateTo">
                    ${message(code:'machine.state.to')}
                  </label>
                  <input type="text" name="stateTo" class="form-control">
                </div>
                
                <div class="form-group col-sm-2">
                  <g:submitButton name="create" class="save btn btn-primary" value="Agregar" />
                </div>
              </div>
              <!-- END OF ROW -->
            </form>
            <!-- END OF MACHINE FORM -->
            <!-- BEGIN ROW -->
            <div class="row">
              <div id="transitionsTableContainer" class="col-lg-12">

              </div>
            </div>
            <!-- END OF ROW -->
          </div>
          <!-- END OF COL-LG-6 -->

          <div id="graphDiv" class="col-lg-6">
            <svg id="svg" width="100%"></svg>
          </div>
        </div>
        <!-- END OF ROW -->
      </div>
      <!-- END PORTLET-BODY -->
    </div>
    <!-- END OF PORTLET -->
  </body>
</html>
