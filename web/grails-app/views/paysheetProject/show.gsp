<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'paysheetProject.label', default: 'PaysheetProject')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-hospital-o fa-3x"></i>
        Proyecto de Nómina
        <small>${paysheetProject.paysheetContract.client}</small>
      </h1>
    </div>
    <div id="show-paysheet-project" class="content scaffold-edit" role="main">
        <g:render template="paysheetProjectData"/>
        <g:render template="listEmployees"/>

        <g:if test="${employeesList}">
          <g:form action="addEmployees" id="${paysheetProject.id}">
            <g:render template="chooseEmployees"/>
          </g:form>
        </g:if>

        <g:render template="listPayers"/>
        <g:render template="listBillers"/>

        <g:if test="${payersList}">
          <g:form action="addPayerCompany">
            <g:render template="choosePayerCompany"/>
          </g:form>
        </g:if>

        <g:if test="${billersList}">
          <g:form action="addBillerCompany">
            <g:render template="chooseBillerCompany"/>
          </g:form>
        </g:if>

        <div class="row">
          <div class="col-md-12 text-right">
            <g:link class="btn btn-primary" controller="paysheetContract" action="show" id="${paysheetProject.paysheetContract.id}">Salir</g:link>
          </div>
        </div>

    </div>

    <asset:javascript src="businessEntity/selectEntities.js"/>
  </body>
</html>
