<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'paysheetReceipt.label', default: 'PaysheetReceipt')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-credit-card-alt fa-3x"></i>
        Recibos de Nómina
        <small>
          ${user.username} - ${user.name}<br/>
        </small>
      </h1>
    </div>
    <div class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div class="portlet-heading">
          <g:form action="showPaysheetReceiptsOfUserForPaysheet">
          <div class="row">
            <div class="col-md-10">
              <g:select class="form-control" name="paysheetId" from="${payedPaysheetsOfUser.sort()}" optionKey="id" value="${paysheet?.id}"/>
            </div>
            <div class="col-md-2 text-right">
              <button class="btn btn-primary" type="submit"/>Buscar</button>
            </div>
          </div>
          </g:form>
        </div>
 
        <g:if test="${employee}">
        <div class="portlet-body">
          <g:render template="linksPaysheetReceiptsOfEmployee"/>
        </div>
        </g:if>

      </div>
    </div>
  </body>
</html>
