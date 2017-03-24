<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="UTF-8">
    <meta name="layout" content="main" />
    <title>Modulus UNO | Crear Máquina</title>
    <asset:javascript src="third-party/handlebars/handlebars.js"/>
    <asset:javascript src="machine/machine_create_controller.js"/>
    <asset:stylesheet src="third-party/EasyAutocomplete/dist/easy-autocomplete.css" />
    <asset:stylesheet src="machine/style.css" />
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
      <!-- BEGIN PORTLET-BODY -->
      <div class="portlet-body">
        <!-- BEGIN ROW -->
        <div class="row" >
          <!-- BEGIN COL-SM-6 -->
          <div class="col-sm-6">
            <!-- BEGIN MACHINE FORM -->
            <form name="machineForm">
              <!-- BEGIN ROW -->
              <div class="row" id="transitionsDiv">
                <div class="form-group col-sm-6">
                  <label for="actionFrom">
                    ${message(code:'machine.initial.state')}
                  </label>
                  <input type="text" class="form-control" name="stateFrom" maxlength="100" autocomplete="off" />
                </div>

                <div class="form-group col-sm-6">
                  <g:submitButton name="create" class="save btn btn-primary" value="Agregar" />
                </div>
              </div>
              <!-- END OF ROW -->
            </form>
            <!-- END OF MACHINE FORM -->
            <!-- BEGIN ROW -->
            <div class="row">
              <div class="col-lg-12" id="transitionsTableContainer">
                
              </div>
            </div>
            <!-- END OF ROW -->
          </div>
          <!-- END OF COL-SM-6 -->
          <div class="col-sm-6 graphDiv" >
            <svg id="svg" width="100%"></svg>
          </div>
        </div>
        <!-- END OF ROW --> 
      </div>
      <!-- END OF PORTLET-BODY -->
    </div>
    <!-- END OF PORTLET -->

    <script id="transitions-form-template" type="text/x-handlebars-template">
      <div class="form-group col-sm-3">
        <label for="actionFrom">
          ${message(code:'machine.initial.state')}
        </label>
        <select name="stateFrom" class="form-control">
          <option value selected>Seleccionar</option>
          {{#each states}}
          <option value="{{name}}">{{name}}</option>
          {{/each}}
        </select>
      </div>

      <div class="form-group col-sm-4">
        <label for="actionFrom">
          ${message(code:'machine.action')}
        </label>
        <input type="text" name="action" class="form-control" />
      </div>

      <div class="form-group col-sm-3">
        <label for="actionFrom">
          ${message(code:'machine.state.to')}
        </label>
        <input type="text" name="stateTo" class="form-control" />
      </div>
      
      <div class="form-group col-sm-2">
        <g:submitButton name="create" class="save btn btn-primary" value="Agregar" />
      </div>
    </script>

    <script id="transitionsTable" type="text/x-handlebars-template">
      {{#if transitions}}
      <g:form name="newMachineForm" controller="machine" action="save" method="POST">
        <g:render template="transitionList"  />
        <!-- BEGIN ROW -->
        <div class="row">
          <div class="col-lg-12">
            <button type="submit" class="btn btn-default">Guardar</button>
          </div>
        </div>
        <!-- END OF ROW -->
      </g:form>
      {{/if}}
    </script>
  </body>
</html>
