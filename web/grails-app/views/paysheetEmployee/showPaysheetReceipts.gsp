<!DOCTYPE html>
<%! import com.modulus.uno.paysheet.PaymentSchema %>
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
        Lista de Recibos de Nómina
        <small>
          ${employee.prePaysheetEmployee.rfc} - ${employee.prePaysheetEmployee.nameEmployee}<br/>
        </small>
      </h1>
    </div>
    <div class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div class="portlet-heading">
          <div class="row">
            <div class="col-md-12">
              Nómina del: ${modulusuno.dateFormat(date:employee.paysheet.prePaysheet.initPeriod)} al: ${modulusuno.dateFormat(date:employee.paysheet.prePaysheet.endPeriod)}<br/>
            </div>
          </div>
        </div>
 
        <div class="portlet-body">
          <g:render template="linksPaysheetReceiptsOfEmployee"/>
        </div>

        <div class="portlet-footer">
          <div class="row">
            <div class="col-md-8"></div>
            <div class="col-md-4 text-right">
              <g:link class="btn btn-primary" controller="paysheet" action="show" id="${employee.paysheet.id}">Ir a la nómina</g:link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
