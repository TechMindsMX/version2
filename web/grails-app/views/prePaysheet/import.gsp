<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'prePaysheet.label', default: 'PrePaysheet')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-credit-card-alt fa-3x"></i>
        Importar Pre-Nómina
        <small>${company}</small>
      </h1>
    </div>
    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div id="horizontalFormExample" class="panel-collapse collapse in">

          <div class="portlet-heading">
            <div class="row">
              <g:form name="downloadLayout" action="downloadLayout" method="POST">
                <div class="col-md-12 text-right">
                  <input class="btn btn-default" type="submit" value="Layout"/>
                </div>
              </g:form>
            </div>
          </div>
          <div class="portlet-body">
            <div class="row">
              <div class="col-md-12">
                <g:form name="uploadXlsPrePaysheet" action="uploadXlsPrePaysheet" method="POST" enctype="multipart/form-data">
                  <div class="form-group">
                    <label>Archivo XLS:</label>
                    <input type="file" name="prePaysheetXlsFile" class="form-control" required="required" accept="application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
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
        </div>
      </div>
    </div>
  </body>
</html>
