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

            <g:form action="getBalanceGeneral" name="formGeneralBalance">
              <div class="row">
                <div class="col-md-4">
                  <label>
                    <g:message code="Del:"/>
                  </label>
                  <input class="form-control" type="text" value="${formatDate(format:'dd-MM-yyyy', date:period.init)}" id="datepicker" name="initDate" required="required">
                </div>
                <div class="col-md-4">
                  <label>
                    <g:message code="Al:"/>
                  </label>
                  <input class="form-control" type="text" value="${formatDate(format:'dd-MM-yyyy', date:period.end)}" id="datepicker1" name="lastDate" required="required">
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
                      <th class="text-center">Cliente</th>
                      <th class="text-center">Solicitudes</th>
                      <th class="text-center">Pagos</th>
                      <th class="text-center">Saldo</th>
                    </tr>
                    <g:each in="${detailGeneralBalance}" var="generalBalance">
                      <tr>
                        <td><g:link action="show" id="${generalBalance.quotationContract.id}">${generalBalance.quotationContract?.client}</g:link></td>
                        <td class="text-right">${modulusuno.formatPrice(number:generalBalance.request)}</td>
                        <td class="text-right">${modulusuno.formatPrice(number:generalBalance.payment)}</td>
                        <td class="text-right">${modulusuno.formatPrice(number:generalBalance.balance)}</td>
                      </tr>
                    </g:each> 
                    <tfoot>
                      <th>Total</th>
                      <th class="text-right">${modulusuno.formatPrice(number:detailGeneralBalance.sum { it.request })}</th>
                      <th class="text-right">${modulusuno.formatPrice(number:detailGeneralBalance.sum { it.payment })}</th>
                      <th class="text-right">${modulusuno.formatPrice(number:detailGeneralBalance.sum { it.balance })}</th>
                    </tfoot>
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