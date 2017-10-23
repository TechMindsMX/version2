<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'paysheetContract.label', default: 'PaysheetContract')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-hospital-o fa-3x"></i>
        Contrato de Nómina
        <small>${paysheetContract.client}</small>
      </h1>
    </div>
    <div id="edit-address" class="content scaffold-edit" role="main">

      <g:render template="paysheetContractData"/>
      <g:render template="paysheetContractEmployees"/>
      
      <g:if test="${availableEmployees}">
        <g:form name="addEmployees" action="saveEmployees" id="${paysheetContract.id}">
          <g:render template="addAvailableEmployees"/>
        </g:form>
      </g:if>

    </div>

    <asset:javascript src="businessEntity/selectEntities.js"/>
    <asset:javascript src="paysheetContract/addEmployees.js"/>

  </body>
</html>
