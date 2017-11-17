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

            <g:form action="balance">
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
                  <input class="form-control" type="text" id="datepicker2" name="lastDate" required="required">
                </div>
                <div class="col-md-4 text-center">
                  <g:submitButton name="consultar" class="btn btn-primary" value="${message(code: 'default.button.consultar.label', default: 'Consultar')}"/>
                  <g:link class="btn btn-default" controller="quotationContract" action="index">PDF</g:link>
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
                        <td>${generalBalance.client}</td>
                        <td>${generalBalance.totalRequest}</td>
                        <td>${generalBalance.totalPayments}</td>
                        <td>${generalBalance.totalRequest - generalBalance.totalPayments}</td>
                      </tr>
                    </g:each> 
                    <tr>
                      <th>Total</th>
                      <th>${detailGeneralBalance.sum { it.totalRequest }}</th>
                      <th>${detailGeneralBalance.sum { it.totalPayments }}</th>
                      <th>${detailGeneralBalance.sum { it.totalRequest - it.totalPayments }}</th>
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