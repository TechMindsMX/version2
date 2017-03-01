<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'address.label', default: 'Address')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-newspaper-o fa-3x"></i>
        <g:message code="address.create.label"/>
        <small>${businessEntity ?: ""}</small>
      </h1>
    </div>
    <div id="create-address" class="content scaffold-create" role="main">
      <div class="portlet portlet-blue">
        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
            <div id="create-address" class="content scaffold-create" role="main">
              <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
              </g:if>
              <g:hasErrors bean="${this.address}">
              <ul class="errors alert alert-danger alert-dismissable" role="alert">
                <g:eachError bean="${this.address}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
              </ul>
              </g:hasErrors>
              <g:form action="save" name="formCreateAddress" >
                <fieldset class="form">
                  <g:render template="form" bean="${address}" />
                </fieldset>
                <br />
                <div class="text-right">
                  <input type="button" id="submitFormAddress" name="create" class="save btn btn-default" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </div>
              </g:form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
