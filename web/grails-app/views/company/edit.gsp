<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'company.label', default: 'Company')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1><g:message code="company.show"/></h1>
      <ol class="breadcrumb">
        <li><i class="fa fa-caret-square-o-up"></i> Compañia</li>
        <li class="active">Informacion de la Empresas <b>${company.toString()}</b></li>
      </ol>
    </div>
    <div id="edit-company" class="content scaffold-edit" role="main">
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
            <g:form resource="${this.company}" class="form-horizontal" method="PUT" role="form">
              <g:hiddenField name="version" value="${this.company?.version}" />
              <g:render template="form" bean="${company}" />
              <div class="form-group">
                <div class="col-sm-offset-4 col-sm-10">
                  <input class="save btn btn-default" id="update" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </div>
              </div>
            </g:form>
          </div>
        </div>
      </div>
    </div>
    <g:javascript>

      function verifyCorrectDates(){
          return Number(document.getElementById("firstPaymentDay").value) < Number(document.getElementById("secondPaymentDay").value)
        };
        $("#update").click( function(){
          if(!verifyCorrectDates()){
            alert('Las fechas son incorrectas, favor de verificar');
            return false
          } else { return true }
        });
    </g:javascript>
  </body>
</html>
