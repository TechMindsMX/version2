<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main" />
      <g:set var="entityName" value="${message(code: 'bankAccount.label', default: 'BankAccount')}" />
      <title><g:message code="default.edit.label" args="[entityName]" /></title>
      <asset:javascript src="clabe.js" />
    </head>
    <body>
      <div class="page-title">
        <h1>
          <i class="fa fa-credit-card fa-3x"></i>
          Cuenta Bancaria
          <g:if test="${params.companyBankAccount}">
            <small>${companyInfo.companyInfo()}</small>
          </g:if>
          <g:else>
            <small>${params.businessEntityInfo}</small>
          </g:else>
        </h1>
      </div>
      <div id="create-bankAccount" class="content scaffold-create" role="main">
        <div class="portlet portlet-blue">
          <div id="horizontalFormExample" class="panel-collapse collapse in">
            <div class="portlet-body">
              <g:if test="${flash.message}">
                <div class="alert alert-danger" role="alert">${flash.message}</div>
              </g:if>
              <g:hasErrors bean="${this.bankAccount}">
                <div class="alert alert-danger" role="alert">
                <ul class="errors" role="alert">
                  <g:eachError bean="${this.bankAccount}" var="error">
                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                  </g:eachError>
                </ul>
                </div>
              </g:hasErrors>

            <g:if test="${error}">
              <div class="alert alert-danger">
                ${error}
              </div>
            </g:if>


              <g:form resource="${this.bankAccount}" method="PUT" class="form-horizontal" role="form">
                <input type="hidden" name="businessEntityInfo" value="${params.businessEntityInfo}"/>
                <g:hiddenField name="id" value="${this.bankAccount?.id}" />
                <g:hiddenField name="version" value="${this.bankAccount?.version}" />

              <g:if test="${relation == 'CLIENTE'}">
                <g:render template="formClient"/>
              </g:if><g:else>
                <g:render template="form"/>
              </g:else>

              <div class="form-group ">
                <div class="col-sm-5">&nbsp;</div>
                  <div class="col-sm-4">
                    <fieldset class="buttons">
                      <input class="save btn btn-primary" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                    </fieldset>
                  </div>
                </div>
              </g:form>

              <div class="text-right">
                <g:if test="${params.companyBankAccount}">
                  <g:link class="btn btn-default" controller="company" action="show" id="${session.company}">Regresar</g:link>
                </g:if>
                <g:else>
                  <g:link controller="businessEntity" action="show" id="${params.businessEntity}" params="[company:session.company]" class="btn btn-default">Regresar</g:link>
                </g:else>
              </div>

            </div>
          </div>
        </div>
      </div>
    </body>
</html>
