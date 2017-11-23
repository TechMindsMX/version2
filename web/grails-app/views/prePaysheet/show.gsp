<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'prePaysheet.label', default: 'PrePaysheet')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-credit-card-alt fa-3x"></i>
        Pre-Nómina
        <small>${prePaysheet.paysheetContract.client}</small>
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
          <g:render template="prePaysheetData"/>
        </div>
      </div>

			<g:if test="${importResults}">
			<div class="row">
				<div class="col-md-12">
					<g:render template="importResults"/>
				</div>
			</div>
			</g:if>

      <div class="row">
        <div class="col-md-12">
          <g:render template="listEmployees"/>
        </div>
      </div>

    </div>
  </body>
</html>


