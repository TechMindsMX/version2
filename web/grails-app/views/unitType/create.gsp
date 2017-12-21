<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'unitType.label', default: 'UnitType')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
      <div class="page-title">
        <h1>
        <i class="fa fa-sitemap fa-3x"></i>
        Tipos de Unidad
        <small><g:message code="unitType.view.create.label" args="[entityName]" /></small>
        </h1>
      </div>
      <br />
      <div id="create-address" class="content scaffold-create" role="main">
        <div class="portlet portlet-blue">
          <div class="portlet-heading">
            <div class="portlet-title">
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="horizontalFormExample" class="panel-collapse collapse in">
            <div class="portlet-body">
              <div id="create-address" class="content scaffold-create" role="main">
                <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
                </g:if>
                <g:hasErrors bean="${this.unitType}">
                <ul class="errors alert alert-danger alert-dismissable" role="alert">
                    <g:eachError bean="${this.unitType}" var="error">
                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
                </g:hasErrors>
                <g:form action="save">
                  <fieldset class="form">
                    <g:render template="form" bean="${unitType}" />
                  </fieldset>
                  <div class="text-right">
                    <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR">
                      <g:submitButton name="create" class="save btn btn-default" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                    </sec:ifAnyGranted>
                  </div>
                </g:form>
              </div>
            </div>
          </div>

          <div class="portlet-footer">
            <div class="row">
              <div class="col-md-12">
                <g:link class="list btn btn-primary" action="index">
                  <g:message code="unitType.view.list.label" args="[entityName]" />
                </g:link>
              </div>
            </div>
          </div>

        </div>
      </div>
    </body>
</html>
