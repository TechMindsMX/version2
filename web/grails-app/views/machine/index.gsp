<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta name="layout" content="main" />
    <title>Modulus UNO | Lista de Máquinas</title>
    <asset:javascript src="third-party/handlebars/handlebars.js"/>
    <asset:javascript src="machine/machine_list_controller.js" />
  </head>
  <body>
    <!-- BEGIN PAGE-TITLE -->
    <div class="page-title">
      <h1>
        <i class="fa fa-cog fa-3x"></i>
        <g:message code="machine.index" args="[entityName]" />
        <small>
          <g:message code="machine.list" />
        </small>
      </h1>
    </div>
    <!-- END PAGE-TITLE -->

    <!-- BEGIN OF PORTLET -->
    <div class="portlet portlet-blue">
      <!-- BEGIN PORTLET-BODY -->
      <div class="portlet-body">
        <!-- BEGIN ROW -->
        <div class="row">
          <!-- BEGIN COL-SM-6 -->
          <div class="form-group col-lg-6">
            <label>Ver máquinas para:</label>
            <g:select name="entity" class="form-control" from="${entities}" optionKey="key" optionValue="value" noSelection="${['':'Seleccionar']}"></g:select>
          </div>
          <!-- END OF COL-SM-6 -->
        </div>
        <!-- END OF ROW -->

        <!-- BEGIN ROW -->
        <div class="row">
          <!-- BEGIN COL-SM-6 -->
          <div class="form-group col-lg-6" id="machine-list">

          </div>
          <!-- END OF COL-SM-6 -->
        </div>
        <!-- END OF ROW -->
        <g:render template="/machine/list" />
        <input type="hidden" value="${createLink(action:'list')}" id="actionListURL" />
      </div>
      <!-- END OF PORTLET-BODY -->
    </div>
    <!-- END OF PORTLET -->
  </body>
</html>
