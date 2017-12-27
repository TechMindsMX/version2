<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'quotationContract.label', default: 'QuotationContract')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
    <asset:stylesheet src="quotationContract/balance.css" />
  </head>

  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-list-alt fa-3x"></i>
        Consulta de saldos
        <small>${balance?.quotationContract?.client}</small>
      </h1>
    </div>

	<sec:ifAnyGranted roles="ROLE_OPERATOR_QUOTATION">
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

            <g:if test="${!balance}">
              <g:form action="balance">
                <g:render template="balance/chooseClient"/>
              </g:form>
            </g:if><g:else>



            <g:if test="${balance.quotationContract}">
            <div class="row">
              <div class="col-md-8">
                <h1>
                <label>
                  <g:message code="Cliente: " />
                </label>
                <label>
                  <g:message message="${balance.quotationContract.client}" />
                </label>
                </h1>
                <g:form action="balance">
                <div class="row">
                  <div class="col-md-6">
                  <label>
                    <g:message code="Del" />
                  </label>
                  <input class="form-control" type="text" value="${formatDate(format:'dd-MM-yyyy', date:period.init)}" id="datepicker" name="initDate" required="required">
                  <input type="hidden" value="${balance.quotationContract.id}" name="id" />
                  </div>
                  <div class="col-md-6">
                  <label>
                    <g:message code="Al" />
                  </label>
                  <input class="form-control" type="text" value="${formatDate(format:'dd-MM-yyyy', date:period.end)}" id="datepicker" name="lastDate" required="required">
                  </div>
                </div>
                <div class="row">
                  <div class="col-md-6">
                  <g:submitButton name="consultar" class="btn btn-primary marginP" value="${message(code: 'default.button.consultar.label', default: 'Consultar')}"
                  />
                  </div>
                  <div class="col-md-3">
                  <button type="button" name="Solicitar pago" class="btn btn-primary marginP" value="${message(code: 'default.button.paymentRequest.label', default: 'Solicitar pago')}" data-toggle="collapse" data-target="#demo"
                  >Solicitar pago </button>
                  </div>
                  <div class="col-md-3">
                  <g:link type="button" action="selectPaymentRequest" controller="QuotationPaymentRequest"  params="[quotation: balance.quotationContract.id, fromContract: 'contract']" class="btn btn-primary marginP"
                  >Consultar pagos </g:link>
                  </div>
                </div>
                </g:form>

              </div>
              <div class="col-md-4 vertical-bar">
                <div class="row">
                  <h2>
                    Disponible: ${modulusuno.formatPrice(number:balance.summary.available)}
                    <input type="hidden" value="${balance.summary.available}" id="available" >
                  </h2>
                </div>
                <div class="row">
                  <h2>
                    En transito: ${modulusuno.formatPrice(number:balance.summary.transit)}
                  </h2>
                </div>
                <div class="row">
                  <h2>
                    Total: ${modulusuno.formatPrice(number:balance.summary.total)}
                  </h2>
                </div>
              </div>
            </div>
            <div class="row">
                <div id="demo" class="collapse">
                  <g:render template="requestPayment/requestPayment"/>
                </div>
            </div>
            <div class="row">
              <div class="col-md-12">
                  <div class="table-responsive">
                    <table class="table table-striped table-condensed">
                      <tr>
                        <th class="text-center">Concepto</th>
                        <th class="text-center">Fecha</th>
                        <th class="text-center">Abono</th>
                        <th class="text-center">Cargo</th>
                        <th class="text-center">Saldo</th>
                      </tr>
                      <g:each in="${balance.conceptList}" var="paymentRequest">
                        <tr>
                          <td>${paymentRequest.concept}</td>
                          <td class="text-center"><g:formatDate format="dd-MM-yyyy" date="${paymentRequest.date}"/></td>
                          <td class="text-right">${modulusuno.formatPrice(number:paymentRequest.deposit)}</td>
                          <td class="text-right">${modulusuno.formatPrice(number:paymentRequest.charge)}</td>
                          <td class="text-right">${modulusuno.formatPrice(number:paymentRequest.balance)}</td>
                        </tr>
                      </g:each>
                    </table>
                  </div>

            </g:if>

            </g:else>
             </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    </sec:ifAnyGranted>
    <asset:javascript src="quotationContract/create.js"/>
  </body>
</html>
