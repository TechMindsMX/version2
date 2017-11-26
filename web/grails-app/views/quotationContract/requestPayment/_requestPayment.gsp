<%! import com.modulus.uno.PaymentMethod%>
  <div class="portlet portlet-blue">
    <div class="portlet-heading">
      <div class="portlet-title"></div>
      <div class="clearfix"></div>
    </div>
    <div id="horizontalFormExample" class="panel-collapse collapse in">
      <div class="portlet-body">
        <g:form id="requestPayment" name="requestPayment" controller="QuotationPaymentRequest" action="saveFromQuotationContract">

        <div class="row">
          <div class="col-md-3">
            <label>
              <g:message code="Monto" />
            </label>
            <input class="form-control" type="number" step="0.01" id="amonut" name="amount" required="required" pattern="([0-9]*[.])?[0-9]+">
            <g:if test="${messageForErrorInBalances}">
              <label>
                <g:message code="Error: El monto en solicitud de pagos es mayor al saldo disponible" />
              </label>
            </g:if>
          </div>
          <div class="col-md-9">
            <label>
              <g:message code="Nota" />
            </label>
            <input class="form-control" name="note" type="text" required="" />
          </div>
        </div>
        <br>
        <div class="row">
          <div class="col-md-3">
            <label>
              <g:message code="Modo de Pago" />
            </label>
            <g:select class="form-control" name="paymentMethod" from="${PaymentMethod.values()}" />
          </div>
          <div class="col-md-3 col-sm-offset-6">
              <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
          </div>
        </div>
        <input name="quotation" type="hidden" value="${balance.quotationContract.id}"/>
        </g:form>
      </div>
    </div>
  </div>