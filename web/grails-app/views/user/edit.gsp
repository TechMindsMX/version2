<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-user fa-3x"></i>
				Editar Usuario
			</h1>
    </div>
    <div class="container">
      <div class="row">
          <div class="portlet portlet-blue">
            <div class="portlet-heading login-heading">
              <div class="portlet-title">
                <br />
              </div>
              <div class="clearfix"></div>
            </div>
            <div class="portlet-body">
              <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
              </g:if>
              <g:hasErrors bean="${this.user}">
                <ul class="errors" role="alert">
                  <g:eachError bean="${this.user}" var="error">
                  <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                  </g:eachError>
                </ul>
              </g:hasErrors>
              <g:form action="update" class="form">
								<g:render template="/corporate/editUser"/>
              </g:form>
            </div>
          </div>
      </div>
    </div>
  </body>
</html>
