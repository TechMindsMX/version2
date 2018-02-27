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
                  <g:render template="businessEntityImportResults"/>
                </div>
              </div>
            </div>
          </div>
          <div class="portlet-footer">

            <div class="row">
              <div class="col-md-6">
                <g:link class="btn btn-default" controller="businessEntity" action="massiveRegistration">Regresar</g:link>
              </div>
              <div class="col-md-6 text-right">
                <g:link class="btn btn-default" controller="businessEntity" action="index">Relaciones Comerciales</g:link>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
  </body>
</html>
