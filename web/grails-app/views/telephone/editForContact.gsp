<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'telephone.label', default: 'Telephone')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-phone fa-3x"></i>
        <g:message code="telephone.company.edit"/>
        <small>${company}</small>
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
          <div id="edit-telephone" class="content scaffold-edit" role="main">
            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>

            <g:hasErrors bean="${this.telephone}">
              <ul class="alert alert-danger" role="alert">
                <g:eachError bean="${this.telephone}" var="error">
                  <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
              </ul>
            </g:hasErrors>

            <g:form action="updateForContact" id="${this.telephone.id}" method="PUT">
              <g:hiddenField name="version" value="${this.telephone?.version}" />
              <fieldset class="form">
                <g:render template="contactPhoneForm"/>
                <br/>
              </fieldset>
              <fieldset class="buttons">
                <input class="save btn btn-default" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
              </fieldset>
            </g:form>
          </div>
        </div>
      </div>

    </div>
  </body>
</html>
