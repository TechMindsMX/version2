<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'contactInformation.label', default: 'ContactInformation')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-address-book fa-3x"></i>
        <g:message code="contactInformation.company.new" default="Contact Information"/>
        <small>${company}</small>
      </h1>
    </div>

    <div class="portlet portlet-blue">
      <div class="portlet-heading">
        <div class="portlet-title">
          <br /><br />
        </div>
        <div class="portlet-widgets"></div>
        <div class="clearfix"></div>
      </div>

      <div id="bluePortlet" class="panel-collapse collapse in">
        <div class="portlet-body">
          <div id="create-contact-information" class="content scaffold-create" role="main">
            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>

            <g:hasErrors bean="${this.contactInformation}">
              <ul class="alert alert-danger" role="alert">
                <g:eachError bean="${this.contactInformation}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                  </g:eachError>
              </ul>
            </g:hasErrors>

            <g:form action="saveForCompany">
              <g:render template="form"/>
              <br/>
              <input type="submit" value="Crear" class="btn btn-default"/>
            </g:form>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
