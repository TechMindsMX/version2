<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'dataImssEmployee.label', default: 'DataImssEmployee')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-hospital-o fa-3x"></i>
        Datos IMSS de Empleado
        <small>${businessEntity}</small>
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

              <g:hasErrors bean="${dataImssEmployee}">
                <ul class="error alert alert-danger" role="alert">
                  <g:eachError bean="${dataImssEmployee}" var="error">
                  <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message code="dataImssEmployee.error.${error.field}" args="${[error.defaultMessage.replace('{0}','')]}"/></li>
                    </g:eachError>
                </ul>
              </g:hasErrors>

              <g:form action="update">
              <g:hiddenField name="id" value="${dataImssEmployee.id}"/>
              <g:hiddenField name="version" value="${dataImssEmployee.version}"/>
              <g:hiddenField name="employee.id" value="${dataImssEmployee.employee.id}"/>
                <fieldset class="form">
                  <g:render template="form" bean="${form}"/>
                </fieldset>
                <br />
                <div class="row">
                  <div class="col-md-6">
                <sec:ifAnyGranted roles="ROLE_OPERATOR_EJECUTOR">
                  <g:submitButton name="update" class="save btn btn-default" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </sec:ifAnyGranted>
                  </div>
                  <div class="col-md-6 text-right">
                    <g:link class="btn btn-primary" controller="businessEntity" action="show" id="${businessEntity.id}">Cancelar</g:link>
                  </div>
                </div>
              </g:form>
          </div>
        </div>
      </div>
    </div>
    <asset:javascript src="dataImssEmployee/salaries.js"/>
  </body>
</html>
