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
        Conciliación de Pago
        <small>Elegir factura(s)</small>
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <div class="panel panel-primary">
          <div class="panel-heading">
            Conciliar como Pago Completo
          </div>
          <div class="panel-body">
            <div class="container-fluid">

              <div class="row">
                <div class="col-md-4 text-center"><strong>Fecha del Pago</strong></div>
                <div class="col-md-4 text-center"><strong>Monto</strong></div>
                <div class="col-md-4 text-center"><strong>Cliente</strong></div>
              </div>
              <div class="row">
                <div class="col-md-4 text-center"><g:formatDate format="dd-MM-yyyy" date="${payment.dateCreated}"/></div>
                <div class="col-md-4 text-center">${modulusuno.formatPrice(number: payment.amount)}</div>
                <div class="col-md-4 text-center">${payment.rfc}</div>
              </div>
              <hr>
              <div class="row">
                <div class="col-md-6">
                  <label>Facturas en pesos:</label>
                  <select class="form-control" name="saleOrder.id"></select>
                </div>
                <div class="col-md-6">
                  <label>Facturas en dólares:</label>
                  <div class="form-group">
                    <label class="col-md-5">Tipo de Cambio:</label>
                    <input class="col-md-2" type="number" min="0.01" step="0.01" name="changeType"/>&nbsp;
                    <button class="btn btn-primary">Buscar facturas</button>
                  </div>
                  <select class="form-control" name="saleOrder.id"></select>
                </div>
              </div>
              <hr>
              <div class="row">
                <div class="col-md-12">
                  <button class="btn btn-success col-md-3 col-md-offset-9">Aplicar</button>
                </div>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>

  </body>
</html>
