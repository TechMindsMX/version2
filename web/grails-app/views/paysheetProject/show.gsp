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
        <g:render template="listPayers"/>

        <g:if test="${payersList}">
          <g:form action="addPayerCompany" id="${paysheetProject.id}">
            <g:render template="choosePayerCompany"/>
          </g:form>
        </g:if>

        <g:if test="${invoicedCompaniesList}">
          <g:form action="addInvoicedCompany" id="${paysheetProject.id}">
            <g:render template="chooseInvoicedCompany"/>
          </g:form>
        </g:if>

        <div class="row">
          <div class="col-md-12 text-right">
            <g:link class="btn btn-primary" controller="paysheetContract" action="list">Salir</g:link>
          </div>
        </div>

    </div>
  </body>
</html>
