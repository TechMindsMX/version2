<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'paysheetContract.label', default: 'PaysheetContract')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
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
      <div class="portlet portlet-blue">
        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
            <div class="row">
              <div class="col-md-4">
                <label><strong><g:message code="paysheetContract.label.client"/></strong></label><br/>
                <label>${paysheetContract.client}</label>
              </div>
              <div class="col-md-4">
                <label><strong><g:message code="paysheetContract.label.initDate"/></strong></label><br/>
                <label><g:formatDate format="dd-MM-yyyy" date="${paysheetContract.initDate}"/></label>
              </div>
              <div class="col-md-4">
                <label><strong><g:message code="paysheetContract.label.executive"/></strong></label><br/>
                <label>${paysheetContract.executive.name}</label>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
