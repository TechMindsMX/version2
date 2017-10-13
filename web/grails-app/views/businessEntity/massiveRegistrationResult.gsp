<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'businessEntity.label', default: 'BusinessEntity')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-user-plus fa-3x"></i>
        Registros / Relaciones Comerciales
        <small><g:message code="businessEntity.view.massive.result.label" /></small>
      </h1>
    </div>
    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div id="horizontalFormExample" class="panel-collapse collapse in">

          <div class="portlet-body">
            <div class="row">
              <div class="col-md-12">
                <div class="table-responsive">
                  <table class="table">
                    <tr>
                    <g:each in="${resultImport.headers}" var="header">
                      <th>${header}</th>             
                    </g:each>
                      <th>RESULTADO</th>
                    </tr>
                    <g:each in="${resultImport.information}" var="values">
                      <td>${values}</td>
                    </g:each>
                      <td>${resultImport.results}</td>                  
                  </table>
                </div>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>
  </body>
</html>
