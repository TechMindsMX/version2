<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'company.label', default: 'Company')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
      <div class="page-title">
        <h1>
        <i class="fa fa-plus-circle fa-3x"></i>
        Registrar Nueva Empresa
        <small><g:message code="company.new" /></small>
        </h1>
      </div>
        <div class="portlet portlet-blue">
            <div class="portlet-heading">
                <div class="portlet-title">
                  <br /><br />
                </div>
                <div class="portlet-widgets">
                </div>
                <div class="clearfix"></div>
            </div>
            <div id="bluePortlet" class="panel-collapse collapse in">
                <div class="portlet-body">
                  <div id="create-address" class="content scaffold-create" role="main">
                    <g:if test="${flash.message}">
                      <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <g:hasErrors bean="${this.company}">
                      <ul class="errors alert alert-danger alert-dismissable" role="alert">
                        <g:eachError bean="${this.company}" var="error">
                          <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                        </g:eachError>
                      </ul>
                    </g:hasErrors>
                    <g:form action="save">
                      <fieldset class="form">
                        <g:render template="form" bean="${company}" />
                      </fieldset>
                      <g:submitButton name="create" class="save btn btn-default" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                    </g:form>
                  </div>
                </div>
            </div>
        </div>
    <g:javascript>
    $(function(){
      $("#rfc").blur(function(){
        $(this).val($(this).val().toUpperCase());
      });
    });
  </g:javascript>
      </body>
</html>
