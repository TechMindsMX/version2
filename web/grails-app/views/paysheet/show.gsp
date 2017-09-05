<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'paysheet.label', default: 'Paysheet')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-credit-card-alt fa-3x"></i>
        Nómina
        <small>${paysheet.company}</small>
      </h1>
    </div>

    <div class="content scaffold-edit" role="main">
      <div class="portlet portlet-default">
        <div class="portlet-heading">
          <div class="row">
            <div class="col-md-6">
            </div>
            <div class="col-md-6 text-right">
              <g:link class="btn btn-default" action="list">Lista</g:link>
            </div>
          </div>
        </div>
        <div class="portlet-body">
          <g:render template="paysheetData"/>
        </div>
      </div>

      <div class="row">
        <div class="col-md-12">
          <g:render template="listEmployees"/>
        </div>
      </div>

    </div>
  </body>
</html>

