<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'company.label', default: 'Company')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>
    <!-- BEGIN PAGE TITLE -->
    <div class="page-title">
      <h1>
        <i class="fa fa-plus-circle fa-3x"></i>
        Registrar Nueva Empresa
        <small><g:message code="company.new" /></small>
      </h1>
    </div>
    <!-- END OF PAGE TITLE -->
    <!-- BEGIN PORTLET -->
    <div class="portlet portlet-blue">
      <div class="panel-collapse collapse in">
        <!-- BEGIN PORTLET-BODY -->
        <div class="portlet-body">
          <!-- BEGIN CONTENT -->
          <div class="content scaffold-create">
            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${company}">
              <ul class="errors alert alert-danger alert-dismissable error-list" role="alert">
                <g:eachError bean="${company}" var="error">
                  <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
              </ul>
            </g:hasErrors>
            <g:form action="saveCompany">
              <fieldset class="form">
                <g:render template="/company/form" bean="${company}" />
              </fieldset>
              <g:submitButton name="create" class="save btn btn-default" value="${message(code: 'default.button.create.label', default: 'Create')}" />
            </g:form>
          </div>
          <!-- END OF CONTENT -->
        </div>
        <!-- END OF PORTLET-BODY -->
      </div>
    </div>
    <!-- END OF PORTLET -->
    <g:javascript>
      $(function(){
        $("#rfc").blur(function(){
          $(this).val($(this).val().toUpperCase());
        });
      });
    </g:javascript>
  </body>
</html>
