<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'commission.label', default: 'Commission')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-info-circle fa-3x"></i>
        <g:message code="commission.edit.label" args="[entityName]" />
        <small>${company}</small>
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
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.commission}">
              <div class="alert alert-danger">
                <ul class="errors" role="alert">
                  <g:eachError bean="${this.commission}" var="error">
                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                  </g:eachError>
                </ul>
              </div>
            </g:hasErrors>
            <g:form resource="${this.commission}" method="PUT">
              <g:hiddenField name="version" value="${this.commission?.version}" />
              <fieldset class="form">
                <g:render template="form" bean="commission"/>
              </fieldset>
              <fieldset class="buttons">
                <input class="save btn btn-default" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
              </fieldset>
            </g:form>
          </div>
        </div>
      </div>
    </div>
    <g:link class="list btn btn-default" action="index" params='[companyId:commission.company.id]'><g:message code="commission.list.label" args="[entityName]" /></g:link>
  </body>
</html>
