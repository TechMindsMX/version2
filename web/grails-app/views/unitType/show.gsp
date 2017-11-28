<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'unitType.label', default: 'UnitType')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
      <div class="page-title">
        <h1>
          <i class="fa fa-sitemap fa-3x"></i>
          Tipos de Unidad
        </h1>
      </div>
      <div id="edit-address" class="content scaffold-edit" role="main">
        <div class="portlet portlet-blue">
          <div class="portlet-heading">
            <div class="portlet-title">
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="horizontalFormExample" class="panel-collapse collapse in">
            <div class="portlet-body">
              <g:if test="${flash.message}">
                <div class="alert alert-info" role="alert">${flash.message}</div>
              </g:if>

              <dl class="dl-horizontal">
                <dt>CLAVE:</dt>
                <dd>${unitType.unitKey}</dd>
                <dt>NOMBRE:</dt>
                <dd>${unitType.name}</dd>
                <dt>SÍMBOLO:</dt>
                <dd>${unitType.symbol}</dd>
              </dl>
            
            </div>

            <div class="portlet-footer">
              <div class="row">
                <div class="col-md-12 text-right">
                  <g:link class="list btn btn-default" action="index"><g:message code="unitType.view.list.label" /></g:link>
                  <g:link class="create btn btn-default" action="edit" id="${unitType.id}"><g:message code="unitType.view.edit.label"/></g:link>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>
    </body>
</html>
