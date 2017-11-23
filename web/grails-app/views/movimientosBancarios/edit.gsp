<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'movimientosBancarios.label', default: 'MovimientosBancarios')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
      <div class="page-title">
        <h1>
          <i class="icon-retiro fa-3x"></i>
          Cuenta ${movimientosBancarios.cuenta}
          <small>Editar Movimiento Bancario</small>
        </h1>
      </div>
      <div class="col-lg-12">
        <div class="portlet portlet-blue">
          <div class="portlet-heading">
            <div class="portlet-title">
              <br /><br />
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">





        <div id="edit-movimientosBancarios" class="content scaffold-edit" role="main">
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.movimientosBancarios}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.movimientosBancarios}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.movimientosBancarios}" method="PUT">
                <g:hiddenField name="id" value="${this.movimientosBancarios?.id}" />
                <g:hiddenField name="version" value="${this.movimientosBancarios?.version}" />
                <fieldset class="form">
                	<g:render template="form"/>
                </fieldset>
                <fieldset class="buttons text-right">
                  <g:link class="btn btn-primary" action="show" id="${movimientosBancarios.cuenta.id}">Cancelar</g:link>
                  <input class="btn btn-primary" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>

            </div>
          </div>
        </div>
      </div>
      <asset:javascript src="movimientosBancarios/create.js"/>
    </body>
</html>
