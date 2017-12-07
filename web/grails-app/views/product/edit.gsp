<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
        <title><g:message code="product.view.edit.label" args="[entityName]" /></title>
    </head>
    <body>
      <div class="page-title">
        <h1>
          <i class="fa fa-shopping-basket fa-3x"></i>
          <g:message code="product.view.edit.label" args="[entityName]" />
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
              <g:hasErrors bean="${this.product}">
              <ul class="errors" role="alert">
                <g:eachError bean="${this.product}" var="error">
                  <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
              </ul>
              </g:hasErrors>
              <g:form resource="${this.product}" method="PUT">
                <g:hiddenField name="version" value="${this.product?.version}" />
                <fieldset class="form">
                  <g:render template="form" bean="${product}" />
                </fieldset>
                <input class="save btn btn-default" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
              </g:form>
            </div>
          </div>
        </div>
      </div>
        <g:link class="list btn btn-default" action="index"><g:message code="product.view.list.label" args="[entityName]" /></g:link>
        <g:link class="create btn btn-default" action="create"><g:message code="product.view.create.label" args="[entityName]" /></g:link>
    </body>
</html>
