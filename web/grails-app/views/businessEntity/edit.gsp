<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'businessEntity.label', default: 'BusinessEntity')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
  </head>
  <body>
      <div class="page-title">
       <h1><g:message code="businessEntity.view.edit.label" args="[entityName]" /></h1>
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
              <div class="alert alert-warning">${flash.message}</div>
              </g:if>
              <g:hasErrors bean="${this.businessEntity}">
                <ul class="errors alert alert-danger" role="alert">
                  <g:eachError bean="${this.businessEntity}" var="error">
                  <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
              </g:hasErrors>
              <g:form resource="${this.businessEntity}" method="PUT">
                <g:hiddenField name="version" value="${this.businessEntity?.version}" />
                <g:hiddenField name="id" value="${this.businessEntity?.id}" />
                <fieldset class="form">
                  <g:render template="form" bean="${businessEntity}"/>
                </fieldset>
                <br />
                <input class="save btn btn-default" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
              </g:form>
            </div>

      <div class="row">
        <div class="col-md-2 col-md-offset-10">
          <g:link class="btn btn-primary" action="show" id="${businessEntity.id}">Regresar</g:link>
        </div>
      </div>

          </div>
        </div>
      </div>
    </div>
  </body>
</html>
