<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
      <div class="page-title">
        <h1>
        <i class="fa fa-shopping-basket fa-3x"></i>
        Registros / Agregar producto o servicio
        <small><g:message code="product.view.create.label" args="[entityName]" /></small>
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
              <div id="create-address" class="content scaffold-create" role="main">
                <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
                </g:if>
                <g:hasErrors bean="${this.product}">
                <ul class="errors alert alert-danger alert-dismissable" role="alert">
                    <g:eachError bean="${this.product}" var="error">
                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
                </g:hasErrors>
                <g:form action="save">
                  <fieldset class="form">
                    <g:render template="form" bean="${product}" />
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
        </div>
      </div>
      <g:link class="list btn btn-primary" action="index">
        <g:message code="product.view.list.label" args="[entityName]" />
      </g:link>
    </body>
</html>
