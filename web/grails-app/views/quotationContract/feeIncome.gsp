<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'quotationContract.label', default: 'QuotationContract')}" />
    <title>
      <g:message code="default.create.label" args="[entityName]" />
    </title>
  </head>

  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-list-alt fa-3x"></i>
        Ingresos por comisiones
        <small>${company}</small>
      </h1>
    </div>

	<sec:ifAnyGranted roles="ROLE_EXECUTOR_QUOTATION">
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

            <g:form action="feeIncome">
              <label>
                <g:message code="Periodo" />
              </label>
              <div class="row">
                <div class="col-md-4">
                  <label>
                    <g:message code="Del:" />
                  </label>
                  <input class="form-control" type="text" value="${formatDate(format:'dd-MM-yyyy', date:period.init)}" id="datepicker" name="initDate" required="required">
                </div>
                <div class="col-md-4">
                  <label>
                    <g:message code="Al:" />
                  </label>
                  <input class="form-control" type="text" value="${formatDate(format:'dd-MM-yyyy', date:period.end)}" id="datepicker1" name="lastDate" required="required">
                </div>
                <div class="col-md-4 text-center">
                  <g:submitButton name="consultar" class="btn btn-primary" value="${message(code: 'default.button.consultar.label', default: 'Consultar')}" style="margin-top:25px"/>
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
                      <th class="text-center">Monto</th>
                      <th class="text-center">Monto de Comisión</th>
                    </tr>
                    <g:each in="${feeIncomes}" var="feeIncome">
                      <tr>
                        <td>${feeIncome.client}</td>
                        <td class="text-right">${modulusuno.formatPrice(number:feeIncome.amount)}</td>
                        <td class="text-right">${modulusuno.formatPrice(number:feeIncome.commissionAmount)}</td>
                      </tr>
                    </g:each>
                    <tfoot>
                      <th>Total</th>
                      <th class="text-right">${modulusuno.formatPrice(number:feeIncomes.sum { it.amount })}</th>
                      <th class="text-right">${modulusuno.formatPrice(number:feeIncomes.sum { it.commissionAmount })}</th>
                    </tfoot>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
	</sec:ifAnyGranted>
    <asset:javascript src="quotationContract/create.js" />
  </body>
</html>
