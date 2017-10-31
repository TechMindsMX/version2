<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'movimientosBancarios.label', default: 'MovimientosBancarios')}" />
    </head>
    <body>
      <div class="page-title">
        <h1>
          <i class="icon-retiro fa-3x"></i>
          Cuenta ${movimientosBancarios.cuenta}
          <small>Crear Movimiento Bancario</small>
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

        <div id="create-movimientosBancarios" class="content scaffold-create" role="main">
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
            <g:form action="save">
                <fieldset class="form">
                 <g:render template="form"  />
                </fieldset>
                    <g:submitButton name="create" class="btn  btn-green" value="${message(code: 'default.button.create.label', default: 'Create')}" />
            </g:form>
        </div>

            </div>
          </div>
        </div>
      </div>

        <asset:javascript src="movimientosBancarios/create.js"/>
    </body>
</html>
