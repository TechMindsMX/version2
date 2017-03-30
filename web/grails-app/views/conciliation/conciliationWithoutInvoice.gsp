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
        Conciliación de Cobros
        <small>Conciliar cobro sin factura</small>
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <div class="panel panel-primary">
          <div class="panel-body">
            <div class="container-fluid">

              <g:render template="dataPayment"/>
              <hr>
              <g:form action="applyConciliationWithoutInvoice">
                <g:hiddenField name="paymentId" value="${payment.id}"/>
                <g:hiddenField name="changeType" value="0"/>
                <g:hiddenField name="amountToApply" value="${payment.amount}"/>

              <div class="row">
                <div class="form-group">
                  <label>Especifique el concepto del pago a conciliar <font color="red">*</font>:</label>
                  <input class="form-control" name="comment" type="text" maxLength="255" required="true" oninvalid="this.setCustomValidity('Debe ingresar el concepto del pago a conciliar')"/>
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
  </body>
</html>
