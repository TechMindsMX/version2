<!DOCTYPE html>
<%! import java.text.SimpleDateFormat %>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'accountStatement.label', default: 'AccountStatement')}" />
    <title><g:message code="manager.pendingAccounts.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
      <i class="fa fa-money fa-3x"></i>
      <g:message code="manager.pendingAccounts.label"/>
      </h1>
    </div>

    <g:render template="pendingAccounts/bankAccountBalance"/>
    <g:render template="pendingAccounts/filter"/>

    <div class="row">
      <div class="col-md-6">
        <g:render template="pendingAccounts/accountsToCharge"/>
      </div><!--end col-6-->
      <div class="col-md-6">
        <g:render template="pendingAccounts/accountsToPay"/>
      </div>
    </div>
    <asset:javascript src="pendingAccounts/pendingAccounts.js"/>
</body>
</html>
