<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'commission.label', default: 'Commission')}" />
    <title><g:message code="commission.create.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-info-circle fa-3x"></i>
        <g:message code="commission.create.label" args="[entityName]" />
        <small>${company}</small>
      </h1>
    </div>
    <br />
    <div id="create-address" class="content scaffold-create" role="main">
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

            <div id="create-commission" class="content scaffold-create" role="main">
              <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
              </g:if>
              <g:if test="${commandErrors}">
              <g:each in="${commandErrors}" var="error">
              <div class="alert alert-danger" role="alert">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error.getFieldError()}"/></li>
              </div>
                  </g:each>
              </g:if>
              <g:hasErrors bean="${this.commission}">
              <ul class="errors" role="alert">
                <g:eachError bean="${this.commission}" var="error">
                <div class="alert alert-danger" role="alert">
                  <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </div>
                    </g:eachError>
              </ul>
                </g:hasErrors>
                <g:form action="save">
                <fieldset class="form">
                  <g:render template="form" bean="${commission}" />
                </fieldset>
                <fieldset class="buttons">
                  <g:submitButton name="create" class="save btn btn-default" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
                  </g:form>
            </div>
          </div>
        </div>
      </div>
    </div>
    <g:link class="list btn btn-default" action="index" params="[companyId:"${company.id}"]">
    <g:message code="commission.list.label" args="[entityName]" />
    </g:link>

  </body>
</html>

