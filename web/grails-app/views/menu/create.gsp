<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'menu.label', default: 'Menu')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>

    <!-- BEGIN PAGE TITLE -->
    <div class="page-title">
      <h1>
        <i class="fa fa-plus-circle fa-3x"></i>
        Configuración de menú
        <small><g:message code="menu.new" /></small>
      </h1>
      <ol class="breadcrumb">
        <li><i class="fa fa-caret-square-o-up"></i>Operación de Menú</li>
        <li class="active">Creación de Operación</li>
      </ol>
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

            <g:hasErrors bean="${this.menu}">
              <ul class="errors" role="alert">
                <g:eachError bean="${this.menu}" var="error">
                  <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
              </ul>
            </g:hasErrors>
            <g:form action="save">
              <fieldset class="form">
                <f:field bean="menu" property="name" wrapper="create" label="Nombre"/>
                <f:field bean="menu" property="internalUrl" wrapper="create" label="URL"/>
                <f:field bean="menu" property="parameters" wrapper="create" label="Parámetros"/>
                <f:field bean="menu" property="position" wrapper="create" label="Posición"/>
                
              </fieldset>
              <fieldset class="buttons">
                <g:submitButton name="create" class="save btn btn-default" value="${message(code: 'default.button.create.label', default: 'Create')}" />
              </fieldset>
            </g:form>
        
            <br/>    
            <g:link class="list" action="index">
              Ver todas las operaciones creadas
            </g:link>

          </div>
          <!-- END OF CONTENT -->
        </div>
        <!-- END OF PORTLET-BODY -->
      </div>
    </div>
    <!-- END OF PORTLET -->
    <g:javascript>
      
    </g:javascript>
  
  </div>
  </body>
</html>
