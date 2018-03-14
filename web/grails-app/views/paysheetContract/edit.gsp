<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'paysheetContract.label', default: 'PaysheetContract')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-hospital-o fa-3x"></i>
        Contrato de Nómina
      </h1>
    </div>
    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>

              <g:hasErrors bean="${paysheetContract}">
                <ul class="error alert alert-danger" role="alert">
                  <g:eachError bean="${paysheetContract}" var="error">
                  <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message code="paysheetContract.error.${error.field}" args="${[error.defaultMessage.replace('{0}','')]}"/></li>
                    </g:eachError>
                </ul>
              </g:hasErrors>

              <g:form action="update">
                <g:hiddenField name="id" value="${paysheetContract.id}"/>
                <fieldset class="form">
                  <g:render template="form" bean="${paysheetContract}"/>
                </fieldset>
                <br />
                <div class="row">
                  <div class="col-md-6">
                    <g:submitButton name="update" class="btn btn-primary" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                  </div>
                  <div class="col-md-6 text-right">
                    <g:link class="btn btn-default" action="list">Cancelar</g:link>
                  </div>
                </div>
              </g:form>
          </div>
        </div>
      </div>
    </div>
    <asset:javascript src="paysheetContract/create.js"/>
  </body>
</html>
