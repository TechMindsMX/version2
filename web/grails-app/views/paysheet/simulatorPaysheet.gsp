<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa fa-book fa-3x"></i>
        Simulador de nomina
      </h1>
    </div>
    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-heading">
            <div class="row">
              <g:link action="downloadLayout" class="btn btn-primary" id="1">Layout</g:link> 
            </div>
          </div>
          <div class="portlet-body">
            <div class="row">
              <div class="col-md-12">
                <g:form name="uploadLayoutForSimulator" action="uploadLayoutForSimulator" method="POST" enctype="multipart/form-data">
                  <div class="form-group">
                    <label>Archivo XLS de registros:</label>
                    <input type="file" name="layoutSimulator" class="file" required="required" accept="application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
                  </div>
                  <div class="row">
                    <div class="col-md-12 text-right">
                      <input class="btn btn-primary" type="submit" value="Subir"/>
                    </div>
                  </div>
                </g:form>
              </div>
            </div>
          </div>
        </div>
        <g:if test="${resultList}">
          <div class="row">
              <g:render template="simulatorPaysheetData"></g:render>
          </div>
          <div class="portlet-footer">
            <div align="right">
              <g:link action="exportSimulatedPaysheet" class="btn btn-primary text-right">Exportar XLS</g:link> 
            </div>
          </div>          
        </g:if>
      </div>
    </div>
    <asset:javascript src="businessEntity/entityType.js"/>
  </body>
</html>

