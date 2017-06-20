<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'accountStatemenBankAccount.label', default: 'AccountStatementBankAccount')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-file-text fa-3x"></i>
        ${accountStatementBankAccount.bankAccount}<small>Nuevo Estado de Cuenta</small>
      </h1>
    </div>


    <div id="create-accountStatemenBankAccount" class="content scaffold-create" role="main">
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
              <div class="alert alert-danger" role="alert">${flash.message}</div>
            </g:if>

            <g:hasErrors bean="${this.accountStatemenBankAccount}">
              <ul class="alert alert-danger alert-dismissable" role=
            "alert">
                <g:eachError bean="${this.accountStatemenBankAccount}" var="error">
                  <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
              </ul>
            </g:hasErrors>
            <g:form action="save" class="form-horizontal" role="form" method="POST" enctype="multipart/form-data">
              <g:hiddenField name="bankAccount.id" value="${accountStatementBankAccount.bankAccount.id}" />
              <fieldset class="form">
                <div class="form-group">
                  <label class="control-label">Mes/Año:</label>
                  <g:datePicker class="form-control" name="month" value="${new Date()}" precision="month" relativeYears="[-5..0]"/>
                </div>
                <div class="form-group">
                  <label class="control-label">PDF:</label>
                  <input type="file" required="" class="form-control" name="pdfDocument" accept="application/pdf" maxlength="5000000" />
                </div>
              </fieldset>
              <fieldset class="buttons text-right">
                <g:submitButton name="create" class="save btn btn-default" value="${message(code: 'default.button.create.label', default: 'Create')}" />
              </fieldset>
            </g:form>
          </div>
        </div>
      </div>
      <div class="row text-right">
        <g:link class="btn btn-default" action="list" id="${accountStatementBankAccount.bankAccount.id}">Regresar</g:link>
      </div>
    </div>
  </body>
</html>
