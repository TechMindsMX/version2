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
        Conciliación sin factura
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <div class="panel panel-primary">
          <div class="panel-body">
            <div class="container-fluid">

              <g:render template="dataPayment"/>
              <hr>
              <div class="row">
                <div class="table-responsive">
                  <table class="table table-condensed table-striped">
                    <thead>
                    <tr>
                      <th class="text-center">Concepto</th>
                      <th class="text-center">Monto</th>
                    </tr>
                    </thead>
                    <tbody>
                      <g:each in="${conciliations}" var="conciliation">
                      <tr>
                        <td>${conciliation.comment}</td>
                        <td class="text-right">${modulusuno.formatPrice(number: conciliation.amount)}</td>
                      </tr>
                      </g:each>
                    </tbody>
                  </table>
                </div>
              </div>
              <hr>
              <div class="row">
                <g:link class="btn btn-info" controller="payment" action="conciliation">Regresar</g:link>
              </div>
              
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>