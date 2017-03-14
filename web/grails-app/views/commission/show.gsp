<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'commission.label', default: 'Commission')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-info-circle fa-3x"></i>
        <g:message code="commission.show.label" />
        <small>${commission.company}</small>
      </h1>
    </div>
    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div class="portlet-heading">
          <div class="portlet-title">
            <br />
            <br />
          </div>
          <div class="clearfix"></div>
        </div>
        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
            <g:if test="${flash.message}">
            <div class="alert alert-danger" role="alert">${flash.message}</div>
            </g:if>
            <div class="form-group">
              <label><g:message code="commission.fee.label"/>:</label>
              ${commission.fee}
            </div>
            <div class="form-group">
              <label><g:message code="commission.percentage.label"/>:</label>
              ${commission.percentage}
            </div>
            <div class="form-group">
              <label><g:message code="commission.type.label"/>:</label>
              ${commission.type}
            </div>
            <div class="form-group">
              <label><g:message code="commission.company"/>:</label>
              ${commission.company}
            </div>
            <g:form resource="${this.commission}" method="DELETE">
            <fieldset class="buttons">
              <g:link class="edit btn btn-default" action="edit" params='[companyId:commission.company.id]' resource="${this.commission}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
              <input class="delete btn btn-default" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
            </fieldset>
              </g:form>
          </div>
        </div>
      </div>
    </div>
    <g:link class="list btn btn-default" action="index" params='[companyId:commission.company.id]'><g:message code="commission.list.label" args="[entityName]" /></g:link>
  </body>
</html>
