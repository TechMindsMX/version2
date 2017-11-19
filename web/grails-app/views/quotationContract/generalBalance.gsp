<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'quotationContract.label', default: 'QuotationContract')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
    
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-list-alt fa-3x"></i>
        Cotización
        <small>${company}</small>
      </h1>
    </div>

    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div class="portlet-heading">
          <div class="portlet-title"></div>
          <div class="clearfix"></div>
        </div>

        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>

            <g:form action="getBalanceGeneral">
              <div class="row">
                <div class="col-md-4">
                  <label>
                    <g:message code="Del:"/>
                  </label>
                  <input class="form-control" type="text" id="datepicker" name="initDate" required="required">
                </div>
                <div class="col-md-4">
                  <label>
                    <g:message code="Al:"/>
                  </label>
                  <input class="form-control" type="text" id="datepicker1" name="lastDate" required="required">
                </div>
                <div class="col-md-4 text-center">
                  <g:submitButton name="consultar" class="btn btn-primary" value="${message(code: 'default.button.consultar.label', default: 'Consultar')}"/>
                  <g:link class="btn btn-default" controller="quotationContract" action="pdfGeneralBalance" params="[initDate:'01-11-2017',lastDate:'30-11-2017']" target="_blank">PDF</g:link>
                </div>
              </div>
              <br>
              <br>  
            </g:form>

            <div class="row">
              <div class="col-md-12">
                <div class="table-responsive">
                  <table class="table table-striped table-condensed">
                    <tr>
                      <th>Cliente</th>
                      <th>Solicitudes</th>
                      <th>Pagos</th>
                      <th>Saldo</th>
                    </tr>
                    <g:each in="${detailGeneralBalance}" var="generalBalance">
                      <tr>
                        <td>${generalBalance.quotationContract?.client}</td>
                        <td><g:formatNumber number="${generalBalance.request}" type="currency" currencyCode="MXN" /></td>
                        <td><g:formatNumber number="${generalBalance.payment}" type="currency" currencyCode="MXN" /></td>
                        <td><g:formatNumber number="${generalBalance.balance}" type="currency" currencyCode="MXN" /></td>
                      </tr>
                    </g:each> 
                    <tr>
                      <th>Total</th>
                      <th><g:formatNumber number="${detailGeneralBalance.sum { it.request }}" type="currency" currencyCode="MXN" /></th>
                      <th><g:formatNumber number="${detailGeneralBalance.sum { it.payment }}" type="currency" currencyCode="MXN" /></th>
                      <th><g:formatNumber number="${detailGeneralBalance.sum { it.balance }}" type="currency" currencyCode="MXN" /></th>
                    </tr>
                  </table>
                  
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
    <asset:javascript src="quotationContract/create.js"/>
  </body>
</html>