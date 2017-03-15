<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'telephone.label', default: 'Telephone')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
      <div class="page-title">
      <h1>
        <i class="fa fa-phone fa-3x"></i>
        <g:message code="telephone.company.new"/>
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
                  <div id="create-telephone" class="content scaffold-create" role="main">
                    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
                    <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <g:hasErrors bean="${this.telephone}">
                    <ul class="errors" role="alert">
                      <g:eachError bean="${this.telephone}" var="error">
                      <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                        </g:eachError>
                    </ul>
                      </g:hasErrors>
                      <g:form action="save">
                      <input type="hidden" name="userId" value="${userId}"/>
                      <input type="hidden" name="company" value="${company}"/>
                      <fieldset class="form">
                        <f:all bean="telephone"/>
                      </fieldset>
                      <fieldset class="buttons">
                        <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                      </fieldset>
                        </g:form>
                  </div>
                </div>
             </div>
          </div>
        </div>
      </div>
    </body>
</html>
