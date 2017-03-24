<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="UTF-8">
    <meta name="layout" content="main" />
    <title>Modulus UNO | Máquina</title>
    <asset:javascript src="machine/machine_show_controller.js" />
    <asset:stylesheet src="machine/style.css" />
  </head>
  <body>
    <!-- BEGIN PAGE-TITLE -->    
    <div class="page-title">
      <h1>
        <i class="fa fa-cog fa-3x"></i>
        <g:message code="machine.show" args="[entity]" />
        <small>
          <g:message code="machine.detail" args="[entity]" />
        </small>
      </h1>
    </div>
    <!-- END OF PAGE-TITLE -->

    <!-- BEGIN OF PORTLET -->
    <div class="portlet portlet-blue">
      <!-- BEGIN PORTLET-BODY -->
      <div class="portlet-body">
        <!-- BEGIN ROW -->
        <div class="row">
          <!-- BEGIN COL-LG-6 -->
          <div class="col-lg-6">
            <g:if test="${transitionList}">
              <table class="table">
                <thead>
                  <tr>
                    <th>Estado Inicial</th>
                    <th>Acción</th>
                    <th>Estado Final</th>
                  </tr>
                </thead>
                <g:each var="transition" in="${transitionList}">
                  <g:each var="action" in="${transition.actions}">
                    <tr>
                      <td>${transition.stateFrom.name}</td>
                      <td>${action}</td>
                      <td>${transition.stateTo.name}</td>
                    </tr>
                  </g:each>
                </g:each>
              </table>
            </g:if>

            <input type="hidden" id="machineShowURL" value="${createLink(controller:'machine',action:'show')}"/>
            <input type="hidden" id="machineUuid" value="${transitionList?.getAt(0)?.machine?.uuid}" />
          </div>

          <!-- END OF COL-LG-6 -->
          <div class="col-lg-6 graphDiv">
            <svg id="svg" width="100%"></svg>
          </div>
          <!-- BEGIN COL-LG-6 -->
          
          <!-- END OF COL-LG-6 -->
        </div>
        <!-- END OF ROW -->
      </div>
      <!-- END OF PORTLET-BODY -->
    </div>
    <!-- END OF PORTLET -->

  </body>
</html>
