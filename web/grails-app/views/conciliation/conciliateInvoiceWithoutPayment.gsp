<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-usd fa-3x"></i>
        Conciliación de Cobro
        <small>Conciliar Factura sin cobro depositado</small>
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <div class="panel panel-primary">
          <div class="panel-body">
            <div class="container-fluid">

              <g:render template="saleOrderData"/>
              <hr>

              <input type="hidden" id="currency" name="currency" value="${saleOrder.currency}"/>
              <input type="hidden" id="amountToPay" name="amountToPay" value="${saleOrder.amountToPay}"/>
              <g:form action="applyConciliationWithoutPayment">
                <g:hiddenField name="saleOrderId" value="${saleOrder.id}"/>

              <div class="row">
                <div class="form-group">
                  <label>Monto a aplicar (MXN) <font color="red">*</font>:</label>
                  <input class="form-control" type="number" min="0.01" max="${saleOrder.amountToPay}" step="0.01" id="amountToApply" name="amountToApply" required="true"/>
                </div>
              </div>

              <div class="row">
                <div class="form-group">
                  <label>Tipo de cambio <font color="red">*</font>:</label>
                  <g:if test="${saleOrder.currency == 'USD'}">
                    <input class="form-control" type="number" min="0.01" step="0.01" required="true" id="changeType" name="changeType" value="0.00"/>
                  </g:if>
                  <g:else>
                    <input class="form-control" type="number" min="0.01" step="0.01" required="true" id="changeType" name="changeType" readOnly="true" value="0.00"/>
                  </g:else>
                </div>
              </div>

              <div class="row">
                <div class="form-group">
                  <label>Observaciones <font color="red">*</font>:</label>
                  <input class="form-control" name="comment" type="text" maxLength="255" required="true" oninvalid="this.setCustomValidity('Debe ingresar una observación de la conciliación de la factura sin pago')"/>
                </div>
              </div>

              <hr>

              <div class="row">
                <div class="col-md-3 col-md-offset-6 text-center">
                  <g:link controller="payment" action="conciliation" class="btn btn-danger btn-block">Cancelar</g:link>
                </div>
                <div class="col-md-3 text-center">
                  <button class="btn btn-success btn-block">Aplicar</button>
                </div>
              </div>

              </g:form>

            </div>
          </div>
        </div>
      </div>
    </div>
    <asset:javascript src="conciliation/conciliateWithoutPayment.js"/>
  </body>
</html>
