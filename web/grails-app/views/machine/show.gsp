<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="UTF-8">
    <meta name="layout" content="main" />
    <title>Modulus UNO | Máquina</title>
  </head>
  <body>
    <!-- BEGIN PAGE-TITLE -->    
    <div class="page-title">
      <h1>
        <i class="fa fa-cog fa-3x"></i>
      </h1>
    </div>
    <!-- END OF PAGE-TITLE -->

    <!-- BEGIN OF PORTLET -->
    <div class="portlet portlet-blue">
      <!-- BEGIN PORTLET-BODY -->
      <div class="portlet-body">
        <!-- BEGIN ROW -->
        <div class="row">
          <div class="col-lg-6">
            <g:if test="${machine.transitions}">
              <table class="table">
                <thead>
                  <tr>
                    <th>Estado Inicial</th>
                    <th>Acción</th>
                    <th>Estado Final</th>
                  </tr>
                </thead>
                <g:each var="transition" in="${machine.transitions}">
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
            
          </div>
        </div>
        <!-- END OF ROW -->
      </div>
      <!-- END OF PORTLET-BODY -->
    </div>
    <!-- END OF PORTLET -->
  </body>
</html>
