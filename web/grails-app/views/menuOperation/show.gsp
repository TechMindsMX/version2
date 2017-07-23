<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'menuOperation.label', default: 'MenuOperation')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>

    <div class="row">
      <!-- BEGIN PAGE TITLE -->
      <div class="page-title">
        <h1>
          <i class="fa fa-info-circle fa-3x"></i>
          <g:message code="menuOperation.show"/>
        </h1>
      </div>
      <!-- END OF PAGE TITLE -->
    </div>

    <g:if test="${flash.message}">
    <div class="alert alert-info" role="alert">
        <div class="message" role="status">${flash.message}</div>
    </div>
    </g:if>

    <div class="row">

      <div class="col-md-12">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h4>Datos de la operación de menú</h4>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <ul class="property-list menuOperation">
                <f:display bean="menuOperation" property="name" wrapper="show" />
                <f:display bean="menuOperation" property="internalUrl" wrapper="show" />
              </ul>
              <div class="property-value" aria-labelledby="menuOperation-label">
                <g:link class="edit btn btn-primary" action="edit" resource="${this.menuOperation}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                <g:link class="edit btn btn-default" action="index" resource="${this.menuOperation}"><g:message code="default.button.index.label" default="List all" /></g:link>
                <input class="delete btn btn-danger" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
              </div>
            </div>
          </div>
        </div>
      </div>

  </body>
</html>
